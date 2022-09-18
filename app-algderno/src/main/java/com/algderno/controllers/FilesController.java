
package com.algderno.controllers;

import com.algderno.App;
import com.algderno.controllers.helper.HelperFilesController;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class FilesController extends AbstractController {

/* MENU */

	public String pathInput, pathOutputCorrect;

	@FXML
	private TextArea inputTA, outputCurrentTA, outputCorrectTA;

	private HelperFilesController helper;

	@Override
	public void init() {

		pathInput = null;
		pathOutputCorrect = null;

		helper = new HelperFilesController(this, logger);

		outputCurrentTA.setEditable(false);
		
	}

	public void openQuestion() {

		helper.fillTextAreas(inputTA, outputCorrectTA, outputCurrentTA);
			
	}

	@FXML
	private void saveMIOn() {

		if (helper.questionCurrent.getName() == null) {

			saveAsMIOn();
			return;

		} else {

			this.pathInput = MainController.path +
					helper.questionCurrent.getMapData().get("pathInput");

			pathOutputCorrect = MainController.path +
					helper.questionCurrent.getMapData().get("pathOutputCorrect");

			helper.save(inputTA, outputCorrectTA, outputCurrentTA);

		}

	}

	@FXML
	private void saveAsMIOn() {

		String[] namesSelecteds = null;
		
		try {
			
			App.showScreen.setWait(true);
			App.showScreen.simpleScreen(MainController.path + "NewQuestion.fxml", 400, 315, "NewQuestion", "");

			namesSelecteds = NewQuestionController.getNamesSelecteds();
			
		} catch (Exception e) {
			
			logger.getExceptions().add("", e).show();
			e.printStackTrace();
		
		}
		
		if (namesSelecteds == null) {

			logger.getErrors().add(
					resources.getString("error.leave.question.empty")).show();

			return;

		}

		switch (MainController.mapWorkbooks.getMapData().get(namesSelecteds[2]).getModelSelected()) {

		case Model1:

			helper.questionCurrent = helper.caseModel1(namesSelecteds[0], namesSelecteds[1], namesSelecteds[2]);

			break;
		case Model2:

			helper.questionCurrent = helper.caseModel2(namesSelecteds[0], namesSelecteds[1], namesSelecteds[2]);

			break;
		default:
			//throw new
			break;

		}

		helper.save(inputTA, outputCorrectTA, outputCurrentTA);

	}

	@FXML
	private void undoMIOn() {

		inputTA.undo();
		outputCurrentTA.undo();
		outputCorrectTA.undo();

	}

	@FXML
	private void redoMIOn() {

		inputTA.redo();
		outputCurrentTA.redo();
		outputCorrectTA.redo();

	}

	@FXML
	private void closeMIOn() {}

	/* EDITAR */

	private TextArea selected;

	public int questionPriority;

	@FXML
	private void redoOneMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.redo();

	}

	@FXML
	private void cutOneMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.cut();

	}

	@FXML
	private void copyOneMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.copy();

	}

	@FXML
	private void pasteOneMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.paste();

	}

	@FXML
	private void deletePreviousCharMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.deletePreviousChar();

	}

	@FXML
	private void selectAllMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.selectAll();

	}

	@FXML
	private void deselectAllMIOn() {

		selected = helper.selectTA(inputTA, outputCorrectTA, outputCurrentTA);
		if (selected != null)
			selected.selectAll();

	}

}
