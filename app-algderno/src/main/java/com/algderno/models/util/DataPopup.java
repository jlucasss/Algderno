
package com.algderno.models.util;

import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class DataPopup {

	private final SimpleStringProperty questionName, exerciseName;

	private final SimpleLongProperty lastRuntime;

	private SimpleBooleanProperty correct;

	private ResourceBundle resources;

	public DataPopup(String questionName, long lastRuntime, String exerciseName, boolean correct, ResourceBundle resources) {
		this.questionName = new SimpleStringProperty(questionName);
		this.exerciseName = new SimpleStringProperty(exerciseName);
		this.lastRuntime = new SimpleLongProperty(lastRuntime);
		this.correct = new SimpleBooleanProperty(correct);
		this.resources = resources;
	}

	public String getQuestionName() {
		return questionName.get();
	}

	public long getLastRuntime() {
		return lastRuntime.get();
	}

	public String getExerciseName() {
		return exerciseName.get();
	}
	
	public boolean isCorrect() {
		return correct.get();
	}
	
	public SimpleStringProperty questionNameProperty() {
		return this.questionName;
	}
	
	public SimpleStringProperty exerciseNameProperty() {
		return this.exerciseName;
	}
	
	public SimpleLongProperty lastRuntimeProperty() {
		return this.lastRuntime;
	}
	
	public SimpleBooleanProperty correctProperty() {
		return this.correct;
	}
	
	public String toCSV() {
		return ("\"" + questionName + "\";\"" +
				lastRuntime + "\";" + 
				exerciseName);
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.resources.getString("text.exercise"));
		sb.append(" = ");
		sb.append(this.getExerciseName());
		sb.append("\n");
		sb.append(this.resources.getString("text.question"));
		sb.append(" = ");
		sb.append(this.getQuestionName());
		sb.append("\n");
		sb.append(this.resources.getString("text.last.runtime"));
		sb.append(" = ");
		sb.append(this.getLastRuntime());
		
		return sb.toString();
		
		//return super.toString();
	}

}

