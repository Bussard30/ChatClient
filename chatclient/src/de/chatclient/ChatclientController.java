/*
 * (C) Copyright 2018 Bussard30.
 */
package de.chatclient;

import java.net.URL;
import java.util.ResourceBundle;

import de.Main;
import de.types.Contact;
import de.types.ContactViewCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class ChatclientController implements Initializable
{
	@FXML
	ListView<Contact> recentcontacts;
	
	private ObservableList<Contact> contacts;
	public ChatclientController()
	{
		contacts = FXCollections.observableArrayList();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		recentcontacts.setItems(contacts);
		recentcontacts.setCellFactory(contactviewcell -> new ContactViewCell());
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
	private void close(MouseEvent event)
	{
		System.exit(0);
	}

	@FXML
	private void minimize(MouseEvent event)
	{
		Main.getInstance().getPrimaryStage().setIconified(true);
	}
	
	public void loadContacts(ObservableList<Contact> contacts)
	{
		recentcontacts.setItems(contacts);
		this.contacts = contacts;
	}
	
	public void addContact(Contact contact)
	{
		contacts.add(contact);
	}
}
