import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class is used as a pocket knife in our project.
 * 
 * Here we have the implementation of every tools we need such as the sigmoid
 * function, weight generation or file IO handling
 * 
 * @author Michail - Panagiotis Bofos
 *
 */
public class Tools {
	static int counter = 0;
	static boolean python = true;
	static double[] w = { 0.1, 0.2, 0.4, 0.5, 0.9, 0.3, 0.6, 0.7, 0.8 };

	/**
	 * This function emulates the sigmoid function.
	 * 
	 * f(x) = 1 / ( 1 + e^(-a*x))
	 * 
	 * @param x double variable
	 * @return double value between zero and one
	 */
	public static double sigmoid(double x) {
		return (1 / (1 + Math.exp(-1.0 * x)));

	}

	/**
	 * This function rounds up a double using custom formula.
	 * 
	 * @param x Double number
	 * @return zero or one
	 */
	public static double customRound(double x) {
		if (x >= 0.9)
			return 1.0;
		return 0.0;

	}

	/**
	 * This function generates random decimal numbers between minus one and one
	 * excluding zero.
	 * 
	 * @return double [-1,1] - {0}
	 */
	public static double randomWeight() {

		int max = 1, min = -1;
		double ran = 0.0;
		do {
			ran = Math.random() * (max - min) + min;
		} while (ran == 0.0);

		return ran;

	}

	/**
	 * This function generates specific numbers used in order to check the neural
	 * network.
	 * 
	 * @return double numbers
	 */
	public static double demoWeightsTwoTwoOne() {
		return w[counter++];

	}

	/**
	 * This function calculates the error of an output.
	 * 
	 * (0.5*Ó(target - real)^2)
	 * 
	 * @param tpj ArrayList(Double) target outputs
	 * @param opj ArrayList(Double) real outputs
	 * @return double value - error
	 */
	public static double error(ArrayList<Double> tpj, ArrayList<Double> opj) {
		double sum = 0;
		for (int i = 0; i < tpj.size(); i++)
			sum += (tpj.get(i) - opj.get(i)) * (tpj.get(i) - opj.get(i));

		return 0.5 * sum;

	}

	/**
	 * This function reads a parameter file and fills an arraylist with the data.
	 * 
	 * @param filename parameters file
	 * @return ArrayList(String) containing the file data
	 */
	public static ArrayList<String> getParameters(String filename) {
		ArrayList<String> list = new ArrayList<>();
		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			int cnt = 0;
			while (list.size() < findLines(filename) && myReader.hasNextLine()) {
				cnt++;
				String data = myReader.next();
				if (cnt == 2) {
					list.add(data);
					cnt = 0;
				}

				// System.out.println(data);

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * This function reads a file and fills two arraylists (of double arrays) with
	 * the training/testing data.
	 * 
	 * @param filename training or testing data file
	 * @param inputs   ArrayList(double[])
	 * @param outputs  ArrayList(double[])
	 */
	public static void fillData(String filename, ArrayList<double[]> inputs, ArrayList<double[]> outputs) {
		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			int cnt = 0;
			int in = inputs.size(), out = outputs.size();
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				StringTokenizer tok = new StringTokenizer(line);
				String letter = tok.nextToken();
				double[] outs = createExpectedOutputArray(letter.charAt(0));

				for (int i = 0; i < out; i++)
					outputs.get(i)[cnt] = outs[i];

				for (int i = 0; i < in; i++)
					inputs.get(i)[cnt] = Double.parseDouble(tok.nextToken());
				cnt++;

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	/**
	 * This function counts the lines of a file.
	 * 
	 * @param filename file we count the lines
	 * @return # of lines
	 */
	public static int findLines(String filename) {
		int cnt = 0;
		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				cnt++;
				myReader.nextLine();

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * This function creates a text file given an arraylist of strings.
	 * 
	 * @param name String the name of the new file
	 * @param in   Arraylist of string file lines
	 */
	public static void feedFile(String name, ArrayList<String> in) {

		try {
			FileWriter myWriter = new FileWriter(name);
			for (String a : in) {
				myWriter.write(a);
				myWriter.write("\n");
			}
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * This function runs a Python script to generate a plot using a text file
	 * 
	 * NOTE: For this function to run successfully your machine ought to have python
	 * and matplot library installed
	 * 
	 * @param filename new name of the source data file
	 */
	public static void runPython(String script, String filename) {
		if (python) {
			String command = "python " + script + " " + filename + " ";
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				System.out.println(
						"Python or pyplot aren't installed on this system.\nNo graphs will be autogenerated.\nCheck files errors.txt and successrate.txt");
				python = false;
			}
		}
	}

	/**
	 * This function creates an array than represents the output we expect from a
	 * neural network in order to categorize letters.
	 * 
	 * @param letter The character we want to recognize
	 * @return Double array (binary logic)
	 */
	public static double[] createExpectedOutputArray(char letter) {
		double[] dump = new double[26];
		for (int i = 0; i < 26; i++)
			dump[i] = 0;
		dump[(int) (letter - 'A')] = 1;
		return dump;
	}

	/**
	 * This function decodes a double array to a character.
	 * 
	 * 10000000000000000000000000 = 'A' 01000000000000000000000000 = 'B' and so on
	 * 
	 * @param array Binary letter form
	 * @return Decoded character
	 */
	public static char createLetterFromArray(double[] array) {
		char letter = ' ';
		for (int i = 0; i < 26; i++) {
			if (array[i] == 1)
				return (char) ('A' + i);
		}
		return letter;
	}

	/**
	 * This function finds the cell within an array that has the highest value, let
	 * it be the i-th cell. Then it returns an array with all the cells equal to
	 * zero except the i-th which is equal to one.
	 * 
	 * @param output Double array we scan
	 * @return Binary array we return
	 */
	public static double[] findPeak(double[] output) {
		double[] maxer = new double[output.length];
		int max_place = 0;
		double max_value = 0;
		for (int i = 0; i < output.length; i++) {
			if (output[i] >= max_value) {
				max_place = i;
				max_value = output[i];
			}
		}
		maxer[max_place] = 1;
		return maxer;
	}

	/**
	 * This function is used to check if two arraylist are the same. NOTE: because
	 * we use double numbers we use math.round to check their equality.
	 * 
	 * @param tpj Double vector one
	 * @param opj Double vector two
	 * @return True if the vectors are equal, otherwise false
	 */
	public static boolean correct(ArrayList<Double> tpj, ArrayList<Double> opj) {
		if (tpj.size() != opj.size()) {
			System.out.println("SOMETHING'S WRONG");
			System.exit(1);
		}

		double[] real = new double[opj.size()];
		double[] target = new double[tpj.size()];
		for (int i = 0; i < opj.size(); i++) {
			real[i] = opj.get(i);
			target[i] = tpj.get(i);
		}
		real = findPeak(real);
		for (int i = 0; i < tpj.size(); i++)
			if (real[i] != target[i])
				return false;

		return true;
	}

	/**
	 * This function counts all the appearances of each letter and returns all the
	 * lines in an arraylist.
	 * 
	 * @param alpha    Integer array that contains the appearances of each letter
	 * @param filename The name of the file we use
	 * @return Arraylist of strings - each line is a member of the arraylist
	 */
	public static ArrayList<String> countLetters(int[] alpha, String filename) {
		int cnt = 0;
		ArrayList<String> lets = new ArrayList<>();
		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				cnt++;
				lets.add(myReader.nextLine());

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		for (String a : lets)
			alpha[a.charAt(0) - 'A']++;

		return lets;
	}

	/**
	 * This function is used to write a few lines to a file.
	 * 
	 * @param filename The name of the file in which we want to write
	 * @param list     The String lines we print to the file
	 */
	public static void writeFile(String filename, ArrayList<String> list) {

		try {
			FileWriter fw = new FileWriter(filename, false);
			for (String s : list) {
				fw.write(s + "\n");// appends the string to the file
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	/**
	 * This function creates the training and testing set of the letter recognition
	 * problem. First we count all the letters used ( A 700 B 720 ...) and then we
	 * add 70% of each letter to the training set and the rest 30% to the testing
	 * set (After we shuffle our sets a hundred times in order to create a set we
	 * can use without it becoming biased).
	 * 
	 * @param args The name of the file which contains all of your (Normalized)
	 *             data.
	 */
	public static void createTrainAndTestSets(String args) {
		int[] alpha = new int[26];
		ArrayList<String> lets = countLetters(alpha, args);
		ArrayList<ArrayList<String>> letterlines = new ArrayList<>();
		for (int i = 0; i < 26; i++)
			letterlines.add(new ArrayList<String>());

		Collections.sort(lets);
		for (String a : lets)
			letterlines.get((int) (a.charAt(0) - 'A')).add(a);

		for (int i = 0; i < 26; i++) {
			Collections.shuffle(letterlines.get(i));
		}

		ArrayList<String> train = new ArrayList<>();
		ArrayList<String> test = new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			int j;
			for (j = 0; j < letterlines.get(i).size() * 0.7; j++)
				train.add(letterlines.get(i).get(j));
			for (; j < letterlines.get(i).size(); j++)
				test.add(letterlines.get(i).get(j));
		}
		for (int i = 0; i < 100; i++) {
			Collections.shuffle(train);
			Collections.shuffle(test);
		}
		writeFile("train.txt", train);
		writeFile("test.txt", test);
	}
}
