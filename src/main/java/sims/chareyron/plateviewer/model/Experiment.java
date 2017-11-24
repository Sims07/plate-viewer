package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Experiment extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String title, description;
	private final Date xpDate;
	private final List<Animal> xpAnimals;
	private List<Plate> plates;

	public Experiment(String title, String description, Date xpDate) {
		super();
		this.title = title;
		this.description = description;
		this.xpDate = xpDate;
		this.xpAnimals = new ArrayList<>();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getXpDate() {
		return xpDate;
	}

	public void addAnimal(Animal animal) {
		this.xpAnimals.add(animal);
	}

	@Override
	public String toString() {
		return "{\n\ttitle : " + title + ", \n\tdescription : " + description + ", \n\txpDate : " + xpDate
				+ ", \n\txpAnimals : " + xpAnimals + ", \n\tplates : " + plates + "\n}";
	}

	public void addAll(List<Animal> animals) {
		this.xpAnimals.addAll(animals);

	}

	public void setPlates(List<Plate> plates) {
		this.plates = plates;

	}

	public List<Animal> getXpAnimals() {
		return xpAnimals;
	}

	public List<Plate> getPlates() {
		return plates;
	}

}
