/*
+ * (C) Copyright 2018 Bussard30.
 */
package de.chatclient;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import de.Main;
import de.chatclient.chat.ChatController;
import de.chatclient.chat.toppane.ChatTopPaneController;
import de.chatclient.contacts.ContactsController;
import de.chatclient.contacts.toppane.ContactsTopPaneController;
import de.networking.logger.Logger;
import de.types.Contact;
import de.types.ContactViewCell;
import de.types.MessageContainer;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Bussard30
 */
public class ChatclientController implements Initializable
{
	@FXML
	ListView<Contact> recentcontacts;

	@FXML
	Pane toppane;

	@FXML
	AnchorPane anchorpane;

	@FXML
	Circle profilepic;

	@FXML
	Text username;

	@FXML
	Text status;

	@FXML
	Pane close_bg;

	@FXML
	Circle online_status;

	@FXML
	Pane topPane;

	@FXML
	Pane midPane;

	/**
	 * Pane required to remove correct pane <br>
	 * when e.g. clicking on contacts while chat is being shown.
	 */
	private Pane currentMidSelection;
	private Pane currentTopSelection;

	// Panes for chat / friends / notifications

	ChatController chc;
	ChatTopPaneController ctpc;

	ContactsController cc;
	ContactsTopPaneController ctc;

	// TODO Notifications controller

	/**
	 * Current describes what pane is being shown.<br>
	 * current == 0 -> Nothing<br>
	 * current == 1 -> Contacts<br>
	 * current == 2 -> Chat
	 */
	int current = 0;

	private ObservableList<Contact> contacts;

	public ChatclientController()
	{
		contacts = FXCollections.observableArrayList();
		try
		{
			contacts.addAll(new Contact(ImageIO.read(Main.class.getResource("imagecc.jpg")), "CCUser37", "Hallo."));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * Adding panes for mid/top pane
		 */
		FXMLLoader fml0 = new FXMLLoader(getClass().getResource("/de/chatclient/chat/chat.fxml"));
		try
		{
			fml0.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chc = fml0.getController();

		FXMLLoader fml1 = new FXMLLoader(getClass().getResource("/de/chatclient/chat/toppane/chattoppane.fxml"));
		try
		{
			fml1.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctpc = fml1.getController();

		FXMLLoader fml2 = new FXMLLoader(getClass().getResource("/de/chatclient/contacts/contacts.fxml"));
		try
		{
			fml2.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cc = fml2.getController();

		FXMLLoader fml3 = new FXMLLoader(
				getClass().getResource("/de/chatclient/contacts/toppane/contactstoppane.fxml"));
		try
		{
			fml3.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ctc = fml3.getController();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		recentcontacts.setItems(contacts);
		recentcontacts.setCellFactory(contactviewcell -> new ContactViewCell());
		recentcontacts.setMaxHeight(contacts.size() * 46 + 4);
		Logger.info(String.valueOf(contacts.size() * 46 + 4));
		Logger.info(String.valueOf(recentcontacts.getFixedCellSize()));
		anchorpane.setMaxSize(900, 600);

		recentcontacts.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				System.out.println("clicked on " + recentcontacts.getSelectionModel().getSelectedItem());
				showChat(recentcontacts.getSelectionModel().getSelectedItem());
			}
		});
		currentMidSelection = midPane;
		currentTopSelection = topPane;
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
	private void hover_minimize(MouseEvent event)
	{
		animate(c0, c1, (Region) event.getSource());
	}

	@FXML
	private void leave_minimize(MouseEvent event)
	{
		if (animations.containsKey(event.getSource()))
		{
			animations.get(event.getSource()).stop();
			animations.remove(event.getSource());
			((Region) event.getSource()).setBackground(
					new Background(new BackgroundFill(Color.rgb(34, 34, 34), CornerRadii.EMPTY, Insets.EMPTY)));
			animate(c1, c0, (Region) event.getSource());
		}
	}

	Color c0 = Color.rgb(34, 34, 34);
	Color c1 = Color.rgb(85, 85, 85);
	Color c2 = Color.rgb(255, 51, 51);

	@FXML
	private void hover_close(MouseEvent event)
	{
		animate(c0, c2, close_bg);
	}

	@FXML
	private void leave_close(MouseEvent e)
	{
		if (animations.containsKey(close_bg))
		{
			animations.get(close_bg).stop();
			animations.remove(close_bg);
			animate(c2, c0, close_bg);
		}
	}

	private HashMap<Region, Animation> animations = new HashMap<>();

	private void animate(Color start, Color end, Region target)
	{
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
		animation.play();
		animations.put(target, animation);
	}

	public boolean tryToDisplayMessage(MessageContainer m)
	{
		if (m.getUser().getUuid() == chc.getUser().getUuid())
		{
			chc.displayMessage(m);
			return true;
		} else
		{
			return false;
		}
	}

	@FXML
	private void minimize(MouseEvent event)
	{
		Main.getInstance().getPrimaryStage().setIconified(true);
	}

	@FXML
	private void openSettings(MouseEvent event)
	{
		Main.getInstance().openSettings();
	}


	public void loadContacts(ObservableList<Contact> contacts)
	{
		recentcontacts.setItems(contacts);
		this.contacts = contacts;
		recentcontacts.setMaxHeight(contacts.size() * 46 + 4);
	}

	public void addContact(Contact contact)
	{
		contacts.add(contact);
		recentcontacts.setMaxHeight(contacts.size() * 46 + 4);
	}

	public void setProfilePic(BufferedImage bi)
	{
		profilepic.setFill(new ImagePattern(SwingFXUtils.toFXImage(bi, null)));
		profilepic.setStyle("-fx-border-width: 0;");
		profilepic.setStroke(Color.TRANSPARENT);
	}

	public void setUserName(String username)
	{
		this.username.setText(username);
	}

	public void setStatus(String status)
	{
		this.status.setText(status);
	}

	Color c5 = Color.rgb(51, 51, 51);
	Color c6 = Color.rgb(85, 85, 85);
	Color c7 = Color.color(0.50, 0.50, 0.50);
	Color c8 = Color.color(0.85, 0.85, 0.85);

	@FXML
	public void mouseEntered(Event event)
	{
		animate(c5, c6, (Region) event.getSource());
	}

	@FXML
	public void mouseExited(Event event)
	{
		if (animations.containsKey((Region) event.getSource()))
		{
			animations.get((Region) event.getSource()).stop();
			animations.remove((Region) event.getSource());
			((Region) event.getSource())
					.setBackground(new Background(new BackgroundFill(c0, CornerRadii.EMPTY, Insets.EMPTY)));
			animate(c6, c5, (Region) event.getSource());
		}
	}

	@FXML
	public void openContacts()
	{
		Logger.info("Doing something");
		current = 1;
		replaceMidPane(cc.getPane());
		replaceTopPane(ctc.getPane());
		try
		{
			cc.addContact(new Contact(ImageIO.read(Main.class.getResource("imagecc.jpg")), "CCUser37", "Hallo."));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		topPane.setVisible(true);
	}

	public void showChat(Contact c)
	{
		Logger.info("Showing chat.");
		current = 2;

		replaceMidPane(chc.getPane());
		replaceTopPane(ctpc.getPane());

		currentTopSelection = ctpc.getPane();

		ctpc.setProfilePic(c.getImage());
		ctpc.setUsername(c.getName());

	}
	
	/**
	 * Replaces mid pane with new pane.<br>
	 * New pane inherits bounds of mid pane.
	 * @param newPane
	 */
	public void replaceMidPane(Pane newPane)
	{
		newPane.setLayoutX(currentMidSelection.getLayoutX());
		newPane.setLayoutY(currentMidSelection.getLayoutY());
		newPane.setPrefWidth(currentMidSelection.getPrefWidth());
		newPane.setPrefHeight(currentMidSelection.getPrefHeight());
		anchorpane.getChildren().remove(currentMidSelection);
		anchorpane.getChildren().add(newPane);

		currentMidSelection = newPane;
	}
	
	/**
	 * Replaces top pane with new pane.<br>
	 * New pane inherits bounds of top pane.
	 * @param newPane
	 */
	public void replaceTopPane(Pane newPane)
	{
		newPane.setLayoutX(currentTopSelection.getLayoutX());
		newPane.setLayoutY(currentTopSelection.getLayoutY());
		newPane.setPrefWidth(currentTopSelection.getPrefWidth());
		newPane.setPrefHeight(currentTopSelection.getPrefHeight());
		anchorpane.getChildren().remove(currentTopSelection);
		anchorpane.getChildren().add(newPane);
	}

}
