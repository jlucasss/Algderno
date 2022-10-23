
package com.algderno.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.algderno.App;
import com.algderno.controllers.helper.HelperChartController;
import com.algderno.controllers.helper.data.SeriesQuestion;
import com.algderno.io.reader.JSONReader;
import com.algderno.io.reader.Reader;
import com.algderno.models.Workbook;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ChartController extends AbstractController {

/* MENU */

	@FXML
	private LineChart<String, Number> lineChart;

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	private HelperChartController helper;

	private String nameChart;

	private HashMap<String, SeriesQuestion> mapSeries;
	
	public static String textExercise = "", textQuestion = "", textLastRuntime = "";
	
	@Override
	public void init() {

		helper = new HelperChartController(this, resources, logger); 
		
		nameChart = resources.getString("label.title");//MainController.mapWorkbooks.getName();
		lineChart.setTitle(nameChart);
		
		xAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);
		lineChart.autosize();
		
		textExercise = resources.getString("legend.text.exercise");
		textQuestion = resources.getString("legend.text.question");
		textLastRuntime = resources.getString("legend.text.last.runtime");
		
	}
	
	public void initChartWithWorkbook(Workbook workbook) {

		if (this.mapSeries == null)
			this.mapSeries = new HashMap<>();
		
		SeriesQuestion serieQuestion = new SeriesQuestion(workbook.getName(), workbook.getMapData(), this.resources);
		
		this.mapSeries.put(workbook.getName(), serieQuestion);

		lineChart.getData().add(serieQuestion.getSeries());

		try {
			serieQuestion.installTooltipAll();
		} catch (Exception e) {

			logger.getExceptions().add(resources.getString("exception.could.not.install.tooltip"), e).show();
			e.printStackTrace();

		}

	}

	private List<String> openedsWorkbooks = new ArrayList<>();

	@FXML
	private void comparingOn() {

		// Open other Workbook

		Workbook workbookModel = null;

		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle(resources.getString("text.open") + " Workbook");

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("JSON file.", "*.json"));

		File fileSelected = fileChooser.showOpenDialog(App.localStage);

		if (fileSelected == null) 
			return;
		
		// Verify if was openned
		if (openedsWorkbooks.contains(fileSelected.toString())) {

			logger.getErrors().add(
				resources.getString("error.workbook.already.opened") ).show();
			return;

		}

		openedsWorkbooks.add(fileSelected.toString());

		try {

			workbookModel = new JSONReader(
					new Reader(fileSelected.toString()).readLines().get(0)//All in one line
				).getWorkbook();

			// Add in chart

			SeriesQuestion openedSeries = new SeriesQuestion(workbookModel.getName(), 
									workbookModel.getMapData(), this.resources);

			lineChart.getData().add(openedSeries.getSeries());

			openedSeries.installTooltipAll();

		} catch (Exception e) {

			logger.getExceptions().add(resources.getString("exception.could.not.read.file") + fileSelected.toString(), e).show();
			e.printStackTrace();

		}

	}

	@FXML
	private void saveOn() {

		String[] namesOptions = new String[] {
			resources.getString("text.CSV"),
			resources.getString("text.PNG")
		};

		List<String> result = App.getAlerts().confirmationWithSeveralCheckbox(
					resources.getString("messages.reply"), 
					resources.getString("messages.do.you"), 
					resources.getString("messages.want.save"),
					namesOptions);

		String localFile = helper.selectFolder();

		if (localFile == null)
			return;

		if ( result.contains(namesOptions[0]) ) {
			
			String[] namesCharts = new String[mapSeries.size()];
			mapSeries.keySet().toArray(namesCharts);
		
			List<String> resultCSV = App.getAlerts().confirmationWithSeveralCheckbox(
						resources.getString("messages.reply"), 
						resources.getString("messages.do.you"), 
						resources.getString("messages.want.save"),
						namesCharts);
			
			helper.saveCSV(mapSeries.get(resultCSV.get(0)).getDatasPopup(), (localFile + "/" + nameChart + ".csv") );
		}
		
		if ( result.contains(namesOptions[1]) )
			helper.savePNG( lineChart, (localFile + "/" + nameChart + ".png") );

	}

	/* Actions */

	@FXML
	private void closeOn() {
		App.localStage.close();
	}

	/* About SeriesQuestion */
/*
	public void cleanDataMain() {
		this.series.clear();
	}

	public void addDataInMain(String exerciseName, Question question) {
		this.series.addCurrentData(exerciseName, question);
	}*/

	public void installAllTooltips(SeriesQuestion series) {

		try {
			series.installTooltipAll();
		} catch (Exception e) {

			logger.getExceptions().add(/*resources.getString("exception.could.not.install.tooltip")*/"", e).show();
			e.printStackTrace();

		}

	}

	/* Getters and Setters */

	public LineChart<String, Number> getLineChart() {
		return this.lineChart;
	}
/*
	public SeriesQuestion getSeries() {
		return this.series;
	}*/

}

