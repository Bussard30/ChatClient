/*
 *
 * (C) Copyright 2018 Bussard30.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package de.register;

import java.net.URL;
import java.util.ResourceBundle;

import de.Main;
import de.networking.logger.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class RegisterController implements Initializable
{

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// TODO
	}

	@FXML
	private void commitKey(KeyEvent event)
	{
		if (event.getCode() == KeyCode.ENTER)
		{
			Logger.info("Your data will be submitted.");

			
		} else if (event.getCode() == KeyCode.ESCAPE)
		{
			Main.getInstance().setScene(0);
		}
	}

	@FXML
	private void back(MouseEvent event)
	{
		Main.getInstance().setScene(0);
	}

	@FXML
	private void minimize(MouseEvent event)
	{
		Main.getInstance().getPrimaryStage().setIconified(true);
	}

	@FXML
	private void close(MouseEvent event)
	{
		System.exit(0);
	}

	@FXML
	private void buttonCursor(MouseEvent event)
	{
		Region r = (Region) event.getSource();
		r.setCursor(Cursor.HAND);
	}

	@FXML
	private void normalCursor(MouseEvent event)
	{
		Region r = (Region) event.getSource();
		r.setCursor(Cursor.DEFAULT);
	}
}
