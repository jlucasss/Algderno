package com.algderno.models.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.algderno.controllers.MainController;
import com.algderno.models.Exercise;
import com.algderno.models.Workbook;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;

public class SelectedsList {

	public static enum ListType {
		HASH_MAP, LIST_TREE_ITEM
	}
	
	private HashMap<String, Workbook> mapWorkbooks;
	private List<TreeItem<String>> selecteds;
	private ListType type;
	
	public SelectedsList(TreeItem<String> itemBranchLevel1, ListType type) {
		
		this.type = type;
		
		if (type.equals(ListType.LIST_TREE_ITEM))
			selecteds = new ArrayList<>();
		else if (type.equals(ListType.HASH_MAP))
			mapWorkbooks = new HashMap<>();
		
		createlistIdsSelected(itemBranchLevel1, type);
		
	}
	
	/* Create list of String as describe where to find Question selected */
	private void createlistIdsSelected(TreeItem<String> itemBranchLevel1, ListType type) {
		
		// For each Workbook
		for (TreeItem<String> itemWorkbook : itemBranchLevel1.getChildren()) 
			
			// For each Exercise
			for (TreeItem<String> itemExercise : itemWorkbook.getChildren()) 
				
				// For each Question
				for (TreeItem<String> itemQuestion : itemExercise.getChildren())
					
					// If is selected
					if (((CheckBoxTreeItem<String>)itemQuestion).isSelected()) 
						
						if (type.equals(ListType.LIST_TREE_ITEM))
							selecteds.add(itemQuestion);
						
						else if (type.equals(ListType.HASH_MAP))
							addHashMap(itemWorkbook.getValue(), itemExercise.getValue(), itemQuestion.getValue());
		
		
	}
	
	private void addHashMap(String nameWorkbook, String nameExercise, String nameQuestion) {
	
		// Workbook 
		
		Workbook currentWorkbook = null;
		
		if (!this.mapWorkbooks.containsKey(nameWorkbook)) {
			
			int priority = MainController.mapWorkbooks.getMapData().get(nameWorkbook).getPriority();
			
			currentWorkbook = new Workbook(priority, nameWorkbook);
			
			this.mapWorkbooks.put(nameWorkbook, currentWorkbook);
			
		} else
			currentWorkbook = this.mapWorkbooks.get(nameWorkbook);
		
		// Exercise
	
		Exercise currentExercise = null;
		
		if (!currentWorkbook.getMapData().containsKey(nameWorkbook)) {
			
			int priority = MainController.mapWorkbooks.getMapData().get(nameWorkbook)
					.getMapData().get(nameExercise).getPriority();
			
			currentExercise = new Exercise(priority, nameExercise);
			
			currentWorkbook.getMapData().put(nameExercise, currentExercise);
			
		} else
			currentExercise = currentWorkbook.getMapData().get(nameExercise);
		
		// Question
		
		currentExercise.getMapData().put(nameQuestion, 
				MainController.mapWorkbooks.getMapData().get(nameWorkbook)
				.getMapData().get(nameExercise).getMapData().get(nameQuestion)
		);
		
		
	}

	public List<String> toArrayString() {

		List<String> out = new ArrayList<>();
		
		for (TreeItem<String> itemQuestion : selecteds)
			out.add(
					itemQuestion.getValue() + " - " +               // Question 
					itemQuestion.getParent().getValue() +  " - " +  // Exercise 
					itemQuestion.getParent().getParent().getValue() // Workbook
				);
		
		return out;
	}
	
	public List<TreeItem<String>> getSelecteds() {
		
		if (!this.type.equals(ListType.LIST_TREE_ITEM))
			throw new RuntimeException("List type incorrect");
		
		return this.selecteds;
	}

	public HashMap<String, Workbook> getMapWorkbooks() {
		
		if (!this.type.equals(ListType.HASH_MAP))
			throw new RuntimeException("List type incorrect");
		
		return this.mapWorkbooks;
	}
	
}
