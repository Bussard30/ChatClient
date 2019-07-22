/*
 * (C) Copyright 2018 Bussard30.
 */

package de.chatclient.chat;

import java.net.URL;
import java.util.ResourceBundle;

import de.chatclient.chat.viewcell.MessageViewCell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.types.MessageContainer;
import de.types.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import networking.types.MessageWrapper;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class ChatController implements Initializable
{
	@FXML
	Pane mainPane;

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
		chatview.setItems(messages);
		chatview.setCellFactory(contactviewcell -> new MessageViewCell());
		chatview.setMaxHeight(messages.size() * 39 + 4);
		
		messages.add(new MessageContainer(new User(null, "paddy", null, null, null, null, null),
				new MessageWrapper("Hallo", null, null, false, false, 0)));
		messages.add(new MessageContainer(new User(null, "paddy", null, null, null, null, null),
				new MessageWrapper("Das hier ist ein Test", null, null, false, false, 0)));
		messages.add(new MessageContainer(new User(null, "CCUser37", null, null, null, null, null),
				new MessageWrapper("Ok super", null, null, false, false, 0)));
		messages.add(new MessageContainer(new User(null, "CCUser37", null, null, null, null, null),
				new MessageWrapper("test123", null, null, false, false, 0)));
		messages.add(new MessageContainer(new User(null, "paddy", null, null, null, null, null),
				new MessageWrapper("ok funktioniert", null, null, false, false, 0)));
		chatview.setLayoutY(chatview.getLayoutY() - 27);
		chatview.setLayoutY(chatview.getLayoutY() - 27);
		chatview.setLayoutY(chatview.getLayoutY() - 27);
		chatview.setLayoutY(chatview.getLayoutY() - 27);
		chatview.setLayoutY(chatview.getLayoutY() - 27);
		
		chatview.setPrefHeight(messages.size() * 33 + 6);
		chatview.setMaxHeight(messages.size() * 33 + 6);
		chatview.setMinHeight(messages.size() * 33 + 6);
		
	}

	public ChatController()
	{
		messages = FXCollections.observableArrayList();

	}

	private double xOffset;
	private double yOffset;

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
			messages.add(new MessageContainer(new User(null, "paddy", null, null, null, null, null),
					new MessageWrapper(chattextfield.getText(), null, null, false, false, 0)));
			chattextfield.setText("");
			chatview.setPrefHeight(messages.size() * 33 + 6);
			chatview.setMaxHeight(messages.size() * 33 + 6);
			chatview.setMinHeight(messages.size() * 33 + 6);
			chatview.setLayoutY(chatview.getLayoutY() - 27);
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
