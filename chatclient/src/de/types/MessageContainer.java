package de.types;

import networking.types.MessageWrapper;

public class MessageContainer
{
	private User u;
	private MessageWrapper message;
	
	public MessageContainer(User u, MessageWrapper message)
	{
		this.u = u;
		this.message = message;
	}
	
	public User getUser()
	{
		return u;
	}
	
	public MessageWrapper getMessage()
	{
		return message;
	}
	
}
