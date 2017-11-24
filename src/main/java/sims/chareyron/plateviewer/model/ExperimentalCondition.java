package sims.chareyron.plateviewer.model;

import java.util.List;

public class ExperimentalCondition {
	private double krbh;
	private List<Traitement> traitements;

	public ExperimentalCondition(final double krbh, final List<Traitement> traitements) {
		super();
		this.krbh = krbh;
		this.traitements = traitements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(krbh);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((traitements == null) ? 0 : traitements.hashCode());
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
		final ExperimentalCondition other = (ExperimentalCondition) obj;
		if (Double.doubleToLongBits(krbh) != Double.doubleToLongBits(other.krbh))
			return false;
		if (traitements == null) {
			if (other.traitements != null)
				return false;
		} else if (!traitements.equals(other.traitements))
			return false;
		return true;
	}

	public double getKrbh() {
		return krbh;
	}

	public void setKrbh(final double krbh) {
		this.krbh = krbh;
	}

	public List<Traitement> getTraitements() {
		return traitements;
	}

	public void setTraitements(final List<Traitement> traitements) {
		this.traitements = traitements;
	}

}
