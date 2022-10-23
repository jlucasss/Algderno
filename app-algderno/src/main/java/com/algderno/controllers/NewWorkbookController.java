
package com.algderno.controllers;

/**
 *
 * This class controls the NewWorkbook.fxml.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;

import com.algderno.App;
import com.algderno.controllers.helper.HelperNewWorkbookController;
import com.algderno.controllers.helper.templates.FilesTemplate1;
import com.algderno.controllers.helper.templates.FilesTemplate2;
import com.algderno.models.Exercise;
import com.algderno.models.Workbook;
import com.algderno.util.DataValidation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class NewWorkbookController extends AbstractController {

/* MENU */

	@FXML
	private TextField nameTF;

	@FXML
	private TextField pathFileSolutionTF;

	@FXML
	private TextField pathRootTF;

	@FXML
	private ToggleGroup modelsTG;

	@FXML
	private TreeView<String> model1TV;

	@FXML
	private TreeView<String> model2TV;

	@FXML
	private VBox textHelpModelsVB;

	@FXML
	private CheckBox containsFilesCB;

	private HelperNewWorkbookController helper;

	private Workbook workbookNew;

	@Override
	public void init() {
		
		helper = new HelperNewWorkbookController(logger);

		helper.fillModel1TreeView(model1TV);
		helper.fillModel2TreeView(model2TV);

	}

	@FXML
	private void selectFileSolutionOn() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(resources.getString("text.open.file.solution"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Java files", "*.java"));

		File fileSelected = fileChooser.showOpenDialog(App.mainStage);

		if (fileSelected != null)
			pathFileSolutionTF.setText(fileSelected.getPath().replace("\\", "/"));

	}

	@FXML
	private void selectFolderRootOn() {

		pathRootTF.setText(helper.selectFolder());

	}

	@FXML
	private void saveOn() {

		String name = nameTF.getText();
		String pathFileSolution = pathFileSolutionTF.getText();
		String pathFolderRoot = pathRootTF.getText();

		boolean validation =
				DataValidation.validStringsContent(new String[] {name, pathFileSolution, pathFolderRoot}) &&
				DataValidation.validPaths(new String[] {pathFileSolution, pathFolderRoot});

		if (validation) {

			if (modelsTG.getSelectedToggle() == null) {

				logger.getErrors().add(
						resources.getString("error.didnt.select.touggle") ).show();

				return;

			}

			String textToggled = ((RadioButton)modelsTG.getSelectedToggle()).getText().toString();
			
			int codeModel = Integer.parseInt( textToggled.substring(textToggled.length()-1) );
			
			EnumModels selected = EnumModels.getModel(codeModel);

			ObservableMap<String, Exercise> mapExercises = FXCollections.emptyObservableMap();

			if (containsFilesCB.isSelected()) { // If exists files(Questions,...)

				try {

					if (selected.equals(EnumModels.Model1))
						mapExercises = new FilesTemplate1(pathFolderRoot).createListExercises();
					else if (selected.equals(EnumModels.Model2))
						mapExercises = new FilesTemplate2(pathFolderRoot).createListExercises();

				} catch (Exception e) {

					logger.getExceptions().add(
							resources.getString("exception.creating.new.workbook"), e).show();
					
					e.printStackTrace();

					return;
				}

			}

			this.workbookNew = new Workbook(MainController.mapWorkbooks.getMapData().size(), name, pathFolderRoot,
								pathFileSolution, mapExercises, selected);
			
		} else
			logger.getErrors().add(
					resources.getString("error.some.data.not.correctly") ).show();

		closeOn();

	}

	@FXML
	private void closeOn() {

		App.localStage.close();

	}

	@FXML
	private void containsFilesOnCB() {

/*		boolean containsFiles = !containsFilesCB.isSelected();

		modelsTG.getToggles()
					.forEach(t -> ((RadioButton)t).setDisable(containsFiles) );

		textHelpModelsVB.setDisable(containsFiles);
*/
	}

	public static enum EnumModels { //Model = Template

		Model1(1),
		Model2(2);

		private int codeModel;
		//private static ResourceBundle bundle;
		
		EnumModels(int code) {
			this.codeModel = code;
		}

		public int getCodeModel() {
			return this.codeModel;
		}

		public static EnumModels getModel(int codeModel) {

			//bundle = ResourceBundle.getBundle("bundle." + App.getLocale() + ".newworkbook", App.getLocale());

			switch (codeModel) {//(bundle.getString("radiobutton.template1").equals(text))
			case 1: 
				return Model1;
			case 2: //else if (bundle.getString("radiobutton.template2").equals(text))
				return Model2;
			}
			return null;
			
		}

	}

	public Workbook getWorkbookNew() {
		return this.workbookNew;
	}

}

