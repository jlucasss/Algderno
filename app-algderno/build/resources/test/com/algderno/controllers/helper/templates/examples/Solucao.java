package com.algderno.controllers.helper.templates.examples;


import java.util.Scanner;

public class Solucao {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		int entrada = in.nextInt();

		System.out.println(entrada*2);

	}

}

