/*
 * (C) Copyright 2018 Bussard30.
 */
package de.chatclient;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import de.Main;
import de.networking.logger.Logger;
import de.types.Contact;
import de.types.ContactViewCell;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
 * @author Jonas S_
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
		recentcontacts.setMaxHeight(contacts.size() * 46 + 4);
		Logger.info(String.valueOf(contacts.size() * 46 + 4));
		Logger.info(String.valueOf(recentcontacts.getFixedCellSize()));
		anchorpane.setMaxSize(900, 600);
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
		recentcontacts.setMaxHeight(contacts.size() *  46 + 4);
	}

	public void addContact(Contact contact)
	{
		contacts.add(contact);
		recentcontacts.setMaxHeight(contacts.size() *  46 + 4);
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

}
