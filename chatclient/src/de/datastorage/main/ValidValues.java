package de.datastorage.main;

public enum ValidValues
{
	KEEPLOGGEDIN(Settings.KEEPLOGGEDIN, true, new Object[]{ true, false }),
	LANGUAGE(Settings.LANGUAGE, true, new Object[]{ "DE", "EN", "de", "en" }),
	ENCRYPTION(Settings.ENCRYPTION, true, new Object[]{ 128, 256, 512, 1024, 2048, 3072 }),

	AUDIO_INPUT(Settings.AUDIO_INPUT,false, new Object[]{}), 
	AUDIO_INPUT_VOLUME(Settings.AUDIO_INPUT_VOLUME, false, new Object[]{}),
	AUDIO_INPUT_THRESHOLD_TYPE(Settings.AUDIO_INPUT_THRESHOLD_TYPE, true, new Object[]{"VoiceActivation", "PushToTalk"}),
	AUDIO_INPUT_THRESHOLD_VALUE(Settings.AUDIO_INPUT_THRESHOLD_VALUE, false, new Object[]{}),

	AUDIO_OUTPUT(Settings.AUDIO_OUTPUT, false, new Object[]{}),
	AUDIO_OUTPUT_VOLUME(Settings.AUDIO_OUTPUT_VOLUME, false, new Object[]{}),

	AUTO_REPLACE_SMILEYS(Settings.AUTO_REPLACE_SMILEYS, true, new Object[]{true, false}),

	FONT_SCALING(Settings.FONT_SCALING, false, new Object[]{}),

	START_ON_SYSTEN_STARTUP(Settings.START_ON_SYSTEN_STARTUP, false, new Object[]{}),

	DEBUG(Settings.DEBUG, true, new Object[]{true, false});
	
	private final Settings s;
	private final Object[] values;
	private final boolean hasValidValueRestriction;

	ValidValues(Settings s, boolean hasValidValueRestriction, Object[] values)
	{
		this.s = s;
		this.values = values;
		this.hasValidValueRestriction = hasValidValueRestriction;
	}

	public Settings getSetting()
	{
		return s;
	}

	public Object[] getValidValues()
	{
		return values;
	}

	public boolean hasValidValueRestriction()
	{
		return hasValidValueRestriction;
	}

}
