
package com.algderno.util;

/**
 *
 * This class summarizes the creation of new alerts.
 *
 * @author José Lucas dos Santos da Silva
 * 	(And some parts of external code ready).
 *
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.algderno.App;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SimpleAlerts {

	private Alert alert;

	public SimpleAlerts() {}

	private void baseAlert(String title, String subTitle,
					String message, AlertType type) {

		alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(subTitle);
		alert.setContentText(message);
		alert.getDialogPane().getStylesheets().add(
				App.class.getResource("style-base.css").toString()
				);

	}

	public void simpleMessage(String title, String subTitle,
			String message, AlertType type) {

		baseAlert(title, subTitle, message, type);

		alert.showAndWait();

	}

	public String messageWithButtons(String title, String subTitle,
			String message, AlertType type, String[] nameButtons) {

		baseAlert(title, subTitle, message, type);

		if (nameButtons != null) {

			ButtonType[] buttons = new ButtonType[nameButtons.length];

			for (int i = 0; nameButtons.length > i; i++)
				buttons[i] = new ButtonType(nameButtons[i]);

			alert.getButtonTypes().setAll(buttons);

		}

		return alert.showAndWait().get().getText();

	}

	public Optional<String> boxChoice(String title, String subTitle,
					String message, String choiceDefault, List<String> choices) {

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choiceDefault, choices);
		dialog.setTitle(title);
		dialog.setHeaderText(subTitle);
		dialog.setContentText(message);

		return (dialog.showAndWait());

	}

	public void errorWithException(String title, String subTitle,
			String message, Throwable e) {

		baseAlert(title, subTitle, message, AlertType.ERROR);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		Label cause = new Label(e.getMessage());
		cause.setWrapText(true);
		cause.setStyle("-fx-font-weight: bold;");

		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(cause, 0, 0);
		expContent.add(label, 0, 1);
		expContent.add(textArea, 0, 2);

		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

	}

	public String questionTextInput(String title, String subTitle,
			String message, String textDefault) {

		TextInputDialog dialog = new TextInputDialog(textDefault);
		dialog.setTitle(title);
		dialog.setHeaderText(subTitle);
		dialog.setContentText(message);

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent())
		    return result.get();

		return null;

	}

	public boolean confirmationWithCheckbox(String title, String subTitle,
			String message, String messageCheckbox) {

		baseAlert(title, subTitle, message, AlertType.WARNING);

		CheckBox cb = new CheckBox(messageCheckbox);
		cb.setSelected(false);

		Label label = new Label(message);
		label.setWrapText(true);
		label.setStyle("-fx-font-weight: bold;");
		label.setPadding(new Insets(10,0,0,0));

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(cb, 0, 0);
		expContent.add(label, 0, 1);

		alert.getDialogPane().setContent(expContent);

		alert.showAndWait();

		return cb.isSelected();

	}

	public List<String> confirmationWithSeveralCheckbox(String title, String subTitle,
			String message, String[] messagesCheckbox) {

		baseAlert(title, subTitle, message, AlertType.WARNING);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);

		Label label = new Label(message);
		label.setWrapText(true);
		label.setStyle("-fx-font-weight: bold;");
		label.setPadding(new Insets(10,0,0,0));

		expContent.add(label, 0, 0);

		// Creat all CheckBox's

		for (int i = 0; messagesCheckbox.length > i; i++)
			expContent.add(new CheckBox(messagesCheckbox[i]), 0, i+1);

		alert.getDialogPane().setContent(expContent);

		List<String> selecteds = new ArrayList<>();

		Optional<ButtonType> result = alert.showAndWait();

		// When press OK
		if (result.isPresent() && result.get() == ButtonType.OK) {

			CheckBox checkBox = null;

			// List all selecteds for return
			for (int i = 0; messagesCheckbox.length > i; i++) {

				checkBox = (CheckBox) expContent.getChildren().get(i+1);

				if (checkBox.isSelected())
					selecteds.add( checkBox.getText() );
			}

		}

		return selecteds;

	}

	public void alertWithContent(String title, String subTitle,
			String message, Node content) {

		baseAlert(title, subTitle, message, AlertType.INFORMATION);
		
		alert.getDialogPane().getChildren().add(content);
		
		alert.show();
		
	}

}

