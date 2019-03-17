package de.networking.client;

import java.security.PublicKey;

import networking.types.CredentialsWrapper;
import networking.types.Protocol;

public enum Requests
{
	// TRANSMIT PUBLIC KEY
	TRSMT_KEY("TRSMT_KEY", NetworkPhases.PRE0, PublicKey.class),

	// TRANSMIT PROTOCOL
	TRSMT_PROTOCOL("TRSMT_PROTOCOL", NetworkPhases.PRE1, Protocol.class),

	// TRANSMIT (user) CREDENTIALS
	TRSMT_CREDS("TRSMT_CREDS", NetworkPhases.PRE2, CredentialsWrapper.class),
	
	// TRANSMIT (user) CREDENTIALS
	TRSMT_TOKEN("TRSMT_TOKEN", NetworkPhases.PRE2, String.class);

	private final String name;
	private final NetworkPhases np;
	private final Class<?> type;

	Requests(String name, NetworkPhases np, Class<?> type)
	{
		this.np = np;
		this.type = type;
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public NetworkPhases getNPhase()
	{
		return np;
	}

	public Class<?> getType()
	{
		return type;
	}
}