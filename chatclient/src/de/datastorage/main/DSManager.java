package de.datastorage.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.filechooser.FileSystemView;

import de.networking.logger.Logger;

public class DSManager
{
	public static String path;
	private static DSManager dsm;

	private HashMap<File, Integer> locks;
	private File options, contacts, chats, password, generaldata, log;

	private HashMap<Settings, Object> settings;
	private Calendar cal;
	private SimpleDateFormat sdf;

	private Thread t;
	private boolean isLogging = true;
	private volatile Vector<String> queue = new Vector<>();

	public DSManager()
	{
		if (dsm == null)
		{
			dsm = this;
		} else
		{
			throw new RuntimeException("Already instantiated");
		}
		path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\" + "ChatClient";
		t = new Thread(new Runnable()
		{

			@SuppressWarnings("unchecked")
			/**
			 * actually checked but eclipse annoys me and i don't want to have
			 * to configure problem severity
			 */
			@Override
			public void run()
			{
				Vector<String> temp = new Vector<>();
				while (isLogging)
				{
					if (!queue.isEmpty())
					{
						for (String s : (Vector<String>) queue.clone())
						{
							if (s != null)
							{
								try
								{
									PrintWriter pw = new PrintWriter(new FileWriter(log, true));
									pw.append(s);
									pw.flush();
									pw.close();
									temp.add(s);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							} else
							{
								temp.add(s);
							}
						}
						for (String s : temp)
						{
							queue.removeElement(s);
						}
						temp.clear();
					}
					try
					{
						Thread.sleep(30);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		});
		init();
		File f = new File(path);
		if (!f.exists())
		{
			if (!f.mkdirs())
			{
				throw new RuntimeException("Couldn't create directory");
			}
		}
		t.start();
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

		System.out.println(path);
		settings = new HashMap<>();
		for (Settings s : Settings.values())
		{
			settings.put(s, null);
		}
		options = new File(path + "\\options.dat");
		contacts = new File(path + "\\contacts.dat");
		chats = new File(path + "\\chats.dat");
		generaldata = new File(path + "\\generaldata.dat");

		log = new File(path + "\\logs" + "\\" + sdf.format(cal.getTime()) + ".txt");

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
			pWOptions.println("#" + "Options file of ChatClient. Do not edit unless you know what you're doing.");
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
			pWContacts.println("#" + "Contacts file of ChatClient. Do not edit unless you know what you're doing.");
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
			pWChats.println("#" + "Chats file of ChatClient. Do not edit unless you know what you're doing.");
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
			pWgd.println("#" + "Chats file of ChatClient. Do not edit unless you know what you're doing.");

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
		// Checking content...
		try
		{
			clear(options);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			createOptions(getValuesOfCurrentOptions());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createOptions(HashMap<Settings, Object> m)
	{
		PrintWriter pw = null;
		try
		{
			pw = new PrintWriter(new FileOutputStream(options, false));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		Logger.info("Printing options...");
		pw.println("#" + "Options file of ChatClient. Do not edit unless you know what you're doing.");
		for (Phases p : Phases.values())
		{
			pw.println("#" + p.getName());
			for (Settings s : Settings.values())
			{
				if (s.getPhase() == p)
				{
					if (m.containsKey(s))
					{
						pw.println("#" + s.getComment());
						pw.println(s.getName() + " = " + m.get(s).toString());
					} else
					{
						pw.println("#" + s.getComment());
						pw.println(s.getName() + " = " + s.getDefaultValue());
					}
				}
			}
			pw.println();
		}
		pw.flush();
		Logger.info("Done.");
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
	 * 
	 * @throws FileNotFoundException
	 */
	public void appendLine(String s) throws FileNotFoundException
	{
		queue.add(s);
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
						if (s.getName().equals(st.sval))
						{
							assignment = s;
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
			if (line.replaceAll("\\s", "").startsWith(set.getName()))
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
		Logger.info("Editing operation of setting \"" + set.getName() + "\" to the value " + value.toString() + " took "
				+ Long.toOctalString(System.currentTimeMillis() - l) + "ms");
		return true;
	}

	public void clear(File target) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(target));

		StringBuffer inputBuffer = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null)
		{
			boolean b = false;
			for (Settings set : Settings.values())
			{
				if (line.replaceAll("\\s", "").startsWith(set.getName()) || line.replaceAll("\\s", "").startsWith("#")
						|| line.replaceAll("\\s", "").length() == 0)
				{
					b = true;
					break;
				}
			}

			if (b)
			{
				inputBuffer.append(line);
				inputBuffer.append('\n');
			} else
			{
				Logger.info("Removed line : " + line);
			}
		}
		PrintWriter pw = new PrintWriter(options);
		pw.print(inputBuffer.toString());
		pw.flush();

		pw.close();
		br.close();
	}

	public Object getSetting(Settings set) throws IOException
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
					if (((st.nval % 1) == 0))
					{
						value = new Integer(new Double(st.nval).intValue());
					} else
					{
						value = st.nval;
					}
					// value = ((st.nval % 1) == 0) ? new Integer(new
					// Double(st.nval).intValue()) : st.nval;
					Logger.info("Value \"" + value + "\" found for setting "
							+ (assignment != null ? assignment.getName() : "NO_SETTING_SET"));
					System.out.println((st.nval % 1 == 0));
					System.out.println(value.getClass());
					if (assignment != null)
						return value;
				}
			} else if (tval == StreamTokenizer.TT_WORD)
			{
				if (!assignValue)
				{
					for (Settings s : Settings.values())
					{
						if (s.getName().equals(st.sval) && s.getName().equals(set.getName()))
						{
							assignment = s;
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
					if (assignment != null)
						return value;
				}
			} else if (tval == StreamTokenizer.TT_EOL)
			{
				assignValue = false;
				assignment = null;
			} else if ((char) st.ttype == '=' && assignment != null)
			{
				assignValue = true;
			}
		}
		throw new RuntimeException();
	}

	public HashMap<Settings, Object> getValuesOfCurrentOptions() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(options));
		HashMap<Settings, Object> temp = new HashMap<>();

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
					if (((st.nval % 1) == 0))
					{
						value = new Integer(new Double(st.nval).intValue());
					} else
					{
						value = st.nval;
					}
					// value = ((st.nval % 1) == 0) ? new Integer(new
					// Double(st.nval).intValue()) : st.nval;
					Logger.info("Value \"" + value + "\" found for setting "
							+ (assignment != null ? assignment.getName() : "NO_SETTING_SET"));
					System.out.println((st.nval % 1 == 0));
					System.out.println(value.getClass());
					if (assignment != null)
						temp.put(assignment, value);
				}
			} else if (tval == StreamTokenizer.TT_WORD)
			{
				if (!assignValue)
				{
					for (Settings s : Settings.values())
					{
						if (s.getName().equals(st.sval))
						{
							assignment = s;
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
					if (assignment != null)
						temp.put(assignment, value);
				}
			} else if (tval == StreamTokenizer.TT_EOL)
			{
				assignValue = false;
				assignment = null;
			} else if ((char) st.ttype == '=' && assignment != null)
			{
				assignValue = true;
			}
		}
		for (Map.Entry<Settings, Object> m : temp.entrySet())
		{
			Logger.info(m.getKey().getName());
		}
		return temp;
	}

	public static DSManager getInstance()
	{
		return dsm;
	}

}
