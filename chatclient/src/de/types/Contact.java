package de.types;

import java.awt.image.BufferedImage;

public class Contact
{
	private BufferedImage image;
	private String name;

	public Contact(BufferedImage image, String name)
	{
		this.image = image;
		this.name = name;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public String getName()
	{
		return name;
	}
}
