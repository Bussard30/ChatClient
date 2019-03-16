package de.datastorage.main;

public enum Phases
{
	PRE("pre"), MID("mid"), POST("post");

	private final String name;

	Phases(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
