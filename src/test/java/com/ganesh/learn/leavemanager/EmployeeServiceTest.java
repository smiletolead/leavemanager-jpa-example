package com.ganesh.learn.leavemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.ganesh.learn.leavemanager.AppliedLeaves.LeaveType;
import com.ganesh.learn.leavemanager.AppliedLeaves.Status;

public class EmployeeServiceTest
{

	EmployeeService empService;
	EntityManager em = null;

	@Before
	public void setUp() throws Exception
	{
		em = Persistence.createEntityManagerFactory("test").createEntityManager();
		empService = new EmployeeService(em);
	}

	@Test
	public void testAddEmployee()
	{
		Employee emp = new Employee();

		emp.setEmpId("casjo01");
		emp.setCasualLeaves(10);
		emp.setDesignation("Software Engineer");
		emp.setEarnedLeaves(10);
		emp.setName("John Cash");
		emp.setSickLeaves(5);

		empService.addEmployee(emp);
		Employee savedEmp = em.find(Employee.class, emp.getEmpId());
		assertEquals("John Cash", savedEmp.getName());
		assertEquals("Software Engineer", savedEmp.getDesignation());
	}

	@Test
	public void testApplyLeave()
	{
		Employee emp = new Employee("Harry Sobers", "sobha01");
		emp.setLeaves(10, 10, 10, 12);

		Employee manager = new Employee("Pete Somez", "sopet01");
		manager.setLeaves(10, 10, 10, 12);

		emp.setManager(manager);

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 05, 11);
		Date startDate = cal.getTime();

		cal.set(2014, 05, 20);
		Date endDate = cal.getTime();

		em.getTransaction().begin();
		em.persist(manager);
		em.persist(emp);
		em.getTransaction().commit();

		// AppliedLeaves leave = new AppliedLeaves(emp, startDate, endDate);
		// leave.setEmployee(emp);

		try
		{
			empService.applyForLeave(emp.getEmpId(), startDate, endDate, LeaveType.CASUAL_LEAVE);
		} catch (NotEnoughLeavesException e)
		{

		}

		assertEquals(emp.getAppliedLeaves().size(), 1);
	}

	@Test
	public void testNotEnoughLeaves()
	{
		Employee emp = new Employee("Harry Sobers", "sobha01");
		emp.setLeaves(5, 10, 10, 12);

		Employee manager = new Employee("Pete Somez", "sopet01");
		manager.setLeaves(5, 10, 10, 12);

		emp.setManager(manager);

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 05, 11);
		Date startDate = cal.getTime();

		cal.set(2014, 05, 23);
		Date endDate = cal.getTime();

		em.getTransaction().begin();
		em.persist(manager);
		em.persist(emp);
		em.getTransaction().commit();

		try
		{
			empService.applyForLeave(emp.getEmpId(), startDate, endDate, LeaveType.EARNED_LEAVE);
			fail("Not enough leaves failed..");
		} catch (NotEnoughLeavesException e)
		{
			System.out.println("Exception...");
		}

	}

	@Test
	public void testPendingLeavesForApproval()
	{
		Employee empFirst = new Employee("Harry Sobers", "sobha01");
		empFirst.setLeaves(15, 10, 10, 12);

		Employee manager = new Employee("Pete Somez", "sopet01");
		manager.setLeaves(15, 10, 10, 12);

		empFirst.setManager(manager);

		Employee empSecond = new Employee("Mary Ann", "anmar01");
		empSecond.setLeaves(15, 10, 10, 12);

		empSecond.setManager(manager);

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 05, 11);
		Date startDate = cal.getTime();

		cal.set(2014, 05, 15);
		Date endDate = cal.getTime();

		em.getTransaction().begin();
		em.persist(manager);
		em.persist(empFirst);
		em.persist(empSecond);
		em.getTransaction().commit();

		try
		{
			empService.applyForLeave(empFirst.getEmpId(), startDate, endDate, LeaveType.EARNED_LEAVE);
			empService.applyForLeave(empSecond.getEmpId(), startDate, endDate, LeaveType.EARNED_LEAVE);

			List<AppliedLeaves> pendingLeaves = empService.getLeavesPendingApproval(manager.getEmpId());
			assertEquals(2, pendingLeaves.size());

		} catch (NotEnoughLeavesException e)
		{
			System.out.println("Exception...");
		}

	}

	@Test
	public void testLeaveApproval()
	{
		Employee empFirst = new Employee("Harry Sobers", "sobha01");
		empFirst.setLeaves(15, 10, 10, 12);

		Employee manager = new Employee("Pete Somez", "sopet01");
		manager.setLeaves(15, 10, 10, 12);

		empFirst.setManager(manager);

		Employee empSecond = new Employee("Mary Ann", "anmar01");
		empSecond.setLeaves(15, 10, 10, 12);

		empSecond.setManager(manager);

		Calendar cal = Calendar.getInstance();
		cal.set(2014, 05, 11);
		Date startDate = cal.getTime();

		cal.set(2014, 05, 15);
		Date endDate = cal.getTime();

		em.getTransaction().begin();
		em.persist(manager);
		em.persist(empFirst);
		em.persist(empSecond);
		em.getTransaction().commit();

		try
		{
			empService.applyForLeave(empFirst.getEmpId(), startDate, endDate, LeaveType.EARNED_LEAVE);
			empService.applyForLeave(empSecond.getEmpId(), startDate, endDate, LeaveType.EARNED_LEAVE);

			List<AppliedLeaves> pendingLeaves = empService.getLeavesPendingApproval(manager.getEmpId());
			assertEquals(2, pendingLeaves.size());

			AppliedLeaves leaveFirst = pendingLeaves.get(0);
			empService.approveOrRejectLeave(leaveFirst.getEmployee().getEmpId(), leaveFirst.getStartDate(), leaveFirst.getEndDate(), true);

			AppliedLeaves leaveSecond = pendingLeaves.get(1);
			empService.approveOrRejectLeave(leaveSecond.getEmployee().getEmpId(), leaveSecond.getStartDate(), leaveSecond.getEndDate(), false);

			leaveFirst = em.find(AppliedLeaves.class, new AppliedLeavesId(leaveFirst.getEmployee(), leaveFirst.getStartDate(), leaveFirst.getEndDate()));
			leaveSecond = em.find(AppliedLeaves.class, new AppliedLeavesId(leaveSecond.getEmployee(), leaveSecond.getStartDate(), leaveSecond.getEndDate()));

			assertEquals(Status.APPROVED, leaveFirst.getStatus());
			assertEquals(Status.REJECTED, leaveSecond.getStatus());

		} catch (NotEnoughLeavesException e)
		{
			System.out.println("Exception...");
		}

	}

}
