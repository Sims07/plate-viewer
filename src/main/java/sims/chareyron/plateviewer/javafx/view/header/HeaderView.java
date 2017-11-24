package sims.chareyron.plateviewer.javafx.view.header;

import java.io.File;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import sims.chareyron.plateviewer.PlateViewerApplication;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractViewWithUiHandlers;
import sims.chareyron.plateviewer.javafx.framework.mvp.Slot;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;

@Component
public class HeaderView extends AbstractViewWithUiHandlers<HeaderUiHandlers> implements HeaderPresenter.MyView {

	@FXML
	MenuBar menuBar;
	@FXML
	MenuItem loadFileMenu;
	@FXML
	AnchorPane mainPanel;
	FileChooser fileChooser;
	FileChooser outputExport;

	@Override
	public void setInSlot(Slot slot, View view) {

	}

	@Override
	public void setViewBindings(Stage stage) {
		fileChooser = new FileChooser();
		mainPanel.prefWidthProperty().bind(stage.widthProperty());
	}

	public void onLoadFileMenuClicked() {
		setDisplayInputDirectory();
	}

	@Override
	public void setDisplayError(Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText("The file is not well formatted\n\n" + e.getMessage());
		alert.showAndWait();
	}

	@Override
	public void setDisplayInputDirectory() {
		File input = new File("input");
		if (input.exists()) {
			fileChooser.setInitialDirectory(input);
		}
		fileChooser.setTitle("Select a CSV experiment file");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV file", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(PlateViewerApplication.MAIN_STAGE);
		if (selectedFile != null) {
			getUiHandlers().onCsvFileSelected(selectedFile);
		}

	}

}
