
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.StringTokenizer;

public class solucao {

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

	public static class FastReader {

		BufferedReader br;
		StringTokenizer st;

		public FastReader() {

			br = new BufferedReader(new InputStreamReader(System.in));

		}

		public String next() {


			while (st == null || !st.hasMoreElements()) {

				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			return st.nextToken();

		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		String line;

		public String nextLine() {

			line = "";

			try {
				line = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return line;

		}

	}

	public static int L, C;

	public static int[][] matriz;

	public static int max(int a, int b) {
		return a < b ? b : a;
	}

	public static int maxRect(int[] p, int len) {

		int i;
		int ptr = 0;
		int ret = 0;
		int[] h = new int[1000];
		int[] s = new int[1000];

		for (i = 0; i < len; ++i) {

			int l = i;

			while (ptr > 0 && p[i] < h[ptr-1]) {

				ret = max(ret, (i-s[ptr-1]+1) * (h[ptr-1]+1));
				l = s[ptr-1];
				--ptr;

			}

			h[ptr] = p[i];
			s[ptr++] = l;

		}

		while (ptr > 0) {

			ret = max(ret, (len-s[ptr-1]+1)*(h[ptr-1]+1));
			--ptr;

		}

		return ret;

	}

	public static void main(String[] args) {



		FastReader in = new FastReader();

		L = in.nextInt();

		C = in.nextInt();

		matriz = new int[L][C];

		for (int i = 0; L > i; i++)
			for (int j = 0; C > j; j++)
				matriz[i][j] = in.nextInt();



		int sol = 0;
		int[] hist = new int[1000];

		for (int i = 0; L-1 > i; ++i) {

			for (int j = 0; C-1 > j; ++j) {

				if (matriz[i][j]+matriz[i+1][j+1] <= matriz[i][j+1] + matriz[i+1][j])
					++hist[j];
				else
					hist[j] = 0;
			}

			sol = max(sol, maxRect(hist, C-1));

		}


		System.out.printf("%d", sol);

	}

}
