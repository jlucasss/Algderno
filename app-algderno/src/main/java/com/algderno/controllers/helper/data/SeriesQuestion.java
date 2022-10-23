
package com.algderno.controllers.helper.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.algderno.controllers.ChartController;
import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.models.util.DataPopup;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;

public class SeriesQuestion {

	private Series<String, Number> series;

	private List<DataPopup> datasPopup;

	private ResourceBundle resources;

	public SeriesQuestion(ResourceBundle resources) {

		this.resources = resources;
		
		setSeries(new Series<>());

		setDatasPopup(new ArrayList<>());

		ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
		setData(data);

	}

	public SeriesQuestion(String name, Map<String, Exercise> mapExercises, ResourceBundle resources) {

		this.resources = resources;
		
		setSeries(new Series<>());

		this.series.setName(name);

		convertListToDataAndAdd(mapExercises);

	}

	public void convertListToDataAndAdd(Map<String, Exercise> mapExercises) {

		setDatasPopup(new ArrayList<>());

		ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
		setData(data);

		// Extract data of mapExercises
		
		for (Exercise exercise : mapExercises.values())
			for (Question question : exercise.getMapData().values())
				addCurrentData(exercise, question);
		
	}

	private int addCurrentData(Exercise exercise, Question question) {

		DataPopup dataPopupNew = new DataPopup(
			question.getName(), 
			question.getLastRuntime(), 
			exercise.getName(),
			question.isResultCorrect(),
			resources
		);
		
		dataPopupNew.questionNameProperty().bind(question.nameProperty());
		dataPopupNew.exerciseNameProperty().bind(exercise.nameProperty());
		dataPopupNew.lastRuntimeProperty().bind(question.lastRuntimeProperty());
		dataPopupNew.correctProperty().bind(question.resultCorrectProperty());
		
		this.datasPopup.add(dataPopupNew);
		
		Data<String, Number> dataNew = new XYChart.Data<String, Number>(
			ChartController.textExercise + " " + dataPopupNew.getExerciseName() 
					+ "\n" + ChartController.textQuestion + " " + dataPopupNew.getQuestionName(), 
			question.getLastRuntime()
		);
	
		dataNew.YValueProperty().bind(question.lastRuntimeProperty());
		
		getData().add(dataNew);

		return getData().size()-1; // return index

	}

	/* Tooltip */

	private void installTooltip(XYChart.Data<String, Number> data, DataPopup dataPopup) throws Exception {

		if (data.getNode() == null)
			throw new NullPointerException("Node of XYChart.Data is null. Add in one chart first!");

		Tooltip tooltip = new Tooltip(dataPopup.toString());
		Tooltip.install(data.getNode(), tooltip);

		// When open chart
		data.nodeProperty().get().setStyle( 
				dataPopup.isCorrect() ?
						"-fx-background-color: DARKGREEN;" : 
							"-fx-background-color: DARKRED;");
		
		
		// When update questions
		dataPopup.correctProperty().addListener(
				(ObservableValue<? extends Boolean> obs, Boolean bOld, Boolean bNew) ->
					data.nodeProperty().get().setStyle( 
						bNew ? "-fx-background-color: DARKGREEN;" : "-fx-background-color: DARKRED;")
		);
		
	}

	private void installTooltip(int index) throws Exception {

		installTooltip(getData().get(index), getDatasPopup().get(index));

	}

	public void installTooltipAll() throws Exception {

		for (int i = 0; getData().size() > i; i++)
			installTooltip(i);

	}

	public void clear() {
		getData().clear();
		getDatasPopup().clear();
	}

	/* getters and setters */

	public List<DataPopup> getDatasPopup() {
		return this.datasPopup;
	}

	public void setDatasPopup(List<DataPopup> datasPopup) {
		this.datasPopup = datasPopup;
	}

	public void setSeries(Series<String, Number> series) {
		this.series = series;
	}

	public Series<String, Number> getSeries() {
		return this.series;
	}

	public void setData(ObservableList<Data<String, Number>> data) {
		this.series.setData(data);
	}

	public ObservableList<Data<String, Number>> getData() {
		return this.series.getData();
	}

}

