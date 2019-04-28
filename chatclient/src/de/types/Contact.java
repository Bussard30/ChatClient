package de.types;

import java.awt.image.BufferedImage;

public class Contact
{
	private BufferedImage image;
	private String name, status;
	
	
	public Contact(BufferedImage image, String name, String status)
	{
		this.image = image;
		this.name = name;
		this.status = status;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getStatus()
	{
		return status;
	}
}
