package com.algderno.io.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.io.WorkbookExample;
import com.algderno.models.Workbook;

class JSONReaderTest {

	public Workbook example;
	private String path;

	@BeforeEach
	private void start() {

		try {

			this.path = new File(JSONReaderTest.class.getResource("")
					.toURI()).getParentFile().getAbsolutePath() + "\\output\\";

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.example = WorkbookExample.exampleComplete(this.path);
	}

	/* Method getWorkbook */

	@Test
	void saveNull() {

		assertThrows(NullPointerException.class,
				() -> new JSONReader(null).getWorkbook());

	}

	@Test
	void getWorkbookComplete() {
		try {

			String current = new Reader(this.path + "/" + example.getName() + ".json"
					).readLines().get(0);//All in one line

			assertEquals(example.toString(),
					new JSONReader(current).getWorkbook()
					.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
