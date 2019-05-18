package de.datastorage.main;

public enum Settings
{
	KEEPLOGGEDIN("keeploggedin", Phases.PRE, false, null),
	LANGUAGE("language", Phases.PRE, "DE", "Language"),
	ENCRYPTION("encryption_level", Phases.PRE, "1024", "Encryption level");

	private final String name;
	private final Phases phase;
	private final Object defaultValue;
	private final String comment;

	/**
	 * 
	 * @param name
	 * @param phase
	 */
	Settings(final String name, Phases phase, Object defaultValue, String comment)
	{
		this.name = name;
		this.phase = phase;
		this.defaultValue = defaultValue;
		this.comment = comment;
	}

	public String getName()
	{
		return name;
	}

	public Phases getPhase()
	{
		return phase;
	}

	public Object getDefaultValue()
	{
		return defaultValue;
	}

	public String getComment()
	{
		return comment;
	}
}
