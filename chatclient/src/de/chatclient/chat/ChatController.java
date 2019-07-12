/*
 * (C) Copyright 2018 Bussard30.
 */

package de.chatclient.chat;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import de.chatclient.chat.viewcell.MessageViewCell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.networking.logger.Logger;
import de.types.MessageContainer;
import de.types.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class ChatController implements Initializable
{

	@FXML
	TextField chattextfield;

	@FXML
	ListView<MessageContainer> chatview;

	@FXML
	Pane submit_bg;

	@FXML
	Pane voice_bg;

	@FXML
	OctIconView submit;

	@FXML
	FontAwesomeIconView voice;

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

	public ChatController()
	{
		chatview.setItems(messages);
		chatview.setCellFactory(contactviewcell -> new MessageViewCell());
		chatview.setMaxHeight(messages.size() * 39 + 4);
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

}
