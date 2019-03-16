package de.networking.main;

import java.io.IOException;

import de.Main;
import de.networking.client.Client;
import de.networking.client.Requests;
import networking.types.Request;

public class ClientManager
{
	private static ClientManager crp;
	
	private Client client;
	
	public ClientManager()
	{
		crp = this;
		client = new Client();
	}
	
	public void establishConnection()
	{
		client.connect(Main.server.getAddress(), Main.port);
		exchangeProtocol();
	}
	
	public void exchangeProtocol()
	{
		try
		{
			client.sendRequest(new Request(Requests.TRSMT_PROTOCOL.getName(), Main.protocol.getProtocolVersion() + "-" + Main.protocol.getClientVersion()));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ClientManager getInstance()
	{
		return crp;
	}
}
