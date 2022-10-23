package com.algderno.util.logger;

/**
*
* This class contains methods about exceptions logs.
*
* @author José Lucas dos Santos da Silva
*
*/

import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.util.SimpleAlerts;

import javafx.application.Platform;

public class LogException extends AbstractLog {

	private Throwable lastThrowable;
	private ResourceBundle resources;
	private static SimpleAlerts ALERTS = App.getAlerts();

	public LogException(ResourceBundle resources) {
		this.resources = resources;
	}

	@Override
	public AbstractLog add(String str) {

		list.addLast(new AtomicLog(currentDate(), str));

		this.lastThrowable = null;

		return this;

	}

	public AbstractLog add(String str, Throwable e) {

		this.lastThrowable = e;

		str = str + "Error menssage: " + e.getMessage();

		list.addLast(new AtomicLog(currentDate(), str));

		return this;

	}

	@Override
	public void show() {

		Platform.runLater(() -> {
				ALERTS.errorWithException(
					getResource("exception.title"), 
					getResource("exception.subtitle"),
					getLast() + "\n" + getResource("exception.message"), 
					lastThrowable);
		});
		
	}

	private String getResource(String str) {

		if (this.resources == null)
			return str;

		return this.resources.getString(str);

	}

}

