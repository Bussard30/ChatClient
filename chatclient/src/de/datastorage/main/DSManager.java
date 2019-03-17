package de.datastorage.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import de.networking.logger.Logger;

public class DSManager
{
	public static String appdata;
	private static DSManager dsm;

	private HashMap<File, Integer> locks;
	private File options, contacts, chats, password, generaldata, log;

	private HashMap<Settings, Object> settings;
	private Calendar cal;
	private SimpleDateFormat sdf;

	public DSManager()
	{
		if (dsm == null)
		{
			dsm = this;
		} else
		{
			throw new RuntimeException("Already instantiated");
		}
		appdata = System.getenv("APPDATA") + "\\" + "ChatClient";
		init();
		File f = new File(appdata);
		if (!f.exists())
		{
			if (!f.mkdirs())
			{
				throw new RuntimeException("Couldn't create directory");
			}
		}
	}

	/**
	 * Files to be fetched: options contacts chats ...
	 * 
	 * @throws IOException
	 */
	private void init()
	{

		PrintWriter pWOptions = null, pWContacts = null, pWChats = null, pWgd = null, pWL = null;
		cal = Calendar.getInstance();
		sdf = new SimpleDateFormat("d-M-Y-HH-mm-ss");

		System.out.println(appdata);
		settings = new HashMap<>();
		for (Settings s : Settings.values())
		{
			settings.put(s, null);
		}
		options = new File(appdata + "\\options.dat");
		contacts = new File(appdata + "\\contacts.dat");
		chats = new File(appdata + "\\chats.dat");
		generaldata = new File(appdata + "\\generaldata.dat");

		log = new File(appdata + "\\logs" + "\\" + sdf.format(cal.getTime()) + ".txt");

		if (!log.getParentFile().exists())
		{
			if (!log.getParentFile().mkdirs())
				throw new RuntimeException();
		}

		if (!log.exists())
		{
			try
			{
				log.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				pWL = new PrintWriter(log);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

		} else
		{
			try
			{
				pWL = new PrintWriter(log);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		Logger.info("Creating PWs/BRs.");

		if (!options.exists())
		{
			try
			{
				options.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				pWOptions = new PrintWriter(new FileOutputStream(options, false));
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			Logger.info("Printing default options...");
			pWOptions.println("#"
					+ "Options file of ChatClient. Do not edit unless you know what you're doing (except you are a dum dum).");
			for (Phases p : Phases.values())
			{
				pWOptions.println("#" + p.getName());
				for (Settings s : Settings.values())
				{
					if (s.getPhase() == p)
					{
						if (s.getComment() != null)
							pWOptions.println("#" + s.getComment());
						pWOptions.println(s.getName() + " = " + s.getDefaultValue());
					}
				}
				pWOptions.println();
			}
			pWOptions.flush();
			Logger.info("Done.");
		} else
		{

		}

		if (!contacts.exists())
		{
			try
			{
				contacts.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				pWContacts = new PrintWriter(contacts);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			Logger.info("Printing default contacts...");
			pWContacts.println("#"
					+ "Contacts file of ChatClient. Do not edit unless you know what you're doing (except you are a dum dum).");
			Logger.info("Done.");
		} else
		{
			Logger.info("Contacts ...");
			try
			{
				pWContacts = new PrintWriter(contacts);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		if (!chats.exists())
		{
			try
			{
				chats.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				pWChats = new PrintWriter(chats);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}

			Logger.info("Printing default chats...");
			pWChats.println("#"
					+ "Chats file of ChatClient. Do not edit unless you know what you're doing (except you are a dum dum).");
			Logger.info("Done.");
		} else
		{
			Logger.info("Chats ...");
			try
			{
				pWChats = new PrintWriter(chats);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		if (!generaldata.exists())
		{
			try
			{
				generaldata.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				pWgd = new PrintWriter(generaldata);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			Logger.info("Printing default general data...");
			pWgd.println("#"
					+ "Chats file of ChatClient. Do not edit unless you know what you're doing (except you are a dum dum).");

			Logger.info("Done.");
		} else
		{
			Logger.info("General data ...");
			try
			{
				pWgd = new PrintWriter(generaldata);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		Logger.info("Done creating PWs/BRs.");
	}

	public static String readStringFromURL(String requestURL) throws IOException
	{
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString()))
		{
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}

	/**
	 * This method is only being used in de.networking.logger.Logger
	 * @throws FileNotFoundException 
	 */
	public void appendLine(String s) throws FileNotFoundException
	{
		PrintWriter pw = new PrintWriter(new FileOutputStream(log,true));
		pw.append(s);
		pw.flush();
		pw.close();
	}

	public void readOptions() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(options));

		StreamTokenizer st = new StreamTokenizer(br);
		st.commentChar('#');
		st.eolIsSignificant(true);
		st.wordChars("_".getBytes()[0], "_".getBytes()[0]);

		Settings assignment = null;
		boolean assignValue = false;
		Object value = null;

		for (int tval; (tval = st.nextToken()) != StreamTokenizer.TT_EOF;)
		{
			if (tval == StreamTokenizer.TT_NUMBER)
			{
				if (assignValue)
				{
					value = st.nval;
					Logger.info("Value \"" + st.nval + "\" found for setting "
							+ (assignment != null ? assignment.getName() : "NO_SETTING_SET"));
					if (assignment != null)
						settings.put(assignment, value);
				}
			} else if (tval == StreamTokenizer.TT_WORD)
			{
				if (!assignValue)
				{
					for (Settings s : Settings.values())
					{
						Logger.info("Checking setting " + s.getName());

						if (s.getName().equals(st.sval))
						{
							assignment = s;
							Logger.info("Setting found: " + st.sval);
						}
					}
				} else
				{
					switch (st.sval)
					{
					case "true":
						value = (Boolean) true;
						break;
					case "false":
						value = (Boolean) false;
						break;
					default:
						value = st.sval;
						break;
					}
					Logger.info("Value \"" + st.sval + "\" found for setting "
							+ (assignment != null ? assignment.getName() : "NO_SETTING_SET"));
					if (assignment != null)
						settings.put(assignment, value);
				}
			} else if (tval == StreamTokenizer.TT_EOL)
			{
				assignValue = false;
				assignment = null;
			} else if ((char) st.ttype == '=' && assignment != null)
			{
				assignValue = true;
				Logger.info("Assigning value ...");
			}
		}
		for (Map.Entry<Settings, Object> m : settings.entrySet())
		{
			System.out.println(m.getKey() + " " + m.getValue());
		}
		br.close();
	}

	public boolean editSetting(Settings set, Object value) throws IOException
	{
		long l = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new FileReader(options));

		StringBuffer inputBuffer = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null)
		{
			if (line.startsWith(set.getName()))
			{
				line = set.getName() + " = " + value.toString();
			}
			inputBuffer.append(line);
			inputBuffer.append('\n');
		}
		String inputStr = inputBuffer.toString();

		PrintWriter pw = new PrintWriter(options);
		pw.print(inputStr);
		pw.flush();

		br.close();
		pw.close();
		Logger.info("Editing operation of setting \"" + set.getName() + "\" took " + Long.toOctalString(System.currentTimeMillis() - l) + "ms");
		return true;
	}

	public static DSManager getInstance()
	{
		return dsm;
	}

}