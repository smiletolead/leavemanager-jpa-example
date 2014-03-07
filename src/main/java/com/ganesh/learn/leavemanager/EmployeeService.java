package com.ganesh.learn.leavemanager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ganesh.learn.leavemanager.AppliedLeaves.LeaveType;
import com.ganesh.learn.leavemanager.AppliedLeaves.Status;

public class EmployeeService implements EmployeeAdminService
{
	EntityManager em = null;

	public EmployeeService(EntityManager entityManager)
	{
		this.em = entityManager;
	}

	public void addEmployee(Employee employee)
	{
		em.getTransaction().begin();
		em.persist(employee);
		em.getTransaction().commit();
	}

	public void applyForLeave(String empId, Date startDate, Date endDate, LeaveType leaveType) throws NotEnoughLeavesException
	{
		Employee emp = em.find(Employee.class, empId);

		int noOfLeaves = getNumberOfLeaves(startDate, endDate, TimeUnit.DAYS);

		checkLeaveAvailability(emp, noOfLeaves, leaveType);

		AppliedLeaves leaveApplication = new AppliedLeaves(emp, startDate, endDate);
		leaveApplication.setLeaveType(leaveType);
		leaveApplication.setStatus(Status.PENDING_APPROVAL);
		emp.getAppliedLeaves().add(leaveApplication);

		em.getTransaction().begin();
		em.persist(leaveApplication);
		em.getTransaction().commit();
	}

	private void checkLeaveAvailability(Employee emp, int noOfLeaves, LeaveType leaveType) throws NotEnoughLeavesException
	{
		int availableLeaves = 0;
		switch (leaveType)
		{
		case CASUAL_LEAVE:
			availableLeaves = emp.getCasualLeaves();
			break;
		case EARNED_LEAVE:
			availableLeaves = emp.getEarnedLeaves();
			break;
		case SICK_LEAVE:
			availableLeaves = emp.getSickLeaves();
			break;
		case WORK_FROM_HOME:
			availableLeaves = emp.getWorkFromHome();
			break;
		}
		if (noOfLeaves > availableLeaves)
		{
			throw new NotEnoughLeavesException();
		}
	}

	private int getNumberOfLeaves(Date startDate, Date endDate, TimeUnit timeUnit)
	{
		return (int) timeUnit.convert(endDate.getTime() - startDate.getTime(), TimeUnit.MILLISECONDS);
	}

	public List<AppliedLeaves> getLeavesPendingApproval(String managerId)
	{
		// Get all the leaves pending your approval from your subordinates
		Query query = em.createNamedQuery("AppliedLeaves.leavesWaitingApproval");
		query.setParameter("managerId", managerId);
		query.setParameter("pendingStatus", Status.PENDING_APPROVAL);

		return query.getResultList();
	}

	public void approveOrRejectLeave(String empId, Date startDate, Date endDate, boolean isApproved)
	{
		AppliedLeaves leave = em.find(AppliedLeaves.class, new AppliedLeavesId(em.find(Employee.class, empId), startDate, endDate));
		em.getTransaction().begin();
		if (isApproved)
		{
			leave.setStatus(Status.APPROVED);
		}
		else
			leave.setStatus(Status.REJECTED);
		em.getTransaction().commit();
	}
}
