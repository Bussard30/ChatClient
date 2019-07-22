/*
 * (C) Copyright 2018 Bussard30.
 */

package de.chatclient.chat.toppane;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import de.types.MessageContainer;
import de.types.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class ChatTopPaneController implements Initializable
{

	@FXML
	Pane mainPane;
	
	@FXML
	Circle image;
	
	@FXML
	Text username;

	private User u;

	private ObservableList<MessageContainer> messages;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		messages = FXCollections.observableArrayList();
	}
	
	public void setProfilePic(BufferedImage bi)
	{
		image.setFill(new ImagePattern(SwingFXUtils.toFXImage(bi, null)));
		image.setStyle("-fx-border-width: 0;");
		image.setStroke(Color.TRANSPARENT);
	}
	
	public void setUsername(String username)
	{
		this.username.setText(username);
	}

	public ChatTopPaneController()
	{
		
	}

	private double xOffset;
	private double yOffset;

	public void setUser(User u)
	{
		this.u = u;
	}

	public User getUser()
	{
		return u;
	}
	
	public void displayMessage(MessageContainer m)
	{
		messages.add(m);
	}

	@FXML
	private void commitKey(KeyEvent event)
	{
		if (event.getCode() == KeyCode.ENTER)
		{
			// send message
		} else if (event.getCode() == KeyCode.ESCAPE)
		{
			// NOTHING
		}
	}
	
	public Pane getPane()
	{
		return mainPane;
	}

}
