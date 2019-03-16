package de.networking.client;

import networking.types.Request;
import networking.types.Response;

public class ClientEventHandling
{
	private NetworkPhases phase;
	
	public ClientEventHandling()
	{
		this.phase = NetworkPhases.PRE0;
	}
	
	public ClientEventHandling(NetworkPhases np)
	{
		this.phase = np;
	}

	public void setMode(NetworkPhases np)
	{
		this.phase = np;
	}

	public void receivedResponse(Response r)
	{
		// TODO
	}

	public void receivedRequest(Request r)
	{
		// TODO
	}

}
