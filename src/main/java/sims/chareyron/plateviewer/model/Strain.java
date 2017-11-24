package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.List;

public class Strain extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String strain;
	private final List<Animal> animals;

	public Strain(String strain) {
		super();
		this.strain = strain;
		this.animals = new ArrayList<>();
	}

	public String getStrain() {
		return strain;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void addAnimalInStrain(Animal animal) {
		this.animals.add(animal);
		animal.setStrain(this);
	}

	@Override
	public String toString() {
		return "{\n\tstrain : " + strain + "\n}";
	}
}
