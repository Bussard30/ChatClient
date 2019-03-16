/*
 * (C) Copyright 2018 Bussard30.
 */
package de.chatclient;

import java.net.URL;
import java.util.ResourceBundle;

import de.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class ChatclientController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
}
