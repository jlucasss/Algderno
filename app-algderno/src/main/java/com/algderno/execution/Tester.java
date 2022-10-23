
package com.algderno.execution;

/**
 *
 * This class translates the Question to JSubmeter.jar and retrieves the results.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.IOException;
import java.util.List;

import com.algderno.io.reader.Reader;
import com.algderno.models.Question;

import com.jsubmeter.execution.Submeter;
import com.jsubmeter.models.DataPerson;

public class Tester {

	private DataPerson data;
	private Submeter submit;
	private String pathQuestions;

	public void prepare(String pathOutput, String pathFileSolution, String pathQuestions
			) throws IOException, InterruptedException {

		if (pathOutput == null || pathOutput.isEmpty() ||
				pathFileSolution == null || pathFileSolution.isEmpty() ||
				pathQuestions == null || pathQuestions.isEmpty())
			throw new IllegalArgumentException("Some entry(s) are invalid for testing.");

		this.pathQuestions = pathQuestions;
				
		this.data = new DataPerson(pathFileSolution,
				pathQuestions, "", pathOutput);

		//System.out.println("DATAPERSON = \n" + pathFileSolution + "\n" + pathQuestions  + "\n" + pathOutput + "\n");
	
		this.submit = new Submeter(data);
		//submit.setFileWithMemoryUsed(false);
		submit.setFileOutputWithTimeExecution(false);
		
		submit.preSubmit();
		
	}
	
	public void submit(Question question) throws IOException {

		if (this.submit == null)
			throw new RuntimeException("The 'prepare' method is not call.");
		
		submit.finishSubmit(question.getMapData().get("pathInput"));

		submit.saveOutputCurrent(question.getMapData().get("pathOutputCorrect"));//It has the same folder name as the answer, and the name only changes the path

		List<String> listOutput = data.getListOutput();

		long time = data.getTimeExecution();

		boolean checkEqualAnswers =
				new Reader(
							(pathQuestions + question.getMapData().get("pathOutputCorrect"))
						).readLines().equals(listOutput);


		question.setLastRuntime(time);

		question.setResultCorrect(checkEqualAnswers);
		
		//System.out.println(question.toString());

	}

}

