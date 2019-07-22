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

import de.Main;
import de.datastorage.main.DSManager;
import de.datastorage.main.Settings;
import de.settings.Settingsdetail.Settingsdetail;
import de.settings.settingsaccount.AccountController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Jonas S_
 */
public class SettingsController implements Initializable
{
	@FXML
	private ListView<String> leftOverview;

	@FXML
	private SplitPane splitPane;
	
	@FXML
	private Pane rightPane;
	
	private ObservableList<String> optionsList;

	private AccountController ac;

	private Settingsdetail language, encryption;

	public SettingsController()
	{
		// Language = new Settingsdetail(,);
		// Encrytion = new Settingsdetail(,);

		optionsList = FXCollections.observableArrayList();
		optionsList.addAll("My Account", "Privacy & Safety", "Voice & Video", "Notifications", "Hotkeys",
				"Text & Images", "Appearance");
	}

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
		leftOverview.setCellFactory(viewcell -> new SettingsViewCell());
		leftOverview.setItems(optionsList);
		FXMLLoader fml0 = new FXMLLoader(getClass().getResource("/de/settings/settingsaccount/account.fxml"));
		try
		{
			fml0.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ac = fml0.getController();
		openMyAccount(null);
	}

	@FXML
	public void openMyAccount(MouseEvent x)
	{
		splitPane.getItems().remove(rightPane);
		ac.getPane().setPrefHeight(380);
		splitPane.getItems().add(ac.getPane());

	}

	@FXML
	public void minimize(MouseEvent e)
	{

	}

	@FXML
	private void close(MouseEvent event)
	{
		Main.getInstance().closeSettings();
	}
}
