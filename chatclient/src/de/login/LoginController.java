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
package de.login;

import java.net.URL;
import java.util.ResourceBundle;

import de.Main;
import de.networking.client.Client;
import de.networking.logger.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class LoginController implements Initializable
{

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{

	}

	private double xOffset;
	private double yOffset;

	@FXML
	private void mousePressed(MouseEvent event)
	{
		xOffset = Main.getInstance().getPrimaryStage().getX() - event.getScreenX();
		yOffset = Main.getInstance().getPrimaryStage().getY() - event.getScreenY();
	}

	@FXML
	private void mouseDragged(MouseEvent event)
	{
		Main.getInstance().getPrimaryStage().setX(event.getScreenX() + xOffset);
		Main.getInstance().getPrimaryStage().setY(event.getScreenY() + yOffset);
	}

	@FXML
	private void commitKey(KeyEvent event)
	{
		((Region) event.getSource()).setStyle("-fx-border-width: 0 0 0 0;");
		if (event.getCode() == KeyCode.ENTER)
		{
			login();
		} else if (event.getCode() == KeyCode.ESCAPE)
		{
			// NOTHING
		}

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
	private Text fp;

	@FXML
	private void fpentered(MouseEvent event)
	{
		fp.setStyle("-fx-underline: true;");
	}

	@FXML
	private void fpexited(MouseEvent event)
	{
		fp.setStyle("-fx-underline: false;");
	}

	@FXML
	private Text r;

	@FXML
	private void rentered(MouseEvent event)
	{
		r.setStyle("-fx-underline: true;");
		;
	}

	@FXML
	private void rexited(MouseEvent event)
	{
		r.setStyle("-fx-underline: false;");
	}

	@FXML
	private void forgotPassword(MouseEvent event)
	{
		Main.getInstance().setScene(1);
	}

	@FXML
	private void register(MouseEvent event)
	{
		Main.getInstance().setScene(2);
	}

	@FXML
	private void login(ActionEvent event)
	{
		login();
	}

	@FXML
	private void resetBorder(Event event)
	{
		Region r = (Region) event.getSource();
		r.setStyle("-fx-border-width: 0 0 0 0;");
	}

	private void login()
	{
		Logger.info("Submitting login data...");
		TextField user = (TextField) Main.getInstance().getLoginLoader().getNamespace().get("username");
		PasswordField pw = (PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password");
		CheckBox cb = (CheckBox) Main.getInstance().getLoginLoader().getNamespace().get("keeploggedin");
		Logger.info(user.getText());
		Logger.info(pw.getText());
		Logger.info(cb.isSelected() ? "Keep logged in !" : "Do not keep logged in!");

		if (pw.getText().length() == 0 && user.getText().length() == 0)
		{
			((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password"))
					.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
			((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username"))
					.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
		} else if (pw.getText().length() == 0)
		{
			((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password"))
					.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
		} else if (user.getText().length() == 0)
		{
			((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username"))
					.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
		} else
		{
			Client.getInstance().transmitCreds(user.getText(), pw.getText(), cb.isSelected());
		}
	}

	public void wrongPassword()
	{
		((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password"))
				.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
		((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username"))
				.setStyle("-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2");
	}

}
