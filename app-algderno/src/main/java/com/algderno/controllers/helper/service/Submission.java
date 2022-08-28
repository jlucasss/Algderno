
package com.algderno.controllers.helper.service;

/**
*
* This class load items in table while verify the Questions.
*
* @author José Lucas dos Santos da Silva
*
*/

import java.io.File;
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

	//private List<List<Integer>> idsSelected;
	private int idsSize;
	private ObservableMap<String, Exercise> mapExercises;
	private HashMap<String, List<Question>> nonexistent;
	//private Map<String, Exercise> data;
	public final Workbook workbook, selectedWorkbook;
	private Tester submit;
	
	private ProgressSubmission progress;

	public Submission(Workbook selectedWorkbook) {

		this.selectedWorkbook = selectedWorkbook;
		
		this.workbook = MainController.mapWorkbooks.get(selectedWorkbook.getName());
		this.mapExercises = workbook.getMapData();
		this.submit = new Tester();
		this.nonexistent = new HashMap<>();

		idsSize = this.mapExercises.size();
		
		this.progress = new ProgressSubmission(idsSize);//idsSelected.size());

		//this.data.sorted(sortData());

	}

	public Workbook testExercisesAll() throws Exception {

		//data.clear();
		submit.prepare(MainController.pathDefault + "/" + selectedWorkbook.getName() + "/",
				workbook.getPathFileSolution(),
				workbook.getPathRoot());

		int numQuestions = 0, countExercises = 0;

		Exercise exercises[] = (Exercise[]) mapExercises.values().toArray();
		sortByPrority(exercises);
		
		System.out.println("numExercises = " + exercises.length);
		
		for (Exercise currentExercise : exercises) {

			if (! selectedWorkbook.getMapData().containsKey(currentExercise.getName())) // If not selected Question
				continue;
			
			countExercises++;
			
			this.progress.updateProgressExercise(currentExercise.getName(), countExercises);

			numQuestions = this.mapExercises.get(currentExercise.getName()).getMapData().size();//idsSelected.get(countExercises).size();

			testQuestionsAll(currentExercise, countExercises, numQuestions);

		}

		return workbook;

	}

	private void testQuestionsAll(Exercise currentExercise, int idExerciseList, int numQuestions) throws Exception {
		
		System.out.println("numQuestion = " + numQuestions);
		
		// Sort values by Priority
		Question questions[] = (Question[]) currentExercise.getMapData().values().toArray();
		sortByPrority(questions);
		
		int count = 0;
		for (Question currentQuestion : questions) {
			
			count = currentQuestion.getPriority();
			
			if (! selectedWorkbook.getMapData().get(currentExercise.getName()).getMapData().containsKey(currentQuestion.getName()) ) // If not selected Question
				continue;
			
			this.progress.updateProgressQuestion(currentQuestion.getName(), 
					count, numQuestions, StatusProgress.PROCESSING);

			if (testQuestion(currentExercise.getName(), currentQuestion)) {

				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						count, numQuestions, StatusProgress.TESTED);


			} else
				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						count, numQuestions, StatusProgress.ERROR);

		}
		
		
/*		for (int j = 1; numQuestions > j; j++) { // j is id question list of questions

			indexQuestion = idsSelected.get(idExerciseList).get(j);

			if (! selectedWorkbook.getMapData().get(currentExercise.getName()).getMapData().containsKey(currentQuestion.getName()) ) // If not selected Question
				continue;
			
			currentQuestion = currentExercise.getMapData().get(indexQuestion);

			this.progress.updateProgressQuestion(currentQuestion.getName(), 
					(j+1), numQuestions, StatusProgress.PROCESSING);

			if (testQuestion(currentExercise.getName(), currentQuestion)) {

				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						(j+1), numQuestions, StatusProgress.TESTED);


			} else
				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						(j+1), numQuestions, StatusProgress.ERROR);

		}*/
		
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

