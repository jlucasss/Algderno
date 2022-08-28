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

public class LogException extends AbstractLog {

	private Exception lastException;
	private ResourceBundle resources;
	private static SimpleAlerts ALERTS = App.getAlerts();

	public LogException(ResourceBundle resources) {
		this.resources = resources;
	}

	@Override
	public AbstractLog add(String str) {

		list.addLast(str);

		this.lastException = null;

		return this;

	}

	public AbstractLog add(String str, Exception e) {

		this.lastException = e;

		str = str + "Error menssage:" + e.getMessage();

		list.addLast(str);

		return this;

	}

	@Override
	public void show() {

		ALERTS.errorWithException(
				this.getResource("exception.title"), 
				this.getResource("exception.subtitle"),
				this.getLast() + "\n" + this.getResource("exception.message"), 
				this.lastException);

	}

	private String getResource(String str) {

		if (this.resources == null)
			return str;

		return this.resources.getString(str);

	}

}

