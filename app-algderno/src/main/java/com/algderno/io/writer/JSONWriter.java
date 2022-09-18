package com.algderno.io.writer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;

/**
 *
 * This class retrieves a Workbook from a JSON using json20210307.jar.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

public class JSONWriter {

	private Workbook workbook;

	public JSONWriter(Workbook workbook) {
		this.workbook = workbook;
	}

	public String save(String localSave) throws IOException {

		if (workbook == null)
			throw new NullPointerException("Settings cannot be null.");

		try {

			localSave = localSave.replaceAll("\\\\", "/");

			Writer writer = new Writer(localSave + "/" + workbook.getName() + ".json");

			JSONObject exercises = convertExercisesToJSON();
			
			workbook.getMapData().values().forEach((ex) -> {
				System.out.println(ex.getName() + " has " + ex.getMapData().size() + "items");
			});
			
			JSONObject output = new JSONObject();
			putCommumObjects(output, workbook);
			output.put("PathFileSolution", workbook.getPathFileSolution());
			output.put("PathRoot", workbook.getPathRoot());
			output.put("ModelSelected", workbook.getModelSelected());
			output.put("Exercises", exercises);

			writer.writeLines( Arrays.asList(output.toString()) );

			return output.toString();

		} catch (IOException e) {
			throw new IOException("Error in write json.", e);
		}

	}

	public JSONObject convertExercisesToJSON() {

		Map<String, Exercise> map = workbook.getMapData();

		JSONObject output = new JSONObject();//map);

		for (Entry<String, Exercise> entry : map.entrySet())
			output.put(entry.getKey(), convertExerciseToJSON(entry.getValue()));
		
		return output;

	}

	public JSONObject convertExerciseToJSON(Exercise exercise) {

		JSONObject questionsJSON = new JSONObject();
		
		for (Entry<String, Question> entry : exercise.getMapData().entrySet())
			questionsJSON.put(entry.getKey(), convertQuestionToJSON(entry.getValue()));
		
		JSONObject output = new JSONObject();
		putCommumObjects(output, exercise);
		output.put("Questions", questionsJSON);
		
		return output;

	}

	public JSONObject convertQuestionToJSON(Question question) {

		JSONObject output = new JSONObject();
		putCommumObjects(output, question);
		output.put("MapData", question.getMapData());
		output.put("MaxRuntime", question.getMaxRuntime());
		
		return output;
	}
	
	private void putCommumObjects(JSONObject json, Group<?> group) {
		json.put("Priority", group.getPriority());
		json.put("Name", group.getName());
		json.put("LastRuntime", group.getLastRuntime());
		json.put("ResultCorrect", group.isResultCorrect());
	}

}
