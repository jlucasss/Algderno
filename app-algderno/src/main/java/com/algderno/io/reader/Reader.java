
package com.algderno.io.reader;

/**
 *
 * This class summarizes the main reading actions.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	protected BufferedReader br;
	private File file;

	public Reader(String pathDefault) throws IOException {

		if (pathDefault == null)
			throw new NullPointerException("The path cannot be null");
		if (pathDefault == "")
			throw new IllegalArgumentException("The String of the path is empty!");

		file = new File(pathDefault);

		if (!file.exists())
			throw new FileNotFoundException("The file \""+ pathDefault + "\" not found.");

		if (!file.canRead())
			file.setReadable(true);

		br = new BufferedReader(new FileReader(file));

	}

	public List<String> readLines() {

		List<String> lines = new ArrayList<>();

		String lineCurrent = null;

		try {

			while (br.ready()) {

				lineCurrent = br.readLine();

				if (lineCurrent != null)
					lines.add(lineCurrent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines;

	}

	public int numFiles() {
		return file.list().length;
	}

	public static int numFiles(String path) {
		return new File(path).list().length;
	}

}

