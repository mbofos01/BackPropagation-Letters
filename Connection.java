import java.util.ArrayList;

/**
 * This object emulates a connection between two neurons, its basically there to
 * help us handle the weights.
 * 
 * @author Michail - Panagiotis Bofos
 */
public class Connection {
	/**
	 * The neuron1 is the first neuron and neuron2 the second.
	 * 
	 * N1--weight-->N2
	 *
	 */
	public Neuron neuron1;
	/**
	 * Neuron two (right)
	 */
	public Neuron neuron2;
	/**
	 * Weight between neurons
	 */
	public double weight;
	/**
	 * All past weights
	 */
	public double weightHistory;

	/**
	 * Default constructor.
	 * 
	 * @param neuron1 Left neuron
	 * @param neuron2 Right neuron
	 * @param weight  Weight between neuron1 and neuron2
	 */
	public Connection(Neuron neuron1, Neuron neuron2, double weight) {
		weightHistory = 0;
		this.neuron1 = neuron1;
		this.neuron2 = neuron2;
		this.weight = weight;
	}

	/**
	 * Neuron One handler.
	 * 
	 * @return Left neuron
	 */
	public Neuron getNeuron1() {
		return neuron1;
	}

	/**
	 * Neuron Two handler.
	 * 
	 * @return Right neuron
	 */
	public Neuron getNeuron2() {
		return neuron2;
	}

	/**
	 * This method gets the weight between the two neurons.
	 * 
	 * @return Double value
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * This method updates the value of the weight and saves the past value to the
	 * weight history arraylist.
	 * 
	 * @param weight Double value new weight
	 */
	public void changeWeight(double weight) {
		weightHistory = this.weight;
		this.weight = weight;

	}

	/**
	 * This method prints the detail of the connection.
	 */
	public void details() {
		System.out.println("Connection between: " + neuron1.getID() + " and " + neuron2.getID() + " weight: " + weight);
	}

	/**
	 * This method returns the last weight of the connection.
	 * 
	 * @return Double value past weight
	 */
	public double getLastWeight() {

		return weightHistory;
	}

}
