package com.algderno.models.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algderno.controllers.MainController;
import com.algderno.models.Exercise;
import com.algderno.models.Workbook;

import javafx.collections.FXCollections;

public class SimpleGroup {

	public final Map<String, Workbook> mapWorkbook;
	
	public SimpleGroup() {
		
		this.mapWorkbook = new HashMap<>();

	}
	
	public void add(String nameWorkbook, String nameExercise, String nameQuestion) {
		
		Workbook originalWorkbook = MainController.mapWorkbooks.get(nameWorkbook);
		
		// Add Workbook, if not contain
		if (!mapWorkbook.containsKey(nameWorkbook)) {
			
			Workbook workbook = new Workbook(
					originalWorkbook.getPriority(), 
					nameWorkbook, 
					originalWorkbook.getPathRoot(),
					originalWorkbook.getPathFileSolution(),
					FXCollections.observableHashMap(),
					originalWorkbook.getModelSelected());
			
			mapWorkbook.put(nameWorkbook, workbook);

		}
		
		//Add Exercise, if not contain
		if (!mapWorkbook.get(nameWorkbook).getMapData().containsKey(nameExercise))
			mapWorkbook.get(nameWorkbook).getMapData().put(nameExercise, 
					new Exercise(
							originalWorkbook.getMapData().get(nameExercise).getPriority(), 
							nameExercise
						));

		// Add Question
		mapWorkbook.get(nameWorkbook).getMapData().get(nameExercise).getMapData()
		.put(nameQuestion, 
				originalWorkbook.getMapData().get(nameExercise).getMapData().get(nameQuestion)
				);
		
	}
	
	public List<String> toArrayString() {

		List<String> out = new ArrayList<>();
		
		mapWorkbook.forEach((keyW, valueW) ->
					valueW.getMapData().forEach((keyE, valueE) -> 
						valueE.getMapData().forEach(
								(keyQ, valueQ) -> 
									out.add(new StringBuilder().append(keyW).append(";")
										.append(keyE).append(";")
										.append(keyQ).toString() )
				)));
		
		return out;
	}
	
}
