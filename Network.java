import java.util.ArrayList;

/**
 * This class is the driver class of the project. Here we create the whole
 * neural network and implement the back propagation algorithm.
 * 
 * @author Michail - Panagiotis Bofos
 *
 */
public class Network {
	/**
	 * Variables used to determine the number of the neurons of each layer.
	 */
	static int INPUT_LAYER = 2, SECOND_LAYER = 1, THIRD_LAYER = 1, OUTPUT_LAYER = 1, EPOCH_LIMIT = 100;
	/**
	 * The size of the training data (#lines).
	 */
	static int train_size;
	/**
	 * Rate and Momentum of the network.
	 */
	static double R, M;
	/**
	 * This arrays contain the neurons of each layer.
	 */
	static Neuron[] first, second, third, fourth;
	/**
	 * Flags used to determine the topology of the network and enable/disable the
	 * debug mode.
	 */
	static boolean inUse1, inUse2, debug = false;
	/**
	 * The names of the training and test input files.
	 */
	static String train, test;
	/**
	 * The connection list between the input layer and the first hidden one.
	 */
	static ArrayList<Connection> input = new ArrayList<Connection>();
	/**
	 * The connection list between the first hidden layer and the second hidden
	 * layer.
	 */
	static ArrayList<Connection> inside = new ArrayList<Connection>();
	/**
	 * The connection list between the last hidden layer and the output layer.
	 */
	static ArrayList<Connection> output = new ArrayList<Connection>();
	/**
	 * The input values of the training data. Each array in the arraylist is an
	 * input variable, each line in those arrays is a data line.
	 */
	static ArrayList<double[]> INPUTS = new ArrayList<>();
	/**
	 * The input values of the training data. Each array in the arraylist is an
	 * output variable, each line in those arrays is a data line.
	 */
	static ArrayList<double[]> OUTPUTS = new ArrayList<>();
	/**
	 * The input values of the testing data. Each array in the arraylist is an input
	 * variable, each line in those arrays is a data line.
	 */
	static ArrayList<double[]> TEST_INPUTS = new ArrayList<>();
	/**
	 * The input values of the testing data. Each array in the arraylist is an
	 * output variable, each line in those arrays is a data line.
	 */
	static ArrayList<double[]> TEST_OUTPUTS = new ArrayList<>();

	/**
	 * This function calculates the input generated by the scalar product of the
	 * inputs and the weights. We only need the target neuron (the neuron of whom we
	 * want to calculate the input) and the incoming layer connections.
	 * 
	 * @param x     Neuron The neuron of whom we want to calculate the input
	 * @param layer ArrayList(Connection) the list of connection coming in to x
	 *              neuron
	 * @return Double the calculated input
	 */
	public static double calculateInput(Neuron x, ArrayList<Connection> layer) {
		double temp = 0;
		for (Connection a : layer)
			if (a.getNeuron2().equals(x)) {
				if (debug) {
					a.getNeuron1().printNeural();
					System.out.println(a.getWeight() + " * " + a.getNeuron1().getInput());
				}
				temp += a.getWeight() * a.getNeuron1().getInput();
			}
		if (debug) {
			System.out.println("Finally: " + Tools.sigmoid(temp) + " Temp: " + temp);
			System.out.println("====================================================================");
		}
		return Tools.sigmoid(temp);

	}

	/**
	 * This function is mandatory to the initialization of the neural network. For
	 * input layer and each of the hidden ones we add an extra neuron called the
	 * bias neuron. The bias neuron has no input connections and has an input value
	 * of one.
	 */
	public static void initNetwork() {
		for (int i = 0; i < INPUT_LAYER; i++) {
			first[i] = new Neuron();
			first[i].setIsInput(true);
		}

		first[INPUT_LAYER - 1].setInput(1);
		first[INPUT_LAYER - 1].setBias(true);
		if (SECOND_LAYER > 0) {
			for (int i = 0; i < SECOND_LAYER; i++) {
				second[i] = new Neuron();
				second[i].changeInner();
			}
			second[SECOND_LAYER - 1].setInput(1);
			second[SECOND_LAYER - 1].setBias(true);
		}
		if (THIRD_LAYER > 0) {
			for (int i = 0; i < THIRD_LAYER; i++) {
				third[i] = new Neuron();
				third[i].changeInner();
			}
			third[THIRD_LAYER - 1].setInput(1);
			third[THIRD_LAYER - 1].setBias(true);
		}
		for (int i = 0; i < OUTPUT_LAYER; i++) {
			fourth[i] = new Neuron();
			fourth[i].setOutput(true);
		}

	}

	/**
	 * This function prints every neuron in the network.
	 */
	public static void printNetwork() {
		System.out.println("====================================================================");
		for (Connection a : input)
			a.details();
		System.out.println("====================================================================");
		for (Connection a : inside)
			a.details();
		System.out.println("====================================================================");
		for (Connection a : output)
			a.details();
		System.out.println("====================================================================");
	}

	/**
	 * This function emulates one step of the algorithm.(Basically the input changes
	 * which generate in the forward passing of the algorithm.
	 * 
	 * @param A Neuron array the input neurons
	 * @param x ArrayList(Connection) the connection of each steps layer
	 */
	public static void step(Neuron A[], ArrayList<Connection> x) {
		for (Neuron a : A)
			if (!a.isBias()) {
				if (debug)
					System.out.println("Inputing: " + a.getID());
				a.input = calculateInput(a, x);
			}
	}

	/**
	 * This function creates the topology of the network. Each layer one neuron is
	 * connected to each layer two neuron (EXCEPT BIAS NEURONS AS PREVIOUSLY
	 * STATED). Each weight is initialized to a random value.
	 * 
	 */
	public static void connectAll() {
		// Connect everyone with everyone
		for (int i = 0; i < INPUT_LAYER; i++)
			for (int j = 0; j < SECOND_LAYER - 1; j++)
				input.add(new Connection(first[i], second[j], Tools.randomWeight()));

		if (inUse2) {
			for (int i = 0; i < SECOND_LAYER; i++)
				for (int j = 0; j < THIRD_LAYER - 1; j++)
					inside.add(new Connection(second[i], third[j], Tools.randomWeight()));

			for (int i = 0; i < THIRD_LAYER; i++)
				for (int j = 0; j < OUTPUT_LAYER; j++)
					output.add(new Connection(third[i], fourth[j], Tools.randomWeight()));
		} else {
			for (int i = 0; i < SECOND_LAYER; i++)
				for (int j = 0; j < OUTPUT_LAYER; j++)
					output.add(new Connection(second[i], fourth[j], Tools.randomWeight()));
		}

	}

	/**
	 * This function prints the details of each neuron.
	 */
	public static void printStats() {
		for (int i = 0; i < INPUT_LAYER; i++)
			first[i].printNeural();
		for (int i = 0; i < SECOND_LAYER; i++)
			second[i].printNeural();
		for (int i = 0; i < THIRD_LAYER; i++)
			third[i].printNeural();
		for (int i = 0; i < OUTPUT_LAYER; i++)
			fourth[i].printNeural();
	}

	/**
	 * This function emulates the passage and calculation of the deltas.
	 * 
	 * @param i Integer line of train/test data
	 */
	public static void spreadDelta(int i) {
		for (int ii = 0; ii < OUTPUT_LAYER; ii++)
			fourth[ii].calculateDelta(null, OUTPUTS.get(ii)[i]);
		if (debug) {
			System.out.println("====================================================================");
			System.out.println("Delta out: " + fourth[0].getDelta());
		}
		if (inUse2) {
			for (int ii = 0; ii < THIRD_LAYER; ii++)
				if (!third[ii].isBias())
					third[ii].calculateDelta(output, 0.0);

			for (int ii = 0; ii < SECOND_LAYER; ii++)
				if (!second[ii].isBias())
					second[ii].calculateDelta(inside, 0.0);

		} else
			for (int ii = 0; ii < SECOND_LAYER; ii++)
				if (!second[ii].isBias()) {
					second[ii].calculateDelta(output, 0.0);
					if (debug) {
						System.out.println("====================================================================");
						System.out.println("\tDelta in " + second[ii].getID() + " " + second[ii].getDelta());
					}
				}
	}

	/**
	 * This function emulates the change of the weights using deltas.
	 */
	public static void changeWeights() {
		for (Connection s : input) {
			if (debug) {
				System.out.println("====================================================================");
				System.out.println("Updating: " + s.getNeuron1().getID() + " to " + s.getNeuron2().getID());
			}
			if (!s.getNeuron1().isBias()) {
				if (debug) {
					System.out.println("Using: " + s.getNeuron2().getID());
				}
				s.changeWeight(s.getWeight() - (R * s.getNeuron2().getDelta() * s.getNeuron1().getInput())
						+ (M * (s.getWeight() - s.getLastWeight())));
			} else {
				if (debug) {
					System.out.println("Using: " + s.getNeuron2().getID());
				}
				s.changeWeight(
						s.getWeight() - (R * s.getNeuron2().getDelta()) + (M * (s.getWeight() - s.getLastWeight())));
			}
			if (debug)
				System.out.println("\t from " + s.getLastWeight() + " to : " + s.getWeight());

		}
		if (inUse2) {
			for (Connection s : inside) {
				if (debug) {
					System.out.println("====================================================================");
					System.out.println("Updating: " + s.getNeuron1().getID() + " to " + s.getNeuron2().getID());
				}
				if (!s.getNeuron1().isBias()) {
					if (debug) {
						System.out.println("Using: " + s.getNeuron2().getID());
					}
					s.changeWeight(s.getWeight() - (R * s.getNeuron2().getDelta() * s.getNeuron1().getInput())
							+ (M * (s.getWeight() - s.getLastWeight())));
				} else {
					if (debug) {
						System.out.println("Using: " + s.getNeuron2().getID());
					}
					s.changeWeight(s.getWeight() - (R * s.getNeuron2().getDelta())
							+ (M * (s.getWeight() - s.getLastWeight())));
				}
				if (debug)
					System.out.println("\t from " + s.getLastWeight() + " to : " + s.getWeight());

			}
		}

		for (Connection s : output) {
			if (debug) {
				System.out.println("====================================================================");
				System.out.println("Updating: " + s.getNeuron1().getID() + " to " + s.getNeuron2().getID());
			}
			if (!s.getNeuron1().isBias()) {
				if (debug) {
					System.out.println("Using: " + s.getNeuron2().getID());
				}
				s.changeWeight(s.getWeight() - (R * s.getNeuron2().getDelta() * s.getNeuron1().getInput())
						+ (M * (s.getWeight() - s.getLastWeight())));
			} else {
				if (debug) {
					System.out.println("Using: " + s.getNeuron2().getID());
				}
				s.changeWeight(
						s.getWeight() - (R * s.getNeuron2().getDelta()) + (M * (s.getWeight() - s.getLastWeight())));
			}
			if (debug)
				System.out.println("\t from " + s.getLastWeight() + " to : " + s.getWeight());

		}
	}

	/**
	 * This function selects the parameters given by the input file.
	 * 
	 * @param list ArrayList(String) all the parameters
	 */
	public static void handleParameters(ArrayList<String> list) {
		SECOND_LAYER = Integer.parseInt(list.get(0));
		THIRD_LAYER = Integer.parseInt(list.get(1));
		INPUT_LAYER = Integer.parseInt(list.get(2));
		OUTPUT_LAYER = Integer.parseInt(list.get(3));
		R = Double.parseDouble(list.get(4));
		M = Double.parseDouble(list.get(5));
		EPOCH_LIMIT = Integer.parseInt(list.get(6));
		train = new String(list.get(7));
		test = new String(list.get(8));

		train_size = Tools.findLines(train);
		for (int i = 0; i < INPUT_LAYER; i++)
			INPUTS.add(new double[train_size]);

		for (int i = 0; i < OUTPUT_LAYER; i++)
			OUTPUTS.add(new double[train_size]);

		int test_size = Tools.findLines(test);
		for (int i = 0; i < INPUT_LAYER; i++)
			TEST_INPUTS.add(new double[test_size]);

		for (int i = 0; i < OUTPUT_LAYER; i++)
			TEST_OUTPUTS.add(new double[test_size]);

		printArguments();

		Tools.fillData(train, INPUTS, OUTPUTS);
		Tools.fillData(test, TEST_INPUTS, TEST_OUTPUTS);

		// ====================================================//

		INPUT_LAYER++;
		first = new Neuron[INPUT_LAYER];
		if (SECOND_LAYER > 0) {
			second = new Neuron[SECOND_LAYER + 1];
			SECOND_LAYER++;
			inUse1 = true;
		}
		if (THIRD_LAYER > 0) {
			third = new Neuron[THIRD_LAYER + 1];
			THIRD_LAYER++;
			inUse2 = true;
		}
		fourth = new Neuron[OUTPUT_LAYER];
	}

	/**
	 * This function prints a table of the arguments.
	 */
	public static void printArguments() {
		System.out.println("            +---------------------------+");
		System.out.println("            |  Input layer:  " + (INPUT_LAYER - 1) + "         |");
		System.out.println("            |  Hidden layer: " + (SECOND_LAYER - 1) + "          |");
		if (THIRD_LAYER != 0)
			System.out.println("            |  2nd Hidden layer: " + (THIRD_LAYER - 1) + "      |");
		System.out.println("            |  Output layer: " + OUTPUT_LAYER + "         |");
		System.out.println("            |  Max epochs: " + EPOCH_LIMIT + "         |");
		System.out.println("            |  Learning rate: " + R + "       |");
		System.out.println("            |  Momenmtum: " + M + "           |");
		System.out.println("            |  Train file: " + train + "    |");
		System.out.println("            |  Test  file: " + test + "     |");
		System.out.println("            +---------------------------+");
	}

	/**
	 * Main function
	 * 
	 * @param args parameters file (.txt)
	 */
	public static void main(String[] args) {
		String filename = "parameters.txt";
		if (args.length >= 1)
			filename = args[0];
		ArrayList<String> list = Tools.getParameters(filename);
		handleParameters(list);

		int test_size = Tools.findLines(test);

		initNetwork();
		connectAll();
		int epochs = 0;
		ArrayList<String> error_txt = new ArrayList<>();
		ArrayList<String> success_txt = new ArrayList<>();

		do {
			double TRAIN_ERROR = 0.0, TRAIN_SUCCESS = 0.0;
			for (int i = 0; i < train_size; i++) {
				ArrayList<Double> opj = new ArrayList<>();
				ArrayList<Double> tpj = new ArrayList<>();
				for (int j = 0; j < INPUT_LAYER - 1; j++)
					first[j].setInput(INPUTS.get(j)[i]);

				step(second, input);
				if (inUse2)
					step(third, inside);
				step(fourth, output);
				spreadDelta(i);
				changeWeights();

				for (int k = 0; k < OUTPUT_LAYER; k++) {
					tpj.add(OUTPUTS.get(k)[i]);
					opj.add(fourth[k].getInput());

				}
				TRAIN_ERROR += Tools.error(tpj, opj);

				if (Tools.correct(tpj, opj))
					TRAIN_SUCCESS++;
				// System.exit(1);
				double[] temp_out = new double[fourth.length];
				for (int a = 0; a < fourth.length; a++)
					temp_out[a] = fourth[a].getInput();
				double[] output_maxed = Tools.findPeak(temp_out);

				char real_letter = Tools.createLetterFromArray(output_maxed);
				// System.out.println(real_letter);
				// System.exit(0);
			}
			TRAIN_SUCCESS = TRAIN_SUCCESS / (train_size * 1.0);
			double TEST_ERROR = 0.0, TEST_SUCCESS = 0.0;
			for (int i = 0; i < test_size; i++) {
				ArrayList<Double> opj = new ArrayList<>();
				ArrayList<Double> tpj = new ArrayList<>();
				for (int j = 0; j < INPUT_LAYER - 1; j++)
					first[j].setInput(TEST_INPUTS.get(j)[i]);
				step(second, input);
				if (inUse2)
					step(third, inside);
				step(fourth, output);

				for (int k = 0; k < OUTPUT_LAYER; k++) {
					tpj.add((double) TEST_OUTPUTS.get(k)[i]);
					opj.add(fourth[k].getInput());

				}
				TEST_ERROR += Tools.error(tpj, opj);
				if (Tools.correct(tpj, opj))
					TEST_SUCCESS++;

			}
			epochs++;
			TEST_SUCCESS = TEST_SUCCESS / (test_size * 1.0);

			error_txt.add(new String(epochs + " " + TRAIN_ERROR + " " + TEST_ERROR));
			success_txt.add(new String(epochs + " " + (TRAIN_SUCCESS * 100) + " " + (TEST_SUCCESS * 100) + ""));

		} while (epochs < EPOCH_LIMIT);
		Tools.feedFile("errors.txt", error_txt);
		Tools.feedFile("successrate.txt", success_txt);

		ArrayList<double[]> outs = new ArrayList<>();
		for (int i = 0; i < test_size; i++) {
			double[] real_temp_out = new double[OUTPUT_LAYER];
			// outs.add();
			for (int j = 0; j < INPUT_LAYER - 1; j++)
				first[j].setInput(TEST_INPUTS.get(j)[i]);
			step(second, input);
			if (inUse2)
				step(third, inside);
			step(fourth, output);
			for (int g = 0; g < OUTPUT_LAYER; g++)
				real_temp_out[g] = fourth[g].getInput();

			double[] output_maxed = Tools.findPeak(real_temp_out);

			char real_letter = Tools.createLetterFromArray(output_maxed);
			double[] exp_out = new double[OUTPUT_LAYER];
			for (int ia = 0; ia < 26; ia++) {
				exp_out[ia] = TEST_OUTPUTS.get(ia)[i];
			}
			System.out.println("expected: " + Tools.createLetterFromArray(exp_out) + " got: " + real_letter);
		}

		// printResults(TEST_OUTPUTS, TEST_INPUTS, outs, test_size);

		Tools.runPython("error_plot.py", "errors.txt");
		Tools.runPython("success_plot.py", "successrate.txt");

	}

	/**
	 * This function prints the results of the final testing run.
	 * 
	 * @param out       ArrayList<double[]> Target outputs
	 * @param in        ArrayList<double[]> Testing inputs
	 * @param r         Double array real outputs
	 * @param test_size The size of the testing data
	 */
	private static void printResults(ArrayList<double[]> out, ArrayList<double[]> in, double[] r, int test_size) {
		System.out.println("+---------------------------------------------------+");
		System.out.println("|  INPUT 1   |   INPUT 2   |   TARGET   |   REAL    |");
		System.out.println("+------------+-------------+------------+-----------+");
		for (int i = 0; i < 4; i++)
			System.out.println("|    " + in.get(0)[i] + "     |     " + in.get(1)[i] + "     |" + "    " + out.get(0)[i]
					+ "     |" + "    " + r[i] + "    |");

		System.out.println("+---------------------------------------------------+");
	}

}
