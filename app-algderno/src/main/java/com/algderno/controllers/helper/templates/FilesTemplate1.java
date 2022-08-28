package com.algderno.controllers.helper.templates;

/**
 *
 * This class is based on organizing input and correct output files separated by extension but in the same folder/Exercise.
 * Extensions:
 * 	Input file: .in
 * 	Correct output: .sol
 *
 * Example:
 * Workbook/
 * -- Exercise1/
 * ---- Question1.in
 * ---- Question1.sol
 *
 * Note: files for a Question must have the same name.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.util.DataFiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class FilesTemplate1 extends AbstractFilesTemplate {

	public FilesTemplate1(String rootFolder) {

		super(rootFolder);

	}

	@Override
	public ObservableMap<String, Exercise> createListExercises() throws FileNotFoundException {

		File fileInput = new File(rootFolder);

		File[] exercises = fileInput.listFiles();

		findExercises(exercises);

		return getListExercisesOutput();

	}

	protected void findExercises(File[] namesExercises) throws FileNotFoundException {

		if (namesExercises.length > 0) {

			Arrays.sort(namesExercises);

			File fileExercise;

			File[] arrayFiles;

			Exercise foundExercise = null;

			String pathName = null;

			int priority = 0;
			
			for (File path : namesExercises) {

				if (path.isFile()) // If not is directory
					continue;
				
				pathName = path.getName();

				fileExercise = new File(this.rootFolder + "/" + pathName);

				arrayFiles = fileExercise.listFiles();

				ObservableMap<String, Question> mapQuestions = findQuestions(pathName, arrayFiles);

				foundExercise = new Exercise(priority++, pathName, mapQuestions);

				this.mapExercisesOutput.put(
						path.getName().replace("/", "").replace("\\", ""), 
						foundExercise);

			}

		} else
			throw new FileNotFoundException("Input folder is empty.");

	}

	protected ObservableMap<String, Question> findQuestions(String nameExercise, File[] arrayFiles) throws FileNotFoundException {

		ObservableMap<String, Question> map = FXCollections.observableHashMap();

		String local = "/" + nameExercise + "/";

		int nameInt = 0, priority = 0;
		for (File file : arrayFiles) {

			if (!file.isFile()) // Ignore if not is file
				continue;

			nameInt = nameToInt(file.getName());

			Question newQuestion = createQuestion(priority, local, file.getName(), nameExercise);

			if (newQuestion == null)
				continue;

			map.put(nameInt+"", newQuestion);

			priority++; // Increment in final, after last IF
			
		}

		return map;

	}

	private int nameToInt(String nameFile) {
		return Integer.parseInt( nameFile.substring(0, nameFile.lastIndexOf(".")) ); // nameFiles are numbers but Strings
	}

	protected Question createQuestion(int priority, String local, String fullNameQuestion, String nameExercise) throws FileNotFoundException {

		String[] extension = DataFiles.splitNameAndExtension(fullNameQuestion);

		local += extension[0];

		String inputLocal = local, outputLocal = local;

		if (extension[1] != null) {

			if (extension[1].equals("sol"))
				return null;

			inputLocal += ".in";
			outputLocal += ".sol";

			if (!hasCorrespondent(this.rootFolder + inputLocal, this.rootFolder + outputLocal)) {

				noFound.put(noFound.size(), local);

				return null;

			}

		}

		return new Question(priority, extension[0], inputLocal, outputLocal, 0, false, 0);

	}

	protected boolean hasCorrespondent(String pathInput, String pathOutput) {

		return (new File(pathInput).exists()) && (new File(pathOutput).exists());

	}

}
