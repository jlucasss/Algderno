package com.algderno.execution.resources.example;

import java.util.Scanner;

public class Hello {

	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {

			String phrase = in.nextLine();
			String word = in.nextLine();
			int number = in.nextInt();

			System.out.println(phrase);
			System.out.print(word);
			System.out.printf("%d",  number);

		} catch(Exception e) {

			e.printStackTrace();

		}

	}

}

