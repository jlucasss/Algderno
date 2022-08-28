package com.algderno.io.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.algderno.io.writer.Writer;

class ReaderTest {

	public Reader reader;

	@Test
	void inputNull() {

		try {

			Assertions.assertThrows(NullPointerException.class, () -> new Reader(null));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void inputEmpty() {

		try {

			Assertions.assertThrows(IllegalArgumentException.class, () -> new Reader(""));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void fileNoExist() {

		try {

			Assertions.assertThrows(FileNotFoundException.class,
					() -> new Reader("txts/file.txt"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void arquivoComTexto() {

		try {

			String path = new File(getClass().getResource("")
					.toURI()).getParentFile().getAbsolutePath() + "/output/txts/FileWithText.txt";

			new Writer(path).writeLines(Arrays.asList("Text"));

			reader = new Reader(path);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertEquals(Arrays.asList("Text"), reader.readLines());

	}

	@Test
	void fileEmpty() {

		try {

			String path = new File(getClass().getResource("")
					.toURI()).getParentFile().getAbsolutePath() + "/output/txts/FileEmpty.txt";

			new Writer(path).writeLines(Arrays.asList(""));

			reader = new Reader(path);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Assertions.assertEquals(Arrays.asList(), reader.readLines());

	}

}
