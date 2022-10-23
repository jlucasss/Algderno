package com.algderno.controllers.helper.templates;

import java.util.HashMap;

import com.algderno.models.Exercise;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public abstract class AbstractFilesTemplate {

	protected String rootFolder, errorMensage;
	protected ObservableMap<String, Exercise> mapExercisesOutput;
	protected HashMap<Integer, String> mapQuestionsString, noFound;

	public AbstractFilesTemplate(String rootFolder) {

		this.rootFolder = rootFolder;
		this.errorMensage = "";
		this.mapExercisesOutput = FXCollections.observableHashMap();
		this.mapQuestionsString = new HashMap<>();
		this.noFound = new HashMap<>();

	}

	public abstract ObservableMap<String, Exercise> createListExercises() throws Exception;

	public ObservableMap<String, Exercise> getListExercisesOutput() {
		return mapExercisesOutput;
	}

	public String getErrorMensage() {
		return errorMensage;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

}
