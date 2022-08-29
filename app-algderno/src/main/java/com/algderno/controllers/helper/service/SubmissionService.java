package com.algderno.controllers.helper.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.algderno.controllers.MainController;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;
import com.algderno.util.logger.SimpleLogger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;

public abstract class SubmissionService extends Service<Map<String, Exercise>> {

	private ProgressBar progressBar;
	public TreeTableView<Group<?>> resultsTTV;

	protected SimpleLogger logger;
	public MainController main;
	protected Task<Map<String, Exercise>> task;
	protected Thread threadTask;

	protected Submission submission;
	private String nameWorkbook;

	public SubmissionService(ProgressBar progressBar,
			Workbook selectedWorkbook,
			TreeTableView<Group<?>> resultsTV,
			SimpleLogger logger,
			MainController main) {

		this.nameWorkbook = selectedWorkbook.getName();
		this.submission = new Submission(selectedWorkbook);

		this.progressBar = progressBar;
		this.resultsTTV = resultsTV;

		this.logger = logger;
		this.main = main;

	}

	protected abstract void startThread();

	@Override
	protected Task<Map<String, Exercise>> createTask() {

		onListenerListQuestionUpdated();
		
		this.task = new Task<>() {

			@Override
			protected Map<String, Exercise> call() throws Exception {

				try {

					submission.testExercisesAll();

				} catch (Exception e) {
					
					logger.getExceptions().add(
						main.getResources().getString("exception.unable.verify.question"), e).show();
					e.printStackTrace();

				}

				return submission.getMapExercises();
			}
		};

		this.threadTask = new Thread(this.task);
		startThread();

		/* Connect updates with controls */

		progressBar.progressProperty().bind(this.submission.getProgress().currentProgressProperty());
		
		onListenerTextProgress();

		//Property<ObservableList<Question>> property = new SimpleObjectProperty<>(this.submission.getData());
		//resultsTTV.getRoot().valueProperty().bind(property);

		return task;
	}

	private void onListenerTextProgress() {

		this.submission.getProgress().currentMessageProperty().addListener(
			new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> obs, String oldV, String newV) {

					Platform.runLater(() -> main.leftL.setText(newV));

				}

			}
		);

	}

	private void onListenerListQuestionUpdated() {

		// Try to find index of Workbook in TreeTableView resultsTTV
		int i = 0, workbookId = -1;
		for (TreeItem<Group<?>> itemWorkbook : main.resultsTTV.getRoot().getChildren()) { // For each workbook
			if (itemWorkbook.getValue().getName().equals(nameWorkbook)) {
				workbookId = i;
				break;
			}
			i++;
		}
		
		if (workbookId == -1) { // If was not created Workbook
			main.resultsTTV.getRoot().getChildren().add(new TreeItem<Group<?>>(this.submission.workbook));
			workbookId = main.resultsTTV.getRoot().getChildren().size();
		}
		
		MainController.helper.getChartController().cleanDataMain(); // Clear chart
		
		// Add new Question in Chart
		submission.addListener((Question q) ->
				Platform.runLater(() ->
					MainController.helper.getChartController().addDataInMain(q.getName(), q))
			);

	}
	
	@Override
	protected void succeeded() {

		HashMap<String, List<Question>> nonexistent = this.submission.getNonexistent();

		if (nonexistent.size() > 0) {
			
			StringBuilder details = new StringBuilder();

			Iterator<Entry<String, List<Question>>> iterator = nonexistent.entrySet().iterator();

			Entry<String, List<Question>> entry = null;

			while (iterator.hasNext()) {
				
				entry = iterator.next();
				
				for (Question question : (Question[])entry.getValue().toArray())
					details.append("\n")
							.append(main.getResources().getString("messages.question.not.exist"))
							.append(question.getName()) 
							.append(" - Exercise: ")
							.append(entry.getKey());
					
			}
			
			logger.getErrors().add(details.toString() + 
					"\n" + main.getResources().getString("messages.delete.create.question")).show();
			
		}
	
		MainController.helper.getChartController().installAllTooltips();
	
		super.succeeded();
		
	}

}

