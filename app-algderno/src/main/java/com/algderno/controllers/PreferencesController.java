package com.algderno.controllers;

/**
 *
 * This class controls the PreferencesProperties.fxml.
 *
 * @author José Lucas dos Santos da Silva
 *
 * Note: yet to be finished...
 *
 */

import java.io.IOException;

import com.algderno.App;
import com.algderno.controllers.subscreens.CSSController;
import com.algderno.util.ShowScreen;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class PreferencesController extends AbstractController {

	@FXML
	private TreeView<String> menuTV;

	@FXML
	private AnchorPane contentAP;

	@SuppressWarnings("unused")
	private CSSController screenCSSController;

	private Parent cssParent;

	private ShowScreen showScreen;

	@Override
	public void init() {

		TreeItem<String> settings = new TreeItem<>(
				resources.getString("text.settings") );
		settings.setExpanded(true);

		TreeItem<String> css = new TreeItem<>(
				resources.getString("text.edit.appearance") );

		settings.getChildren().add(css);

		menuTV.setRoot(settings);

		addListenerTreeView();

		cssParent = null;

		this.screenCSSController = null;


	}

	private void addListenerTreeView() {

		menuTV.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

			if (menuTV.getSelectionModel().getSelectedItem() != null) {

				TreeItem<String> child = menuTV.getSelectionModel().getSelectedItem();

				if (child.getChildren().size() == 0)
					openSetting(child.getValue());

			}

		});


	}

	private void openSetting(String value) {

		String setting = "subscreens/";
		int minSize = 200;

		if ( value.equals(resources.getString("text.edit.appearance")) ) {

			setting += "CSS.fxml";

			App.localStage.setWidth(minSize + 360);

			if (cssParent != null)
				showScreen(cssParent);
			else
				createNewInstanceScreen(value, setting, "css");

			return;

		}

	}

	private void createNewInstanceScreen(String value, String screen, String bundleName) {

		try {
	
			if (this.showScreen == null)
				this.showScreen = new ShowScreen(null);
				
			Parent content = showScreen.findFXML(screen, bundleName);
			
			saveDataScreenCreated(value, showScreen.getLoader(), content);
			showScreen(content);

		} catch (IOException e) {
			
			logger.getExceptions().add(
					resources.getString("exception.could.not.open.new.screen"), e).show();
			e.printStackTrace();
		
		}
		
	}

	private void showScreen(Parent content) {

		contentAP.getChildren().clear();
		contentAP.getChildren().add(content);

	}

	private void saveDataScreenCreated(String value, FXMLLoader fxml, Parent parent) {

		switch (value) {

		case "Edit appearance":

			screenCSSController = fxml.getController();
			cssParent = parent;

		break;

		default:
			return;
		}

	}

	@FXML
	private void saveOn() {

		closeStage();

	}

	@FXML
	private void cancelOn() {
		closeStage();
	}

	private void closeStage() {
		App.localStage.close();
	}

}
