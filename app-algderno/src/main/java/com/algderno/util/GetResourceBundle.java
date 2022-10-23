package com.algderno.util;

/**
 *
 * This class contains methods about ResourceBundle.
 *
 * @author Jos� Lucas dos Santos da Silva
 *
 */

import java.util.ResourceBundle;

import com.algderno.App;

public class GetResourceBundle {

	public GetResourceBundle() {
	}

	public static ResourceBundle getBundle(String name) {
		return ResourceBundle.getBundle("bundle." + App.getLocale().toString().toLowerCase() + "." + name, App.getLocale());
	}

}
