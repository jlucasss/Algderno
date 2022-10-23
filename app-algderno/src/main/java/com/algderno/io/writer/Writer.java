
package com.algderno.io.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * This class summarizes the main writing actions.
 *
 * @author José Lucas dos Santos da Silva
 *
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Writer {

	protected PrintWriter pw;

	public Writer(String pathFile) throws IOException {

		if (pathFile == null)
			throw new NullPointerException("The path cannot be null");
		if (pathFile == "")
			throw new IllegalArgumentException("The String of the path is empty!");

		File file = new File(pathFile);

		if (!file.exists()) {

			file.getParentFile().mkdirs();

			file.createNewFile();

		}

		if (!file.canWrite())
			file.setWritable(true);


		pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

	}

	public boolean writeLines(List<String> lines) {

		for (int i = 0; lines.size() > i; i++) {
			if (i != 0)
				pw.append("\n");

			pw.write(lines.get(i));

		}

		pw.flush();

		pw.close();

		return pw.checkError();

	}

	public static boolean createDiretory(String path) {

		return new File(path).mkdirs();

	}

	public static boolean createFile(String path) throws IOException {

		return new File(path).createNewFile();

	}

}

