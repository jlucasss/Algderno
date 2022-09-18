package com.algderno.controllers;

/**
 *
 * This class controls the WorkbookProperties.fxml.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.util.Arrays;

import com.algderno.App;
import com.algderno.models.Workbook;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WorkbookPropertiesController extends AbstractController {

	@FXML
	private TextField nameTF;

	@FXML
	private TextField fileSolutionTF;

	@FXML
	private TextField rootTF;

	private Workbook workbook;

	@Override
	public void init() {

		String[] arrayNamesWorkbook = (String[]) MainController.mapWorkbooks.getMapData().keySet().toArray();
		
		String nameWorkbook = App.getAlerts()
									.boxChoice(resources.getString("message.reply"), 
												resources.getString("message.choose"), 
												resources.getString("message.what.workbook.open"), 
												arrayNamesWorkbook[0], 
												Arrays.asList(arrayNamesWorkbook)
												).get();
		
		workbook = MainController.mapWorkbooks.getMapData().get(nameWorkbook);

		restoreAllOn();

	}

	@FXML
	private void restoreAllOn() {

		if (workbook == null)
			return;

		nameTF.setText(workbook.getName());
		rootTF.setText(workbook.getPathRoot());
		fileSolutionTF.setText(workbook.getPathFileSolution());

	}

	@FXML
	private void selectSolutionOn() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(
				resources.getString("text.select.solution.file"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter(resources.getString("text.file.java"), "*.java"));

		File fileSelected = fileChooser.showOpenDialog(App.mainStage);

		if (fileSelected != null)
			fileSolutionTF.setText(fileSelected.getPath().replace("\\", "/"));

	}

	@FXML
	private void selectRootOn() {

		rootTF.setText(selectFolder());

	}


	private String selectFolder() {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(
				resources.getString("text.select.folder") );

		File diretorySelected = directoryChooser.showDialog(App.localStage);

		if (diretorySelected != null)
			return (diretorySelected.getPath().replace("\\", "/") + "/");

		return null;

	}

	private boolean isChanged() {

		return !(
				getName().equals(workbook.getName()) &&
				getFileSolution().equals(workbook.getPathFileSolution()) &&
				getRoot().equals(workbook.getPathRoot())
			);

	}

	private String getName() {
		return nameTF.getText();
	}

	private String getFileSolution() {
		return fileSolutionTF.getText();
	}

	private String getRoot() {
		return rootTF.getText();
	}

	@FXML
	private void saveOn() {

		// The method does not save in the same file, you need to save a new file
		
		if (workbook == null)
			return;

		if (isChanged()) {

			MainController.mapWorkbooks.getMapData().get(workbook.getName()).setName(getName());
			MainController.mapWorkbooks.getMapData().get(workbook.getName()).setPathRoot(getRoot());
			MainController.mapWorkbooks.getMapData().get(workbook.getName()).setPathFileSolution(getFileSolution());

		}

		close();

	}

	@FXML
	private void cancelOn() {
		close();
	}

	private void close() {
		App.localStage.close();
	}

}
