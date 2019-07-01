package networking.types;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

/**
 * Holds audio for voice chat.
 * @author Bussard30
 *
 */
public class AudioWrapper extends Wrapper
{
	private byte[] data;
	public AudioWrapper(String[] s)
	{
		assert s.length == 1;
		data = Base64.getDecoder().decode(s[0]);
	}
	
	@Override
	public String[] getStrings()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream go;
		try
		{
			go = new GZIPOutputStream(baos);
			go.write(data);
	        go.flush();
	        go.close();
	        baos.flush();
	        baos.close();
	        return new String[]{Base64.getEncoder().encodeToString(data)};
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return new String[]{};
	}

}
