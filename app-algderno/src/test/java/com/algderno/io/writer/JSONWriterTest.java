package com.algderno.io.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.io.WorkbookExample;
import com.algderno.io.reader.Reader;
import com.algderno.models.Workbook;

class JSONWriterTest {

	public Workbook example;
	private String path;

	@BeforeEach
	private void start() {

		try {

			this.path = new File(JSONWriterTest.class.getResource("")
					.toURI()).getParentFile().getAbsolutePath() + "\\output\\";

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.example = WorkbookExample.exampleComplete(this.path);
	}

	@Test
	void saveNull() {

		assertThrows(NullPointerException.class,
				() -> new JSONWriter(null).save(null));

	}

	@Test
	void saveComplete() {
		try {

			String correct = new JSONWriter(example).save(this.path);

			String current = new Reader(this.path + "/" + example.getName() + ".json"
					).readLines().get(0);//All in one line

			assertEquals(correct, current);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
