
package com.algderno.models;

import javafx.collections.FXCollections;

public class Question extends Group<String> {
	
	private long maxRuntime;
	
	public Question(int priority, String name, long lastRuntime, boolean resultCorrect, long maxRuntime) {

		super(priority, name);
		this.maxRuntime = maxRuntime;
		
	}

	public Question(int priority, String name, String pathInput, String pathOutputCorrect,
			long lastRuntime, boolean resultCorrect, long maxRuntime) {

		super(priority, name);

		this.setLastRuntime(lastRuntime);
		this.setResultCorrect(resultCorrect);
		this.maxRuntime = maxRuntime;
		
		this.mapData = FXCollections.observableHashMap();
		this.mapData.put("pathInput", pathInput);
		this.mapData.put("pathOutputCorrect", pathOutputCorrect);
		
	}

	public void setMaxRuntime(long maxRuntime) {
		this.maxRuntime = maxRuntime;
	}
	
	public long getMaxRuntime() {
		return maxRuntime;
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("\n(");
		sb.append(this.getPriority());
		sb.append(",");
		sb.append(this.mapData.get("pathInput"));
		sb.append(",");
		sb.append(this.mapData.get("pathOutputCorrect"));
		sb.append(",");
		sb.append(this.getName());
		sb.append(",");
		sb.append(this.isResultCorrect());
		sb.append(",");
		sb.append(this.getLastRuntime());
		sb.append(",");
		sb.append(this.getMaxRuntime());
		sb.append(");\n");

		return sb.toString();//super.toString();

	}

}

