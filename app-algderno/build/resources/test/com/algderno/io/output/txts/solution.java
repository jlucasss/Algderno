package com.algderno.io.output.txts;

import java.util.Scanner;

public class solution {

	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {
			int input = in.nextInt();

			int output = input*2;

			System.out.println(output);
		} catch(Exception e) {

			e.printStackTrace();

		}

	}

}
