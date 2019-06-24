package de.networking.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import de.networking.logger.Logger;
import networking.types.Request;
import networking.types.Response;


public class Client
{
	private static Client c;
	private Socket s;
	private boolean connected;
	// private int threadAmount;
	private Vector<Integer> numbers;

	private ClientHandler handler;

	private ClientEventHandling eventHandling;

	private HashMap<Integer, Request> bufferRequests;
	private HashMap<Integer, Response> bufferResponses;

	public Client()
	{
		if (c != null)
		{
			throw new RuntimeException();
		} else
		{
			c = this;
		}

		init();
	}

	public Client(int port)
	{
		if (c != null)
		{
			throw new RuntimeException();
		} else
		{
			c = this;
		}

		connect(port);
		init();
	}

	private void init()
	{
		eventHandling = new ClientEventHandling();
		numbers = new Vector<Integer>();
		bufferRequests = new HashMap<Integer, Request>();
		bufferResponses = new HashMap<Integer, Response>();
	}

	/**
	 * 
	 * @returns ports of sockets in LAN
	 */
	public Vector<Integer> searchServers()
	{
		boolean b = true;
		Vector<Integer> temp = new Vector<Integer>();
		for (int i = 25565; b; i++)
		{
			try
			{
				System.out.println(i);
				Socket s = new Socket(InetAddress.getByName("localhost"), i);
				s.close();
				temp.add(i);
			} catch (IOException e)
			{
				b = false;
				e.printStackTrace();
				continue;
			}
		}
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	public void connect(int port)
	{
		try
		{
			s = new Socket(InetAddress.getByName(null), port);
			connected = true;
		} catch (IOException e0)
		{
			// TODO Auto-generated catch block
			e0.printStackTrace();
		}
		log("Connected to " + s.getInetAddress().getHostAddress());
		handler = new ClientHandler(s);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while (connected)
				{
					try
					{
						handler.run();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					try
					{
						Thread.sleep(0, 500000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void connect(InetAddress ip, Integer port)
	{
		try
		{
			s = new Socket(ip, port);
			connected = true;
		} catch (IOException e0)
		{
			// TODO Auto-generated catch block
			e0.printStackTrace();
		}
		log("Connected to " + s.getInetAddress().getHostAddress());
		handler = new ClientHandler(s);

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (connected)
				{
					try
					{
						handler.run();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void initConnection()
	{
		try
		{
			Logger.info("Sending request");
			sendRequest(new Request(Requests.TRSMT_RSAKEY.getName(), getPublicKey()));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transmitCreds(String username, String password, boolean wantsToken)
	{
		handler.transmitCreds(username, password, wantsToken);
	}
	
	public void searchUsersFor(String s)
	{
		handler.searchUsersFor(s);
	}

	public void sendRequest(Request r) throws Exception
	{
		handler.send(r);
	}

	public void sendResponse(Response r) throws Exception
	{
		handler.send(r);
	}

	public boolean isOnline()
	{
		return connected;
	}

	public void disconnect()
	{
		try
		{
			s.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getIndex()
	{
		int i = 0;
		for (; !numbers.contains(i); i++)
		{

		}
		return i;
	}

	public HashMap<Integer, Request> getBufferRequests()
	{
		return bufferRequests;
	}

	public HashMap<Integer, Response> getBufferResponses()
	{
		return bufferResponses;
	}

	public PublicKey getPublicKey()
	{
		return handler.getPublicKey();
	}

	public void handle(Request r)
	{

	}

	public void handle(Response r)
	{

	}

	public void handle(Object o)
	{

	}

	public static Client getInstance()
	{
		return c;
	}

	private void log(String s)
	{
		Logger.info("CLIENT", s);
	}

	private void log(Throwable t)
	{
		Logger.error("CLIENT", t);
	}

}
