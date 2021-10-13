import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		for (int i = 0; i < tpj.size(); i++) {
			sum += (tpj.get(i) - opj.get(i)) * (tpj.get(i) - opj.get(i));
		}
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
			while (myReader.hasNextLine()) {
				cnt++;
				String data = myReader.next();
				// System.out.println(data);
				if (cnt == 2) {
					list.add(data);
					cnt = 0;
				}

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
				for (int i = 0; i < in; i++)
					inputs.get(i)[cnt] = myReader.nextInt() * 1.0;
				for (int i = 0; i < out; i++)
					outputs.get(i)[cnt] = myReader.nextInt() * 1.0;
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
		if(python){
		String command = "python " + script + " " + filename + " ";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("Python or pyplot aren't installed on this system.\nNo graphs will be autogenerated.\nCheck files errors.txt and successrate.txt");
			python = false;
		}
	}
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
		if (tpj.size() != opj.size())
			System.out.println("SOMETHINGS WRONG");
		int corrects = 0;
		for (int i = 0; i < tpj.size(); i++) {
			if (tpj.get(i) == 0 && opj.get(i) <= 0.1)
				corrects++;
			else if (tpj.get(i) == 1 && opj.get(i) >= 0.9)
				corrects++;
		}
		return corrects == tpj.size();
	}

}
