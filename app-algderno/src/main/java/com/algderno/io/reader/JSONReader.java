package com.algderno.io.reader;

import org.json.JSONObject;

import com.algderno.controllers.NewWorkbookController.EnumModels;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
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
		
		String pathRoot = content.getString("PathRoot");
		String pathFileSolution = content.getString("PathFileSolution");
		EnumModels modelSelected =
				EnumModels.valueOf(content.getString("ModelSelected"));

		ObservableMap<String, Exercise> exercises = recoverExercisesMap(
					content.get("Exercises").toString()
				);

		Workbook workbook = new Workbook
				(-1, "", pathRoot, pathFileSolution,
						exercises, modelSelected);

		recoverCommumObjects(content, workbook);
		
		return workbook;
	}

	public ObservableMap<String, Exercise> recoverExercisesMap(String jsonPart) {

		JSONObject content = new JSONObject(jsonPart);
		
		ObservableMap<String, Exercise> exercises = FXCollections.observableHashMap();
		
		for (String key : content.keySet())
			exercises.put(
					key, 
					recoverExercise( content.get(key).toString() ) 
			);
		
		return exercises;

	}

	public Exercise recoverExercise(String jsonPart) {

		JSONObject content = new JSONObject(jsonPart);
		
		Exercise exercise = new Exercise();

		recoverCommumObjects(content, exercise);
		
		JSONObject map = content.getJSONObject("Questions");
		
		ObservableMap<String, Question> mapQuestions = FXCollections.observableHashMap();
		
		for (String key : map.keySet())
			mapQuestions.put(
					key, 
					recoverQuestion( map.get(key).toString() ) 
			);
		
		exercise.setMapData(mapQuestions);

		return exercise;
	}

	public Question recoverQuestion(String jsonPart) {

		JSONObject content = new JSONObject(jsonPart);
		
		JSONObject mapData = new JSONObject(content.get("MapData").toString());
		String pathInput = mapData.get("pathInput").toString(); 
		String pathOutputCorrect = mapData.get("pathOutputCorrect").toString();
		long maxRuntime = content.getLong("MaxRuntime");
		
		Question question = new Question(-1, "", pathInput, pathOutputCorrect,
				0, false, maxRuntime);

		recoverCommumObjects(content, question);
		
		return question;

	}

	private void recoverCommumObjects(JSONObject json, Group<?> group) {
		group.setPriority     ( Integer.parseInt(     json.get("Priority")     .toString()) );
		group.setName         (                       json.get("Name")         .toString()  );
		group.setLastRuntime  ( Long.parseLong(       json.get("LastRuntime")  .toString()) );
		group.setResultCorrect( Boolean.parseBoolean( json.get("ResultCorrect").toString()) );
	}
	
}
