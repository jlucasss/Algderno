package com.algderno.controllers;

/**
 *
 * This controller class serves to 
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import com.algderno.App;
import com.algderno.controllers.helper.HelperFiltersMainController.EnumStatus;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class FiltersMainController extends AbstractController {

	@FXML
	private Spinner<Integer> minSpi, maxSpi;

	@FXML
	private ChoiceBox<EnumStatus> statusChoiceB;

	@SuppressWarnings("rawtypes")
	private SpinnerValueFactory minFactory, maxFactory;

	public static final int MIN = 1, MAX = 2000000000, // min = 0, max = 2.000.000.000 nanosec, or 2 sec 
				INITIAL_MIN_VALUE = 1, STEP_INCREMENT = 1,
				INITIAL_MAX_VALUE = MAX;


	@Override
	public void init() {

		initValues(
			MainController.helperFilters.getMin(), 
			MainController.helperFilters.getMax(), 
			INITIAL_MIN_VALUE, 
			INITIAL_MAX_VALUE, 
			STEP_INCREMENT, 
			MainController.helperFilters.getStatus()
		);

	}

	@SuppressWarnings("unchecked")
	private void initValues(int MIN, int MAX, int INITIAL_MIN_VALUE, int INITIAL_MAX_VALUE, int STEP_INCREMENT, EnumStatus status) {

		// MIN SPINNER

		minFactory = new SpinnerValueFactory
			.IntegerSpinnerValueFactory(MIN, MAX, INITIAL_MIN_VALUE, STEP_INCREMENT);

		minSpi.setValueFactory(minFactory);

		// MAX SPINNER

		maxFactory = new SpinnerValueFactory
			.IntegerSpinnerValueFactory(MIN, MAX, INITIAL_MAX_VALUE, STEP_INCREMENT);

		maxSpi.setValueFactory(maxFactory);

		// ChoiceBox

		statusChoiceB.getItems().clear();
		statusChoiceB.getItems().addAll(EnumStatus.values());
		statusChoiceB.getSelectionModel().select(status);

	}

	@FXML
	private void resetOn() {

		initValues(MIN, 
			MAX, 
			INITIAL_MIN_VALUE, 
			INITIAL_MAX_VALUE, 
			STEP_INCREMENT, 
			EnumStatus.NONE);

	}

	@FXML 
	private void applyOn() {

		if (minSpi.getValue() >= maxSpi.getValue()) {

			logger.getErrors().add(
					resources.getString("error.min.greater.equal.max") ).show();

			return;

		}

		MainController.helperFilters.setMin(minSpi.getValue());
		MainController.helperFilters.setMax(maxSpi.getValue());
		MainController.helperFilters.setStatus(statusChoiceB.getValue());

		MainController.helper.updateFilteredData();

		close();

	}

	@FXML 
	private void cancelOn() {

		close();

	}

	private void close() {

		App.localStage.close();

	}

}
