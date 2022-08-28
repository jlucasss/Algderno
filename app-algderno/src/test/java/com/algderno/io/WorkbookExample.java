package com.algderno.io;

import com.algderno.controllers.NewWorkbookController.EnumModels;
import com.algderno.models.Exercise;
import com.algderno.models.Question;
import com.algderno.models.Workbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class WorkbookExample {

	public static Workbook exampleComplete(String path) {

		int maxRuntime = 1;
		
		ObservableMap<String, Exercise> mapExercise = FXCollections.observableHashMap();

		ObservableMap<String, Question> maptQ1 = FXCollections.observableHashMap();
		maptQ1.put("0-1", new Question(0, "0-1", "/1/1.in", "/1/1.sol", 300, true, maxRuntime));
		maptQ1.put("0-2", new Question(1, "0-2", "/1/2.in", "/1/2.sol", 350, true, maxRuntime));
		
		mapExercise.put("0", new Exercise(0, "1", maptQ1));

		ObservableMap<String, Question> mapQ2 = FXCollections.observableHashMap();
		mapQ2.put("1-1", new Question(0, "1-1", "/2/1.in", "/2/1.sol", 400, true, maxRuntime));
		
		mapExercise.put("0", new Exercise(1, "2", mapQ2));

		return new Workbook(0,
				"Name",
				path + "lists/workbook/exercises/",
				path + "solution.java", mapExercise, EnumModels.Model1);

	}

}
