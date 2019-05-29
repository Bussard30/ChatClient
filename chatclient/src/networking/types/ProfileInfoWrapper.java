package networking.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Base64;

import javax.imageio.ImageIO;

import de.exceptions.EmailFormationException;
import de.types.Email;
import de.types.User;

public class ProfileInfoWrapper extends Wrapper
{
	private User user;

	public ProfileInfoWrapper()
	{
		// TODO Auto-generated constructor stub
	}

	public ProfileInfoWrapper(String[] s)
	{

			try
			{
				user = new User(s[2] != null ? new Email(s[2]) : null, s[0], s[1], s[3],
						s[3] != null ? ImageIO.read(new ByteArrayInputStream(s[3].getBytes())) : null,
						s[4] != null ? new Date(Long.valueOf(s[4])) : null, s[5] != null ? s[5].getBytes() : null);
			} catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EmailFormationException e)
			{
				try
				{
					user = new User(null, s[0], s[1], s[3],
							s[3] != null ? ImageIO.read(new ByteArrayInputStream(s[3].getBytes())) : null,
							s[4] != null ? new Date(Long.valueOf(s[4])) : null, s[5] != null ? s[5].getBytes() : null);
				} catch (NumberFormatException | IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	@Override
	public String[] getStrings()
	{
		String string = null;
		if (user.getProfilepic() != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try
			{
				ImageIO.write(user.getProfilepic(), "png", baos);
			} catch (IOException e2)
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try
			{
				baos.flush();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			string = Base64.getEncoder().encodeToString(baos.toByteArray());
			try
			{
				baos.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new String[]
		{ user.getUsername() != null ? user.getUsername() : "null",
				user.getPassword() != null ? user.getPassword() : "null",
				user.getEmail() != null ? user.getEmail().getLocal() + "@" + user.getEmail().getDomain() + "."
						+ user.getEmail().getTLD() : "null",
				user.getStatus() != null ? user.getStatus() : "null", user.getProfilepic() != null ? string : "null",
				user.getDate() != null ? String.valueOf(user.getDate().getTime()) : "null",
				user.getUuid() != null ? new String(user.getUuid()) : "null" };
	}

	public User getUser()
	{
		return user;
	}
}
