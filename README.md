# Algderno

![Logo](app-algderno/src/main/resources/com/algderno/images/logo.svg)

Algderno is an application with a graphical interface that aims to facilitate the testing of a solution for different types of input where each input has a correct expected output.

It follows the idea of `Workbook`, where a `Workbook` contains a `List of Exercises` and an `Exercise` contains a `List of Questions`, however, for all questions, it only contains a solution file.

Note: This project is in an early version, although it works, there may be a better way to do it.

To learn more, visit page: [algderno.github.io](https://)

## Installation

Donwload [file](https://), install and run.

## Usage

The application has the functions of:
- Create/Open/Edit/Save `Workbook`;
- Create/Open/Edit/Save/Delete `Question`(ie your input files, expected correct output and current output);
- Test `Question`s from a text file as input, verify that the output produced by the solution is equal to the content of the file that has correct expected output;

Note:
- To create an `Exercise`, create a `Question` and save it with the name of a non-existent `Exercise`;
- If an `Exercise` has no `Question`s, it is automatically deleted;
- 

Simple example:

Organizing the Workbook folder like this:

```
Workbook/
-- Exercise1/
---- Question1.in
---- Question1.sol
Solution.java
```

Where the files have as content:

`Question1.in` (input):

```
10
```

`Question1.sol` (expected correct output):

```
20
```

`Solution.java` (solution file):

```java
import java.util.Scanner;

public class Solution {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		int n = in.nextInt();

		System.out.println(n*2);

	}

}
```
Note: the input must be `System.in` and output must be `System.out.print...` .

## Roadmap
Ideas for releases in the future:
- Support for other programming languages;
- Appearance editing (which has just started);

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[GNU GPLv3.0](https://choosealicense.com/licenses/gpl-3.0/)
