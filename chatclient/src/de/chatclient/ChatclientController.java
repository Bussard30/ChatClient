/*
 * (C) Copyright 2018 Bussard30.
 */
package de.chatclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import de.Main;
import de.networking.logger.Logger;
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
		try
		{
			contacts.addAll(new Contact(ImageIO.read(Main.class.getResource("image.png")), "user0", "ayyyyyyyy"),
					new Contact(ImageIO.read(Main.class.getResource("image.png")), "user1", "status"),
					new Contact(ImageIO.read(Main.class.getResource("image.png")), "user2", "status"),
					new Contact(ImageIO.read(Main.class.getResource("image.png")), "user3", "status"),
					new Contact(ImageIO.read(Main.class.getResource("image.png")), "user4", "status"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		recentcontacts.setItems(contacts);
		recentcontacts.setCellFactory(contactviewcell -> new ContactViewCell());
		recentcontacts.setMaxHeight(contacts.size() * 46 + 2 );
		Logger.info(String.valueOf(contacts.size() * 46 + 2));
		Logger.info(String.valueOf(recentcontacts.getFixedCellSize()));
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
		recentcontacts.setMaxHeight(contacts.size() * 40 + 5);
	}

	public void addContact(Contact contact)
	{
		contacts.add(contact);
		recentcontacts.setMaxHeight(contacts.size() * 40 + 5);
	}
}
