package com.algderno.controllers.helper;

/**
 *
 * This class has the job of helping ChartController to separate FXML methods and private helpers, thus organizing the controller.
 *
 * Used this solution to save as PNG: https://stackoverflow.com/questions/28221139/how-to-save-a-java-fx-chart-without-using-swing-api 
 * 
 * 
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.controllers.ChartController;
import com.algderno.controllers.PopupChartController;
import com.algderno.io.writer.Writer;
import com.algderno.models.util.DataPopup;
import com.algderno.util.PngEncoderFX;
import com.algderno.util.ShowScreen;
import com.algderno.util.SimpleAlerts;
import com.algderno.util.logger.SimpleLogger;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HelperChartController {

	@SuppressWarnings("unused")
	private ChartController controller;
	private SimpleAlerts ALERTS;
	private SimpleLogger logger;
	private ResourceBundle resources;

	private Popup popup;

	private PopupChartController popupController;

	private Stage stage = App.localStage;

	public HelperChartController(ChartController controller, ResourceBundle resources, SimpleLogger logger) {

		this.controller = controller;
		this.logger = logger;
		this.resources = resources;

		ALERTS = App.getAlerts();

		this.popup = new Popup();

		initPopup();

	}

	/* Popup */

	public void initPopup() {

		ShowScreen screen = new ShowScreen(stage);

		Parent parent = null;

		try {

			parent = screen.findFXML("PopupChart.fxml", "popupchart");

		} catch (Exception e) {

			logger.getExceptions().add(
					resources.getString("exception.file.not.found") + " " + "PopupChart.fxml", e);
			e.printStackTrace();

		}

		popup.getContent().add(parent);

		this.popupController = (PopupChartController) screen.getLoader().getController();

	}

	public void showPopup(DataPopup dataPopup, String nameChart, double positionX, double positionY) {

		popup.setX(positionX);
		popup.setY(positionY);

		popupController.initValues(nameChart, dataPopup);

		popup.show(stage);

	}

	public void hidePopup() {
		popup.hide();
	}

	/* Select folder */

	public String selectFolder() {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle( resources.getString("text.select.folder") );

		File diretorySelected = directoryChooser.showDialog(App.mainStage);

		if (diretorySelected != null)
			return (diretorySelected.getPath().replace("\\", "/") + "/");

		return null;

	}

	/* SAVE DATA */

	public void saveCSV(List<DataPopup> datasPopup, String localFile) {

		try {

			List<String> output = new ArrayList<>();

			// Fill output with CSV data
			for (int i = 0; datasPopup.size() > i; i++)
				output.add(datasPopup.get(i).toCSV());

			if ( !isContinueIfExist(Paths.get(localFile)) )
				return;

			new Writer(localFile).writeLines(output);

			logger.getInfos().add(
					resources.getString("text.saved") + " " + localFile).show();

		} catch (IOException e) {

			logger.getExceptions().add(
				resources.getString("exception.could.not.save.file" + " " + localFile), e).show();
			e.printStackTrace();

		}

	}

	@SuppressWarnings({ "unused", "rawtypes" })
	public void savePNG(LineChart chart, String localFile) {

		try {

			if (chart.getScene() == null) {
				Scene snapshotScene = new Scene(new Group(chart));
			}

			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);

			Image chartSnapshot = chart.snapshot(params, null);

			PngEncoderFX encoder = new PngEncoderFX(chartSnapshot, true);

			byte[] bytes = encoder.pngEncode();

			// Save file

			Path path = Paths.get(localFile);

			if ( !isContinueIfExist(path) )
				return;

			Files.write(Files.createFile(path), bytes);

			logger.getInfos().add(
					resources.getString("text.saved") + " " + localFile).show();

		} catch (IOException e) {

			logger.getExceptions().add(
				resources.getString("exception.could.not.save.file" + " " + localFile), e).show();
			e.printStackTrace();

		}

	}

	public boolean isContinueIfExist(Path path) throws IOException {

		if (Files.exists(path)) {

			String[] answers = new String[] {
					resources.getString("text.yes"),
					resources.getString("text.no")
			};
		
			String out = ALERTS.messageWithButtons(
					resources.getString("messages.reply"),
					resources.getString("messages.do.you"), 
					resources.getString("messages.want.replace") + " " + path, 
					AlertType.CONFIRMATION, answers);

			if ( out.equals(answers[0]) ) {

				Files.deleteIfExists(path);
				return true;

			} else 
				return false; // Not delete file

		}

		return true;

	}

}

