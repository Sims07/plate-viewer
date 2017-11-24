package sims.chareyron.plateviewer.model;

public class Traitement extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name, dose, duration;
	private final Lot lot;

	public Traitement(final String name, final String dose, final String duration, final Lot lot) {
		super();
		this.name = name;
		this.dose = dose;
		this.duration = duration;
		this.lot = lot;
	}

	public String getName() {
		return name;
	}

	public String getDose() {
		return dose;
	}

	public String getDuration() {
		return duration;
	}

	public Lot getLot() {
		return lot;
	}

	@Override
	public String toString() {
		return "TT : {\n\tname : " + name + ", \n\tdose : " + dose + ", \n\tduration : " + duration + ", \n\tlot : "
				+ lot + "\n}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dose == null) ? 0 : dose.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Traitement other = (Traitement) obj;
		if (dose == null) {
			if (other.dose != null)
				return false;
		} else if (!dose.equals(other.dose))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
