package de.chatclient.chat.viewcell;

import java.io.IOException;

import de.types.MessageContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MessageViewCell extends ListCell<MessageContainer>
{
	
	@FXML
	Pane pane;
	
	@FXML
	Text user;
	
	@FXML
	Text message;
	
	private FXMLLoader mLLoader;
	
	@Override
	protected void updateItem(MessageContainer mc, boolean empty)
	{
		super.updateItem(mc, empty);

		if (empty || mc == null)
		{
			setText(null);
			setGraphic(null);
		} else
		{
			if (mLLoader == null)
			{
				mLLoader = new FXMLLoader(getClass().getResource("/de/chatclient/chat/viewcell/messageviewcell.fxml"));
				mLLoader.setController(this);

				try
				{
					mLLoader.load();
				} catch (IOException e)
				{
					e.printStackTrace();
				}

			}
			super.setStyle("-fx-background-radius: 3;");
			
			message.setText(mc.getMessage().getMessage());
			user.setText(mc.getUser().getUsername());
			
			message.setX(user.getLayoutBounds().getWidth());
			setText(null);
			setGraphic(pane);
		}
	}

	public void setMessageContainer(MessageContainer m)
	{
		user.setText(m.getUser().getUsername());
		message.setText(m.getMessage().getMessage());
	}
}
