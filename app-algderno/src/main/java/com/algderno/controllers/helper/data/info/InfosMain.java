package com.algderno.controllers.helper.data.info;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;

import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class InfosMain {

	private AnchorPane anchorPane;
	public static ResourceBundle resources;
	private Map<String, WorkbookInfos> mapInfos;
	private WorkbookInfos general;
	public int selecteds;
	
	public InfosMain(AnchorPane anchorPane, ResourceBundle resources) {
		
		this.anchorPane = anchorPane;
		InfosMain.resources = resources;
		this.selecteds = 0;
		
		this.mapInfos = new HashMap<>();
		
		this.general = new WorkbookInfos("General", 0, 0, 0);

		anchorPane.getChildren().add(this.general.createGridPane());
		
		anchorPane.setOnMouseClicked(opeAlertnMapInfos());
		
	}

	/* Show all WorkbookInfos in alert with scroll */
	private EventHandler<? super MouseEvent> opeAlertnMapInfos() {

		return ((mouseEvent) -> {

			VBox vbox = new VBox();
			vbox.getChildren().add(anchorPane);
			
			mapInfos.forEach((key, value) -> 
								vbox.getChildren().add(value.createGridPane()));
			
			App.getAlerts().alertWithContent(
					resources.getString("text.info.title"), 
					resources.getString("text.info.subtitle"), 
					resources.getString("text.info.message"),
					new ScrollPane(vbox));
			
		});

	}

	/* Update all WorkbookInfos */
	public void updateAllWorkbookInfos(TreeItem<Group<?>> rootWorkbooks) {

		if (this.mapInfos.size() > 0)
			this.mapInfos.clear();
		
		this.general.updateInfos(0, 0, 0);
		
		int amountExercise = 0, 
				amountQuestion = 0, 
				amountCorrect = 0,
				generalAmountExercise = 0, 
				generalAmountQuestion = 0, 
				generalAmountCorrect = 0;
		
		for (TreeItem<Group<?>> itemWorkbook : rootWorkbooks.getChildren()) {
			
			ObservableMap<String, Exercise> mapExercise = ((Workbook)itemWorkbook.getValue()).getMapData();

			//Amount exercises, questions and correct questions
			amountExercise = 0;
			amountQuestion = 0;
			amountCorrect = 0;
			for (Entry<String, Exercise> exercise : mapExercise.entrySet()) { // for each exercise
				
				amountExercise++;
				
				for (Entry<String, Question> question : exercise.getValue().getMapData().entrySet()) { // for each question
					
					amountQuestion++;
					if (question.getValue().isResultCorrect())
						amountCorrect++;
					
				}
				
			}

			// Update content map of WorkbookInfos
			updateMapInfos(amountExercise, amountQuestion, amountCorrect, 
							itemWorkbook.getValue().getName());
			
			
			//Update infos of this.general
			generalAmountExercise += amountExercise;
			generalAmountQuestion += amountQuestion;
			generalAmountCorrect += amountCorrect;
			
		}

		this.general.updateInfos(generalAmountExercise, generalAmountQuestion, generalAmountCorrect);
		
	}

	private void updateMapInfos(int amountExercise, int amountQuestion, int amountCorrect, String nameWorkbook) {
		
		if (this.mapInfos.containsKey(nameWorkbook)) // If exist
			this.mapInfos.get(nameWorkbook).updateInfos(amountExercise, amountQuestion, amountCorrect);
		else {
		
			this.mapInfos.put(nameWorkbook, new WorkbookInfos(nameWorkbook,
					amountExercise, amountQuestion, amountCorrect));
		
			this.mapInfos.get(nameWorkbook).createGridPane();
			
		}
	}	
	
}
