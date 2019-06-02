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
import java.util.HashMap;
import java.util.ResourceBundle;

import de.Main;
import de.networking.client.Client;
import de.networking.logger.Logger;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
		((Region) event.getSource()).setStyle("-fx-border-width: 0 0 0 0;\n	-fx-border-style: solid centered;");
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
		r.setStyle("-fx-border-width: 0 0 0 0;\n	-fx-border-style: solid centered;");
	}

	private void login()
	{
		TextField user = (TextField) Main.getInstance().getLoginLoader().getNamespace().get("username");
		PasswordField pw = (PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password");
		CheckBox cb = (CheckBox) Main.getInstance().getLoginLoader().getNamespace().get("keeploggedin");

		if (pw.getText().length() == 0 && user.getText().length() == 0)
		{
			((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password")).setStyle(
					"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
			((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username")).setStyle(
					"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
		} else if (pw.getText().length() == 0)
		{
			((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password")).setStyle(
					"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
		} else if (user.getText().length() == 0)
		{
			((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username")).setStyle(
					"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
		} else
		{
			Logger.info("Submitting login data...");
			Client.getInstance().transmitCreds(user.getText(), pw.getText(), cb.isSelected());
		}
	}

	public void wrongPassword()
	{
		((PasswordField) Main.getInstance().getLoginLoader().getNamespace().get("password")).setStyle(
				"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
		((TextField) Main.getInstance().getLoginLoader().getNamespace().get("username")).setStyle(
				"-fx-border-color: red;\n -fx-border-width: 2;\n -fx-border-radius: 2;\n	-fx-border-style: solid centered;");
	}

	Color c0 = Color.rgb(34, 34, 34);
	Color c1 = Color.rgb(85, 85, 85);
	Color c2 = Color.rgb(255, 51, 51);

	@FXML
	private void animate0(Event event)
	{
		animate(c0, c2, (Region) event.getSource());
	}

	@FXML
	private void animate1(Event event)
	{
		animate(c0, c1, (Region) event.getSource());
	}

	@FXML
	private void stopAnimation0(Event event)
	{
		if (animations.containsKey(event.getSource()) && animations.get(event.getSource()) != null)
		{
			animations.get(event.getSource()).stop();
			animations.remove(event.getSource());
			((Region) event.getSource())
					.setBackground(new Background(new BackgroundFill(c0, CornerRadii.EMPTY, Insets.EMPTY)));
			animate(c2, c0, (Region) event.getSource());
		}
	}

	@FXML
	private void stopAnimation1(Event event)
	{
		if (animations.containsKey(event.getSource()))
		{
			animations.get(event.getSource()).stop();
			animations.remove(event.getSource());
			((Region) event.getSource())
					.setBackground(new Background(new BackgroundFill(c0, CornerRadii.EMPTY, Insets.EMPTY)));
			animate(c1, c0, (Region) event.getSource());
		}
	}

	private HashMap<Region, Animation> animations = new HashMap<>();

	private void animate(Color start, Color end, Region target)
	{
		Logger.info("animating...");
		if (animations.containsKey(target))
		{
			animations.get(target).stop();
			animations.remove(target);
		}

		final Animation animation = new Transition()
		{
			{
				setCycleDuration(Duration.millis(40));
				setInterpolator(Interpolator.EASE_OUT);
			}

			@Override
			protected void interpolate(double frac)
			{
				Color vColor = new Color((start.getRed() + ((end.getRed() - start.getRed()) * frac)),
						(start.getGreen() + ((end.getGreen() - start.getGreen()) * frac)),
						(start.getBlue() + ((end.getBlue() - start.getGreen()) * frac)),
						(start.getOpacity() + ((end.getOpacity() - start.getOpacity()) * frac)));
				target.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		};
		animation.setCycleCount(1);
		animations.put(target, animation);
		animation.play();
	}

}
