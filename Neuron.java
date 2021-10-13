import java.util.ArrayList;

/**
 * This object emulates one neuron used in a neural network. Each neuron can get
 * identified by a static counter ID.
 * 
 * 
 * @author Michail - Panagiotis Bofos
 *
 */

public class Neuron {
	/**
	 * Each neuron has an input (or output if its an output neuron), a delta value
	 * (the input neurons don't actually use it ), an ID and can be defined as a
	 * inner/output/input or/and bias neuron.
	 * 
	 */
	public double input;
	private int ID;
	private double delta;
	private boolean inner = false;
	private boolean output = false;
	private boolean isInput = false;
	private boolean bias = false;
	/**
	 * ID giver
	 */
	public static int id = 1;

	/**
	 * Default empty constructor.
	 */
	public Neuron() {
		this(0);
	}

	/**
	 * Constructor using only the input.
	 * 
	 * @param input double value
	 */
	public Neuron(double input) {
		this.input = input;
		ID = id;
		id = id + 1;
	}

	/**
	 * This method checks if a neuron is inner or not.
	 * 
	 * @return True if neuron is inner, otherwise false
	 */
	public boolean isInner() {
		return inner;
	}

	/**
	 * This method changes a neuron from inner to not inner.
	 */
	public void changeInner() {
		this.inner = !inner;
	}

	/**
	 * This method gets the ID number of a neuron.
	 * 
	 * @return integer ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * This method gets the input value of the neuron.
	 * 
	 * @return double input
	 */
	public double getInput() {
		return input;
	}

	/**
	 * This method is used to change the input value of the neuron.
	 * 
	 * @param input double new input
	 */
	public void setInput(double input) {
		this.input = input;
	}

	/**
	 * This method gets the delta value of the neuron.
	 * 
	 * @return double delta
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * This method changes the delta value of a neuron.
	 * 
	 * @param delta double new delta
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}

	/**
	 * This method checks if a neuron is used as a bias input.
	 * 
	 * @return True if used as a bias, otherwise false
	 */
	public boolean isBias() {
		return bias;
	}

	/**
	 * This method changes the bias state of a neuron.
	 * 
	 * @param bias boolean value
	 */
	public void setBias(boolean bias) {
		this.bias = bias;
	}

	/**
	 * This method is used to calculate the delta value of each neuron.
	 * 
	 * @param incoming The connection list using the neuron (used for the inner
	 *                 neurons ONLY)
	 * @param target   the target output (used for output neurons ONLY)
	 */
	public void calculateDelta(ArrayList<Connection> incoming, double target) {
		if (!isInput()) {
			if (isInner() && !isBias()) {
				double delt = this.getInput() * (1 - this.getInput());
				double temp = 0;
				for (Connection s : incoming)
					if (s.getNeuron1().equals(this)) {
						temp += (s.getWeight() * s.getNeuron2().getDelta());
					}
				temp = delt * temp;
				this.setDelta(temp);

			}

			else if (isOutput()) {
				double temp = this.getInput() * (1 - this.getInput()) * (this.getInput() - target);
				this.setDelta(temp);
			}
		}
	}

	/**
	 * This method prints the details of a neuron.
	 */
	public void printNeural() {
		System.out.print("Neural " + ID);
		if (inner)
			System.out.print(" inner ");
		if (isInput)
			System.out.print(" input ");
		if (output)
			System.out.print(" output ");
		if (bias)
			System.out.print(" bias ");
		System.out.print("[input = " + input + " delta = " + delta + " ]\n");
	}

	/**
	 * This method checks if a neuron is used as output.
	 * 
	 * @return True if neuron is output, otherwise false
	 */
	public boolean isOutput() {
		return output;
	}

	/**
	 * This method set a neuron to output or not.
	 * 
	 * @param output boolean true if it is output neuron
	 */
	public void setOutput(boolean output) {
		this.output = output;
	}

	/**
	 * This method checks if a neuron is used as input.
	 * 
	 * @return True if neuron is input, otherwise false
	 */
	public boolean isInput() {
		return isInput;
	}

	/**
	 * This method set a neuron to input or not.
	 * 
	 * @param isInput boolean true if it is input neuron
	 */
	public void setIsInput(boolean isInput) {
		this.isInput = isInput;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Neuron other = (Neuron) obj;
		return ID == other.ID;
	}

}
