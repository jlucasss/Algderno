
package com.algderno.controllers.helper.service;

/**
 *
 * This class control message and value progress for thread/ProgressBar.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.lang.StringBuilder;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProgressSubmission {

	private SimpleDoubleProperty currentProgress;

	private SimpleStringProperty currentMessage;

	private String nameExercise, nameQuestion;

	private final double MAXPROGRESS, MAXEXERCISE;

	private int currentIndexExercise, currentIndexQuestion, maxQuestion;

	public ProgressSubmission(int maxExercise) {

		this.MAXPROGRESS = 1.0;
		this.MAXEXERCISE = maxExercise;
		this.maxQuestion = 0;

		this.currentProgress = new SimpleDoubleProperty(0.0);
		this.currentMessage = new SimpleStringProperty(StatusProgress.INITIALIZING.toString());

		this.currentIndexExercise = 0;
		this.currentIndexQuestion = 0;

		this.nameExercise = "";
		this.nameQuestion = "";

	}

	public void updateProgressExercise(String name, int current) {

		this.nameExercise = name;
		this.currentIndexExercise = current;

		updateMessage(StatusProgress.INITIALIZING);

	}

	private final double base = 10.0;

	public void updateProgressQuestion(String name, int current, int max, StatusProgress status) {

		if (!this.nameQuestion.equals(name))
			this.nameQuestion = name;

		this.currentIndexQuestion = current;
		this.maxQuestion = max;

		double decimalProgress = ((double)current) / ((double)max); // Part of Questions

		decimalProgress /= base; // One Question is a part of one Exercise

		decimalProgress += ((double)this.currentIndexExercise)/this.MAXEXERCISE; // Part of Exercises

		if (decimalProgress == 1) {
			this.currentIndexExercise++;
			this.currentIndexQuestion = 0;
		}

		this.setCurrentProgress(decimalProgress);

		updateMessage(status);

	}

	private void updateMessage(StatusProgress status) {

		StringBuilder tmpMessage = new StringBuilder();

		tmpMessage
			.append("(")
			.append(this.currentIndexExercise)
			.append("/")
			.append(this.MAXEXERCISE)
			.append(")")
			.append(this.nameExercise)
			.append("> ")
			.append("(")
			.append(this.currentIndexQuestion)
			.append("/")
			.append(this.maxQuestion)
			.append(")")
			.append(this.nameQuestion)
			.append(": ")
			.append(status.toString());

		this.setCurrentMessage(tmpMessage.toString());

	}

	public static enum StatusProgress {

		INITIALIZING("initializing"), PROCESSING("processing"), ERROR("error"), TESTED("tested");

		private String content;

		StatusProgress(String content) {
			this.content = content;
		}

		public String toString() {
			return this.content;
		}

	}

	public double getMAXPROGRESS() {
		return this.MAXPROGRESS;
	}

	/* Properties */

	private void setCurrentProgress(double currentProgress) {
		this.currentProgress.setValue(currentProgress);
	}

	public double getCurrentProgress() {
		return this.currentProgress.get();
	}

	public SimpleDoubleProperty currentProgressProperty() {
		return this.currentProgress;
	}

	private void setCurrentMessage(String currentMessage) {
		this.currentMessage.setValue(currentMessage);
	}

	public String getCurrentMessage() {
		return this.currentMessage.get();
	}

	public SimpleStringProperty currentMessageProperty() {
		return this.currentMessage;
	}


}

