package de.networking.logger;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.datastorage.main.DSManager;

/**
 *
 */
public class Logger
{
	private static SimpleDateFormat sdf;
	 
	static
	{
		sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	public static void info(String s)
	{
		print("INFO", ((s.length() > 100) ? s.substring(0, 99) : s));
	}

	public static void info(String source, String s)
	{
		print("INFO", source, ((s.length() > 100) ? s.substring(0, 99) : s));
	}

	protected static String getTime()
	{
		return sdf.format(Calendar.getInstance().getTime());
	}

	public static void error(Exception error)
	{
		print("ERROR", error.getMessage());
	}

	public static void error(String source, Throwable error)
	{
		
		print("ERROR", source, error.getMessage());
	}

	private static void print(String prefix, String s)
	{
		try
		{
			DSManager.getInstance().appendLine("[" + getTime() + "]" + "[" + prefix + "]" + ((s.length() > 100) ? s.substring(0, 99) : s) + "\n");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[" + getTime() + "]" + "[" + prefix + "]" + ((s.length() > 100) ? s.substring(0, 99) : s));
	}

	private static void print(String prefix, String source, String s)
	{
		try
		{
			DSManager.getInstance().appendLine("[" + getTime() + "]" + "[" + prefix + "]" + "[" + source + "]" + ((s.length() > 100) ? s.substring(0, 99) : s)+ "\n");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[" + getTime() + "]" + "[" + prefix + "]" + "[" + source + "]" + ((s.length() > 100) ? s.substring(0, 99) : s));
	}
}
