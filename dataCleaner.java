import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class dataCleaner {
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

	public static void appendFile(String filename, ArrayList<String> list) {

		try {
			FileWriter fw = new FileWriter(filename, false); // the true will append the new data
			for (String s : list) {
				// s.replaceAll(",*", "\\.*");

				// if (s.contains(","))
				// System.exit(0);
				// System.out.println(s);
				fw.write(s + "\n");// appends the string to the file
			}
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static void runMe(String args) {

		// TODO Auto-generated method stub
		int[] alpha = new int[26];
		ArrayList<String> lets = countLetters(alpha, args);
		ArrayList<ArrayList<String>> letterlines = new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			// System.out.println((char) ('A' + i) + " " + alpha[i]);
			letterlines.add(new ArrayList<String>());
		}

		Collections.sort(lets);
		for (String a : lets) {
			// System.out.println((int) (a.charAt(0) - 'A'));
			letterlines.get((int) (a.charAt(0) - 'A')).add(a);
		}

		for (int i = 0; i < 26; i++) {
			Collections.shuffle(letterlines.get(i));
		}
		/*
		 * for (int i = 0; i < letterlines.get(0).size(); i++) {
		 * System.out.println(letterlines.get(0).get(i)); } System.exit(0);
		 */
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
		appendFile("train.txt", train);
		appendFile("test.txt", test);
	}

}
