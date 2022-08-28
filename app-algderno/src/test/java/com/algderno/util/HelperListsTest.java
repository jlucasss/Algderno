package com.algderno.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelperListsTest {

	private String[] lista1, lista2;

	@BeforeEach
	public void inicializacao() {

		lista1 = new String[] {"1", "2", "3", "4"};
		lista2 = new String[] {"1", "2", "3"};

	}

	/* Method getIntersection */

	@Test
	void getIntersectionCorrect() {

		assertEquals(Arrays.asList("1", "2", "3").toString(),
				HelperLists.getIntersection(
						Arrays.asList(lista1),
						Arrays.asList(lista2) ).toString()
				);

	}

	@Test
	void getIntersectionCorrectInverted() {

		assertEquals(Arrays.asList("1", "2", "3").toString(),
				HelperLists.getIntersection(
						Arrays.asList(lista2),
						Arrays.asList(lista1) ).toString()
				);

	}

	/* Method getUnion */

	@Test
	void getUnionCorrect() {

		assertEquals(Arrays.asList("1", "2", "3", "4").toString(),
				HelperLists.getUnion(
						Arrays.asList(lista1),
						Arrays.asList(lista2) ).toString()
				);

	}

	/* Method getMinus */

	@Test
	void getMinusCorrect() {

		assertEquals(Arrays.asList("4").toString(),
				HelperLists.getMinus(
						Arrays.asList(lista1),
						Arrays.asList(lista2) ).toString()
				);

	}

}
