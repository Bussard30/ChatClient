package de.networking.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import networking.types.Request;

public class MicThread extends Thread
{
	public static AudioFormat defaultFormat = new AudioFormat(11025f, 8, 1, true, true);
	// 11.025khz, 8bit, mono, signed, big endian (changes nothing in 8 bit)
	// ~8kb/s
	public static int defaultDataLength = 300;
	public static double amplification = 1.0;
	private TargetDataLine mic;
	public static boolean recording = false;

	public MicThread() throws LineUnavailableException
	{
		// open microphone line, an exception is thrown in case of error
		AudioFormat af = defaultFormat;
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, null);
		mic = (TargetDataLine) (AudioSystem.getLine(info));
		mic.open(af);
		mic.start();
		
	}

	@Override
	public void run()
	{
		while (recording)
		{
			if (mic.available() >= defaultDataLength)
			{
				byte[] buff = new byte[defaultDataLength];
				while (mic.available() >= defaultDataLength)
				{
					mic.read(buff, 0, buff.length); // read from microphone
				}
				// this part is used to decide whether to send or not the
				// packet. if volume is too low, an empty packet will be
				// sent and the remote client will play some comfort noise
				long tot = 0;
				for (int i = 0; i < buff.length; i++)
				{
					buff[i] *= amplification;
					tot += Math.abs(buff[i]);
				}
				tot *= 2.5;
				tot /= buff.length;
                if(tot != 0)
                {
                	try
					{
						Client.getInstance().sendRequest(new Request(Requests.TRSMT_AUDIO.getName(), buff));
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }

			} else
			{
				try
				{
					Thread.sleep(1);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		this.interrupt();
	}
}