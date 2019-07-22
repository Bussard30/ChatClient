package de.settings;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SettingsViewCell extends ListCell<String>
{

	@Override
	protected void updateItem(String item, boolean empty)
	{
		super.updateItem(item, empty);
		setTextFill(Color.rgb(238, 237, 237));
		setFont(new Font(16));
		if(item != null || item == "" || item == "null")
		{
		setAlignment(Pos.CENTER_RIGHT);
		setText(item + "  ");
		}

	}

	public SettingsViewCell()
	{

	}
}
