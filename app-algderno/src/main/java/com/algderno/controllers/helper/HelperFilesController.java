package com.algderno.controllers.helper;

/**
 *
 * This class has the job of helping FilesController to separate FXML methods and private helpers, thus organizing the controller.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.algderno.App;
import com.algderno.controllers.FilesController;
import com.algderno.controllers.MainController;
import com.algderno.io.reader.Reader;
import com.algderno.io.writer.Writer;
import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.models.Workbook;
import com.algderno.util.SimpleAlerts;
import com.algderno.util.logger.SimpleLogger;

import javafx.scene.control.TextArea;

public class HelperFilesController {

	private FilesController controller;
	private SimpleAlerts ALERTS;
	private SimpleLogger logger;

	public HelperFilesController(FilesController controller, SimpleLogger logger) {
		this.controller = controller;
		this.logger = logger;
		ALERTS = App.getAlerts();
	}

	/* FILES */

	public void save(TextArea inputTA, TextArea outputCorrectTA, TextArea outputCurrentTA) {

		if (inputTA.getText().isEmpty() ||
				outputCorrectTA.getText().isEmpty()) 
			logger.getErrors().add("Fill in the expected Input and Output to save!.").show();

		try {

			String local = MainController.mapWorkbooks.getMapData().get(workbookCurrent.getName()).getPathRoot();

			new Writer(local + controller.pathInput).writeLines(
					Arrays.asList( inputTA.getText().split("\n") )
							);
			new Writer(local + controller.pathOutputCorrect).writeLines(
					Arrays.asList( outputCorrectTA.getText().split("\n") )
					);

		} catch (IOException e) {

			logger.getExceptions().add("An error has happened.", e).show();
			e.printStackTrace();

		}

		//MainController.helper.updateTreeView();

		App.localStage.close();

	}

	/* Save Models */

	public Question caseModel1(String nameWorkbook, String nameExercise, String nameQuestion) {

		if (nameWorkbook.isEmpty() || nameExercise.isEmpty() || nameQuestion.isEmpty()) {

			logger.getErrors().add("You cannot leave the previous box empty!").show();

			return null;

		}

		controller.pathInput = nameExercise + "/" + nameQuestion + ".in";
		controller.pathOutputCorrect = nameExercise + "/" + nameQuestion + ".sol";

		return addQuestionToExercise(nameWorkbook, nameExercise, nameQuestion);

	}

	private Question addQuestionToExercise(String nameWorkbook, String nameExercise, String nameQuestion) {

		//Create Question

		Question question = new Question(-1, nameQuestion, controller.pathInput, controller.pathOutputCorrect,
				0, false, 0);

		//Add in exercise

		Map<String, Exercise> mapExercise = MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData();

		//Find existing exercise
		boolean hasExercise = mapExercise.containsKey(nameExercise);

		//If it doesn't exist, create it
		if (!hasExercise)
			mapExercise.put(nameExercise, new Exercise(-1, nameExercise));

		//Add question

		mapExercise.get(nameExercise).addNewQuestion(question);

		return question;

	}

	public Question caseModel2(String nameWorkbook, String nameExercise, String nameQuestion) {

		String extention = ALERTS.questionTextInput("Question",
				"Answer", "What is the file extension(.txt, etc.)?"
						+ "\nCan be without extension too.", "");

		controller.pathInput = "/input/" + nameQuestion + extention;
		controller.pathOutputCorrect = "/output/" + nameQuestion + extention;

		//Create Question

		Question question = new Question(-1, nameQuestion, controller.pathInput,
				controller.pathOutputCorrect, 0, false, 0);

		//Add in exercise

		Exercise exercise = MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData().get(nameExercise); // Future edit

		exercise.addNewQuestion(question);

		return question;

	}

	/* To TextAreas */

	public TextArea selectTA(TextArea inputTA, TextArea outputCorrectTA, TextArea outputCurrentTA) {

		if (inputTA.isFocused())
			return inputTA;
		else if (outputCurrentTA.isFocused())
			return outputCurrentTA;
		else if (outputCorrectTA.isFocused())
			return outputCorrectTA;

		return null;
	}

	private Workbook workbookCurrent;
	public Question questionCurrent;
	
	public void fillTextAreas(TextArea inputTA, TextArea outputCorrectTA, TextArea outputCurrentTA) {

		//Sets up path

		this.workbookCurrent = MainController.helper.selectedWorkbook;
		
		this.questionCurrent = (Question) ((Exercise)workbookCurrent.getMapData().values().toArray()[0])
															.getMapData().values().toArray()[0];

		String input = workbookCurrent.getPathRoot() + questionCurrent.getMapData().get("pathInput"),

				outputCorrect = workbookCurrent.getPathRoot() + questionCurrent.getMapData().get("pathOutputCorrect"),

				outputCurrent = MainController.pathDefault + "/" + workbookCurrent.getName() + "/" + 
									questionCurrent.getMapData().get("pathOutputCorrect");

		//Verify fills with content of files

		fillTextArea(input, inputTA, false);
		fillTextArea(outputCorrect, outputCorrectTA, false);
		fillTextArea(outputCurrent, outputCurrentTA, true);

	}

	public void fillTextArea(String input, TextArea ta, boolean ignoreError) {

		ta.clear();

		if (new File(input).exists()) {

			try {

				new Reader(input).readLines().forEach(e -> ta.appendText(e + "\n"));

			} catch (IOException e) {

				logger.getExceptions().add("An error occurred while trying to load the file: " + input, e).show();
				e.printStackTrace();

			}

		} else if (!ignoreError)
			logger.getErrors().add("File not found: " + input).show();

	}

}

