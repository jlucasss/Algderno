package com.algderno.controllers.helper.data.info;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.algderno.controllers.InformationsWorkbooksController;
import com.algderno.controllers.MainController;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Workbook;
import com.algderno.util.ShowScreen;
import com.algderno.util.logger.SimpleLogger;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InfosMain {

	List<Map<String, SimpleStringProperty>> listGenerals;
	List<Map<String, SimpleDoubleProperty>> listCharts;
	private InformationsWorkbooksController controller;
	private SimpleLogger logger;
	
	public InfosMain(AnchorPane anchorPane, SimpleLogger logger) throws Exception {

		this.logger = logger;
		
		// Create screen chart
		
		ShowScreen screen = new ShowScreen(new Stage());
		
		Parent parent = screen.findFXML(MainController.path + "InformationsWorkbooks.fxml", "informationsworkbooks");
		AnchorPane.setTopAnchor(parent, 0.0);
		AnchorPane.setBottomAnchor(parent, 0.0);
		AnchorPane.setLeftAnchor(parent, 0.0);
		AnchorPane.setRightAnchor(parent, 0.0);
		
		anchorPane.getChildren().add(parent);	
		
		this.controller = screen.getLoader().getController();

		createListInfos();
		
	}

	private void createListInfos() {
		// Create variables with info
		
		this.listGenerals = new ArrayList<>();
		this.listCharts = new ArrayList<>();
		
		for (int i = 0; 3 > i; i++) { // Is 3 because is to Workbook, Exercise and Question
			listGenerals.add(
					Map.<String, SimpleStringProperty>of(
									this.controller.nameCountAverage, new SimpleStringProperty("0"), 
									this.controller.nameCountChecked, new SimpleStringProperty("0"),
									this.controller.nameCountTotal, new SimpleStringProperty("0"))
					);
			listCharts.add(
					Map.<String, SimpleDoubleProperty>of(
							this.controller.nameCountCorrect, new SimpleDoubleProperty(0.0), 
							this.controller.nameCountIncorrect, new SimpleDoubleProperty(0.0) )
					);
		}
		
		this.controller.fillBindInformations(
				this.listGenerals.get(0), this.listCharts.get(0), 
				this.listGenerals.get(1), this.listCharts.get(1),
				this.listGenerals.get(2), this.listCharts.get(2)
			);
		
	}

	/*
	 * No has updateQuestions because updateExercise exists
	 * */

	private void clearInfos() {
		listGenerals.forEach(
				(m) -> 
						m.values().forEach(
							(info) -> info.set("0")
						)
				);
		
		listCharts.forEach(
				(m) ->
					m.values().forEach(
						(pie) -> pie.set(0.0)
					)
				);
	}
	
	public void updateGroupWorkbooks(TreeItem<String> treeItem) {
		
		clearInfos();
		
		logger.getInfos().add("Workbooks size = " + MainController.mapWorkbooks.getMapData().size());
		
		for (Workbook w : MainController.mapWorkbooks.getMapData().values())
			logger.getInfos().add("Before = Workbook " + w.getName() + "; Is = " + w.isResultCorrect());
		
		for (TreeItem<String> child : treeItem.getChildren())
			updateWorkbook(child, MainController.mapWorkbooks.getMapData().get(child.getValue()));

		// Ever after for each Workbook
		updateInformationOf(0, treeItem, MainController.mapWorkbooks); // Workbook
		
		for (Workbook w : MainController.mapWorkbooks.getMapData().values())
			logger.getInfos().add("After = Workbook " + w.getName() + "; Is = " + w.isResultCorrect());
		logger.getInfos().add("\n\n");
		
	}
	
	private void updateWorkbook(TreeItem<String> treeItem, Workbook workbook) {

		for (TreeItem<String> child : treeItem.getChildren())
			updateExercise(child, workbook.getMapData().get(child.getValue()));

		// After for each Exercise
		updateInformationOf(1, treeItem, workbook);
		
	}
	
	private void updateExercise(TreeItem<String> treeItem, Exercise exercise) {
		
		updateInformationOf(2, treeItem, exercise);
		
	}
	
	/*
	 * Extract new informations by TreeItem and Group
	 * */
	private void updateInformationOf(int type, TreeItem<String> treeItem, 
									Group<?> group) {

		int countTotal = Integer.parseInt( listGenerals.get(type).get(this.controller.nameCountTotal).get() ), 
				countCorrect = (int) listCharts.get(type).get(this.controller.nameCountCorrect).get(), 
				countIncorrect = (int) listCharts.get(type).get(this.controller.nameCountIncorrect).get(), 
				checked = Integer.parseInt( listGenerals.get(type).get(this.controller.nameCountChecked).get() );
		
		BigDecimal averageRuntime = new BigDecimal(listGenerals.get(type).get(this.controller.nameCountAverage).get());
		
		for (TreeItem<String> subItem : treeItem.getChildren()) {
			
			countTotal++;
			
			if (group.isResultCorrect())
				countCorrect++;
			
			if ( ((CheckBoxTreeItem<String>)subItem).isSelected() )
				checked++;
			
			averageRuntime = averageRuntime.add(new BigDecimal(group.getLastRuntime()));
			
		}
		
		if (countTotal != 0)
			averageRuntime = averageRuntime.divide(new BigDecimal(countTotal), RoundingMode.HALF_UP);
		
		countIncorrect += (countTotal - countCorrect);
		
		// Update global variables
		
		listGenerals.get(type).get(this.controller.nameCountAverage).set(averageRuntime.toString());
		listGenerals.get(type).get(this.controller.nameCountChecked).set(checked + "");
		listGenerals.get(type).get(this.controller.nameCountTotal).set(countTotal + "");
		
		listCharts.get(type).get(this.controller.nameCountCorrect).set(countCorrect);
		listCharts.get(type).get(this.controller.nameCountIncorrect).set(countIncorrect);
	}
	
}
