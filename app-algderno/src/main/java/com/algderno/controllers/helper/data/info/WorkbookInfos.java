package com.algderno.controllers.helper.data.info;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class WorkbookInfos {
	
	public String name;
	public int amountExercise,
				amountQuestion,
				amountCorrect ;

	public WorkbookInfos(String name) {

		this.name = name;
		updateInfos(0, 0, 0);
		
	}
	
	public WorkbookInfos(String name,
							int amountExercise,
							int amountQuestion,
							int amountCorrect) {
		
		this.name = name;
		updateInfos(amountExercise, amountQuestion, amountCorrect);
		
	}
	
	/* Update texts of labels */
	public void updateInfos(
			int amountExercise,
			int amountQuestion,
			int amountCorrect) {

		this.amountExercise = amountExercise;
		this.amountQuestion = amountQuestion;
		this.amountCorrect = amountCorrect;
		
		/*this.amountExercise = createOrUpdate(this.amountExercise, amountExercise);
		this.amountQuestion = createOrUpdate(this.amountQuestion, amountQuestion);
		this.amountCorrect = createOrUpdate(this.amountCorrect, amountCorrect);
		*/
	}
	
	/* Create label if not exist or update the text */	
	/*private Label createOrUpdate(Label label, int content) {
		
		if (label == null)
			return new Label("" + content);
		
		label.setText("" + content);
		
		return label;
	}*/
	
	/* Create and fill gridPane */
	public GridPane createGridPane() {

		GridPane gridPane = new GridPane();
		
		gridPane.add(new Label(this.name), 0, 0);
		
		addGridPane(gridPane, "text.info.amount.exercise", this.amountExercise);
		addGridPane(gridPane, "text.info.amount.question", this.amountQuestion);
		addGridPane(gridPane, "text.info.amount.correct", this.amountCorrect);
		
		return gridPane;
		
	}
	
	private int countLine = 1;
	
	/* Add content in GridPane */
	private void addGridPane(GridPane gridPane, String infoResource, int amount) {

		gridPane.add(new Label(InfosMain.resources.getString(infoResource)), 0, countLine);
		gridPane.add(new Label("" + amount), 1, countLine++);

	}
	
	
}