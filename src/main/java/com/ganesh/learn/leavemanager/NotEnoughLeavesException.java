package com.ganesh.learn.leavemanager;

public class NotEnoughLeavesException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7576300703328210720L;

	public NotEnoughLeavesException()
	{
		super();
	}

	public NotEnoughLeavesException(String message)
	{
		super(message);
	}

	public NotEnoughLeavesException(String message, Throwable throwable)
	{
		super(message, throwable);
	}

	public NotEnoughLeavesException(Throwable throwable)
	{
		super(throwable);
	}

}
