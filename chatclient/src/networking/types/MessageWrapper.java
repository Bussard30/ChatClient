package networking.types;

import java.io.Serializable;

/**
 * Used to hold a message between two clients.<br>
 * Contains <b>message</b>, <b>source</b>, <b>destination</b>,<br>
 * whether the message has been <b>received</b> by the server or not,<br>
 * whether the message has been <b>read</b> by the other client and the <b>message id</b>
 * 
 * @author Bussard30
 *
 */
public class MessageWrapper extends Wrapper implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5667179476964469748L;
	private String message, source, destination;
	private boolean received, read;
	private int id;

	/**
	 * Creates MessageWrapper object.
	 * @param message
	 * @param source
	 * @param destination
	 */
	public MessageWrapper(String message, String source, String destination)
	{
		this(message, source, destination, false, false, -1);
	}

	/**
	 * Creates MessageWrapper object.
	 * @param message
	 * @param source
	 * @param destination
	 * @param received
	 * @param read
	 * @param id
	 */
	public MessageWrapper(String message, String source, String destination, boolean received, boolean read, int id)
	{
		this.message = message;
		this.source = source;
		this.destination = destination;
		this.received = received;
		this.id = id;
	}

	/**
	 * Creates CredentialsWrapper object with String obtained by {@link #getStrings()}
	 * @param s
	 */
	public MessageWrapper(String[] s)
	{
		assert s.length == 5;
		message = s[0];
		source = s[1];
		s[2] = destination;
		received = s[3].equals("true") ? true : false;
		read = s[4].equals("true") ? true : false;
		try
		{
			id = Integer.parseInt(s[5]);
		} catch (Throwable t)
		{
			throw new RuntimeException("Parameter 5 cannot be converted to an int");
		}
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getDestination()
	{
		return destination;
	}

	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	public boolean isReceived()
	{
		return received;
	}

	public void setReceived(boolean received)
	{
		this.received = received;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return String array required for {@link #CredentialsWrapper(String[])}
	 */
	@Override
	public String[] getStrings()
	{
		return new String[]
		{ message, source, destination, received ? "true" : "false", read ? "true" : "false", String.valueOf(id) };
	}

}
