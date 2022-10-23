
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

public class Submission {

	private HashMap<String, List<Question>> nonexistent;
	private Tester submit;
	private List<ListenerGroup<Question>> listListenerQuestion;
	private List<ListenerGroup<Exercise>> listListenerExercise;
	private List<ListenerGroup<Workbook>> listListenerWorkbook;
	
	private ProgressSubmission progress;
	private Workbook[] arrayWorkbooks;

	public Submission(Workbook[] arrayWorkbooks) {

		this.arrayWorkbooks = arrayWorkbooks;
		
		this.submit = new Tester();
		this.nonexistent = new HashMap<>();
		this.listListenerQuestion = new ArrayList<>();
		this.listListenerExercise = new ArrayList<>();
		this.listListenerWorkbook = new ArrayList<>();

		this.progress = new ProgressSubmission(arrayWorkbooks.length);

	}

	public void testAllWorkbooks() throws Exception {
		
		for (Workbook workbook : this.arrayWorkbooks) {
			
			Workbook currentWorkbook = MainController.mapWorkbooks.getMapData().get(workbook.getName());
			
			listListenerWorkbook.forEach(c -> c.changed(currentWorkbook));
			
			testAllExercises(currentWorkbook);
		
		}
		
	}
	
	private void testAllExercises(Workbook currentWorkbook) throws Exception {

		Map<String, Exercise> mapExercises = currentWorkbook.getMapData();
		
		submit.prepare(MainController.pathDefault + "/" + currentWorkbook.getName() + "/",
				currentWorkbook.getPathFileSolution(),
				currentWorkbook.getPathRoot());

		int numQuestions = 0, countExercises = 0;
		long averageLastRuntime = 0;
		boolean allCorrects = true;
		
		Exercise exercises[] = new Exercise[mapExercises.size()]; // Is necessary prepare array to toArray
		mapExercises.values().toArray(exercises);
		sortByPrority(exercises);
		
		for (Exercise currentExercise : exercises) {

			listListenerExercise.forEach(c -> c.changed(currentExercise));
			
			if (! currentWorkbook.getMapData().containsKey(currentExercise.getName())) { // If not selected Question

				averageLastRuntime += currentExercise.getLastRuntime();
				
				if (allCorrects) // Verify the not tested
					allCorrects = currentExercise.isResultCorrect();
				
				continue;
			}
			
			countExercises++;
			
			this.progress.updateProgressExercise(currentExercise.getName(), countExercises);

			numQuestions = mapExercises.get(currentExercise.getName()).getMapData().size();//idsSelected.get(countExercises).size();

			testQuestionsAll(currentWorkbook, currentExercise, countExercises, numQuestions);

			if (allCorrects) // Verify the tested
				allCorrects = currentExercise.isResultCorrect();
			
			averageLastRuntime += currentExercise.getLastRuntime();
			
		}

		averageLastRuntime = averageLastRuntime / currentWorkbook.getMapData().size();

		currentWorkbook.setLastRuntime(averageLastRuntime);
		currentWorkbook.setResultCorrect(allCorrects); // If all exercises is corrects
		
		// Too for update table
		currentWorkbook.setLastRuntime(averageLastRuntime);
		currentWorkbook.setResultCorrect(allCorrects);

	}

	private void testQuestionsAll(Workbook currentWorkbook, Exercise currentExercise, int idExerciseList, int numQuestions) throws Exception {
		
		// Sort values by Priority
		Question questions[] = new Question[currentExercise.getMapData().size()];
		currentExercise.getMapData().values().toArray(questions);
		sortByPrority(questions);
		
		long averageLastRuntime = 0;
		boolean allCorrects = true;
		
		int priority = 0;
		for (Question currentQuestion : questions) {
			
			priority = currentQuestion.getPriority();
			
			if (! currentWorkbook.getMapData().get(currentExercise.getName()).getMapData().containsKey(currentQuestion.getName()) ) { // If not selected Question
				
				if (allCorrects) // Verify the not tested
					allCorrects = currentQuestion.isResultCorrect();
				
				averageLastRuntime += currentQuestion.getLastRuntime();
				
				listListenerQuestion.forEach(c -> c.changed(currentQuestion));
				
				continue;
			}
			
			this.progress.updateProgressQuestion(currentQuestion.getName(), 
					priority, numQuestions, StatusProgress.PROCESSING);

			if (testQuestion(currentWorkbook, currentExercise.getName(), currentQuestion)) {

				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						priority, numQuestions, StatusProgress.TESTED);


			} else
				this.progress.updateProgressQuestion(currentQuestion.getName(), 
						priority, numQuestions, StatusProgress.ERROR);

			averageLastRuntime += currentQuestion.getLastRuntime();
			
			if (allCorrects) // Verify the tested
				allCorrects = currentQuestion.isResultCorrect();
			
			listListenerQuestion.forEach(c -> c.changed(currentQuestion));
			
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

	private boolean testQuestion(Workbook workbook, String exerciseName, Question currentQuestion) throws Exception {

		if (!isExistsQuestion(workbook, exerciseName, currentQuestion))
			return false;

		submit.submit(currentQuestion);

		workbook.getMapData()
					.get(exerciseName).getMapData()
						.put(currentQuestion.getName(), currentQuestion);

		return true;

	}

	public boolean isExistsQuestion(Workbook workbook, String exerciseName, Question currentQuestion) {
		if (
			!(new File(workbook.getPathRoot()+currentQuestion.getMapData().get("pathInput")).exists()) &&
			!(new File(workbook.getPathRoot()+currentQuestion.getMapData().get("pathOutputCorrect")).exists())
		) {

			System.out.println("PathRoot = " + workbook.getPathRoot()+currentQuestion.getMapData().get("pathInput"));

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

	public HashMap<String, List<Question>> getNonexistent() {
		return this.nonexistent;
	}

	public Workbook[] getArrayWorkbooks() {
		return arrayWorkbooks;
	}

	public List<ListenerGroup<Exercise>> getListListenerExercise() {
		return listListenerExercise;
	}

	public List<ListenerGroup<Workbook>> getListListenerWorkbook() {
		return listListenerWorkbook;
	}

	public List<ListenerGroup<Question>> getListListenerQuestion() {
		return listListenerQuestion;
	}

}

