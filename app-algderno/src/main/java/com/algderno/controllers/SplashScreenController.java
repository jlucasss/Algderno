package com.algderno.controllers;

import java.io.InputStream;
import java.util.ArrayList;

import com.algderno.App;
import com.algderno.util.ShowScreen;

import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreenController extends AbstractController {

	@FXML
	private VBox layoutVB;

	@FXML 
	private ProgressBar progressPB;

	@FXML
	private ImageView imageIV;
	
	@Override
	public void init() {
		
		InputStream logoIS = App.class.getResourceAsStream("images/logo.png");

		imageIV.setImage(new Image(logoIS));
		
	}
	
	public void loadTask() {
	
		Service<ArrayList<String>> service = new Service<ArrayList<String>>() {

			@Override
			public Task<ArrayList<String>> createTask() {
				
				return new Task<ArrayList<String>>() {
					@Override
					protected ArrayList<String> call() throws Exception {
						
						int max = 10;
						
						for (int i = 0; max > i; i++) {
							Thread.sleep(300);
							updateProgress(i, max-1);
							updateMessage((i*10)+"/100");
						}
						
						return null;
					}
				};
				
			}
			
		};

		service.start();

		service.setOnRunning((ru) -> transitionScreen(service));
		
		service.setOnSucceeded((su) -> loadMainScreen());
		
	}

	// https://pt.stackoverflow.com/questions/59859/como-usar-thread-em-javafx/63387#63387
	private void transitionScreen(Service<ArrayList<String>> service) {
		
		progressPB.progressProperty().bind(service.progressProperty());
		
		service.stateProperty().addListener((observableValue, oldState, newState) -> {
			
			if (newState == Worker.State.SUCCEEDED) {
				
					progressPB.progressProperty().unbind();
					progressPB.setProgress(1);
					App.mainStage.toFront();
					FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), layoutVB);
					fadeSplash.setFromValue(1.0);
					fadeSplash.setToValue(0.0);
					fadeSplash.setOnFinished((asd) -> App.mainStage.hide());
					fadeSplash.play();

			}

		});

	}
	
	private void loadMainScreen() {
	
		try {

			App.mainScreenStage = new Stage();
			
			InputStream iconInputStream = App.class.getResourceAsStream("images/symbol.png");

			ShowScreen showScreen = new ShowScreen(App.mainScreenStage);

			Parent parent = showScreen.findFXML("Main.fxml", "main");

			MainController mainController = (MainController) showScreen.getLoader().getController();

			// Define method of close
			App.mainScreenStage.setOnCloseRequest((e) -> mainController.closeMain());

			App.mainScreenStage.setScene(new Scene(parent, 1350, 645));

			App.mainScreenStage.getIcons().add(new Image(iconInputStream));
						
			App.mainScreenStage.setTitle("Algderno - Java");
			
			App.mainScreenStage.show();
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
		
	}

}
