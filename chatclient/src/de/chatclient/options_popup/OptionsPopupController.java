/*
 *
 * (C) Copyright 2018 Bussard30.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package de.chatclient.options_popup;

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
public class OptionsPopupController implements Initializable {

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
