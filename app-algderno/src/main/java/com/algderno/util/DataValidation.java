package com.algderno.util;

/**
 *
 * This class contains methods about data validation.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.nio.file.Paths;

public class DataValidation {

	public DataValidation() {
	}

	public static boolean validStringsContent(String[] data) {

		if (data == null)
			return false;

		for (String element : data)
			validStringContent(element);

		return true;

	}

	public static boolean validStringContent(String data) {

		if (data == null)
			return false;
		else if (data.trim().isEmpty())
			return false;

		return true;

	}

	public static boolean validPaths(String[] paths) {

		for (String path : paths) {

			try {

				Paths.get(path);

			} catch (Exception e) {
				return false;
			}

		}

		return true;

	}


}
