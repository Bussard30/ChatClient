package de.networking.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import de.Main;
import de.networking.logger.Logger;
import de.types.User;
import javafx.application.Platform;
import networking.exceptions.BadPacketException;
import networking.types.AESKeyWrapper;
import networking.types.CredentialsWrapper;
import networking.types.LoginResponseWrapper;
import networking.types.MessageWrapper;
import networking.types.ProfileInfoWrapper;
import networking.types.ProtocolWrapper;
import networking.types.Request;
import networking.types.Response;
import networking.types.SearchUserWrapper;
import networking.types.Wrapper;

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

	private User u;

	private KeyGenerator keygen;
	private SecretKey AESKey;

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

		try
		{
			keygen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keygen.init(128);
		AESKey = keygen.generateKey();

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
				send(new Request(Requests.TRSMT_CREDS.getName(),
						new CredentialsWrapper(username, password, wantsToken)));
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

	public void searchUsersFor(String s)
	{
		if (phase == NetworkPhases.COM)
		{
			Logger.info("Getting users for current search content.");
			try
			{
				send(new Request(Requests.SEARCH_USER.getName(), new SearchUserWrapper(s)));
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(MessageWrapper m)
	{
		// TODO
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

			Logger.info("Decrypting in phase " + phase.name());
			if (phase == NetworkPhases.COM)
			{
				b = decrypt(AESKey, b);

			} else if (phase == NetworkPhases.PRE2 || phase == NetworkPhases.POST)
			{
				b = decrypt(AESKey, b);
			} else if (phase == NetworkPhases.PRE1)
			{
				if (networkphaseprogress.get(phase)[0])
				{
					b = decrypt(AESKey, b);
				}
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
							case TRSMT_RSAKEY:
								// deprecated
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
							switch (r)
							{
							case RSP_RSAKEY:
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
								Logger.info(r.getName() + " in pre0");
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
								if (((Response) o).getBuffer() instanceof ProtocolWrapper)
								{
									if (!((ProtocolWrapper) ((Response) o).getBuffer()).getProtocolVersion()
											.equals(Main.protocol.getProtocolVersion()))
									{

										Logger.info("Protocol version not en par with server!");
										Logger.info("Update necessary to continue!");
									} else
									{
										Logger.info("Protocol version en par with server!");
										networkphaseprogress.get(phase)[2] = true;
									}
									if (!((ProtocolWrapper) ((Response) o).getBuffer()).getClientVersion()
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
								Logger.info(r.getName() + " in pre1");
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
								if (((Response) o).getBuffer() instanceof LoginResponseWrapper)
								{
									if (!(((LoginResponseWrapper) ((Response) o).getBuffer()).isLoggedIn()))
									{
										Logger.info("Wrong user credentials");
										networkphaseprogress.get(phase)[0] = false;
										Main.getInstance().wrongPassword();
									} else
									{
										Logger.info("Right user credentials! Received token!");
										networkphaseprogress.get(phase)[1] = true;
									}
								}
								break;
							case RSP_TOKEN:
								if (((Response) o).getBuffer() instanceof LoginResponseWrapper)
								{
									if (!(((LoginResponseWrapper) ((Response) o).getBuffer()).isLoggedIn()))
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

							default:
								Logger.info(r.getName() + " in pre1");
								break;
							}
						}
					}
					break;
				case COM:
					for (Responses r : Responses.values())
					{
						if (r.getName().equals(((Response) o).getName()))
						{
							switch (r)
							{
							case RSP_DATA:
								Logger.info("Received user");
								if (((Response) o).getBuffer() instanceof ProfileInfoWrapper)
								{
									if (((ProfileInfoWrapper) ((Response) o).getBuffer()).getUser()
											.getProfilepic() != null
											&& ((ProfileInfoWrapper) ((Response) o).getBuffer()).getUser()
													.getUsername() != null
											&& ((ProfileInfoWrapper) ((Response) o).getBuffer()).getUser()
													.getStatus() != null)
									{
										Main.getInstance().setLocalUser(
												(((ProfileInfoWrapper) ((Response) o).getBuffer()).getUser()));
									}
								}
								break;
							default:
								break;
							}
						}
					}
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

			// initiating stuff
		}
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
			if (!networkphaseprogress.get(phase)[0] && !networkphaseprogress.get(phase)[1])
			{
				Logger.info("Sending AES key..");
				send(new Request(Requests.TRSMT_AESKEY.getName(), new AESKeyWrapper(AESKey)));
				networkphaseprogress.get(phase)[0] = true;
				networkphaseprogress.get(phase)[1] = false;
			}
			if (networkphaseprogress.get(phase)[2] && networkphaseprogress.get(phase)[0])
			{
				Logger.info("advance");
				advance();
			} else if (!networkphaseprogress.get(phase)[2] && !networkphaseprogress.get(phase)[3]
					&& networkphaseprogress.get(phase)[0])
			{
				Logger.info("Sending protocol !");
				send(new Request(Requests.TRSMT_PROTOCOL.getName(), Main.protocol));
				networkphaseprogress.get(phase)[3] = true;
			}
			break;
		case PRE2:
			if (networkphaseprogress.get(phase)[0] == false)
			{
				// do nothing
			} else if (networkphaseprogress.get(phase)[1] == true)
			{
				Logger.info("Logged in, advancing.");

				Platform.runLater(() -> {
					Main.getInstance().setScene(3);
				});
				advance();
			}
			break;
		case COM:
			if (networkphaseprogress.get(phase)[0] == false)
			{
				send(new Request(Requests.REQST_DATA.getName(), null));
				networkphaseprogress.get(phase)[0] = true;
			}
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
			networkphaseprogress.put(NetworkPhases.PRE1, new boolean[5]);
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

	// public void send(Request r) throws Exception
	// {
	// if (current == Integer.MAX_VALUE)
	// {
	// current = 0;
	// } else
	// {
	// current++;
	// }
	// r.setNr(current);
	// byte[] b = serialize(r);
	// if (phase != NetworkPhases.PRE0)
	// {
	// byte[] b0 = encrypt(pub1, b);
	// out.writeInt(b0.length);
	// out.write(b0);
	// } else
	// {
	// out.writeInt(b.length);
	// out.write(b);
	// }
	// out.flush();
	// }
	//
	// public void send(Response r) throws Exception
	// {
	// byte[] b = serialize(r);
	// if (phase != NetworkPhases.PRE0)
	// {
	// byte[] b0 = encrypt(pub1, b);
	// out.writeInt(b0.length);
	// out.write(b0);
	// } else
	// {
	// out.writeInt(b.length);
	// out.write(b);
	// }
	// out.flush();
	// }

	public void send(Request r) throws Exception
	{
		if (phase == NetworkPhases.COM)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Req;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Req;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
			out.writeInt(b0.length);
			out.write(b0);

		} else if (phase == NetworkPhases.PRE2 || phase == NetworkPhases.COM)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Req;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Req;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
			out.writeInt(b0.length);
			out.write(b0);

		} else if (phase == NetworkPhases.PRE1)
		{
			if (networkphaseprogress.get(phase)[0])
			{
				String[] sa = getStrings(r.getBuffer());
				String s = null;
				if (sa.length == 1)
				{
					s = "Req;" + r.getName();
					s = s + ";" + sa[0];
				} else if (sa.length > 1)
				{
					s = "Req;" + r.getName();
					for (int i = 0; i < sa.length; i++)
					{
						s = s + ";" + sa[i];
					}
				}
				Logger.info("Sending " + s);
				byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
				out.writeInt(b0.length);
				out.write(b0);
			} else
			{
				String[] sa = getStrings(r.getBuffer());
				String s = null;
				if (sa.length == 1)
				{
					s = "Req;" + r.getName();
					s = s + ";" + sa[0];
				} else if (sa.length > 1)
				{
					s = "Req;" + r.getName();
					for (int i = 0; i < sa.length; i++)
					{
						s = s + ";" + sa[i];
					}
				}
				Logger.info("Sending " + s);
				byte[] b0 = s.getBytes("UTF8");
				out.writeInt(b0.length);
				out.write(b0);
			}
		} else if (phase == NetworkPhases.PRE0)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Req;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Req;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = s.getBytes("UTF8");
			out.writeInt(b0.length);
			out.write(b0);

		} else
		{
			Logger.info("Unknown phase.");
		}
		out.flush();
	}

	public void send(Response r) throws Exception
	{
		if (phase == NetworkPhases.COM)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Res;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Res;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
			out.writeInt(b0.length);
			out.write(b0);

		} else if (phase == NetworkPhases.PRE2 || phase == NetworkPhases.COM)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Res;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Res;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
			out.writeInt(b0.length);
			out.write(b0);

		} else if (phase == NetworkPhases.PRE1)
		{
			if (networkphaseprogress.get(phase)[0])
			{
				String[] sa = getStrings(r.getBuffer());
				String s = null;
				if (sa.length == 1)
				{
					s = "Res;" + r.getName();
					s = s + ";" + sa[0];
				} else if (sa.length > 1)
				{
					s = "Res;" + r.getName();
					for (int i = 0; i < sa.length; i++)
					{
						s = s + ";" + sa[i];
					}
				}
				Logger.info("Sending " + s);
				byte[] b0 = encrypt(AESKey, s.getBytes("UTF8"));
				out.writeInt(b0.length);
				out.write(b0);
			} else
			{
				String[] sa = getStrings(r.getBuffer());
				String s = null;
				if (sa.length == 1)
				{
					s = "Res;" + r.getName();
					s = s + ";" + sa[0];
				} else if (sa.length > 1)
				{
					s = "Res;" + r.getName();
					for (int i = 0; i < sa.length; i++)
					{
						s = s + ";" + sa[i];
					}
				}
				Logger.info("Sending " + s);
				byte[] b0 = s.getBytes("UTF8");
				out.writeInt(b0.length);
				out.write(b0);
			}
		} else if (phase == NetworkPhases.PRE0)
		{
			String[] sa = getStrings(r.getBuffer());
			String s = null;
			if (sa.length == 1)
			{
				s = "Res;" + r.getName();
				s = s + ";" + sa[0];
			} else if (sa.length > 1)
			{
				s = "Res;" + r.getName();
				for (int i = 0; i < sa.length; i++)
				{
					s = s + ";" + sa[i];
				}
			}
			Logger.info("Sending " + s);
			byte[] b0 = s.getBytes("UTF8");
			out.writeInt(b0.length);
			out.write(b0);

		} else
		{
			Logger.info("Unknown phase.");
		}
		out.flush();
	}

	private String[] getStrings(Object o) throws UnsupportedEncodingException
	{
		if (o == null)
		{
			return new String[]
			{ "null" };
		}
		if (o instanceof Wrapper)
		{
			return ((networking.types.Wrapper) o).getStrings();
		}
		if (o instanceof Key)
		{
			if (o instanceof PublicKey)
			{
				return new String[]
				{ decodePublicKey((PublicKey) o) };
			} else if (o instanceof PrivateKey)
			{
				return new String[]
				{ decodePrivateKey((PrivateKey) o) };
			}
		}
		if (o instanceof String)
		{
			return new String[]
			{ (String) o };
		}
		if (o instanceof Boolean)
		{
			return new String[]
			{ ((boolean) o) ? "true" : "false" };
		}
		throw new RuntimeException("Could not convert Object to string");
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

	public static byte[] encrypt(SecretKey key, byte[] msg) throws NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException
	{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(msg);
	}

	public static byte[] decrypt(SecretKey key, byte[] encrypted) throws NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
	{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encrypted);
	}

	private Object deserialize(byte[] b) throws UnsupportedEncodingException, BadPacketException
	{
		String s = new String(b, "UTF8");
		Logger.info("Deserializing string: " + ((s.length() < 100) ? s : s.substring(0, 99)));
		String[] temp = s.split(";");
		String[] info = new String[]
		{ temp[0], temp[1] };
		String[] data = new String[temp.length - 2];
		for (int i = 2; i < temp.length; i++)
		{
			data[i - 2] = temp[i];
		}
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == "null")
			{
				data[i] = null;
			}
		}
		if (info[0].equals("Req"))
		{
			for (Requests r : Requests.values())
			{
				Logger.info("checking some stuff");
				Logger.info(info[1]);
				if (r.getName().equals(info[1]))
				{
					Logger.info(r.getName());
					if (r.getType().getSuperclass() != null)
					{
						if (r.getType().getSuperclass().equals(Wrapper.class))
						{
							// this is 100% safe
							return new Request(info[1],
									Wrapper.getWrapper((Class<? extends Wrapper>) r.getType(), data));
						}
					} else if (r.getType().equals(PublicKey.class))
					{
						try
						{
							return new Request(info[1], loadPublicKey(data[0]));
						} catch (GeneralSecurityException e)
						{
							e.printStackTrace();
						}
					} else
					{
						String stemp = null;
						for (String c : data)
						{
							stemp += c;
						}
						return new Request(info[1], stemp);
					}
				}
			}
			throw new BadPacketException("Package malfunctional");
		} else if (info[0].equals("Res"))
		{
			for (Responses r : Responses.values())
			{
				Logger.info("checking some stuff");
				if (r.getName().equals(info[1]))
				{
					Logger.info(r.getName());
					if (r.getType().getSuperclass() != null)
					{
						if (r.getType().getSuperclass().equals(Wrapper.class))
						{
							Logger.info("Getting wrapper...");
							Logger.info(r.getType().getName());
							return new Response(info[1],
									Wrapper.getWrapper((Class<? extends Wrapper>) r.getType(), data));
						}
					} else if (r.getType().equals(PublicKey.class))
					{
						try
						{
							return new Response(info[1], loadPublicKey(data[0]));
						} catch (GeneralSecurityException e)
						{
							e.printStackTrace();
						}
					} else
					{
						String stemp = null;
						for (String c : data)
						{
							stemp += c;
						}
						return new Response(info[1], stemp);
					}
				}
			}
			throw new BadPacketException("Package not properly built");
		} else

		{
			throw new BadPacketException("Type of package not declared");
		}

	}

	private PrivateKey loadPrivateKey(String key) throws GeneralSecurityException, UnsupportedEncodingException
	{
		return KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key.getBytes("UTF8"))));
	}

	public static PublicKey loadPublicKey(String key) throws GeneralSecurityException, UnsupportedEncodingException
	{
		return KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key.getBytes("UTF8"))));
	}

	public static String decodePublicKey(PublicKey key) throws UnsupportedEncodingException
	{
		return new String(Base64.getEncoder().encode(key.getEncoded()), "UTF8");
	}

	public static String decodePrivateKey(PrivateKey key)
	{
		return new String(new PKCS8EncodedKeySpec(key.getEncoded()).getEncoded());
	}

}
