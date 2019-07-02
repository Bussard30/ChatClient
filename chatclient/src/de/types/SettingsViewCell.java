package de.types;

import java.io.IOException;
import java.util.HashMap;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.networking.logger.Logger;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SettingsViewCell extends ListCell<String>
{
	@FXML
	private AnchorPane pane;
	
	@FXML
	private Text text;

	private FXMLLoader mLLoader;
	

	@Override
	protected void updateItem(String settingsName, boolean empty)
	{
		super.updateItem(settingsName, empty);

		if (empty || settingsName == null)
		{
			setText(null);
			setGraphic(null);
		} else
		{
			if (mLLoader == null)
			{
				mLLoader = new FXMLLoader(getClass().getResource("/de/chatclient/contact/contact.fxml"));
				mLLoader.setController(this);

				try
				{
					mLLoader.load();
				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}

			setText(null);
			text.setText(settingsName);
			setGraphic(pane);
		}
	}

	Color c0 = Color.rgb(51, 51, 51);
	Color c1 = Color.rgb(85, 85, 85);
	Color c2 = Color.color(0.50, 0.50, 0.50);
	Color c3 = Color.color(0.85, 0.85, 0.85);

//	@FXML
//	public void mouseEntered(Event event)
//	{
//		super.setStyle("-fx-background-radius: 3;");
//		close.setVisible(true);
//		animate(c0, c1, anchorpane);
//	}

//	@FXML
//	public void mouseExited(Event event)
//	{
//		super.setStyle("-fx-background-radius: 3;");
//		close.setVisible(false);
//		if (animations.containsKey(anchorpane))
//		{
//			animations.get(anchorpane).stop();
//			animations.remove(anchorpane);
//			setB(new Background(new BackgroundFill(c1, CornerRadii.EMPTY, Insets.EMPTY)));
//			animate(c1, c0, (Region) anchorpane);
//		}
//	}

	private HashMap<Region, Animation> animations = new HashMap<>();

	private void animate(Color start, Color end, Region target)
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

	private HashMap<FontAwesomeIconView, Animation> animations1 = new HashMap<>();

	private void animate(Color start, Color end, FontAwesomeIconView target)
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
}