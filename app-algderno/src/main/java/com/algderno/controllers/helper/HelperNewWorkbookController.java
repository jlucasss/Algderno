package com.algderno.controllers.helper;

/**
 *
 * This class has the job of helping WorkbookController to separate FXML methods and private helpers, thus organizing the controller.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;

import com.algderno.App;
import com.algderno.util.logger.SimpleLogger;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class HelperNewWorkbookController {

	@SuppressWarnings("unused")
	private SimpleLogger logger;

	public HelperNewWorkbookController(SimpleLogger logger) {
		this.logger = logger;
	}

	public void fillModel1TreeView(TreeView<String> model1TV) {

		TreeItem<String> root = new TreeItem<>("Workbook");
		root.setExpanded(true);

		TreeItem<String> question1 = new TreeItem<>("1");
		question1.setExpanded(true);
		question1.getChildren().add(new TreeItem<>("1.in"));
		question1.getChildren().add(new TreeItem<>("1.sol"));
		question1.getChildren().add(new TreeItem<>("2.in"));
		question1.getChildren().add(new TreeItem<>("2.sol"));

		TreeItem<String> question2 = new TreeItem<>("2");
		question2.setExpanded(true);
		question2.getChildren().add(new TreeItem<>("1.in"));
		question2.getChildren().add(new TreeItem<>("1.sol"));

		root.getChildren().add(question1);
		root.getChildren().add(question2);

		model1TV.setRoot(root);

	}

	public void fillModel2TreeView(TreeView<String> model2TV) {

		TreeItem<String> root = new TreeItem<>("Workbook");
		root.setExpanded(true);

		TreeItem<String> questions = new TreeItem<>("input");
		questions.setExpanded(true);
		questions.getChildren().add(new TreeItem<>("A"));
		questions.getChildren().add(new TreeItem<>("B"));

		TreeItem<String> outputCorrect = new TreeItem<>("output");
		outputCorrect.setExpanded(true);
		outputCorrect.getChildren().add(new TreeItem<>("A"));
		outputCorrect.getChildren().add(new TreeItem<>("B"));

		root.getChildren().add(outputCorrect);
		root.getChildren().add(outputCorrect);

		model2TV.setRoot(root);

	}

	public String selectFolder() {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select a folder:");

		File diretorySelected = directoryChooser.showDialog(App.mainStage);

		if (diretorySelected != null)
			return (diretorySelected.getPath().replace("\\", "/") + "/");

		return null;

	}

}
