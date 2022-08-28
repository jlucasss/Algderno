package com.algderno.io.writer;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONObject;

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

			JSONObject exercises = new JSONObject(workbook.getMapData());//convertExercisesToJSON();

			JSONObject output = new JSONObject();
			output.put("Name", workbook.getName());
			output.put("PathFileSolution", workbook.getPathFileSolution());
			output.put("PathRoot", workbook.getPathRoot());
			output.put("ModelSelected", workbook.getModelSelected());
			output.put("Exercise", exercises);

			writer.writeLines( Arrays.asList(output.toString()) );

			return output.toString();

		} catch (IOException e) {
			throw new IOException("Error in write json.", e);
		}

	}
/*
	public JSONObject convertExercisesToJSON() {

		Map<String, Exercise> map = workbook.getMapData();

		JSONObject output = new JSONObject(map);

		return output;

	}*/
/*
	public JSONObject convertExerciseToJSON(Exercise exercise) {

		JSONObject output = new JSONObject();
		output.put("Priority", exercise.getPriority());
		output.put("Name", exercise.getName());
		output.put("Questions", new JSONObject( exercise.getMapData() ) );

		return output;

	}

	public JSONObject convertQuestionToJSON(Question question) {

		JSONObject output = new JSONObject();

		output.put("Priority", question.getPriority());
		output.put("Name", question.getName());
		output.put("MapData", question.getMapData());
		output.put("LastRuntime", question.getLastRuntime());
		output.put("ResultCorrect", question.isResultCorrect());

		return output;
	}
*/
}
