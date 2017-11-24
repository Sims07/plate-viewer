package sims.chareyron.plateviewer.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class Well extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int index;
	private final double KRBH;
	private Plate plate;
	private final List<Traitement> tts;
	private final List<Sample> samples;
	private ExperimentalCondition experimentalCondition;
	private boolean empty;
	private ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");

	public Well(final int index, final double kRBH, final Plate plate, final List<Traitement> tts,
			final List<Sample> samples) {
		super();
		this.index = index;
		this.KRBH = kRBH;
		this.plate = plate;
		this.tts = tts;
		this.samples = samples;
		this.empty = false;
		this.experimentalCondition = new ExperimentalCondition(kRBH, tts);
	}

	public Well(final Integer wellIndex) {
		this.index = wellIndex;
		this.empty = true;
		this.KRBH = -1;
		this.plate = null;
		this.tts = null;
		this.experimentalCondition = null;
		this.samples = new ArrayList<>();
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(final Plate plate) {
		this.plate = plate;
	}

	public int getIndex() {
		return index;
	}

	public double getKRBH() {
		return KRBH;
	}

	public List<Traitement> getTts() {
		return tts;
	}

	public void addTraitement(final Traitement tt) {
		this.tts.add(tt);
	}

	public void addSample(final Sample sample) {
		this.samples.add(sample);
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(final boolean empty) {
		this.empty = empty;
	}

	public List<Sample> getSamples() {
		return samples;
	}

	public Color getColor() {
		return color.get();
	}

	public void setColor(final Color newColor) {
		color.set(newColor);
	}

	public ObjectProperty<Color> colorProperty() {
		return color;
	}

	public void setColorProperty(final ObjectProperty<Color> cp) {
		this.color = cp;
	}

	public ExperimentalCondition getExperimentalCondition() {
		return experimentalCondition;
	}

	public void setExperimentalCondition(final ExperimentalCondition experimentalCondition) {
		this.experimentalCondition = experimentalCondition;
	}

	@Override
	public String toString() {
		return "{\n\tindex : " + index + ", \n\tKRBH : " + KRBH + ", \n\ttts : " + tts + ", \n\tsamples : " + samples
				+ "\n}";
	}

}
