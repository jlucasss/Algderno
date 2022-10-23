package com.algderno.controllers.helper.templates;

/**
 *
 * This class is based on organizing input and correct output files separated by folder but without a default extension.
 * Folders for:
 * 	Input file: input
 * 	Correct output: output
 *
 * Example:
 * Workbook/
 * -- input/
 * ---- Question1
 * -- output/
 * ---- Question1
 *
 * Note: files for a Question must have the same name.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.util.DataFiles;
import com.algderno.util.HelperLists;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class FilesTemplate2 extends AbstractFilesTemplate {

	public FilesTemplate2(String rootFolder) {

		super(rootFolder);

	}

	@Override
	public ObservableMap<String, Exercise> createListExercises() throws FileNotFoundException {

		File rootFile = new File(rootFolder);

		String[] namesFiles = rootFile.list();

		findExercise(namesFiles, rootFile.getName());

		return getListExercisesOutput();

	}

	protected void findExercise(String[] namesFiles, String nameRoot) throws FileNotFoundException {

		if (namesFiles.length > 0) {

			Arrays.sort(namesFiles);

			if (0 > Arrays.binarySearch(namesFiles, "input") &&
					0 > Arrays.binarySearch(namesFiles, "output"))
				throw new FileNotFoundException("Could not find 'input' and/or 'output' folders."
						+ "Maybe you selected the wrong template.");

			String pathInput = rootFolder + "/input/";
			String pathOutput = rootFolder + "/output/";

			//check whether the two folders contain files with the same name

			String[] listaInput = new File(pathInput).list();
			String[] listaOutput = new File(pathOutput).list();

			checkEqualityOfLists(listaInput, listaOutput);

			ObservableMap<String, Question> mapQuestions = createListQuestions();

			getListExercisesOutput().put(nameRoot, new Exercise(0, nameRoot, mapQuestions));

		}

	}

	protected ObservableMap<String, Question> createListQuestions() {

		ObservableMap<String, Question> mapQuestions = FXCollections.observableHashMap();

		for (int i = 0; mapQuestionsString.size() > i; i++) {

			Question newQuestion =
					new Question(i,
								mapQuestionsString.get(i),
								"/input/"+mapQuestionsString.get(i),
								"/output/"+mapQuestionsString.get(i),
								0, false, 0);

			mapQuestions.put(newQuestion.getName(), newQuestion);

		}

		return mapQuestions;

	}

	protected void checkEqualityOfLists(String[] listInput, String[] listOutput) {

		String[] listInputWithoutExtension = removeExtensionList(listInput);

		String[] listOutputWithoutExtension = removeExtensionList(listOutput);

		mapQuestionsString.clear();

		mapQuestionsString.putAll(
				
				convertCollectionToMap(
						HelperLists.getIntersection(
								Arrays.asList(listInputWithoutExtension),
								Arrays.asList(listOutputWithoutExtension)) 
				)
				
		);

		noFound.clear();

		noFound.putAll(
				
				convertCollectionToMap(
						HelperLists.getMinus(
								Arrays.asList(listInputWithoutExtension),
								Arrays.asList(listOutputWithoutExtension)) 
				)

		);

	}

	private <T> TreeMap<Integer, T> convertCollectionToMap(Collection<T> c) {
		
		TreeMap<Integer, T> treeMap = new TreeMap<>();
		
		int count = 0;
		for (T t : c) 
			treeMap.put(count++, t);
		
		return treeMap;
		
	}
	
	protected String[] removeExtensionList(String[] list) {

		String[] listWithoutExtension = new String[list.length];

		for (int i = 0; list.length > i; i++)
			listWithoutExtension[i] = DataFiles.splitNameAndExtension(list[i])[0];

		return listWithoutExtension;

	}

}
