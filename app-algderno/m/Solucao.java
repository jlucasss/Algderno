
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;

public class Solucao {

	public static PrintStream main2(String input, String output) {
		PrintStream fileOutput = null;
		try {
			FileInputStream fileInput = new FileInputStream(input);
			fileOutput = new PrintStream(new FileOutputStream(output));
			System.setIn(fileInput);
			System.setOut(fileOutput);
			main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileOutput;
	}

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		int entrada = in.nextInt();

		System.out.println(entrada*2);

	}

}
