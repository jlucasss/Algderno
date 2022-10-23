
package com.algderno.controllers;

import java.net.URISyntaxException;

/**
 *
 * This class abstract controls.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.util.SimpleAlerts;
import com.algderno.util.logger.SimpleLogger;

import javafx.fxml.Initializable;
import javafx.scene.Scene;

public abstract class AbstractController implements Initializable {

	protected static Scene scene;
	protected SimpleLogger logger;
	protected ResourceBundle resources;
	protected static SimpleAlerts ALERTS;

	protected abstract void init();

	public void initialize(URL url, ResourceBundle resources) {

		this.logger = new SimpleLogger();
		this.resources = resources;
		ALERTS = App.getAlerts();

		init();

	}

	public void initStyle() {

		try {
			changeCSS(App.getURLCSS());
		} catch (URISyntaxException e) {

			logger.getExceptions().add("File not found?: " + App.getURLCSS(), e).show();
			e.printStackTrace();

		}

	}

	public static void changeCSS(URL urlCSS) throws URISyntaxException {

		scene.getStylesheets().clear();

		scene.getStylesheets().add(urlCSS.toURI().toString());

	}

	public void setScene(Scene scene) {
		AbstractController.scene = scene;
	}

	public ResourceBundle getResources() {
		return this.resources;
	}
	
}

