package sims.chareyron.plateviewer.javafx.model;

public interface Action {

	String idExecute();

	String idRollback();

	void execute() throws Exception;

	void rollback() throws Exception;
}
