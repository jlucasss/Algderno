
package com.algderno.models.util;

public class DataPopup {

	private final String questionName, exerciseName;

	private final long lastRuntime;

	public DataPopup(String questionName, long lastRuntime, String exerciseName) {
		this.questionName = questionName;
		this.exerciseName = exerciseName;
		this.lastRuntime = lastRuntime;
	}

	public String getQuestionName() {
		return questionName;
	}

	public long getLastRuntime() {
		return lastRuntime;
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public String toCSV() {
		return ("\"" + questionName + "\";\"" +
				lastRuntime + "\";" + 
				exerciseName);
	}

}

