package de.networking.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import javax.crypto.Cipher;

import de.Main;
import de.networking.logger.Logger;
import javafx.application.Platform;
import networking.types.CredentialsWrapper;
import networking.types.Protocol;
import networking.types.Request;
import networking.types.Response;

public class ClientHandler
{

	private String password;
	private Socket s;

	private DataInputStream in;
	private DataOutputStream out;

	private int current = 0;

	private PublicKey pub0;
	private PrivateKey pvt;

	private PublicKey pub1;

	private NetworkPhases phase;
	private HashMap<NetworkPhases, boolean[]> networkphaseprogress;

	public ClientHandler(Socket s)
	{
		this.s = s;
		phase = NetworkPhases.PRE0;
		try
		{
			in = new DataInputStream(s.getInputStream());
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			out = new DataOutputStream(s.getOutputStream());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KeyPairGenerator kpg = null;
		try
		{
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kpg.initialize(3072);
		KeyPair kp = kpg.generateKeyPair();
		pub0 = kp.getPublic();
		pvt = kp.getPrivate();

		networkphaseprogress = new HashMap<NetworkPhases, boolean[]>();
		phase = NetworkPhases.PRE0;
		networkphaseprogress.put(phase, new boolean[5]);
		for (int i = 0; i < networkphaseprogress.get(phase).length; i++)
		{
			networkphaseprogress.get(phase)[i] = false;
		}
	}

	public void initProtocol()
	{
		try
		{
			out.write(serialize(pub0));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		phase = NetworkPhases.PRE1;
	}

	public void transmitCreds(String username, String password, boolean wantsToken)
	{
		if (phase == NetworkPhases.PRE2 && !networkphaseprogress.get(phase)[0])
		{
			Logger.info("Sending user credentials!");
			try
			{
				send(new Request(Requests.TRSMT_CREDS.getName(), new CredentialsWrapper(username, password, wantsToken)));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			networkphaseprogress.get(phase)[0] = true;
		} else
		{
			Logger.info("Already sent user credentials!");
		}
	}

	public void run() throws Exception
	{
		byte b[] = null;
		if (in.available() != 0)
		{
			Logger.info(s.getInetAddress().getHostAddress(), "Received bytes...");
			try
			{
				int ch1 = in.read();
				int ch2 = in.read();
				int ch3 = in.read();
				int ch4 = in.read();
				if ((ch1 | ch2 | ch3 | ch4) < 0)
					// inputstream already ended
					throw new EOFException();
				int length = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
				b = new byte[length];
				for (int a = 0; a < length; a++)
				{
					b[a] = in.readByte();
				}
			} catch (Throwable t)
			{
				if (t instanceof ArrayIndexOutOfBoundsException)
				{
					//
				} else
				{
					//
				}
			}
			if (phase != NetworkPhases.PRE0)
			{
				Logger.info("Decrypting in phase " + (phase == NetworkPhases.PRE0 ? "pre0" : "some other phase"));
				b = decrypt(pvt, b);
			}

			Object o = deserialize(b);
			if (o instanceof Request)
			{
				Logger.info("Evaluating request...");
				switch (phase)
				{
				case PRE0:
					for (Requests r : Requests.values())
					{
						if (r.getName().equals(((Request) o).getName()))
						{
							switch (r)
							{
							case TRSMT_KEY:
								break;
							default:
								break;
							}
						}
					}

					break;
				case PRE1:

					break;
				case PRE2:

					break;
				case COM:

					break;
				case POST:

					break;
				default:
					throw new RuntimeException("Networkphase could not be identified.");
				}
			} else if (o instanceof Response)
			{
				Logger.info("Evaluating response...");
				switch (phase)
				{

				case PRE0:
					for (Responses r : Responses.values())
					{
						if (r.getName().equals(((Response) o).getName()))
						{
							Logger.info("Some kind of achievement!");
							switch (r)
							{
							case RSP_KEY:
								if (((Response) o).getBuffer() instanceof PublicKey)
								{
									Logger.info(s.getInetAddress().getHostAddress(), "Received Key.");
									pub1 = (PublicKey) ((Response) o).getBuffer();
									networkphaseprogress.get(phase)[0] = true;
								} else
								{
									Logger.info(s.getInetAddress().getHostAddress(),
											"Received invalid key trasmission.");
								}
								break;
							default:
								Logger.info(r.getName());
								break;
							}
						}
					}
					break;

				case PRE1:
					for (Responses r : Responses.values())
					{
						if (r.getName().equals(((Response) o).getName()))
						{
							switch (r)
							{
							case RSP_PROTOCOL:
								if (((Response) o).getBuffer() instanceof Protocol)
								{
									if (!((Protocol) ((Response) o).getBuffer()).getProtocolVersion()
											.equals(Main.protocol.getProtocolVersion()))
									{

										Logger.info("Protocol version not en par with server!");
										Logger.info("Update necessary to continue!");
									} else
									{
										Logger.info("Protocol version en par with server!");
										networkphaseprogress.get(phase)[1] = true;
									}
									if (!((Protocol) ((Response) o).getBuffer()).getClientVersion()
											.equals(Main.protocol.getClientVersion()))
									{
										Logger.info("Client version not en par with server!");
									} else
									{
										Logger.info("Client version en par with server!");
									}
								} else
								{
									Logger.info("Invalid data.");
								}
								break;
							default:
								break;

							}
						}
					}
					break;

				case PRE2:
					for (Responses r : Responses.values())
					{
						if (r.getName().equals(((Response) o).getName()))
						{
							switch (r)
							{
							case RSP_CREDS:
								if (((Response) o).getBuffer() instanceof String)
								{
									if (((Response) o).getBuffer().equals("ACCESS DENIED"))
									{
										Logger.info("Wrong user credentials");
										networkphaseprogress.get(phase)[0] = false;
									} else
									{
										Logger.info("Right user credentials! Received token!");
										networkphaseprogress.get(phase)[1] = true;
									}
								}
								break;
							case RSP_TOKEN:
								break;
							default:
								break;
							}
						}
					}
					break;
				case COM:

					break;
				case POST:

					break;
				default:
					throw new RuntimeException("Networkphase could not be identified.");
				}
			} else
			{
				// throw new BadPacketException();
			}
		}
		// initiating stuff

		switch (phase)
		{
		case PRE0:
			if (networkphaseprogress.get(phase)[0])
			{
				Logger.info(s.getInetAddress().getHostAddress(), "Key exchange complete.");
				advance();
			}

			break;
		case PRE1:

			if (networkphaseprogress.get(phase)[1] == true)
			{
				Logger.info("advance");
				advance();
			} else if (networkphaseprogress.get(phase)[0] == false)
			{
				Logger.info("Sending protocol !");
				Protocol p = Main.protocol;
				send(new Request(Requests.TRSMT_PROTOCOL.getName(), p));
				networkphaseprogress.get(phase)[0] = true;
			}
			break;
		case PRE2:
			// if (networkphaseprogress.get(phase)[0] == false)
			// {
			// // do nothing
			// } else
			if (networkphaseprogress.get(phase)[1] == true)
			{
				Logger.info("Logged in, advancing.");
	             Platform.runLater(new Runnable() {
	                 @Override public void run() {
	     				Main.getInstance().setScene(3);
	                 }
	             });

				advance();
			}
			break;
		case COM:

			break;
		case POST:
			// actually i dont think this phase is strictly necessary but well
			// at least i dont know what to do with it
			// lets just keep it yo
			break;
		default:
			throw new RuntimeException("Networkphase could not be identified.");
		}
	}

	public void advance()
	{
		switch (phase)
		{
		case PRE0:
			networkphaseprogress.remove(phase);
			networkphaseprogress.put(NetworkPhases.PRE1, new boolean[2]);
			phase = NetworkPhases.PRE1;
			for (int i = 0; i < networkphaseprogress.get(phase).length; i++)
			{
				networkphaseprogress.get(phase)[i] = false;
			}
			break;
		case PRE1:
			networkphaseprogress.remove(phase);
			networkphaseprogress.put(NetworkPhases.PRE2, new boolean[2]);
			phase = NetworkPhases.PRE2;
			for (int i = 0; i < networkphaseprogress.get(phase).length; i++)
			{
				networkphaseprogress.get(phase)[i] = false;
			}
			break;
		case PRE2:
			networkphaseprogress.remove(phase);
			networkphaseprogress.put(NetworkPhases.COM, new boolean[2]);
			phase = NetworkPhases.COM;
			for (int i = 0; i < networkphaseprogress.get(phase).length; i++)
			{
				networkphaseprogress.get(phase)[i] = false;
			}
			break;
		case COM:
			break;
		case POST:
			// terminate this handler
			break;
		default:
			break;
		}
	}

	public DataInputStream getInputStream()
	{
		return in;
	}

	public DataOutputStream getOutputStream()
	{
		return out;
	}

	public void send(Request r) throws Exception
	{
		if (current == Integer.MAX_VALUE)
		{
			current = 0;
		} else
		{
			current++;
		}
		r.setNr(current);
		byte[] b = serialize(r);
		if (phase != NetworkPhases.PRE0)
		{
			byte[] b0 = encrypt(pub1, b);
			out.writeInt(b0.length);
			out.write(b0);
		} else
		{
			out.writeInt(b.length);
			out.write(b);
		}
		out.flush();
	}

	public void send(Response r) throws Exception
	{
		byte[] b = serialize(r);
		if (phase != NetworkPhases.PRE0)
		{
			byte[] b0 = encrypt(pub1, b);
			out.writeInt(b0.length);
			out.write(b0);
		} else
		{
			out.writeInt(b.length);
			out.write(b);
		}
		out.flush();
	}

	public Socket getSocket()
	{
		return s;
	}

	public PublicKey getPublicKey()
	{
		return pub0;
	}

	public PublicKey getOpPublicKey()
	{
		return pub1;
	}

	private byte[] serialize(Object o) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(o);
		return out.toByteArray();
	}

	public static byte[] encrypt(PublicKey publicKey, byte[] msg) throws Exception
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(msg);
	}

	public static byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws Exception
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(encrypted);
	}

	private Object deserialize(byte[] b) throws IOException, ClassNotFoundException
	{
		try (ByteArrayInputStream bis = new ByteArrayInputStream(b); ObjectInputStream in = new ObjectInputStream(bis))
		{
			return in.readObject();
		}
	}

}
