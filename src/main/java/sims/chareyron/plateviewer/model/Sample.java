package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.List;

public class Sample extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String id;
	private final List<Animal> inputAnimals;

	public Sample(String id) {
		super();
		this.id = id;
		this.inputAnimals = new ArrayList<>();
	}

	public void addInpuAnimal(Animal animal) {
		this.inputAnimals.add(animal);
		animal.addSample(this);
	}

	public String getId() {
		return id;
	}

	public List<Animal> getInputAnimals() {
		return inputAnimals;
	}

	@Override
	public String toString() {
		return "Sample :{\n\tid : " + id + "\n}";
	}

}
