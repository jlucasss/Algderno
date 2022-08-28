package com.algderno.util.logger;

/**
 *
 * This class contains methods about error logs.
 * 
 * It calls a "ResourceBundle" object from "App" to get the general messages, 
 * 	but the specific ones are provided by each class when using the "add" method of each "AbstractLog"/"LogException".
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.algderno.App;
import com.algderno.util.SimpleAlerts;

import javafx.scene.control.Alert.AlertType;

public class SimpleLogger {

	private AbstractLog info, warning, error;
	private LogException exception;
	private ResourceBundle resources;
	private SimpleAlerts ALERTS;

	public SimpleLogger() {

		ALERTS = App.getAlerts();
		resources = App.getMessagesBundle();

		this.info = createInfo();
		this.warning = createWarning();
		this.error = createError();
		this.exception = new LogException(resources);

	}

	/* Info */

	private AbstractLog createInfo() {
		return new AbstractLog() {

			@Override
			public void show() {

				ALERTS.simpleMessage(getResource("info.title"), 
						getResource("info.subtitle"),
						this.getLast(), 
						AlertType.INFORMATION);	

			}

		};
	}

	public AbstractLog getInfos() {
		return this.info;
	}

	/* Warning */

	private AbstractLog createWarning() {
		return new AbstractLog() {

			@Override
			public void show() {

				ALERTS.simpleMessage(getResource("warning.title"), 
						getResource("warning.subtitle"),
						this.getLast(), 
						AlertType.WARNING);

			}

		};
	}

	public AbstractLog getWarnings() {
		return this.warning;
	}

	/* Error */

	private AbstractLog createError() {
		return new AbstractLog() {

			@Override
			public void show() {

				ALERTS.simpleMessage(getResource("error.title"), 
						getResource("error.subtitle"),
						this.getLast(), 
						AlertType.ERROR);	

			}

		};
	}

	public AbstractLog getErrors() {
		return this.error;
	}

	/* Excepiton */

	public LogException getExceptions() {
		return this.exception;
	}

	/* All */

	/*
		Maybe...


	private AbstractLog createAbstraction(String typeName, AlertType alertType) {
		return new AbstractLog() {

			@Override
			public void show() {

				simpleMessage(getResource(typeName + ".title"), 
						getResource(typeName + ".subtitle"),
						this.getLast(), 
						alertType);	

			}

		};
	}*/

	private String getResource(String str) {

		if (resources == null)
			return str;

		return resources.getString(str);

	}

	public List<String> getAndCleanAll() {

		List<String> copy = new ArrayList<>();

		copy.add("Infos: ");
		copy.addAll( Arrays.asList(info.getAndClearAll()) );

		copy.add("\nWarnings: ");
		copy.addAll( Arrays.asList(warning.getAndClearAll()) );

		copy.add("\nErrors:");
		copy.addAll( Arrays.asList(error.getAndClearAll()) );

		copy.add("\nExceptions:");
		copy.addAll( Arrays.asList(exception.getAndClearAll()) );

		return copy;

	}

}

