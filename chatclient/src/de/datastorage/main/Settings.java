package de.datastorage.main;

public enum Settings
{
	KEEPLOGGEDIN("keeploggedin", Phases.PRE, false, null),
	LANGUAGE("language", Phases.PRE, "DE", "Language"),
	ENCRYPTION("encryption_level", Phases.PRE, "1024", "Encryption level"),
	
	AUDIO_INPUT("audio_input", Phases.MID, "default", "Defines microphone which is being used for voice calls"),
	AUDIO_INPUT_VOLUME("audio_input_volume", Phases.MID, 100, "Audio input volume defined in per cent"),
	AUDIO_INPUT_THRESHOLD_TYPE("audio_input_threshold_type", Phases.MID, "VoiceActivation", "Defines type of threshold for audio input"),
	AUDIO_INPUT_THRESHOLD_VALUE("audio_input_threshold_value", Phases.MID, 0, "Audio input threshold for when to be sending packets"),
	
	AUDIO_OUTPUT("audio_output", Phases.MID, "default", "Defines microphone which is being used for emitting sound"),
	AUDIO_OUTPUT_VOLUME("audio_output_volume", Phases.MID, 100, "Audio output volume defined in per cent"),
	
	AUTO_REPLACE_SMILEYS("auto_replace_smileys", Phases.MID, true, "Defines whether text like \":)\" should be replaced with a smiley"),
	
	FONT_SCALING("font_scaling", Phases.PRE, 16, "Defines font scaling"),
	
	START_ON_SYSTEN_STARTUP("start_on_system_startup", Phases.PRE, true, "If true ChatClient will boot in system startup"),
	
	DEBUG("debug", Phases.PRE, false, "Enables debug mode.")
	
	
	;
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
