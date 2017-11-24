package sims.chareyron.plateviewer.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Animal extends AbstractModel {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Date birthday;
	private final String species, name, bw, bl, description, sex, age;
	private Cage cage;
	private Strain strain;
	private final List<Sample> eluateSamples;

	public Animal(Date birthday, String species, String name, String bw, String bl, String description, String sex,
			String age) {
		super();
		this.birthday = birthday;
		this.species = species;
		this.name = name;
		this.bw = bw;
		this.bl = bl;
		this.description = description;
		this.sex = sex;
		this.eluateSamples = new ArrayList<>();
		this.age = age;
	}

	public Strain getStrain() {
		return strain;
	}

	public void setStrain(Strain strain) {
		this.strain = strain;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getSpecies() {
		return species;
	}

	public Cage getCage() {
		return cage;
	}

	public void setCage(Cage cage) {
		this.cage = cage;
	}

	public String getName() {
		return name;
	}

	public String getBw() {
		return bw;
	}

	public String getBl() {
		return bl;
	}

	public String getDescription() {
		return description;
	}

	public String getSex() {
		return sex;
	}

	public void addSample(Sample sample) {
		this.eluateSamples.add(sample);
	}

	public List<Sample> getEluateSamples() {
		return eluateSamples;
	}

	public String getAge() {
		return age;
	}

	public String toStringDisplay() {
		return (notNull(cage) ? "cage : " + cage.getName() + "," : "")
				+ (notNull(species) ? "species : " + species + ", " : "")
				+ (notNull(birthday) ? "birthday : " + formatter.format(birthday) + "," : "")
				+ (notNull(age) ? "age : " + age + ", \n" : "") + (notNull(bw) ? "bw : " + bw + ", " : "")
				+ (notNull(bl) ? "bl : " + bl + ", \n" : "") + (notNull(sex) ? "sex : " + sex + ", \n" : "")
				+ (notNull(strain) ? "strain : " + strain.getStrain() : " ");
	}

	private boolean notNull(Object birthday2) {
		return birthday2 != null && !"_".equals(birthday2.toString().trim());
	}

	@Override
	public String toString() {
		return "{\n\t" + (birthday != null ? "birthday : " + birthday + ", \n\t" : "")
				+ (species != null ? "species : " + species + ", \n\t" : "")
				+ (name != null ? "name : " + name + ", \n\t" : "") + (bw != null ? "bw : " + bw + ", \n\t" : "")
				+ (bl != null ? "bl : " + bl + ", \n\t" : "")
				+ (description != null ? "description : " + description + ", \n\t" : "")
				+ (sex != null ? "sex : " + sex + ", \n\t" : "") + (cage != null ? "cage : " + cage + ", \n\t" : "")
				+ (strain != null ? "strain : " + strain + ", \n\t" : "")
				+ (eluateSamples != null ? "eluateSamples : " + eluateSamples : "") + "\n}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Animal other = (Animal) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
