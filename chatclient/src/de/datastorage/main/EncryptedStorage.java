package de.datastorage.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TLS/SSL in order to get encrypted password
 * 1028 bit encryption
 * @author Jonas S_
 *
 */
public class EncryptedStorage
{
	private File data;
	private PrintWriter pw;
	private BufferedReader br;
	
	private boolean closed;
	
	public EncryptedStorage()
	{
		data = new File(DSManager.appdata + "\\" + "encrypteddata.dat");
		if(!data.exists())
		{
			if(data.getParentFile().exists() && data.getParentFile().isDirectory())
			{
				try
				{
					pw = new PrintWriter(data);
					br = new BufferedReader(new FileReader(data));
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				if(!data.getParentFile().mkdirs())
				{
					throw new RuntimeException("Could not create directory for data;");
				}
				try
				{
					pw = new PrintWriter(data);
					br = new BufferedReader(new FileReader(data));
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		closed = false;
	}
	
	private void reinit()
	{
		data = new File(DSManager.appdata + "\\" + "encrypteddata.dat");
		if(!data.exists())
		{
			if(data.getParentFile().exists() && data.getParentFile().isDirectory())
			{
				try
				{
					pw = new PrintWriter(data);
					br = new BufferedReader(new FileReader(data));
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				if(!data.getParentFile().mkdirs())
				{
					throw new RuntimeException("Could not create directory for data;");
				}
				try
				{
					pw = new PrintWriter(data);
					br = new BufferedReader(new FileReader(data));
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		}
		closed = false;
	}
	
	public void close()
	{
		try
		{
			br.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		pw.close();
		closed = true;
	}
}
