package com.algderno.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public abstract class Group<T>  implements Comparable<Group<T>> {

	private final SimpleIntegerProperty priority;
	protected final SimpleStringProperty name;
	private final SimpleLongProperty lastRuntime;
	private final SimpleBooleanProperty resultCorrect;

	protected ObservableMap<String, T> mapData;
	
	public Group(int priority, String name) {

		this.priority = new SimpleIntegerProperty(priority);
		this.name = new SimpleStringProperty(name);
		this.lastRuntime = new SimpleLongProperty(0);
		this.resultCorrect = new SimpleBooleanProperty(false);
		mapData = FXCollections.observableHashMap();
		
	}

	public Group(int priority, String name, ObservableMap<String, T> mapData) {

		this.priority = new SimpleIntegerProperty(priority);
		this.name = new SimpleStringProperty(name);
		this.lastRuntime = new SimpleLongProperty(0);
		this.resultCorrect = new SimpleBooleanProperty(false);
		this.mapData = mapData;
		
	}
	
	// Getters and Setters
	
	public void setPriority(int priority) {
		this.priority.set(priority);
	}
	
	public void setName(String name) {
		this.name.set(name);
	}

	public void setLastRuntime(long lastRuntime) {
		this.lastRuntime.set(lastRuntime);
	}

	public void setResultCorrect(boolean resultCorrect) {
		this.resultCorrect.set(resultCorrect);
	}
	
	public int getPriority() {
		return priority.get();
	}
	
	public SimpleIntegerProperty priorityProperty() {
		return priority;
	}
	
	public String getName() {
		return name.get();
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public long getLastRuntime() {
		return lastRuntime.get();
	}
	
	public SimpleLongProperty lastRuntimeProperty() {
		return lastRuntime;
	}

	public boolean isResultCorrect() {
		return resultCorrect.get();
	}

	public SimpleBooleanProperty resultCorrectProperty() {
		return resultCorrect;
	}
	
	public ObservableMap<String, T> getMapData() {
		return mapData;
	}

	public void setMapData(ObservableMap<String, T> mapData) {
		this.mapData = mapData;
	}
	
	@Override
	public int compareTo(Group<T> o) {
		return o.getName().compareTo(getName());
	}

}
