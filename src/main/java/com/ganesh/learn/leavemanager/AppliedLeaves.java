package com.ganesh.learn.leavemanager;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@IdClass(AppliedLeavesId.class)
@NamedQuery(name = "AppliedLeaves.leavesWaitingApproval", query = "SELECT l from AppliedLeaves l join l.emp e WHERE e.manager.empId=:managerId AND" + " l.status=:pendingStatus")
public class AppliedLeaves
{
	public AppliedLeaves()
	{
	}

	public AppliedLeaves(Employee emp, Date startDate, Date endDate)
	{
		this.emp = emp;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	enum LeaveType
	{
		CASUAL_LEAVE, SICK_LEAVE, EARNED_LEAVE, WORK_FROM_HOME
	};

	enum Status
	{
		PENDING_APPROVAL, APPROVED, REJECTED
	}

	@Id
	@ManyToOne
	private Employee emp;
	@Id
	private Date startDate;
	@Id
	private Date endDate;

	@Enumerated(EnumType.STRING)
	private LeaveType leaveType;

	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING_APPROVAL;

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public LeaveType getLeaveType()
	{
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType)
	{
		this.leaveType = leaveType;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Employee getEmployee()
	{
		return emp;
	}

	public void setEmployee(Employee emp)
	{
		this.emp = emp;
	}

}

class AppliedLeavesId implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Employee emp;
	private Date startDate;
	private Date endDate;

	public AppliedLeavesId()
	{
	}

	public AppliedLeavesId(Employee emp, Date startDate, Date endDate)
	{
		this.emp = emp;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
