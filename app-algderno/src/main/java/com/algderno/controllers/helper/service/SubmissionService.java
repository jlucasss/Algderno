package com.algderno.controllers.helper.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.algderno.controllers.MainController;
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

public abstract class SubmissionService extends Service<Workbook[]> {

	private ProgressBar progressBar;
	public TreeTableView<Group<?>> resultsTTV;

	protected SimpleLogger logger;
	public MainController main;
	protected Task<Workbook[]> task;
	protected Thread threadTask;

	protected Submission submission;
	//private String nameWorkbook;
	
	//public Workbook currentWorkbook;

	public SubmissionService(SimpleLogger logger,
			MainController main,
			ProgressBar progressBar,
			TreeTableView<Group<?>> resultsTTV,
			Workbook[] arrayWorkbooks) {

		this.logger = logger;
		this.main = main;
		this.progressBar = progressBar;
		this.resultsTTV = resultsTTV;

		this.submission = new Submission(arrayWorkbooks);

	}

	protected abstract void startThread();

	@Override
	protected Task<Workbook[]> createTask() {

		this.task = new Task<>() {

			@Override
			protected Workbook[] call() throws Exception {

				try {


					submission.getListListenerWorkbook().add((w) -> {
						onListenerListQuestionUpdated(w);
						logger.getInfos().add("Next workbook = " + w.getName());
					});

					Platform.runLater(() -> {
						progressBar.progressProperty().bind(submission.getProgress().currentProgressProperty());
						onListenerTextProgress();
					});
					// Submitting
					
					submission.testAllWorkbooks();
				
					Platform.runLater(() -> {
					
						if (progressBar.progressProperty().isBound())
							progressBar.progressProperty().unbind();

					});
					
				} catch (Exception e) {
					
					logger.getExceptions().add(
						main.getResources().getString("exception.unable.verify.question"), e).show();
					e.printStackTrace();

				}

				return submission.getArrayWorkbooks();
			}
		};

		this.threadTask = new Thread(this.task);
		startThread();

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

	private void onListenerListQuestionUpdated(Workbook workbook) {

		// Try to find index of Workbook in TreeTableView resultsTTV
		int i = 0, workbookId = -1;
		for (TreeItem<Group<?>> itemWorkbook : main.resultsTTV.getRoot().getChildren()) { // For each workbook
			if (itemWorkbook.getValue().getName().equals(workbook.getName())) {
				workbookId = i;
				break;
			}
			i++;
		}
		
		if (workbookId == -1) { // If was not created Workbook
			main.resultsTTV.getRoot().getChildren().add(new TreeItem<Group<?>>(workbook));
			workbookId = main.resultsTTV.getRoot().getChildren().size();
		}

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
				
				for (Question question : entry.getValue())
					details.append("\n")
							.append(main.getResources().getString("messages.question.not.exist"))
							.append(question.getName()) 
							.append(" - Exercise: ")
							.append(entry.getKey());
					
			}
			
			logger.getErrors().add(details.toString() + 
					"\n" + main.getResources().getString("messages.delete.create.question")).show();
			
		}
	
		super.succeeded();
		
	}

}

