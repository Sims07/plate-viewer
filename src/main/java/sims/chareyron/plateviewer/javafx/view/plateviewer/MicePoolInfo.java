package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.List;

import sims.chareyron.plateviewer.model.Animal;
import sims.chareyron.plateviewer.model.Well;

public class MicePoolInfo {
	private int index;
	private List<Animal> mices;
	private Well well;

	public MicePoolInfo(Well well, List<Animal> mices) {
		super();
		this.mices = mices;
		this.well = well;
	}

	public Well getWell() {
		return well;
	}

	public void setWell(Well well) {
		this.well = well;
	}

	public List<Animal> getMices() {
		return mices;
	}

	public void setMices(List<Animal> mices) {
		this.mices = mices;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mices == null) ? 0 : mices.hashCode());
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
		MicePoolInfo other = (MicePoolInfo) obj;
		if (mices == null) {
			if (other.mices != null)
				return false;
		} else if (!mices.equals(other.mices))
			return false;
		return true;
	}

}
