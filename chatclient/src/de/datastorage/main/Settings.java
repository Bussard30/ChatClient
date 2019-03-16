package de.datastorage.main;

public enum Settings
{
	KEEPLOGGEDIN("keeploggedin", Phases.PRE, false, null), AUTOSAVE_INTERVAL("autosave_interval", Phases.MID, 10,
			"value in minutes");

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
