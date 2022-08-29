
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
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Exercise = ");
		sb.append(this.getExerciseName());
		sb.append("\n");
		sb.append("Question = ");
		sb.append(this.getQuestionName());
		sb.append("\n");
		sb.append("Last Runtime = ");
		sb.append(this.getLastRuntime());
		
		return sb.toString();
		
		//return super.toString();
	}

}

