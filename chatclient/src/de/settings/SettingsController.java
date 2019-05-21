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
package de.settings;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import de.datastorage.main.DSManager;
import de.datastorage.main.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class SettingsController implements Initializable
{

//	@FXML
//	private Text encryption;
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		HashMap<Settings, Object> hm = null;
		
		
		try
		{
			hm = DSManager.getInstance().getValuesOfCurrentOptions();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Boolean b = (Boolean)hm.get(Settings.ENCRYPTION);
//		if(b)
//		{
//			encryption.setText("Aktiv");
//		}
//		else
//		{
//			encryption.setText("Nicht Aktiv");
//		}
	}
}
