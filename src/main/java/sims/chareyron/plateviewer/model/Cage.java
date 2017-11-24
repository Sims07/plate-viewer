package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.List;

public class Cage extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final List<Animal> animals;

	public Cage(String name) {
		super();
		this.name = name;
		this.animals = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addAnimalInCage(Animal animal) {
		animals.add(animal);
		animal.setCage(this);
	}

	@Override
	public String toString() {
		return "{\n\tname : " + name + "\n}";
	}

}
