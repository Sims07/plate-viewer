package sims.chareyron.plateviewer.javafx.view.header;

import java.io.File;

import sims.chareyron.plateviewer.javafx.framework.mvp.UiHandlers;

public interface HeaderUiHandlers extends UiHandlers {

	void onCsvFileSelected(File selectedFile);
}
