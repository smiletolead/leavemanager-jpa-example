package com.ganesh.learn.leavemanager;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

public class AppliedLeavesTest
{
	EntityManagerFactory factory = null;
	EntityManager entityManager = null;

	@Before
	public void setUp() throws Exception
	{
		factory = Persistence.createEntityManagerFactory("test");
		entityManager = factory.createEntityManager();

	}

	@Test
	public void testManagerRelationShip()
	{
		entityManager.getTransaction().begin();
		Employee manager = new Employee();
		manager.setEmpId("houda10");
		manager.setName("Dave Houton");
		manager.setDesignation("Manager, Software Engineering");
		manager.setCasualLeaves(10);
		manager.setSickLeaves(5);
		manager.setWorkFromHome(10);

		Employee emp = new Employee();
		emp.setEmpId("podno02");
		emp.setName("Noami Pod");
		emp.setDesignation("Software Engineer");
		emp.setCasualLeaves(12);
		emp.setSickLeaves(6);
		emp.setWorkFromHome(12);
		emp.setManager(manager);
		entityManager.persist(manager);
		entityManager.persist(emp);

		AppliedLeaves applyLeave = new AppliedLeaves();
		applyLeave.setEmployee(emp);
		emp.getAppliedLeaves().add(applyLeave);

		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, 04, 01);
		applyLeave.setStartDate(startDate.getTime());

		Calendar endDate = Calendar.getInstance();
		endDate.set(2014, 04, 10);
		applyLeave.setEndDate(endDate.getTime());

		entityManager.persist(applyLeave);
		entityManager.getTransaction().commit();

		// emp = entityManager.find(Employee.class, "podno02");
		assertEquals(1, emp.getAppliedLeaves().size());

	}

}
