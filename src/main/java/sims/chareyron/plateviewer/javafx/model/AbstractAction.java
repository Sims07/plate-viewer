package sims.chareyron.plateviewer.javafx.model;

public abstract class AbstractAction implements Action {

	@Override
	public String toString() {
		return idExecute() + "-" + idRollback();
	}

}
