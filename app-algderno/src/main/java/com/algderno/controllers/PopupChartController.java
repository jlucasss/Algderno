
package com.algderno.controllers;

import com.algderno.models.util.DataPopup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupChartController extends AbstractController {

/* MENU */

	@FXML
	private Label titleL, xValueL, yValueL, questionValueL;

	@Override
	public void init() {
	}

	public void initValues(String title, DataPopup dataPopup) {

		titleL.setText(resources.getString("label.title") + title);
		xValueL.setText(dataPopup.getQuestionName());
		yValueL.setText(dataPopup.getLastRuntime() + "");
		questionValueL.setText(dataPopup.getExerciseName());

	}

}
