package sims.chareyron.plateviewer.model;

import java.util.Date;

public class Lot extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String provider;
	private final Date peremptionDate;
	private final String lot;

	public Lot(String provider, Date peremptionDate, String lot) {
		super();
		this.provider = provider;
		this.peremptionDate = peremptionDate;
		this.lot = lot;
	}

	public String getProvider() {
		return provider;
	}

	public Date getPeremptionDate() {
		return peremptionDate;
	}

	public String getLot() {
		return lot;
	}

	@Override
	public String toString() {
		return "LOT : {\n\tprovider : " + provider + ", \n\tperemptionDate : " + peremptionDate + ", \n\tlot : " + lot
				+ "\n}";
	}

}
