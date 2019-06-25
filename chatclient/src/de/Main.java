/*
 * (C) Copyright 2018 Bussard30.
 */

package de;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import de.chatclient.ChatclientController;
import de.datastorage.main.DSManager;
import de.forgotpassword.ForgotPasswordController;
import de.login.LoginController;
import de.networking.client.Client;
import de.networking.logger.Logger;
import de.register.RegisterController;
import de.settings.SettingsController;
import de.types.Contact;
import de.types.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import networking.types.MessageWrapper;
import networking.types.ProtocolWrapper;

/**
 *
 * @author Jonas S_
 */
public class Main extends Application
{

	private static volatile Main main;

	private Stage primaryStage;
	private Stage optionsStage;
	private Stage popupStage;

	private FXMLLoader loginLoader;
	private FXMLLoader forgotPasswordLoader;
	private FXMLLoader registrationLoader;
	private FXMLLoader chatClientLoader;
	private FXMLLoader settingsLoader;

	private Scene loginScene;
	private Scene forgotPasswordScene;
	private Scene registrationScene;
	private Scene chatClientScene;
	private Scene settingsScene;
	private Scene popupScene;

	private ChatclientController ccc;
	private ForgotPasswordController fpc;
	private RegisterController rc;
	private LoginController lc;
	private SettingsController sc;

	// TODO PopupController
	private Object pc;

	private DSManager dsm;

	public static InetSocketAddress server = new InetSocketAddress("cancersquad.dyn.ddnss.de", 55555);
	public static int port = 55555;

	public static final ProtocolWrapper protocol = new ProtocolWrapper("1.0", "1.0");

	public Main()
	{
		if (main == null)
		{
			main = this;
		} else
		{
			throw new RuntimeException();
		}
		long old = System.currentTimeMillis();
		dsm = new DSManager();
		Logger.info("DSQuery took " + (System.currentTimeMillis() - old) + "ms");
		Client c = new Client();
		c.connect(55555);
		Logger.info("---------------------------------------------------");
		(new Timer()).schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				c.initConnection();
			}
		}, 1);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		long old = System.currentTimeMillis();
		Font.loadFont(Main.class.getResource("font.ttf").toExternalForm(), 10);
		primaryStage = stage;
		initLogin();
		stage.setScene(loginScene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		Logger.info("It took " + (System.currentTimeMillis() - old) + "ms");
		old = System.currentTimeMillis();
		initForgotPassword();
		initRegistration();
		initChatClient();
		initSettings();
		// TODO implementation of keepmeloggedin
		//

		Logger.info("It took " + (System.currentTimeMillis() - old) + "ms");
	}

	public void initLogin() throws Exception
	{
		loginLoader = new FXMLLoader(getClass().getResource("login/login.fxml"));
		Parent root = loginLoader.load();
		// initing loginScene for quick switches
		loginScene = new Scene(root);
		loginScene.getStylesheets().add(Main.class.getResource("login/login.css").toExternalForm());
		lc = loginLoader.getController();
	}

	public void initForgotPassword() throws Exception
	{
		forgotPasswordLoader = new FXMLLoader(getClass().getResource("forgotpassword/forgotpassword.fxml"));
		Parent root = forgotPasswordLoader.load();

		// initing scene
		forgotPasswordScene = new Scene(root);
		forgotPasswordScene.getStylesheets()
				.add(Main.class.getResource("forgotpassword/forgotpassword.css").toExternalForm());
		fpc = forgotPasswordLoader.getController();
	}

	
	public void initRegistration() throws Exception
	{
		registrationLoader = new FXMLLoader(getClass().getResource("register/register.fxml"));
		Parent root = registrationLoader.load();

		// initing scene
		registrationScene = new Scene(root);
		registrationScene.getStylesheets().add(Main.class.getResource("register/register.css").toExternalForm());
		rc = registrationLoader.getController();
	}

	public void initChatClient() throws Exception
	{
		chatClientLoader = new FXMLLoader(getClass().getResource("chatclient/chatclient.fxml"));
		Parent root = chatClientLoader.load();

		// initing scene
		chatClientScene = new Scene(root);
		chatClientScene.getStylesheets().add(Main.class.getResource("chatclient/chatclient.css").toExternalForm());
		chatClientScene.getStylesheets().add(Main.class.getResource("chatclient/contact/contact.css").toExternalForm());
		ccc = chatClientLoader.getController();
	}

	public void initContacts()
	{

	}

	public void initSettings() throws IOException
	{
		settingsLoader = new FXMLLoader(getClass().getResource("settings/settings.fxml"));
		Parent root = settingsLoader.load();

		settingsScene = new Scene(root);
		settingsScene.getStylesheets().add(Main.class.getResource("settings/settings.css").toExternalForm());
		sc = settingsLoader.getController();
	}

	public void initInfopanels()
	{
		// load both contact and notifications infopanel
	}

	public void initChat()
	{

	}

	public void initMessagePopup() throws IOException
	{
		FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("settings/settings.fxml"));
		Parent root = popupLoader.load();

		popupScene = new Scene(root);
		popupScene.getStylesheets().add(Main.class.getResource("settings/settings.css").toExternalForm());
		pc = popupLoader.getController();

		popupStage.hide();
		popupStage.setScene(popupScene);
		popupStage.initModality(Modality.NONE);

	}

	public void popup(MessageWrapper m)
	{
		// TODO
	}

	/**
	 * 
	 * @param choice
	 *            0 == login, 1 == forgotpassword, 2 == registration, 3 ==
	 *            chatclient
	 */
	public void setScene(int choice)
	{
		try
		{
			switch (choice)
			{
			case 0:
				primaryStage.setScene(loginScene);
				break;
			case 1:
				primaryStage.setScene(forgotPasswordScene);
				break;
			case 2:
				primaryStage.setScene(registrationScene);
				break;
			case 3:
				primaryStage.setScene(chatClientScene);
				break;
			default:
				throw new RuntimeException();
			}
		} catch (Exception e)
		{
			for (;;)
			{
				e.printStackTrace();
			}
		}
	}

	public void openSettings()
	{
		if (optionsStage != null)
		{
			optionsStage.close();
		}
		optionsStage = new Stage();
		optionsStage.setScene(settingsScene);

		optionsStage.setTitle("Settings");
		optionsStage.initStyle(StageStyle.UNDECORATED);

		optionsStage.setX(primaryStage.getX() + 200);
		optionsStage.setY(primaryStage.getY() + 200);

		optionsStage.show();

	}

	public void closeSettings()
	{
		if (optionsStage != null)
		{
			optionsStage.close();
			optionsStage = null;
		}
	}

	public void addContact(Contact c)
	{

	}

	public void setLocalUser(User u)
	{
		Logger.info(u.getStatus());
		ccc.setProfilePic(u.getProfilepic());
		ccc.setStatus(u.getStatus());
		ccc.setUserName(u.getUsername());
	}

	public void wrongPassword()
	{
		lc.wrongPassword();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args)
	{
		launch(args);
	}

	public synchronized static Main getInstance()
	{
		return main;
	}

	public FXMLLoader getLoginLoader()
	{
		return loginLoader;
	}

	public FXMLLoader getForgotPasswordLoader()
	{
		return forgotPasswordLoader;
	}

	public FXMLLoader getRegistrationLoader()
	{
		return registrationLoader;
	}

	public FXMLLoader getChatClientLoader()
	{
		return chatClientLoader;
	}

	public Stage getPrimaryStage()
	{
		return primaryStage;
	}

}
