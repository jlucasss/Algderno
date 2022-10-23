
package com.algderno.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.controllers.MainController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShowScreen {

	private Stage stage;
	private Parent parent;
	private FXMLLoader loader;
	private boolean wait;
	
	public ShowScreen(Stage stage) {
		this.stage = stage;
		setWait(false);
	}

	public void simpleScreen(String pathFileFXML, int width, int height, String title, String bundle) throws Exception {

		try {
			
			parent = findFXML(pathFileFXML, bundle);
			
			stage.setScene(new Scene(parent, width, height));

			stage.setMaximized(false);

			stage.setTitle(title);

			if (isWait())
				stage.showAndWait();
			else
				stage.show();
			
			setWait(false);

		} catch (Exception e) {

			App.getLogger().getExceptions().add("Unable to open the screen.", e).show();
			e.printStackTrace();
			throw new Exception("Unable to open the screen.", e);

		}

	}

	public Parent findFXML(String pathFXML, String bundle) throws IOException {
		
		try (InputStream fxmlIS = App.class.getResourceAsStream("/com/algderno/controllers/" + pathFXML)) {

			ResourceBundle resourceBundle = GetResourceBundle.getBundle(bundle);

			this.setLoader(new FXMLLoader());

			getLoader().setResources(resourceBundle);

			if (App.getURLControllers() == null)
				getLoader().setLocation(MainController.class.getResource(""));
			else
				getLoader().setLocation(App.getURLControllers());

			return getLoader().load(fxmlIS);

		}
		
	}
	
	public void maximize() {
		stage.setMaximized(true);
	}

	public FXMLLoader getLoader() {
		return loader;
	}

	private void setLoader(FXMLLoader loader) {
		this.loader = loader;
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

}

