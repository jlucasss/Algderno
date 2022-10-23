package com.algderno.models.util;

import javafx.beans.property.SimpleStringProperty;

public class InformationModel {

	private final SimpleStringProperty description, value;
	
	public InformationModel(String description, String value) {
		
		this.description = new SimpleStringProperty(description);
		this.value = new SimpleStringProperty(value);
		
	}

	/* Getters */
	
	public String getDescription() {
		return this.description.get();
	}
	
	public String getValue() {
		return this.value.get();
	}
	
	/* Setters */

	public void setDescription(String description) {
		this.description.set(description);
	}
	
	public void setValue(String value) {
		this.value.set(value);
	}
	
	/* Properties */
	
	public SimpleStringProperty descriptionProperty() {
		return this.description;
	}
	
	public SimpleStringProperty valueProperty() {
		return this.value;
	}
	
}
