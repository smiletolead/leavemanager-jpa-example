package com.ganesh.learn.leavemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

public class EmployeeTest
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

		manager.getSubordinates().add(emp);
		entityManager.persist(manager);
		entityManager.persist(emp);
		entityManager.getTransaction().commit();

		Employee storedEmp = entityManager.find(Employee.class, "podno02");
		assertTrue("Employee name should be Noami Pod", storedEmp.getName().equals("Noami Pod"));
		assertTrue("Manager name should be Dave Houton", storedEmp.getManager().getName().equals("Dave Houton"));
		assertEquals(manager.getSubordinates().size(), 1);

		emp = new Employee();
		entityManager.getTransaction().begin();
		emp.setEmpId("kumga04");
		emp.setName("Garry Kumon");
		emp.setDesignation("Senior Software Engineer");
		emp.setCasualLeaves(12);
		emp.setSickLeaves(6);
		emp.setWorkFromHome(12);
		emp.setManager(manager);
		manager.getSubordinates().add(emp);
		entityManager.persist(emp);
		entityManager.getTransaction().commit();

		assertEquals(manager.getSubordinates().size(), 2);

	}

}
