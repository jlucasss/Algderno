package com.algderno.io.writer;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.io.reader.Reader;

class WriterTest {

	public Writer writer;
	private String path;

	@BeforeEach
	void inicializa() {

		try {

			this.path = new File(WriterTest.class.getResource("")
					.toURI()).getParentFile().getAbsolutePath() + "/output/txts/";

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void inputNull() {

		try {

			Assertions.assertThrows(NullPointerException.class, () -> new Writer(null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void inputEmpty() {

		try {

			Assertions.assertThrows(IllegalArgumentException.class, () -> new Writer(""));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void createFile() {

		try {

			Writer.createFile(path+"file-created.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertTrue(new File(path+"file-created.txt").exists());

	}

	@Test
	void createDirectory() {

		try {

			Writer.createDiretory(path+"newDirectory/");

		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertTrue(new File(path+"newDirectory/").exists());

	}

	@Test
	void writeFileExistent() {

		try {

			writer = new Writer(path+"FileWithText.txt");

			writer.writeLines(Arrays.asList("Text"));

			Assertions.assertEquals(
						new Reader(path+"FileWithText.txt").readLines(), Arrays.asList("Text"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void writeFileNoexistent() {

		try {

			writer = new Writer(path+"FileWithTextNoexistent.txt");

			writer.writeLines(Arrays.asList("Text"));

			Assertions.assertEquals(
						new Reader(path+"FileWithTextNoexistent.txt").readLines(), Arrays.asList("Text"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
