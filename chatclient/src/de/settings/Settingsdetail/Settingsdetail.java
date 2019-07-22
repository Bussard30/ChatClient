package de.settings.Settingsdetail;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class Settingsdetail implements Initializable
{
	@FXML
	private TextField textfield;

	@FXML
	private Pane pane;

	public Settingsdetail(String text, String type)
	{
		textfield.setText(text);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// TODO Auto-generated method stub

	}
}