package sims.chareyron.plateviewer.javafx.view.plateviewer;

import javafx.scene.layout.Pane;
import sims.chareyron.plateviewer.model.Experiment;
import sims.chareyron.plateviewer.model.Plate;

public class PlatePaneDecorator {
	private Pane decorated;
	private String title;
	private Experiment xp;
	private Plate plate;

	public PlatePaneDecorator(Pane decorated, String title, Experiment xp, Plate plate) {
		super();
		this.decorated = decorated;
		this.title = title;
		this.xp = xp;
		this.plate = plate;
	}

	public Pane getDecorated() {
		return decorated;
	}

	public void setDecorated(Pane decorated) {
		this.decorated = decorated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Experiment getXp() {
		return xp;
	}

	public void setXp(Experiment xp) {
		this.xp = xp;
	}

	public Plate getPlate() {
		return plate;
	}

	public void setPlate(Plate plate) {
		this.plate = plate;
	}

}
