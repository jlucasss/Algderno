package com.algderno.controllers;

import com.algderno.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewQuestionController extends AbstractController {


	@FXML
	private TextField nameQuestionTF;

	@FXML
	private ComboBox<String> nameWorkbookCB;

	@FXML
	private ComboBox<String> nameExerciseCB;

	@FXML
	private Button saveB;
	
	@FXML
	private Label statusL;
	
	private static String[] namesSelecteds;
	
	@Override
	public void init() {

		nameQuestionTF.setOnInputMethodTextChanged((e) -> {
			
			String valueW = nameWorkbookCB.getValue();
	
			if (valueW.isEmpty()) {
				updateStatus(Status.ERROR_SOME_EMPTY);
				return;
			}
			
			if (MainController.mapWorkbooks.getMapData().containsKey(valueW)) { // If exist Workbook name
				
					String valueE = nameExerciseCB.getValue();
			
					if (valueE.isEmpty()) {
						updateStatus(Status.ERROR_SOME_EMPTY);
						return;
					}
					
					if (MainController.mapWorkbooks.getMapData().get(valueW).getMapData().containsKey(valueE)) { // If exist Exercise name
						
						if (nameQuestionTF.getText().isEmpty()) {
							updateStatus(Status.ERROR_SOME_EMPTY);
							return;
						}
						
						if (valueE.isEmpty()) {
							updateStatus(Status.LABEL_STATUS_OK);
							return;
						}
						
						if (MainController.mapWorkbooks.getMapData().get(valueW).getMapData().get(valueE).getMapData().containsKey(nameQuestionTF.getText())) // If exist Question name
							updateStatus(Status.ERROR_NAME_QUESTION_EXIST);
					}
				}
					
		});
		
	}
	
	private void updateStatus(Status status) {
		
		statusL.setText(prepareResourceString(status));
		
		if (status.toString().startsWith("ERROR")) {
			
			statusL.setStyle("-fx-text-fill: red");
			saveB.setDisable(true);
			
		} else if (status.toString().startsWith("WARNING")) {
			
			statusL.setStyle("-fx-text-fill: yellow");
			saveB.setDisable(false);
			
		} else {
			
			statusL.setStyle("");
			saveB.setDisable(false);
			
		}
		
	}
	
	private String prepareResourceString(Status status) {
		
		return resources.getString(status.toString().toLowerCase().replaceAll("_", "."));
		
	}

	private enum Status {
		
		LABEL_STATUS_OK,
		
		ERROR_SOME_EMPTY,
		ERROR_NAME_QUESTION_EXIST,
		ERROR_WORKBOOK_NO_EXIST,
		
		WARNING_EXERCISE_NO_EXIST
		
	}
	
	public static String[] getNamesSelecteds() {
		return NewQuestionController.namesSelecteds;
	}

	@FXML
	private void saveOn() {

		if (nameWorkbookCB.getValue().isEmpty() ||
				nameExerciseCB.getValue().isEmpty() ||
				nameQuestionTF.getText().isEmpty()) {
			
			updateStatus(Status.LABEL_STATUS_OK);
			return;
		
		}
		
		namesSelecteds = new String[] {
				nameWorkbookCB.getValue(),
				nameExerciseCB.getValue(),
				nameQuestionTF.getText()
				};
		
		close();
		
	}

	@FXML
	private void cancelOn() {
		close();
	}

	private void close() {
		App.localStage.close();
	}
	
}
