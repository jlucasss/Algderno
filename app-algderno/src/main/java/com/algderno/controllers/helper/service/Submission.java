
package com.algderno.controllers.helper.service;

/**
*
* This class load items in table while verify the Questions.
*
* @author José Lucas dos Santos da Silva
*
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algderno.controllers.MainController;
import com.algderno.controllers.helper.service.ProgressSubmission.StatusProgress;
import com.algderno.execution.Tester;
import com.algderno.models.Exercise;
import com.algderno.models.Group;
import com.algderno.models.Question;
import com.algderno.models.Workbook;

import javafx.collections.ObservableMap;

public class Submission {

	private int idsSize;
	private ObservableMap<String, Exercise> mapExercises;
	private HashMap<String, List<Question>> nonexistent;
	public final Workbook workbook, selectedWorkbook;
	private Tester submit;
	private List<ListenerSubmissionQuestion> listListener;
	
	private ProgressSubmission progress;

	public Submission(Workbook selectedWorkbook) {

		this.selectedWorkbook = selectedWorkbook;
		
		this.workbook = MainController.mapWorkbooks.get(selectedWorkbook.getName());
		this.mapExercises = workbook.getMapData();
		this.submit = new Tester();
		this.nonexistent = new HashMap<>();
		this.listListener = new ArrayList<>();

		idsSize = this.mapExercises.size();
		
		this.progress = new ProgressSubmission(idsSize);

	}

	public void addListener(ListenerSubmissionQuestion l) {
		this.listListener.add(l);
	}
	
	public List<ListenerSubmissionQuestion> getListListener() {
		return this.listListener;
	}
	
	public Workbook testExercisesAll() throws Exception {

		//data.clear();
		submit.prepare(MainController.pathDefault + "/" + selectedWorkbook.getName() + "/",
				workbook.getPathFileSolution(),
				workbook.getPathRoot());

		int numQuestions = 0, countExercises = 0;
		long averageLastRuntime = 0;
		boolean allCorrects = true;
		
		Exercise exercises[] = new Exercise[this.mapExercises.size()]; // Is necessary prepare array to toArray
		this.mapExercises.values().toArray(exercises);
		sortByPrority(exercises);
		
		for (Exercise currentExercise : exercises) {

			if (! selectedWorkbook.getMapData().containsKey(currentExercise.getName())) { // If not selected Question

				averageLastRuntime += currentExercise.getLastRuntime();
				
				if (allCorrects) // Verify the not tested
					allCorrects = currentExercise.isResultCorrect();
				
				continue;
			}
			
			countExercises++;
			
			this.progress.updateProgressExercise(currentExercise.getName(), countExercises);

			numQuestions = this.mapExercises.get(currentExercise.getName()).getMapData().size();//idsSelected.get(countExercises).size();

			testQuestionsAll(currentExercise, countExercises, numQuestions);

			if (allCorrects) // Verify the tested
				allCorrects = currentExercise.isResultCorrect();
			
			averageLastRuntime += currentExercise.getLastRuntime();
			
		}

		averageLastRuntime = averageLastRuntime / workbook.getMapData().size();

		workbook.setLastRuntime(averageLastRuntime);
		workbook.setResultCorrect(allCorrects); // If all exercises is corrects
		
		// Too for update table
		selectedWorkbook.setLastRuntime(averageLastRuntime);
		selectedWorkbook.setResultCorrect(allCorrects);

		return workbook;

	}

	private void testQuestionsAll(Exercise currentExercise, int idExerciseList, int numQuestions) throws Exception {
		
		// Sort values by Priority
		Question questions[] = new Question[currentExercise.getMapData().size()];
		currentExercise.getMapData().values().toArray(questions);
		sortByPrority(questions);
		
		long averageLastRuntime = 0;
		boolean allCorrects = true;
		
		int priority = 0;
		for (Question currentQuestion : questions) {
			
			priority = currentQuestion.getPriority();
			
			if (! selectedWorkbook.getMapData().get(currentExercise.getName()).getMapData().containsKey(currentQuestion.getName()) ) { // If not selected Question
				
				if (allCorrects) // Verify the not tested
					allCorrects = currentQuestion.isResultCorrect();
				
				averageLastRuntime += currentQuestion.getLastRuntime();
				
				for (int i = 0; this.listListener.size() > i; i++)
					this.listListener.get(i).changed(currentExercise.getName(), currentQuestion);
				
				continue;
			}
			
			this.progress.updateProgressQuestion(currentQuestion.getName(), 
					priority, numQuestions, StatusProgress.PROCESSING);

			if (testQuestion(currentExercise.getName(), currentQuestion)) {

				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						priority, numQuestions, StatusProgress.TESTED);


			} else
				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						priority, numQuestions, StatusProgress.ERROR);

			averageLastRuntime += currentQuestion.getLastRuntime();
			
			if (allCorrects) // Verify the tested
				allCorrects = currentQuestion.isResultCorrect();
			
			for (int i = 0; this.listListener.size() > i; i++)
				this.listListener.get(i).changed(currentExercise.getName(), currentQuestion);
			
		}
		
		averageLastRuntime = averageLastRuntime / currentExercise.getMapData().size();

		// Update data of the currentExercise
		
		currentExercise.setLastRuntime(averageLastRuntime);
		currentExercise.setResultCorrect(allCorrects); // If all questions is corrects
		
	}

	private void sortByPrority(Group<?>[] array) {
		Arrays.sort(array, new Comparator<Group<?>>() {

			@Override
			public int compare(Group<?> o1, Group<?> o2) {
				return Integer.compare(o1.getPriority(), o2.getPriority());
			}
			
		});
	}

	private boolean testQuestion(String exerciseName, Question currentQuestion) throws Exception {

		if (!isExistsQuestion(exerciseName, currentQuestion))
			return false;

		submit.submit(currentQuestion);
System.out.println("afterSubmit = " + currentQuestion.toString());
		workbook.getMapData()
					.get(exerciseName).getMapData()
						.put(currentQuestion.getName(), currentQuestion);

		return true;

	}

	public boolean isExistsQuestion(String exerciseName, Question currentQuestion) {
		if (
			!(new File(workbook.getPathRoot()+currentQuestion.getMapData().get("pathInput")).exists()) &&
			!(new File(workbook.getPathRoot()+currentQuestion.getMapData().get("pathOutputCorrect")).exists())
		) {

			if (nonexistent.containsKey(exerciseName))
				nonexistent.get(exerciseName).add(currentQuestion);
			else
				nonexistent.put(exerciseName, Arrays.asList(currentQuestion));

			currentQuestion.setResultCorrect(false);
			workbook.getMapData()
						.get(exerciseName).getMapData()
							.put(currentQuestion.getName(), currentQuestion);

			return false;

		}

		return true;

	}

	/* Getters and Setters */

	public ProgressSubmission getProgress() {
		return this.progress;
	}

	public Map<String, Exercise> getMapExercises() {
		return this.mapExercises;
	}

	public HashMap<String, List<Question>> getNonexistent() {
		return this.nonexistent;
	}

	/*public Map<String, Exercise> getData() {
		return this.data;
	}*/

}

