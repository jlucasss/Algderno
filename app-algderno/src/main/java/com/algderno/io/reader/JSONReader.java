package com.algderno.io.reader;

import org.json.JSONObject;

import com.algderno.controllers.NewWorkbookController.EnumModels;
import com.algderno.models.Exercise;
import com.algderno.models.Workbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class JSONReader {

	private String json;

	public JSONReader(String json) {
		this.json = json;
	}

	public Workbook getWorkbook() {

		if (json == null)
			throw new NullPointerException("JSON cannot be null.");

		JSONObject content = new JSONObject(json);

		String name = content.getString("Name");
		String pathRoot = content.getString("PathRoot");
		String pathFileSolution = content.getString("PathFileSolution");
		EnumModels modelSelected =
				EnumModels.valueOf(content.getString("ModelSelected"));

		ObservableMap<String, Exercise> exercises = recoverExercisesMap(
					content.get("Exercises").toString()
				);

		Workbook workbook = new Workbook
				(-1, name, pathRoot, pathFileSolution,
						exercises, modelSelected);

		return workbook;
	}

	public ObservableMap<String, Exercise> recoverExercisesMap(String jsonPart) {

		ObservableMap<String, Exercise> exercises = FXCollections.emptyObservableMap();

		new JSONObject(jsonPart).toMap()
				.forEach((key, value) -> 
					exercises.put(key, ((Exercise)value))
		);
		
		return exercises;

	}
/*
	public Exercise recoverExercise(String jsonPart) {

		Exercise exercise = new Exercise();


		JSONObject content = new JSONObject(jsonPart);

		String name = content.getString("Name");
		exercise.setName(name);

		Map<String, Object> map = content.getJSONObject("Questions").toMap();
		
		ObservableMap<String, Question> mapQuestions = FXCollections.observableHashMap();
		map.forEach((key, value) -> mapQuestions.put(key, ((Question)value)) );
		
		exercise.setMapData(mapQuestions);

		return exercise;
	}*/
/*
	public Question recoverQuestion(String jsonPart) {

		JSONObject content = new JSONObject(jsonPart);

		int priority = content.getInt("Priority");
		String name = content.getString("Name");
		long lastRuntime = content.getLong("LastRuntime");
		String pathInput = content.getString("PathInput");
		String pathOutputCorrect = content.getString("PathOutputCorrect");
		boolean resultCorrect = content.getBoolean("ResultCorrect");
		long maxRuntime = content.getLong("MaxRuntime");
		
		Question question = new Question(priority, name, pathInput, pathOutputCorrect,
				lastRuntime, resultCorrect, maxRuntime);

		return question;

	}*/

}
