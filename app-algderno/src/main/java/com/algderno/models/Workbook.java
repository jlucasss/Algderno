
package com.algderno.models;

import com.algderno.controllers.NewWorkbookController.EnumModels;

import javafx.collections.ObservableMap;

public class Workbook extends Group<Exercise> {

	private String pathRoot;
	private String pathFileSolution;
	private EnumModels modelSelected;
	
	public Workbook(int priority, String name) {

		super(priority, name);
		this.setPathRoot(null);
		this.setPathFileSolution(null);
		this.setModelSelected(null);
		
	}
	
	public Workbook(int priority, String name, String pathRoot,
			String pathFileSolution, ObservableMap<String, Exercise> mapExercises, EnumModels modelSelected) {

		super(priority, name, mapExercises);
		this.setPathRoot(pathRoot);
		this.setPathFileSolution(pathFileSolution);
		this.setModelSelected(modelSelected);
		updateAverageResults();
		
	}

	public void updateAverageResults() {
		
		if (this.getMapData() == null)
			throw new NullPointerException("Null Exercise[]");
		
		if (this.getMapData().size() > 0) {
			
			long sum = 0;
			boolean corrects = true;
			
			for (Exercise exercise : mapData.values()) {
				sum += exercise.getLastRuntime();
				corrects = corrects && exercise.isResultCorrect();
			}
			
			this.setLastRuntime(sum);
			this.setResultCorrect(corrects);
			
		}
		
	}

	//Setters

	public void setPathRoot(String pathRoot) {
		this.pathRoot = pathRoot;
	}

	public void setPathFileSolution(String pathFileSolution) {
		this.pathFileSolution = pathFileSolution;
	}

	public void setModelSelected(EnumModels modelSelected) {
		this.modelSelected = modelSelected;
	}

	//Getters

	public String getPathRoot() {
		return pathRoot;
	}

	public String getPathFileSolution() {
		return pathFileSolution;
	}

	public EnumModels getModelSelected() {
		return modelSelected;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(this.getName());
		sb.append("\n");
		sb.append(this.getPathRoot());
		sb.append("\n");
		sb.append(this.getPathFileSolution());
		sb.append("\n");
		sb.append(this.getModelSelected());
		sb.append("\n");

		sb.append("{");
		
		this.getMapData().forEach(
				(index, exercise) -> sb.append(exercise.toString()));
		
		sb.append("};\n");

		return sb.toString();//super.toString();

	}

}

