package com.algderno.controllers.helper.templates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.models.Exercise;
import com.algderno.models.Question;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

class FilesTemplate1Test {


	private FilesTemplate1 template1;
	private String pathRoot;
	private HashMap<String, Exercise> listExercisesCorrects;

	@BeforeEach
	public void start() {

		pathRoot = null;
		try {

			pathRoot = new File(
						FilesTemplate2Test.class.getResource("examples/template1/")
					.toURI()).getAbsolutePath();

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		template1 = new FilesTemplate1(pathRoot);

		startExercisesComplete();

	}

	private void startExercisesComplete() {

		String exercise1 = "/1/",
				exercise2 = "/2/";
		
		int maxRuntime = 0;

		Question question1 = new Question(0, "1",
				exercise1+"1.in", exercise1+"1.sol", 0, false, maxRuntime);

		Question question2 = new Question(0, "1",
				exercise2+"1.in", exercise2+"1.sol", 0, false, maxRuntime);
		Question question3 = new Question(1, "2",
				exercise2+"2.in", exercise2+"2.sol", 0, false, maxRuntime);

		ObservableMap<String, Question> listQuestions1 = FXCollections.observableHashMap();
		listQuestions1.put("1", question1);

		ObservableMap<String, Question> listQuestions2 = FXCollections.observableHashMap();
		listQuestions2.put("1", question2);
		listQuestions2.put("2", question3);

		listExercisesCorrects = new HashMap<>();

		listExercisesCorrects.put("1", new Exercise(0, "1", listQuestions1));
		listExercisesCorrects.put("2", new Exercise(1, "2", listQuestions2));

	}

	/* Method createListExercises */

	@Test
	void createListExercisesComplete() {

		try {
			template1.createListExercises();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(listExercisesCorrects.toString(), template1.getListExercisesOutput().toString());

	}

	/* Method findExercises */

	@Test
	void findExercisesComplete() {

		try {
			template1.findExercises(new File(pathRoot).listFiles());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(listExercisesCorrects.toString(), template1.getListExercisesOutput().toString());

	}

	/* Method findQuestions */

	@Test
	void findQuestionsComplete() {

		Map<String, Question> correct = listExercisesCorrects.get("1").getMapData();

		Map<String, Question> current = null;

		try {
			current = template1.findQuestions("1", new File(pathRoot+"/1/").listFiles());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(correct.toString(), current.toString());

	}

	/* Method createQuestion */

	@Test
	void createQuestionComplete() {

		Question correct = listExercisesCorrects.get("1").getMapData().get("1");

		Question current = null;

		try {
			current = template1.createQuestion(0, "/1/", "1.in", "1");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		assertEquals(correct.toString(), current.toString());

	}

	/* Method hasCorrespondent */

	@Test
	void hasCorrespondentComplete() {

		assertTrue(template1.hasCorrespondent(
						template1.getRootFolder()+"/1/1.in",
						template1.getRootFolder()+"/1/1.sol")
					);

	}

}
