package com.algderno.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.algderno.models.util.InformationModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class InformationsWorkbooksController extends AbstractController {

	@FXML
	private AnchorPane mainAP;
	
	@FXML
	private TableView<InformationModel> workbookTV;
	@FXML
	private TableColumn<InformationModel, String> descriptionWorkbookTC;
	@FXML
	private TableColumn<InformationModel, String> valuesWorkbookTC;
	@FXML
	private PieChart workbookPC;

	@FXML
	private TableView<InformationModel> exerciseTV;
	@FXML
	private TableColumn<InformationModel, String> descriptionExerciseTC;
	@FXML
	private TableColumn<InformationModel, String> valuesExerciseTC;
	@FXML
	private PieChart exercisePC;

	@FXML
	private TableView<InformationModel> questionTV;
	@FXML
	private TableColumn<InformationModel, String> descriptionQuestionTC;
	@FXML
	private TableColumn<InformationModel, String> valuesQuestionTC;
	@FXML
	private PieChart questionPC;
	
	public String nameCountAverage = "", nameCountChecked = "", nameCountTotal = "",
			nameCountCorrect = "", nameCountIncorrect = "";
	private Map<String, String> mapNameTranslate;

	@Override
	protected void init() {

		nameCountAverage = "text.table.count.average";
		nameCountChecked = "text.table.count.checked";
		nameCountTotal = "text.table.count.total";
		nameCountCorrect = "text.table.count.correct";
		nameCountIncorrect = "text.table.count.incorrect";
		
		mapNameTranslate = new HashMap<>();
		mapNameTranslate.put(nameCountAverage,   resources.getString(nameCountAverage));
		mapNameTranslate.put(nameCountChecked,   resources.getString(nameCountChecked));
		mapNameTranslate.put(nameCountTotal,     resources.getString(nameCountTotal));
		mapNameTranslate.put(nameCountCorrect,   resources.getString(nameCountCorrect));
		mapNameTranslate.put(nameCountIncorrect, resources.getString(nameCountIncorrect));
		
		// Workbook
		
		descriptionWorkbookTC.setCellValueFactory(new PropertyValueFactory<>("description"));
		valuesWorkbookTC.setCellValueFactory(new PropertyValueFactory<>("value"));

		workbookPC.setLabelsVisible(false);
		
		contentTableAndPieChart(workbookTV, workbookPC);
		
		// Exercise
		
		descriptionExerciseTC.setCellValueFactory(new PropertyValueFactory<>("description"));
		valuesExerciseTC.setCellValueFactory(new PropertyValueFactory<>("value"));
		
		exercisePC.setLabelsVisible(false);

		contentTableAndPieChart(exerciseTV, exercisePC);
		
		// Question
		
		descriptionQuestionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
		valuesQuestionTC.setCellValueFactory(new PropertyValueFactory<>("value"));

		questionPC.setLabelsVisible(false);
		
		contentTableAndPieChart(questionTV, questionPC);
		
	}
	
	private void contentTableAndPieChart(TableView<InformationModel> table, PieChart pieChart) {

		table.getItems().addAll(
				Arrays.asList(
						new InformationModel(mapNameTranslate.get(nameCountAverage), "0"), 
						new InformationModel(mapNameTranslate.get(nameCountChecked), "0"),
						new InformationModel(mapNameTranslate.get(nameCountTotal), "0"))
				);
		
		pieChart.getData().addAll(
				Arrays.asList(new PieChart.Data(mapNameTranslate.get(nameCountCorrect), 0.0),
						new PieChart.Data(mapNameTranslate.get(nameCountIncorrect), 0.0))
				);
		
	}

	public void fillBindInformations(
			Map<String, SimpleStringProperty> mapWorkbookTV, Map<String, SimpleDoubleProperty> mapWorkbookPC,
			Map<String, SimpleStringProperty> mapExerciseTV, Map<String, SimpleDoubleProperty> mapExercisePC,
			Map<String, SimpleStringProperty> mapQuestionTV, Map<String, SimpleDoubleProperty> mapQuestionPC
			) {
		
		bindValues(mapWorkbookTV, mapWorkbookPC, workbookTV, workbookPC);
		bindValues(mapExerciseTV, mapExercisePC, exerciseTV, exercisePC);
		bindValues(mapQuestionTV, mapQuestionPC, questionTV, questionPC);
		
	}
	
	private void bindValues(Map<String, SimpleStringProperty> mapTable, Map<String, SimpleDoubleProperty> mapChart, TableView<InformationModel> table, PieChart pieChart) {

		SimpleStringProperty totalProperty = null;
		
		// For table
		
		for (InformationModel model : table.getItems()) // Content table
			for (Entry<String, String> entry : mapNameTranslate.entrySet()) { // Map Translate
				
				if (model.getDescription().equals(entry.getValue()) && mapTable.containsKey(entry.getKey())) { // If is same name
					
					model.valueProperty().bindBidirectional( mapTable.get(entry.getKey()) ); // Bind
				
					// Update total for add percentage
					if (entry.getKey().equals(this.nameCountTotal))
						totalProperty = mapTable.get(entry.getKey());
					
				}
				
			}
		// For chart
		
		for (PieChart.Data data : pieChart.getData()) { // Content pieChart
			
			for (Entry<String, String> entry : mapNameTranslate.entrySet()) // Map Translate
				if (data.getName().equals(entry.getValue()) && mapChart.containsKey(entry.getKey())) // If is same name
					data.pieValueProperty().bindBidirectional( mapChart.get(entry.getKey()) ); // Bind
		
			addPercentDataChartTooltip(data, totalProperty);
		}

	}
	
	private void addPercentDataChartTooltip(PieChart.Data data, SimpleStringProperty totalProperty) {
		
		Tooltip tooltip = new Tooltip("0%");
		
		// Update Tooltip when totalProperty update
		totalProperty.addListener((c) ->
			Platform.runLater(() -> {
				double totalDouble = Double.parseDouble(totalProperty.get());
				
				double percentage = 100;
				
				if (totalDouble != 0.0)
					percentage *= data.getPieValue() / totalDouble;
				
				tooltip.setText(percentage + "%");
			})
		);
		
		Tooltip.install(data.getNode(), tooltip);
		
	}
	
}
