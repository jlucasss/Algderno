package com.algderno.controllers.helper.templates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.models.Exercise;
import com.algderno.models.Question;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

class FilesTemplate2Test {

	private FilesTemplate2 template2;
	private String pathRoot;
	private HashMap<String, Exercise> listExerciseCorrect;

	@BeforeEach
	public void start() {

		pathRoot = null;
		try {

			pathRoot = new File(
						FilesTemplate2Test.class.getResource("examples/template2/")
					.toURI()).getAbsolutePath();

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		template2 = new FilesTemplate2(pathRoot);

		startExercisesComplete();

	}

	private void startExercisesComplete() {

		String nameExercise = "template2",
				pathInput = "/input/",
				pathOutput = "/output/";

		int maxRuntime = 0;
		
		Question question1 = new Question(0, "A",
				pathInput+"A", pathOutput+"A", 0, false, maxRuntime);
		Question question2 = new Question(1, "B",
				pathInput+"B", pathOutput+"B", 0, false, maxRuntime);
		Question question3 = new Question(2, "C",
				pathInput+"C", pathOutput+"C", 0, false, maxRuntime);

		ObservableMap<String, Question> listQuestions =  FXCollections.observableHashMap();
		listQuestions.put("0", question1);
		listQuestions.put("1", question2);
		listQuestions.put("2", question3);

		listExerciseCorrect = new HashMap<>();

		listExerciseCorrect.put(nameExercise, new Exercise(0, nameExercise, listQuestions));

	}

	/* Method getListExercisesOutput */

	@Test
	void getListExercisesOutputCorrect() {

		try {
			template2.createListExercises();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map<String, Exercise> listaAtual = template2.getListExercisesOutput();

		assertEquals(listExerciseCorrect.toString(), listaAtual.toString());

	}

	/* Method findExercise */

	@Test
	void findExerciseCorrect() {

		try {

			template2.findExercise(new File(pathRoot).list(),
					listExerciseCorrect.get("template2").getName());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map<String, Exercise> listCurrent = template2.getListExercisesOutput();

		assertEquals(listExerciseCorrect.toString(), listCurrent.toString());

	}

	/* Method createListQuestions */

	@Test
	void createListQuestionsCorrect() {

		String pathInput = "/input/",
				pathOutput = "/output/";

		Question question1 = new Question(0, "1",
				pathInput+"1", pathOutput+"1", 0, false, 0);
		Question question2 = new Question(1, "2",
				pathInput+"2", pathOutput+"2", 0, false, 0);

		Map<String, Question> listCorrect = new HashMap<>(); 
		listCorrect.put("1", question1);
		listCorrect.put("2", question2);

		template2.checkEqualityOfLists(
				new String[] {"1", "2"},
			    new String[] {"1", "2"});

		Map<String, Question> listCurrent =
				template2.createListQuestions();

		assertEquals(listCorrect.toString(), listCurrent.toString());

	}

	/* Method checkEqualityOfLists */

	@Test
	void checkEqualityOfListsCorrect() {

		String[] list1 = new String[] {"1.in", "2.in", "3.in"};
		String[] list2 = new String[] {"1.in", "2.in", "3.in"};

		template2.checkEqualityOfLists(list1, list2);

		assertEquals(Arrays.asList().size(), template2.noFound.size());

	}

	/* Method removeExtensionList */

	@Test
	void removeExtensionListCorretWithExtension() {

		String[] list = new String[] {"1.in", "2.in", "3.in"};

		assertEquals(Arrays.asList("1","2","3"),
						Arrays.asList(template2.removeExtensionList(list))
					);

	}

	@Test
	void removeExtensionListCorrectWithoutExtension() {

		String[] list = new String[] {"1", "2", "3"};

		assertEquals(Arrays.asList("1","2","3"),
						Arrays.asList(template2.removeExtensionList(list))
					);

	}

}
