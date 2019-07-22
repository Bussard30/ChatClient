package de.chatclient.contacts.viewcell;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import de.jensd.fx.glyphs.octicons.OctIconView;
import de.networking.logger.Logger;
import de.types.Contact;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
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

public class ContactsViewcell extends ListCell<Contact>
{
	@FXML
	private Circle image;

	@FXML
	private Text username;

	@FXML
	private Text status;

	@FXML
	private OctIconView icon0;
	@FXML
	private OctIconView icon1;

	@FXML
	private AnchorPane anchorpane;

	@FXML
	private Pane hitboxclose;

	private FXMLLoader mLLoader;

	@Override
	protected void updateItem(Contact contact, boolean empty)
	{
		super.updateItem(contact, empty);

		if (empty || contact == null)
		{
			setText(null);
			setGraphic(null);
		} else
		{
			if (mLLoader == null)
			{
				mLLoader = new FXMLLoader(getClass().getResource("/de/chatclient/contacts/viewcell/contact.fxml"));
				mLLoader.setController(this);
				try
				{
					mLLoader.load();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			setProfilePic(contact.getImage());
			username.setText(contact.getName());
			super.setStyle("-fx-background-radius: 3;");
			icon0.setFill(c2);
			icon1.setFill(c2);
			icon0.setVisible(false);
			icon1.setVisible(false);
			setText(null);
			setGraphic(anchorpane);
		}
	}
	
	public void setProfilePic(BufferedImage bi)
	{
		image.setFill(new ImagePattern(SwingFXUtils.toFXImage(bi, null)));
		image.setStyle("-fx-border-width: 0;");
		image.setStroke(Color.TRANSPARENT);
	}

	Color c0 = Color.TRANSPARENT;
	Color c1 = Color.rgb(85, 85, 85);
	Color c2 = Color.color(0.50, 0.50, 0.50);
	Color c3 = Color.color(0.85, 0.85, 0.85);


	private HashMap<Node, Animation> animations = new HashMap<>();

	private void animate(Color start, Color end, Node target)
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
				setB(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		};
		animation.setCycleCount(1);
		animations.put(target, animation);
		animation.play();
	}

	private HashMap<OctIconView, Animation> animations1 = new HashMap<>();

	/**
	 * @param start
	 * @param end
	 * @param target
	 */
	private void animate(Color start, Color end, OctIconView target)
	{
		Logger.info("animating...");
		if (animations1.containsKey(target))
		{
			animations1.get(target).stop();
			animations1.remove(target);
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
				target.setFill(vColor);
			}
		};
		animation.setCycleCount(1);
		animations1.put(target, animation);
		animation.play();
	}

	private void setB(Background b)
	{
		super.setBackground(b);
	}

	@FXML
	public void mouseEntered1(Event event)
	{
		animate(c2, c3, icon0);
		animate(c2, c3, icon1);
		hitboxclose.setCursor(Cursor.HAND);
	}

	@FXML
	public void mouseExited1(Event event)
	{
		if (animations1.containsKey(icon0))
		{
			animations1.get(icon0).stop();
			animations1.remove(icon0);
			animate(c3, c2, icon0);
		}
		if (animations1.containsKey(icon1))
		{
			animations1.get(icon1).stop();
			animations1.remove(icon1);
			animate(c3, c2, icon1);
		}
		hitboxclose.setCursor(Cursor.DEFAULT);
	}
	
	@FXML
	public void mouseEntered(Event event)
	{
		icon0.setVisible(true);
		icon1.setVisible(true);
		animate(Color.TRANSPARENT, c2, icon0);
		animate(Color.TRANSPARENT, c2, icon1);
	}
	
	@FXML
	public void mouseExited(Event event)
	{
		if (animations1.containsKey(icon0))
		{
			animations1.get(icon0).stop();
			animations1.remove(icon0);
		}
		if (animations1.containsKey(icon1))
		{
			animations1.get(icon1).stop();
			animations1.remove(icon1);
		}
		icon0.setVisible(false);
		icon1.setVisible(false);
	}
}
