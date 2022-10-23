package com.algderno.util;

/**
 *
 * This class contains methods about editing files.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

public class DataFiles {

	public DataFiles() {
	}

	public static String[] splitNameAndExtension(String file) {

		String name = file;
		String extension = null;

		if (file.contains(".")) {

			name = file.substring(0, file.indexOf("."));
			extension = file.substring(file.indexOf(".")+1);

		}

		return new String[]{name, extension};

	}

}
