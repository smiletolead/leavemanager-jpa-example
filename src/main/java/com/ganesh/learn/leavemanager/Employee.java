package com.ganesh.learn.leavemanager;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Employee
{
	public Employee()
	{
	}

	public Employee(String name, String empId)
	{
		this.name = name;
		this.empId = empId;
	}

	public void setLeaves(int earnedLeaves, int casualLeaves, int workFromHome, int sickLeaves)
	{
		setEarnedLeaves(earnedLeaves);
		setCasualLeaves(casualLeaves);
		setWorkFromHome(workFromHome);
		setSickLeaves(sickLeaves);
	}

	@Id
	private String empId;
	private String name;

	@ManyToOne(cascade =
	{ CascadeType.ALL })
	@JoinColumn(name = "managerId")
	private Employee manager;

	@OneToMany
	private Set<AppliedLeaves> appliedLeaves = new HashSet<AppliedLeaves>();

	@OneToMany(mappedBy = "manager")
	private Set<Employee> subordinates = new HashSet<Employee>();

	private String designation;
	private int earnedLeaves;
	private int casualLeaves;

	private int sickLeaves;
	private int workFromHome;

	public String getEmpId()
	{
		return empId;
	}

	public void setEmpId(String empId)
	{
		this.empId = empId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Employee getManager()
	{
		return manager;
	}

	public void setManager(Employee manager)
	{
		this.manager = manager;
	}

	public Set<Employee> getSubordinates()
	{
		return subordinates;
	}

	public void setSubordinates(Set<Employee> subordinates)
	{
		this.subordinates = subordinates;
	}

	public String getDesignation()
	{
		return designation;
	}

	public void setDesignation(String designation)
	{
		this.designation = designation;
	}

	public int getEarnedLeaves()
	{
		return earnedLeaves;
	}

	public void setEarnedLeaves(int earnedLeaves)
	{
		this.earnedLeaves = earnedLeaves;
	}

	public int getCasualLeaves()
	{
		return casualLeaves;
	}

	public void setCasualLeaves(int casualLeaves)
	{
		this.casualLeaves = casualLeaves;
	}

	public int getSickLeaves()
	{
		return sickLeaves;
	}

	public void setSickLeaves(int sickLeaves)
	{
		this.sickLeaves = sickLeaves;
	}

	public int getWorkFromHome()
	{
		return workFromHome;
	}

	public void setWorkFromHome(int workFromHome)
	{
		this.workFromHome = workFromHome;
	}

	public Set<AppliedLeaves> getAppliedLeaves()
	{
		return appliedLeaves;
	}

}
