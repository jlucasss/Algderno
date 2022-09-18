package com.algderno.controllers.helper;

/**
 *
 * This class has the job of helping MainController to separate FXML methods and private helpers, thus organizing the controller.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;

import com.algderno.App;
import com.algderno.controllers.ChartController;
import com.algderno.controllers.FiltersMainController;
import com.algderno.controllers.MainController;
import com.algderno.controllers.helper.data.info.InfosMain;
import com.algderno.io.writer.JSONWriter;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;
import com.algderno.util.SetCheckBoxTreeView;
import com.algderno.util.ShowScreen;
import com.algderno.util.logger.SimpleLogger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class HelperMainController {

	private MainController main;
	private SimpleLogger logger;

	public HelperMainController(MainController main, SimpleLogger logger) {
		this.main = main;
		this.logger = logger;
	}

	@SuppressWarnings("static-access")
	public void booting() {

		updateProgressBarGroup(0.00F, 
				main.getResources().getString("text.no.pending.processing"), "Ok");

		if (MainController.mapWorkbooks != null)
			MainController.mapWorkbooks.getMapData().clear();
		else
			MainController.mapWorkbooks = new Group<Workbook>(0, "Workbooks") {};

		main.filteredData = null;

		selectedWorkbook = null;

		//MainController.pathTemporary = null;

		punctuatedTreeView = null;

		itemBranchLevel1 = null;
	
		try {
		
			main.infosMain = new InfosMain(main.infosAP, this.logger);
		
		} catch (Exception e) {
			logger.getExceptions().add(main.getResources().getString("exception.could.not.open.screen"), e);
			e.printStackTrace();
		}
		
		if (!main.chartAP.getChildren().isEmpty())
			main.chartAP.getChildren().clear();

	}

	/* Listeners */

	public void addListenerTreeView() {

		/*
		* Clicking on a TreeView test triggers the itemSelectedTreeView method
		* to open the item in TextAreas
		*
		*/

		main.treeTV.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

			if (main.treeTV.getSelectionModel().getSelectedItem() != null) {

				TreeItem<String> child = main.treeTV.getSelectionModel().getSelectedItem();

				if ( !(child.getValue().getClass()
								.equals(Question.class)) ) // If selected != Question
					return;
				
				String nameExercise = child.getParent().getValue(),
						nameWorkbook = child.getParent().getParent().getValue();
				Question question = (Question) MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData()
																.get(nameExercise).getMapData()
																.get(child.getValue());
				
				if (itemSelectedTreeView())
					openFilesTextArea(nameWorkbook, nameExercise, question);
				
				
				/*if (child.getChildren().size() == 0) {//If the child has no children (that is, it is not a group)

					TreeItem<Map<String, GroupType>> parent = child.getParent();

					if (itemSelectedTreeView())
						openFilesTextArea(parent.getValue(), child.getValue());

				}*/

			}

		});

		addListenerOfSelectionTreeTableView();

	}

	/*
	*
	* When selecting an item in TreeTableView, also select, and scroll, in ListView
	*
	*/

	private Question questionCurrent;
	public void addListenerOfSelectionTreeTableView() {

		main.resultsTTV.getSelectionModel().selectedItemProperty()
			.addListener((obs, oldV, newV) -> {

				//questionCurrent = main.resultsTTV.getSelectionModel().getSelectedItem();
	//			TreeItem< Group<?> > selected = main.resultsTTV.getSelectionModel().getSelectedItem();

//				System.out.println("Type="+selected.getValue().getClass().getName());
				
				if (! ( newV.getValue().getClass().equals(Question.class.getClass()) ) ) // If selected != Question
					return;
				
				// Find exercise and workbook of question
				/*String exercise = selected.getParent().getValue().getName(),
						workbook = selected.getParent().getParent().getValue().getName();
				
				questionCurrent = MainController.mapWorkbooks.get(workbook).getMapData()
									.get(exercise).getMapData()
									.get(selected.getValue().getName());
				*/
				
				questionCurrent = (Question) newV.getValue();
				
				if(questionCurrent != null)
					selectTreeViewMatchTreeTableView(newV);

		});

	}

	//for future, if necessary, refactoring
	private void selectTreeViewMatchTreeTableView(TreeItem<Group<?>> itemSelected) {

		String nameExercise = itemSelected.getParent().getValue().getName(),
				nameWorkbook = itemSelected.getParent().getParent().getValue().getName();
		
		TreeItem<String> itemRoot = new TreeItem<>(main.treeTV.getRoot().getValue()),
				itemWorkbook = new TreeItem<>(nameWorkbook),
				itemExercise = new TreeItem<>(nameExercise),
				itemQuestion = new TreeItem<>(itemSelected.getValue().getName());
		
		itemExercise.getChildren().add(itemQuestion);
		itemWorkbook.getChildren().add(itemExercise);
		itemRoot.getChildren().add(itemWorkbook);
		
		main.treeTV.getSelectionModel().select(itemQuestion);
		
		/*
		FIRST:
		for (TreeItem<String> parents : treeTV.getTreeItem(0)// Enter the first item which is always the one that houses everything
			.getChildren()) {

			if ( parents.getValue().equals(questionCurrent.getParent().getValue().getName()) ) {

				SECOND:
				for (TreeItem<String> childs : parents.getChildren()) {

					if ( childs.getValue().equals(questionCurrent.getValue().getName()) ) {

						treeTV.getSelectionModel().clearSelection();
						treeTV.getSelectionModel().select(childs);
						int index = treeTV.getTreeItem(0).getChildren().indexOf(parents);
						treeTV.scrollTo(index);//rolls treeview to where the parent of the selection

						break FIRST;

					}

					if ( childs.getValue().equals(questionCurrent.getValue().getName()) )
						break SECOND;
				}
			}
		}*/

	}



	/* TOOLBAR */

	public Workbook selectedWorkbook = null;

	public void openFilesTextArea(String nameWorkbook, String nameExercise, Question question) {

		selectedWorkbook = new Workbook(-1, nameWorkbook, null, null, null, null);
		selectedWorkbook.getMapData().put(nameExercise, new Exercise(-1, nameExercise));
		selectedWorkbook.getMapData().get(nameExercise).addNewQuestion(question);
		
		String fxml = MainController.path + "Files.fxml";

		try {

			App.showScreen.simpleScreen(fxml, 600, 400, (
					main.getResources().getString("text.question.files") + question.getName()), "files");

			//main.treeTV.getSelectionModel().clearSelection();

			//main.resultsTV.getSelectionModel().clearSelection(); // This method is throwing exception (JavaFX Bug?)

			updateProgressBarGroup(0, 
						main.getResources().getString("text.open") +
							" Question \"" + question.getName() + "\", Exercise \"" + nameExercise + "\".",
						main.getResources().getString("text.finished"));

		} catch (Exception e) {
		
			logger.getExceptions().add(
					main.getResources().getString("exception.could.not.open.file") + fxml, e).show();
			e.printStackTrace();
		
		}

		App.showScreen.maximize();

	}

	public boolean itemSelectedTreeView() {

		updateProgressBarGroup(ProgressIndicator.INDETERMINATE_PROGRESS, 
				main.getResources().getString("text.waiting.confirmation"), "...");

		if (MainController.mapWorkbooks == null) {

			logger.getErrors().add(
					main.getResources().getString("error.not.opened.workbook")).show();

			return false;

		}

		String[] nameButtons = new String[] {
			main.getResources().getString("text.ok"),
			main.getResources().getString("text.cancel")
		};

		String result = App.getAlerts().messageWithButtons(
				main.getResources().getString("messages.reply"), 
				main.getResources().getString("messages.check.if"), 
				main.getResources().getString("messages.want.open.selected.item"),
				AlertType.CONFIRMATION, nameButtons);

		updateProgressBarGroup(0.00F, main.getResources().getString("text.finished"), "Ok");

		return result.equals("Ok");

	}


	/* OPEN NEW QUESTION */

	private SetCheckBoxTreeView<String> punctuatedTreeView;

	public void openWorkbook(Workbook workbookNew) {

		try {

			// Add new workbook
			MainController.mapWorkbooks.getMapData().put(workbookNew.getName(), workbookNew);
			
			// Verify result correct of all
			
			boolean isAllWorkbooks = true;
			
			for (Workbook w : MainController.mapWorkbooks.getMapData().values())
				isAllWorkbooks = isAllWorkbooks && w.isResultCorrect();
			
			MainController.mapWorkbooks.setResultCorrect(isAllWorkbooks);
			
			// Update data
			
			fillTreeView(workbookNew);
			
			main.treeTP.setExpanded(true);
			
			fillTreeTableView(workbookNew);
			
			punctuatedTreeView = new SetCheckBoxTreeView<>();

			punctuatedTreeView.listenerStateCheckBox(itemBranchLevel1);
			punctuatedTreeView.listenerValueChange(itemBranchLevel1);

			// Opens screen

			openChart(workbookNew);
			openFiltersMain(workbookNew);

			MainController.infosMain.updateGroupWorkbooks(main.treeTV.getRoot());
			
			// Update progress

			main.progressPB.setProgress(1.00F);

			Thread.sleep(500);

		} catch (Exception e) {
		
			logger.getExceptions().add(
					main.getResources().getString("exception.trying.workbookmodel"), e).show();
			e.printStackTrace();
		
		}

		updateProgressBarGroup(0.00F, main.getResources().getString("text.finished"), "Ok");

	}

	private ChartController chartController;
	private Object filtersMainController;

	private void openChart(Workbook workbookNew) {

		if (this.chartController == null) {
			
			try {
				ShowScreen screen = new ShowScreen(new Stage());
	
				Parent parent = null;
	
				parent = screen.findFXML("Chart.fxml", "chart");
	
				this.chartController = (ChartController) screen.getLoader().getController();
	
				AnchorPane.setBottomAnchor(parent, 0.0);
				AnchorPane.setTopAnchor(parent, 0.0);
				AnchorPane.setLeftAnchor(parent, 0.0);
				AnchorPane.setRightAnchor(parent, 0.0);
	
				// Add fxml of chart in main
				main.chartAP.getChildren().add(parent);
				
			} catch (Exception e) {
	
				logger.getExceptions().add(
						main.getResources().getString("exception.could.not.open.file") + " " + "Chart.fxml", e);
				e.printStackTrace();
	
			}
			
		}
		
		this.chartController.initChartWithWorkbook(workbookNew);
		
	}

	//private FiltersMainController filtersMainController;

	private void openFiltersMain(Workbook workbookNew) {

		if (this.filtersMainController == null) {
			try {
				ShowScreen screen = new ShowScreen(new Stage());
	
				Parent parent = null;
	
				parent = screen.findFXML("FiltersMain.fxml", "filtersmain");
	
				this.filtersMainController = (FiltersMainController) screen.getLoader().getController();
	
				AnchorPane.setBottomAnchor(parent, 0.0);
				AnchorPane.setTopAnchor(parent, 0.0);
				AnchorPane.setLeftAnchor(parent, 0.0);
				AnchorPane.setRightAnchor(parent, 0.0);
	
				// Add fxml of chart in main
				main.filtersAP.getChildren().add(parent);
	
			} catch (Exception e) {
	
				logger.getExceptions().add(
						main.getResources().getString("exception.could.not.open.file") + " " + "FiltersMain.fxml", e);
				e.printStackTrace();
	
			}
		}
	}

	public void updateChart() {

		System.out.println("Updating chartController");
		//chartController.updateChart();
	}

	/*public void updateTreeView(Workbook workbookNew) {

		fillTreeView(workbookNew);

	}*/

	private void fillTreeView(Workbook workbookNew) {

		TreeItem<String> root = main.treeTV.getRoot();
		
		if (root == null || root.getChildren().size() == 0) {
		
			itemBranchLevel1 = new CheckBoxTreeItem<>("Workbooks");
			itemBranchLevel1.setExpanded(true);
	
			itemBranchLevel1.getChildren().add(createCheckBoxTreeItemExerciseAndQuestion(workbookNew));
	
			main.treeTV.setRoot(itemBranchLevel1);
			
		} else {

			itemBranchLevel1.getChildren().add(createCheckBoxTreeItemExerciseAndQuestion(workbookNew));
			
		}

		main.treeTV.refresh();

	}

	/* Create tree-items with check-box of Exercise and Question names */
	private CheckBoxTreeItem<String> createCheckBoxTreeItemExerciseAndQuestion(Workbook workbook) {

		CheckBoxTreeItem<String> itemWorkbook = new CheckBoxTreeItem<>(workbook.getName());
		CheckBoxTreeItem<String> itemExercise = null;

		for (Exercise exercise : workbook.getMapData().values()) { // For each Exercise
			
			itemExercise = new CheckBoxTreeItem<>(exercise.getName());
			
			for (Question question : exercise.getMapData().values()) // For each Question
				itemExercise.getChildren().add( new CheckBoxTreeItem<>(question.getName()) );
			
			itemWorkbook.getChildren().add(itemExercise);
			
		}
		
		return itemWorkbook;
		
	}

	private void fillTreeTableView(Workbook workbookNew) {

		// Set in table

		if (main.resultsTTV.getRoot() == null)
			main.resultsTTV.setRoot(new TreeItem<Group<?>>(MainController.mapWorkbooks));
		
		// Fill list

		TreeItem<Group<?>> treeWorkbook = new TreeItem<>(workbookNew), treeExercise;

		for (Exercise exercise : workbookNew.getMapData().values()) { // For each Exercise
		
			treeExercise = new TreeItem<>(exercise);
			
			for (Question question : exercise.getMapData().values()) // For each Question
				treeExercise.getChildren().add(new TreeItem<Group<?>>(question)); // Add Question
			
			treeWorkbook.getChildren().add(treeExercise); // Add Exercise
		}
		
		// Fill filter

		if (MainController.filteredData == null) {
			
			ObservableList<TreeItem<Group<?>>> listQuestions = FXCollections.observableArrayList();
			listQuestions.add(treeWorkbook); // Add Workbook

			MainController.filteredData = new FilteredList<TreeItem<Group<?>>>(listQuestions);
			
			main.resultsTTV.getRoot().getChildren().setAll(MainController.filteredData);
			
		} else 
			main.resultsTTV.getRoot().getChildren().add(treeWorkbook);
			//MainController.filteredData.add(treeWorkbook);

		main.resultsTTV.refresh();

		addListenerSearch();

	}

	public void updateFilteredData() {
		
		MainController.filteredData.setPredicate(
			MainController.helperFilters.createPredicate()
		);

	}

	private void addListenerSearch() {

		main.searchTF.textProperty().addListener((observable, oldValue, newValue) -> 

			MainController.filteredData.setPredicate(
				MainController.helperFilters.createPredicate(newValue)
			)

		);

	}

	/*public <T extends Comparable<T>> int searchLinear(List<T> list, T t) {

		int output = -1;

		for (int i = 0; i < list.size(); i++)
			if (list.get(i).compareTo(t) == 0)
				return i;

		return output;

	}

	public <T extends Comparable<T>> int searchLinear(HashMap<Integer, T> list, T t) {

		int output = -1;

		for (int i = 0; i < list.size(); i++)
			if (list.get(i).compareTo(t) == 0)
				return i;

		return output;

	}*/
	
	/* Save Workbook */
	

	public void saveWorkbook(String nameWorkbook) {

		System.out.println("Saving = " + nameWorkbook);
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(
				main.getResources().getString("text.saveas") );

		File diretorySelected = directoryChooser.showDialog(App.localStage);

		System.out.println("File directory = " + directoryChooser);
		
		if (diretorySelected != null)
			try {
				
				new JSONWriter(MainController.mapWorkbooks.getMapData().get(nameWorkbook)).save(diretorySelected.toString());
			
			} catch (Exception e) {
				
				logger.getExceptions().add(main.getResources().getString("exception.could.not.write.file") 
						+ diretorySelected.toString() + MainController.mapWorkbooks.getMapData().get(nameWorkbook).getName(), e).show();
				e.printStackTrace();
			
			}
		
	}
	
	/* execultarOn() */

	private CheckBoxTreeItem<String> itemBranchLevel1;
/*
	private ObservableList<TreeItem<Group<?>>> list, subList;

	private List<List<Integer>> idsSelected;

	public List<List<Integer>> getItemsSelected() {
		return idsSelected;
	}

	public void createlistIdsSelected(String nameWorkbook) {

		idsSelected = new ArrayList<>();

		list = itemBranchLevel1.getChildren();

		subList = null;

		CheckBoxTreeItem<Group<?>> subItem = null;

		List<Integer> listIds = null;

		for (int i = 0; list.size() > i; i++) {

			subList = list.get(i).getChildren();

			for (int j = 0; subList.size() > j; j++) {

				subItem = ((CheckBoxTreeItem<Group<?>>) subList.get(j));

				if (subItem.isSelected()) {

					if (listIds == null) {

						listIds = new ArrayList<>();
						listIds.add(i);// The first id is the Exercise

					}

					listIds.add(j);

				}

			}

			if (listIds != null) {

				idsSelected.add(listIds);
				listIds = null;

			}

		}

	}*/

	/* Create list of String as describe where to find Question selected */
/*	public List<TreeItem<String>> createlistIdsSelected() {
		
		SelectedsList selectedNames = new SelectedsList();
		
		for (TreeItem<String> itemWorkbook : itemBranchLevel1.getChildren()) // For each Workbook
			for (TreeItem<String> itemExercise : itemWorkbook.getChildren()) // For each Exercise
				for (TreeItem<String> itemQuestion : itemExercise.getChildren()) // For each Question
					if (((CheckBoxTreeItem<String>)itemQuestion).isSelected()) // If is selected
						selectedNames.add(itemWorkbook,
											itemExercise,
											itemQuestion);
				
				

		return selectedNames;
	}*/
	
	/* DELETE */

	public void deleteFromDisc(boolean delete, TreeItem<String> selected) {

		String nameWorkbook = selected.getParent().getValue(),
			nameExercise = selected.getParent().getValue(), 
			nameQuestion = selected.getValue();
		
		if (delete) {

			new File(MainController.mapWorkbooks.getMapData().get(nameWorkbook).getPathRoot() +
					MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData().get(nameExercise)
						.getMapData().get(nameQuestion).getMapData()
						.get("pathInput")).deleteOnExit();

			new File(MainController.mapWorkbooks.getMapData().get(nameWorkbook).getPathRoot() +
					MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData().get(nameExercise)
						.getMapData().get(nameQuestion).getMapData()
						.get("pathOutputCorrect")).deleteOnExit();

		}

	}

	public void removeFromTreeView(TreeItem<String> selectedQuestion) {

		TreeItem<String> itemExercise = selectedQuestion.getParent();
		TreeItem<String> itemWorkbook = itemExercise.getParent();

		String nameWorkbook = itemExercise.getValue(),
				nameExercise = itemExercise.getValue(), 
				nameQuestion = selectedQuestion.getValue();
			
		// Remove Question

		itemExercise.getChildren().remove(selectedQuestion);
		
		MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData().get(nameExercise)
		.getMapData().remove(nameQuestion);
		
		// Remove Exercise (if necessary)
		
		if (itemExercise.getChildren().isEmpty()) {
			
			itemWorkbook.getChildren().remove(itemExercise);

			MainController.mapWorkbooks.getMapData().get(nameWorkbook).getMapData().remove(nameExercise);

		}
		
		// Remove Workbook (if necessary)
		
		if (itemWorkbook.getChildren().isEmpty()) {
			
			main.treeTV.getRoot().getChildren().remove(itemWorkbook);
			
			MainController.mapWorkbooks.getMapData().remove(nameWorkbook);

		}

		main.treeTV.refresh();

	}

	/* OTHERS */

	public void updateProgressBarGroup(double percentage, String textLeft, String textRight) {

		main.progressPB.setProgress(percentage);
		main.leftL.setText(textLeft);
		main.rightL.setText(textRight);

	}

	/* Getters */

	public ChartController getChartController() {
		return this.chartController;
	}

}

