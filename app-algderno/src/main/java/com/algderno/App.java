package com.algderno;

/**
*
* This class is a main, it open SplashScreen.fxml and load all predefinition.
*
* @author José Lucas dos Santos da Silva
*
*/

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Locale;
import java.util.ResourceBundle;

import com.algderno.controllers.SplashScreenController;
import com.algderno.util.ShowScreen;
import com.algderno.util.SimpleAlerts;
import com.algderno.util.GetResourceBundle;
import com.algderno.util.logger.SimpleLogger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

	@SuppressWarnings("exports")
	public static Stage mainStage, mainScreenStage, localStage;

	@SuppressWarnings("exports")
	public static ShowScreen showScreen;

	private static Locale locale;

	private static URL urlControllers, urlCSS;

	private static SimpleLogger logger;

	private static ResourceBundle messagesBundle;

	private static SimpleAlerts alerts;

	private Parent parent = null;

	private FXMLLoader loader = null;

	private FileSystem fileSystem;
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage mainStage) {

		try {

			App.mainStage = mainStage;

			getLocale(); // Init default language 

			// Init general objects

			messagesBundle = null;

			App.getMessagesBundle(); // Init messageBundle that was null

			App.logger = App.getLogger();

			App.urlControllers = null;

			App.urlCSS = null;
			
			this.fileSystem = FileSystems.getFileSystem(URI.create("jrt:/"));
			
			InputStream iconIS = App.class.getResourceAsStream("images/symbol.png");

			initializePopupStage();

			// Load URLs and Files

			this.loader = new FXMLLoader();

			loadURLsAndFiles();

			// Prepare SplashScreen

			Scene scene = new Scene(parent, 290, 140);

			mainStage.setScene(scene);

			mainStage.getIcons().add(new Image(iconIS));

			mainStage.initStyle(StageStyle.TRANSPARENT);

			mainStage.show();

			// Load SplashScreen loading MainController

			SplashScreenController controller = (SplashScreenController) loader.getController();
			
			controller.loadTask();

		} catch (Exception e) {

			logger.getExceptions().add("If it persists, contact the developer.", e);

			e.printStackTrace();

		}

	}

	private void loadURLsAndFiles() {

		try (InputStream fxmlIS = App.class.getResourceAsStream("SplashScreen.fxml")) {

			loader.setResources(GetResourceBundle.getBundle("splashscreen"));

			loader.setLocation(App.class.getResource(""));

			if (loader.getLocation() == null) {

				//URL to "jrt:/"
				loader.setLocation( getURLFileSystem("/modules/com.algderno/com/algderno/") );

				// For other FXML inside folder controllers/
				setURLControllers( getURLFileSystem("/modules/com.algderno/com/algderno/controllers/") );

				// For file style-base.css
				setURLCSS( getURLFileSystem("/modules/com.algderno/com/algderno/style-base.css") );

				System.out.println("urlControllers = " + App.urlControllers);
				System.out.println("urlCSS = " + App.urlCSS);

			}

			parent = loader.load(fxmlIS);

		} catch (Exception e) {

			logger.getExceptions().add("A error occorred! Contact the developer!", e).show();
			e.printStackTrace();

		}

	}

	private URL getURLFileSystem(String path) throws MalformedURLException, URISyntaxException {

		URL location = fileSystem.getPath(path).toUri().toURL();

		return new URI(location.toString()+"/").toURL();

	}

	private void initializePopupStage() {

		localStage = new Stage();
		
		// To prevent going back to mainStage while it's open
		localStage.initModality(Modality.WINDOW_MODAL);
		localStage.initOwner(App.mainStage);

		InputStream iconInputStream = App.class.getResourceAsStream("images/symbol.png");
		
		localStage.getIcons().add(new Image(iconInputStream));
		
		showScreen = new ShowScreen(localStage);

		getAlerts();

	}

	/* Getters that init objects */
	
	public static Locale getLocale() {

		if (locale == null) {

			locale = new Locale("en");
		
			if (Locale.getDefault().equals(new Locale("pt", "BR")))
				locale = Locale.getDefault();

		}

		return locale;

	}

	public void setLocale(Locale locale) {
		App.locale = locale;
	}
	
	@SuppressWarnings("exports")
	public static SimpleAlerts getAlerts() {

		if (alerts == null)
			alerts = new SimpleAlerts();

		return alerts;

	}

	public static ResourceBundle getMessagesBundle() {

		if (messagesBundle == null)
			messagesBundle = GetResourceBundle.getBundle("messages");

		return messagesBundle;

	}

	@SuppressWarnings("exports")
	public static SimpleLogger getLogger() {

		if (logger == null)
			logger = new SimpleLogger();

		return logger;

	}

	/* Getters and Setters of URLs for extennal class */

	public static URL getURLControllers() {
		return urlControllers;
	}

	private void setURLControllers(URL urlCotrollers) {
		App.urlControllers = urlCotrollers;
	}

	public static URL getURLCSS() {
		return urlCSS;
	}

	private void setURLCSS(URL urlCSS) {
		App.urlCSS = urlCSS;
	}

	/* Enum */

	public enum Languages {

		EN("en"),
		PT_BR("pt_BR");

		private String content;

		Languages(String content) {
			this.content = content;
		}

		public String toString() {
			return this.content;
		}

	}
	
	public static void main(String[] args) {
		launch();
	}

}

