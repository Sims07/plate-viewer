package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sims.chareyron.plateviewer.model.Animal;
import sims.chareyron.plateviewer.model.Plate;
import sims.chareyron.plateviewer.model.Sample;
import sims.chareyron.plateviewer.model.Well;

public class PlateContentUI implements Initializable {
	@FXML
	TableView<Well> wellTable;
	@FXML
	TableColumn<Well, Number> wellColumn;
	@FXML
	TableColumn<Well, String> sampleColumn;
	@FXML
	TableColumn<Well, String> micesColumn;
	@FXML
	TableColumn<Well, Number> krbhColumn;
	@FXML
	TableColumn<Well, String> treatmentsColumn;

	public void setData(Plate pl) {
		wellTable.setItems(FXCollections.observableArrayList(pl.getWells()));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		wellColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue(), "index"));
		sampleColumn.setCellValueFactory(cellData -> getSamples(cellData.getValue().getSamples()));
		micesColumn.setCellValueFactory(cellData -> getMices(cellData.getValue().getSamples()));
		krbhColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue(), "KRBH"));
		treatmentsColumn.setCellValueFactory(cellData -> getMices(cellData.getValue().getSamples()));
	}

	private SimpleStringProperty getMices(List<Sample> samples) {

		StringBuilder samplesStringBuilder = new StringBuilder();
		samples.forEach(s -> samplesStringBuilder.append(getMicesStr(s.getInputAnimals()) + "\n"));
		return new SimpleStringProperty(samplesStringBuilder.toString());
	}

	private String getMicesStr(List<Animal> inputAnimals) {

		StringBuilder micesStr = new StringBuilder();
		inputAnimals.forEach(a -> micesStr.append(getMicesStr(a) + "\n"));
		return micesStr.toString();
	}

	private String getMicesStr(Animal a) {

		return String.format("%s:%s-%s [length=%s,weight=%s,diet=%s,d]", a.getName(), a.getSpecies(),
				a.getStrain().getStrain(), a.getBl(), a.getBw(), a.getSex());
	}

	private SimpleStringProperty getSamples(List<Sample> samples) {

		StringBuilder samplesStringBuilder = new StringBuilder();
		samples.forEach(s -> samplesStringBuilder.append(s.getId() + "\n"));
		return new SimpleStringProperty(samplesStringBuilder.toString());

	}
}
