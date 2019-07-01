package de.networking.client;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class SpeakerThread extends Thread
{
	private ArrayList<byte[]> queue = new ArrayList<byte[]>();
	@SuppressWarnings("unused")
	private int lastSoundPacketLen = defaultDataLength;

	public static int defaultDataLength = 300;
	public static double amplification = 1.0;
	public static AudioFormat defaultFormat = new AudioFormat(11025f, 8, 1, true, true);
	public static boolean open;

	public SpeakerThread()
	{

	}

	public void closeAndKill()
	{
		if (speaker != null)
		{
			speaker.close();
		}
		interrupt();
	}

	/**
	 * Add a byte sequence to be played
	 * @param b the byte sequence
	 */
	public void addToQueue(byte[] b)
	{
		queue.add(b);
	}

	private SourceDataLine speaker = null; // speaker

	@Override
	public void run()
	{
		try
		{
			// open channel to sound card
			AudioFormat af = defaultFormat;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
			speaker = (SourceDataLine) AudioSystem.getLine(info);
			speaker.open(af);
			speaker.start();
			// sound card ready
			while (open)
			{
				if (queue.isEmpty())
				{ // nothing to play, wait
					Thread.sleep(1);
					continue;
				} else
				{// we got something to play
					byte[] in = queue.get(0);
					queue.remove(in);
					speaker.write(in, 0, in.length);
					lastSoundPacketLen = in.length;
				}
			}
		} catch (Exception e)
		{ // sound card error or connection error, stop
			if (speaker != null)
			{
				speaker.close();
			}
			this.interrupt();
		}
	}
}