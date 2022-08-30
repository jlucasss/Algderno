
package com.algderno.controllers.helper.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.models.util.DataPopup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;

public class SeriesQuestion {

	private Series<String, Number> series;

	private List<DataPopup> datasPopup;

	public SeriesQuestion() {

		setSeries(new Series<>());

		setDatasPopup(new ArrayList<>());

		ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
		setData(data);

	}

	public SeriesQuestion(String name, Map<String, Exercise> mapExercises) {

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
				addCurrentData(exercise.getName(), question);
		
	}

	public int addCurrentData(String exerciseName, Question question) {

		this.datasPopup.add(
				new DataPopup(
					question.getName(), 
					question.getLastRuntime(), 
					exerciseName
				)
		);

		getData().add(
			new XYChart.Data<String, Number>(
				"Exercise: " + exerciseName + "\nQuestion: " + question.getName(), 
				question.getLastRuntime()
			)
		);

		return getData().size()-1; // return index

	}

	/* Tooltip */

	public void installTooltip(XYChart.Data<String, Number> data, DataPopup dataPopup) throws Exception {

		if (data.getNode() == null)
			throw new NullPointerException("Node of XYChart.Data is null. Add in one chart first!");

		Tooltip tooltip = new Tooltip(dataPopup.toString());
		Tooltip.install(data.getNode(), tooltip);

	}

	public void installTooltip(int index) throws Exception {

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

