package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.List;

public class Plate extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String id;
	private final List<Well> wells;

	public Plate(String id) {
		super();
		this.id = id;
		this.wells = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public int getNbWells() {
		return wells.size();
	}

	public List<Well> getWells() {
		return wells;
	}

	public void addWell(Well well) {
		this.wells.add(well);
	}

	@Override
	public String toString() {
		return "{\n\tid : " + id + ", \n\twells : " + wells + "\n}";
	}

	public boolean isEmpty() {
		return !wells.stream().filter(w -> w.getKRBH() != 0).findAny().isPresent();
	}

}
