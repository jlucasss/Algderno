
package com.algderno.controllers;

/**
 *
 * This class controls the Main.fxml.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.algderno.App;
import com.algderno.App.Languages;
import com.algderno.controllers.helper.HelperFiltersMainController;
import com.algderno.controllers.helper.HelperMainController;
import com.algderno.controllers.helper.data.info.InfosMain;
import com.algderno.controllers.helper.service.SubmissionThread;
import com.algderno.io.reader.JSONReader;
import com.algderno.io.reader.Reader;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;
import com.algderno.models.util.SimpleGroup;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController extends AbstractController {

	/* TOP */

	@FXML
	private Pane escapamentPane;

	@FXML
	private ComboBox<Languages> languageCB;

	@FXML
	public TextField searchTF;

	/* LEFT */

	@FXML
	public TreeView< String > treeTV;

	/* CENTER */

	@FXML
	public AnchorPane infosAP;
	
	@FXML
	public TreeTableView< Group<?> > resultsTTV;

	@FXML
	private TreeTableColumn<Question, Integer> priorityTTC;
	
	@FXML
	private TreeTableColumn<Question, String> nameTTC;

	@FXML
	private TreeTableColumn<Question, Long> timeTTC;

	@FXML
	private TreeTableColumn<Question, Boolean> statusTTC;

	/* RIGHT */

	@FXML
	public AnchorPane chartAP;

	@FXML
	public AnchorPane filtersAP;

	/* BOTTOM */

	@FXML
	public ProgressBar progressPB;

	@FXML
	public Label leftL;

	@FXML
	public Label rightL;

	@FXML
	private Button playB, pauseB, stopB;

	/*  */

	public static String path;

	public static Map<String, Workbook> mapWorkbooks;

	public static FilteredList<TreeItem<Group<?>>> filteredData;

	public static HelperMainController helper;

	public static HelperFiltersMainController helperFilters;

	public static String pathDefault;

	private SubmissionThread service;

	public static InfosMain infosMain;
	
	@Override
	public void init() {

		path = "";
		
		pathDefault = System.getProperty("user.dir");

		mapWorkbooks = new HashMap<>();
		
		// ComboBox language fill
		languageCB.getItems().setAll(FXCollections.observableArrayList(Languages.values()));
		languageCB.getSelectionModel().select(Languages.valueOf(App.getLocale().toString().toUpperCase()));

		// TreeView
		treeTV.setEditable(true);
		treeTV.setCellFactory(CheckBoxTreeCell.< String >forTreeView());

		HBox.setHgrow(escapamentPane, Priority.ALWAYS);

		infosMain = new InfosMain(infosAP, resources);
		
		// Set size selected items
		treeTV.selectionModelProperty().addListener((l) ->
			infosMain.selecteds = 
					treeTV.getSelectionModel().getSelectedIndices().size() 
		);
		
		priorityTTC.setCellValueFactory(new TreeItemPropertyValueFactory<>("priority"));
		nameTTC.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
		timeTTC.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastRuntime"));
		//memoryTC.setCellValueFactory(new PropertyValueFactory<>("memoryUsed"));
		statusTTC.setCellValueFactory(new TreeItemPropertyValueFactory<>("resultCorrect"));

		// Change the color of each cell in the "status" column
		statusTTC.setCellFactory(column -> {
			return new TreeTableCell<>() {
				@Override
				protected void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {

						setText(null);
						setStyle("");

					} else {
						if (item.booleanValue()) {
							setStyle("-fx-background-color: darkgreen");
							setText("Correct");
							setTextFill(Color.WHITE);
						} else {
							setStyle("-fx-background-color: darkred");
							setText("Incorrect");
							setTextFill(Color.WHITE);
						}
					}
				}
			};
		});

		resultsTTV.setPlaceholder( new Label(
				resources.getString("text.not.opened.workbook")) );

		helper = new HelperMainController(this, logger);

		helper.booting();

		helper.addListenerTreeView();

		helperFilters = new HelperFiltersMainController(logger);

	}

	@FXML
	private void runCorrectionOn() {

		if (mapWorkbooks != null) {

			helper.updateProgressBarGroup(ProgressIndicator.INDETERMINATE_PROGRESS, 
					resources.getString("text.testing.solution"),
					resources.getString("text.after.finalized") );

			SimpleGroup selecteds = helper.createlistIdsSelected();

			if (selecteds.mapWorkbook.size() > 0) {

				pauseB.setDisable(false);
				stopB.setDisable(false);

				selecteds.mapWorkbook.values().forEach((value) -> {
					
					System.out.println(value.toString());
					
					runSelecteds(value);
				});

			} else {

				helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

				logger.getErrors().add(
						resources.getString("error.selected.nothing")).show();

				return;

			}

		} else {

			logger.getErrors().add(
					resources.getString("error.not.opened.workbook")).show();

			helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

		}

	}

	private void runSelecteds(Workbook selectedWorkbook) {
		
		this.service = new SubmissionThread(progressPB, selectedWorkbook,
				resultsTTV, logger, this);

		this.service.setOnSucceeded((success) -> {

			progressPB.progressProperty().unbind();
			//leftL.textProperty().unbind();

			/* In the future replace the flow: {"update* -> table -> filtered -> table"} to {"update* -> filtered -> table"} */

			resultsTTV.getRoot().valueProperty().unbind();
			filteredData = new FilteredList<TreeItem<Group<?>>>(resultsTTV.getRoot().getChildren());
			
			// Set in table

			resultsTTV.getRoot().getChildren().setAll(filteredData);
			resultsTTV.refresh();

			//helper.updateChart();

			helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

			// Update info messages
			
			infosMain.updateAllWorkbookInfos(resultsTTV.getRoot());
			
		});
		
		this.service.start();
	}

	/* MENU */

	/* FILE */

	@FXML
	private void newWorkbookMIOn() {

		String fxml = path + "NewWorkbook.fxml";

		try {

			App.showScreen.setWait(true);
			App.showScreen.simpleScreen(fxml, 500, 622, "New Workbook", "newworkbook");
			
		} catch (Exception e) {

			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();

		}

		if (!MainController.mapWorkbooks.isEmpty())
			helper.openWorkbooks();

	}

	@FXML
	private void openWorkbookMIOn() {

		helper.updateProgressBarGroup(ProgressIndicator.INDETERMINATE_PROGRESS, 
						resources.getString("text.openning"), "...");

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle(resources.getString("text.open") + " Workbook");

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON file.", "*.json"));

		File fileSelected = fileChooser.showOpenDialog(App.localStage);

		if (fileSelected != null) {

			try {

				Workbook workbook = new JSONReader(
						new Reader(fileSelected.toString()).readLines().get(0)//All in one line
					).getWorkbook();
				
				workbook.setPriority(MainController.mapWorkbooks.size());
				
				MainController.mapWorkbooks.put(workbook.getName(), workbook);

				//MainController.pathTemporary = pathDefault + "/" +
				//		mapWorkbooks.getName() + "/";

				helper.openWorkbooks();

			} catch (Exception e) {
			
				logger.getExceptions().add(resources.getString("exception.could.not.read.file") + fileSelected.toString(), e).show();
				e.printStackTrace();
				
			}


		}

		helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

	}


	@FXML
	private void openRecentMOn() {
	}

	@FXML
	private void closeMIOn() {
		// close != exit, don't close, but leave "empty"

		if (mapWorkbooks == null) {
			
			logger.getErrors().add(resources.getString("error.not.opened.workbook")).show();
			
			return;
			
		}
	
		helper.booting();

		helper.updateProgressBarGroup(0.5F, 
				resources.getString("text.closing.workbook"), 
				resources.getString("text.after.clearing") );

		treeTV.getTreeItem(0).getChildren().clear();
		treeTV.setRoot(null);

		resultsTTV.setRoot(null);

		helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

	}

	@FXML
	private void saveAsMIOn() {

		helper.updateProgressBarGroup(ProgressIndicator.INDETERMINATE_PROGRESS, 
				resources.getString("text.saving"), 
						resources.getString("text.after.finalized") );

		// Select one Workbook or all
		
		List<String> options = new ArrayList<>();
		options.add(resources.getString("messages.option.all"));
		options.addAll(Arrays.asList((String[])mapWorkbooks.keySet().toArray()));
		
		String nameWorkbook =App.getAlerts()
									.boxChoice(resources.getString("messages.choice"), 
												resources.getString("messages.select"), 
												resources.getString("messages.what.workbook.want.save"), 
												options.get(0), 
												options )
									.get(); // Get the selected
		
		if (nameWorkbook == null) // If null was selected
			return;

		if (nameWorkbook.equals(options.get(0))) // If nameWorkbook == All
			mapWorkbooks.keySet().forEach((key) -> helper.saveWorkbook(key));
		else
			helper.saveWorkbook(nameWorkbook);

		progressPB.setProgress(1.00F);

		helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");

	}


	@FXML
	private void preferenciasMIOn() {

		String fxml = path + "Preferences.fxml";

		try {
			App.showScreen.simpleScreen(fxml, 600, 400, 
					resources.getString("text.preferences"), "preferences");
		} catch (Exception e) {
		
			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}
	}

	@FXML
	private void exitMIOn() {

		helper.updateProgressBarGroup(ProgressIndicator.INDETERMINATE_PROGRESS, 
				resources.getString("text.wainting.confirmation"), "...");

		String[] answers = new String[] {
				resources.getString("text.Yes"),
				resources.getString("text.No"),
				resources.getString("text.Cancel")
		};
		
		String result = App.getAlerts().messageWithButtons(
				resources.getString("messages.title"), 
				resources.getString("messages.before.closing"),
				resources.getString("messages.want.save.everything"), AlertType.CONFIRMATION,
				answers);

		if ( result.equals(answers[0]) ) {

			// If you have something to save before closing

			if (mapWorkbooks == null)
				logger.getErrors().add("error.not.opened.workbook").show();
			
			helper.updateProgressBarGroup(1.00F, resources.getString("text.finished"), "Ok");
			App.mainStage.close();

		} else if ( result.equals(answers[1]) ) {

			helper.updateProgressBarGroup(1.00F, resources.getString("text.finished"), "Ok");
			App.mainStage.close();

		} else if ( result.equals(answers[2]) ) {

			helper.updateProgressBarGroup(0.00F, resources.getString("text.finished"), "Ok");
			// Do nothing

		}

	}

	/* WORKBOOK */

	@FXML
	private void workbookPropertiesOnMI() {

		String fxml = path + "WorkbookProperties.fxml";

		try {

			App.showScreen.simpleScreen(fxml, 554, 350, "Workbook properties", "workbookproperties");

		} catch (Exception e) {
			
			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();

		}

	}

	/* HELP */

	@FXML
	private void aboutMIOn() {

		String fxml = path + "About.fxml";

		try {
		
			App.showScreen.simpleScreen(fxml, 600, 400, 
					resources.getString("text.about.algderno"), "about");

		} catch (Exception e) {
		
			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}

	}

	@FXML
	private void newQuestionOn() {

		if (mapWorkbooks == null) {

			logger.getErrors().add(resources.getString("error.not.opened.workbook")).show();
			return;

		}

		//helper.questionTA = new Question(name, 0, false);

		String fxml = path + "Files.fxml";

		try {

			App.showScreen.simpleScreen(fxml, 600, 400, resources.getString("text.new.question"), "newquestion");

			FilesController controller = App.showScreen.getLoader().getController();
			
			controller.openQuestion();
			
		} catch (Exception e) {

			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}

		App.showScreen.maximize();

	}

	@FXML
	private void deleteQuestionOn() {

		if (mapWorkbooks == null) {

			logger.getErrors().add(resources.getString("error.not.opened.workbook")).show();
			
			return;

		}
		
		// Initializes and prepares

		SimpleGroup selecteds = helper.createlistIdsSelected();

		StringBuilder list = new StringBuilder();

		List<String> selectedsString = selecteds.toArrayString();
		selectedsString.forEach( selected -> list.append(selected).append(")\n") );

		// Create question App.alerts

		String[] answers = new String[] {
				resources.getString("text.yes"),
				resources.getString("text.no")
		};
		
		String out = App.getAlerts().messageWithButtons(
				resources.getString("messages.confirmation"), 
				resources.getString("messages.want.delete"), 
				list.toString(),
				AlertType.CONFIRMATION, answers);

		boolean deleteFromDisc = App.getAlerts().confirmationWithCheckbox(
				resources.getString("messages.reply"), 
				resources.getString("messages.check.if"), 
				resources.getString("messages.confirmation.checkbox"), 
				resources.getString("messages.checkbox.delete") );

		// Make deletions depending on answer

		if ( out.equals(answers[0]) )
			selectedsString.forEach(s -> {

				String[] split = s.split(";");
				
				String nameWorkbook = split[0],
						nameExercise = split[1],
						nameQuestion = split[2];

				helper.deleteFromDisc(deleteFromDisc, nameWorkbook, nameExercise, nameQuestion);

				helper.removeFromTreeView(nameWorkbook, nameExercise, nameQuestion);

			});

	}

	/* Swap languages */

	@FXML
	private void languageSwapCBOn() {

		if (languageCB.getSelectionModel().getSelectedItem() != null) {

			Languages selected = languageCB.getSelectionModel().getSelectedItem();

			App.localStage.close();
			App.mainStage.close();
			App.mainScreenStage.close();
			Platform.runLater( () -> {

				App app = new App();
				app.setLocale( new Locale(selected.toString()) );
				app.start( new Stage() ); 

			});

		}

	}

	/* Filter Button */
	
	@FXML
	private void openFiltersOn() {

		if (mapWorkbooks == null) {

			logger.getErrors().add(resources.getString("error.not.opened.workbook")).show();
			
			return;

		}

		String fxml = path + "FiltersMain.fxml";

		try {
		
			App.showScreen.simpleScreen(fxml, 300, 360, resources.getString("text.filters.main.table"), "filtersmain");

		} catch (Exception e) {
		
			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}
	}

	/* Chart Button */
	
	@FXML
	private void openChartOn() {

		if (mapWorkbooks == null) {

			logger.getErrors().add(resources.getString("error.not.opened.workbook")).show();
			
			return;

		}

		String fxml = path + "Chart.fxml";

		try {
		
			App.showScreen.simpleScreen(fxml, 900, 590, 
					resources.getString("text.screen.title.chart") /*+ " " + mapWorkbooks.getName()*/, "chart");

		} catch (Exception e) {
		
			logger.getExceptions().add(resources.getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}
	}

	/* Buttons progress */

	@FXML
	private void playOn() {

		playB.setDisable(true);

		this.service.resumeThread();

	}

	@FXML
	private void pauseOn() {

		pauseB.setDisable(true);
		playB.setDisable(false);

		this.service.suspendThread();
	}

	@FXML
	private void stopOn() {

		stopB.setDisable(true);
		pauseB.setDisable(true);
		playB.setDisable(true);

		System.out.println("stopped");
		this.service.stopThread();
	}

	/* Close */

	public void closeMain() {

		System.out.println("Closing");

		if (this.service != null)// stop thread
			System.out.println("Stop = " + this.service.stopAll());

		App.localStage.close();
		App.mainStage.close();

	}

}
