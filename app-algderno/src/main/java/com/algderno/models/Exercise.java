
package com.algderno.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Exercise extends Group<Question> {

	public Exercise() {

		super(-1, "null");
		this.setMapData( FXCollections.observableHashMap() );

	}

	public Exercise(int priority, String name) {

		super(priority, name);
		this.setMapData( FXCollections.observableHashMap() );

	}
	
	public Exercise(int priority, String name, ObservableMap<String, Question> mapQuestions) {

		super(priority, name);
		this.setMapData(mapQuestions);
		updateAverageResults();
		
	}

	public void updateAverageResults() {
		
		if (this.getMapData() == null)
			throw new NullPointerException("Null Question[]");
		
		if (this.getMapData().size() > 0) {
			
			long sum = 0;
			boolean corrects = true;
			
			for (Question question : mapData.values()) {
				sum += question.getLastRuntime();
				corrects = corrects && question.isResultCorrect();
			}
			
			this.setLastRuntime(sum);
			this.setResultCorrect(corrects);
			
		}
		
	}
	
	public void addNewQuestion(Question question) {

		//question.setPriority(getMapData().size());

		getMapData().put(question.getName(), question);

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(this.getName());

		sb.append(":[");
		this.getMapData().forEach(
				(index, question) -> sb.append(question.toString()));
		
		sb.append("];\n");

		return sb.toString();//super.toString();

	}

}

