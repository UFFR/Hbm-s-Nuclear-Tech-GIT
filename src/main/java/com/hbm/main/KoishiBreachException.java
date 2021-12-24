package com.hbm.main;

import java.util.Random;

public class KoishiBreachException extends Exception
{
	public static final String[] messages =
		{
				"Koishi on my computer? More likely than you think!",
				"Error: Insufficient shrimp fry (<1)",
				"Hey " + System.getProperty("user.name") + ", I'm right behind you!",
				"Oops, sorry about that one!",
				"Don't leave yet! I still have more skills to show off!"
		};
	/**
	 * 
	 */
	private static final long serialVersionUID = 4125023980567616164L;

	public KoishiBreachException()
	{
		// TODO Auto-generated constructor stub
		this(randomMessage());
	}

	public KoishiBreachException(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public KoishiBreachException(Throwable arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public KoishiBreachException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public KoishiBreachException(String arg0, Throwable arg1, boolean arg2, boolean arg3)
	{
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
	
	public static String randomMessage()
	{
		return messages[new Random().nextInt(messages.length)];
	}
}
