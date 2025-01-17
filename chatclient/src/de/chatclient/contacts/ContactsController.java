/*
 * (C) Copyright 2018 Bussard30.
 */

package de.chatclient.contacts;

import java.net.URL;
import java.util.ResourceBundle;

import de.chatclient.contacts.viewcell.ContactsViewcell;
import de.types.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Bussard30
 */
public class ContactsController implements Initializable
{
	private ObservableList<Contact> contacts;
	
	@FXML
	Pane pane;
	
	@FXML
	ListView<Contact> contactview;

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		contactview.setItems(contacts);
		contactview.setCellFactory(contactviewcell -> new ContactsViewcell());
		contactview.setMaxHeight(contacts.size() * 50 + 4);
	}

	public ContactsController()
	{
		contacts = FXCollections.observableArrayList();

	}
	
	public Pane getPane()
	{
		return pane;
	}
	
	public void addContact(Contact c)
	{
		contacts.add(c);
		contactview.setMaxHeight(contacts.size() * 48 + 4);
	}
}
