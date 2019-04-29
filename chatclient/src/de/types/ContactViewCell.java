package de.types;

import java.io.IOException;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ContactViewCell extends ListCell<Contact>
{

	@FXML
	private ImageView image;

	@FXML
	private Text username;

	@FXML
	private Text status;

	@FXML
	private FontAwesomeIconView close;

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

			image.setImage(SwingFXUtils.toFXImage(contact.getImage(), null));
			username.setText(contact.getName());

			setText(null);
			setGraphic(anchorpane);
		}
	}

	@FXML
	public void mouseEntered()
	{
		close.setVisible(true);
	}

	@FXML
	public void mouseExited()
	{
		close.setVisible(false);
	}

	@FXML
	public void mouseEntered1()
	{
		close.setFill(Color.color(0.85, 0.85, 0.85));
		hitboxclose.setCursor(Cursor.HAND);
	}

	@FXML
	public void mouseExited1()
	{
		close.setFill(Color.color(0.50, 0.50, 0.50));
		hitboxclose.setCursor(Cursor.DEFAULT);
	}
}