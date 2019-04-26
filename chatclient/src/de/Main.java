/*
 * (C) Copyright 2018 Bussard30.
 */

package de;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import de.datastorage.main.DSManager;
import de.networking.client.Client;
import de.networking.logger.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import networking.types.ProtocolWrapper;

/**
 *
 * @author Jonas S_
 */
public class Main extends Application
{

	private static volatile Main main;

	private Stage primaryStage;

	private FXMLLoader loginLoader;
	private FXMLLoader forgotPasswordLoader;
	private FXMLLoader registrationLoader;
	private FXMLLoader chatClientLoader;

	private Scene loginScene;
	private Scene forgotPasswordScene;
	private Scene registrationScene;
	private Scene chatClientScene;

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
		dsm = new DSManager();

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
		}, 2 * 1000);

	}

	@Override
	public void start(Stage stage) throws Exception
	{
		primaryStage = stage;
		initLogin();
		initForgotPassword();
		initRegistration();
		initChatClient();
		// TODO implementation of keepmeloggedin
		//
		stage.setScene(loginScene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public void initLogin() throws Exception
	{
		loginLoader = new FXMLLoader(getClass().getResource("login/login.fxml"));
		Parent root = loginLoader.load();
		// initing loginScene for quick switches
		loginScene = new Scene(root);
		loginScene.getStylesheets().add(Main.class.getResource("login/login.css").toExternalForm());
	}

	public void initForgotPassword() throws Exception
	{
		forgotPasswordLoader = new FXMLLoader(getClass().getResource("forgotpassword/forgotpassword.fxml"));
		Parent root = forgotPasswordLoader.load();

		// initing scene
		forgotPasswordScene = new Scene(root);
		forgotPasswordScene.getStylesheets()
				.add(Main.class.getResource("forgotpassword/forgotpassword.css").toExternalForm());
	}

	public void initRegistration() throws Exception
	{
		registrationLoader = new FXMLLoader(getClass().getResource("register/register.fxml"));
		Parent root = registrationLoader.load();

		// initing scene
		registrationScene = new Scene(root);
		registrationScene.getStylesheets().add(Main.class.getResource("register/register.css").toExternalForm());
	}

	public void initChatClient() throws Exception
	{
		chatClientLoader = new FXMLLoader(getClass().getResource("chatclient/chatclient.fxml"));
		Parent root = chatClientLoader.load();

		// initing scene
		chatClientScene = new Scene(root);
		chatClientScene.getStylesheets().add(Main.class.getResource("chatclient/chatclient.css").toExternalForm());
		chatClientScene.getStylesheets().add(Main.class.getResource("chatclient/contact/contact.css").toExternalForm());
	}

	public void initContacts()
	{

	}

	public void initInfopanels()
	{
		// load both contact and notifications infopanel
	}

	public void initChat()
	{

	}

	/**
	 * 
	 * @param choice
	 *            0 == login, 1 == forgotpassword, 2 == registration, 3 ==
	 *            chatclient
	 */
	public void setScene(int choice)
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
