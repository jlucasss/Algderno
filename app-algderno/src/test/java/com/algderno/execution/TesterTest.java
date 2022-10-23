package com.algderno.execution;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.algderno.models.Question;

class TesterTest {

	private String path = null;

	@BeforeEach
	void start() {
		try {

			path = new File(getClass().getResource("resources/example/")
					.toURI()).getAbsolutePath()+"\\";

		} catch (Exception e) {
			e.printStackTrace();
		}

		//It is necessary that all paths have this slash "/"
		path = path.replaceAll("\\\\", "/");

	}

	@Test
	void inputEmpty() {

		String pathInput = "",
				pathFileSolution = "",
				pathOutputCorrect = "";

		assertThrows(IllegalArgumentException.class, () -> {

			Tester t = new Tester();
			t.prepare(pathOutputCorrect, pathFileSolution, pathInput);
			t.submit(null);
			
		});

	}

	@Test
	void inputComplete() {

		String pathInput = path,
				pathFileSolution = path + "Hello.java",
				pathOutputCorrect = path;

		pathFileSolution = pathFileSolution.replaceAll("bin/test", "src/test/resources");

		Question question =
				new Question(1, "Hello", "1.in", "1.sol", 0, false, 1000000);

		try {

			Tester t = new Tester();
			t.prepare(pathOutputCorrect, pathFileSolution, pathInput);
			t.submit(question);
			
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}

		assertTrue(question.isResultCorrect());

	}

}
