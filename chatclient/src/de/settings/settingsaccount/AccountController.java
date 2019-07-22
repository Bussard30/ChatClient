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
package de.settings.settingsaccount;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import de.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class AccountController implements Initializable
{

	@FXML
	private Pane mainpane;

	@FXML
	private Text username;

	@FXML
	private Text status;

	@FXML
	private Circle image;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		username.setText("paddy");
		status.setText("ich heiﬂe paddy");
		try
		{
			setProfilePic(ImageIO.read(Main.class.getResource("image.jpg")));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setProfilePic(BufferedImage bi)
	{
		image.setFill(new ImagePattern(SwingFXUtils.toFXImage(bi, null)));
		image.setStyle("-fx-border-width: 0;");
		image.setStroke(Color.TRANSPARENT);
	}
	
	public Pane getPane()
	{
		return mainpane;
	}

}
