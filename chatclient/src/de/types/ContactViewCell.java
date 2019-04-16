package de.types;

import java.awt.image.BufferedImage;
import java.io.IOException;

import de.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ContactViewCell extends ListCell<Contact> {

    @FXML
    private ImageView image;

    @FXML
    private Text username;

    @FXML
    private FontAwesomeIconView close;

    @FXML
    private Pane pane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Contact contact, boolean empty) 
    {
    	if(empty || contact == null)
    	{
    		setText(null);
    		setGraphic(null);
    	}
    	else
    	{
    		mLLoader = new FXMLLoader(Main.class.getResource("de/chatclient/contact/contact.fxml"));
    		mLLoader.setController(this);
    	}
    	
        try {
            mLLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        image.setImage(SwingFXUtils.toFXImage(contact.getImage(),null));
        username.setText(contact.getName());
        
        setGraphic(pane);
        
    }
    
}