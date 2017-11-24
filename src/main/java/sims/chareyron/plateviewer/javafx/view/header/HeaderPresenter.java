package sims.chareyron.plateviewer.javafx.view.header;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.stage.Stage;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractWidgetPresenter;
import sims.chareyron.plateviewer.javafx.framework.mvp.PlaceManager;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;
import sims.chareyron.plateviewer.javafx.framework.mvp.ViewWithUiHandlers;
import sims.chareyron.plateviewer.javafx.view.Token;
import sims.chareyron.plateviewer.service.PlateLoadingException;
import sims.chareyron.plateviewer.service.PlateViewerLoader;

@Component
public class HeaderPresenter extends AbstractWidgetPresenter<HeaderPresenter.MyView> implements HeaderUiHandlers {

	private final PlaceManager placeManager;
	private final PlateViewerLoader plateViewerLoader;

	public interface MyView extends View, ViewWithUiHandlers<HeaderUiHandlers> {
		void setViewBindings(Stage stage);

		void setDisplayError(Exception e);

		void onLoadFileMenuClicked();

		void setDisplayInputDirectory();

	}

	@Autowired
	public HeaderPresenter(MyView view, PlaceManager placeManager, PlateViewerLoader plateViewerLoader) {
		super(view);
		this.view = view;
		this.placeManager = placeManager;
		this.plateViewerLoader = plateViewerLoader;
	}

	@Override
	public void onBind() {
		getView().setUiHandlers(this);
		getView().setViewBindings(placeManager.getStage());

	}

	public void displayInputSelection() {
		getView().setDisplayInputDirectory();
	}

	@Override
	public void onReveal() {

	}

	@Override
	public void onCsvFileSelected(File selectedFile) {
		try {
			plateViewerLoader.loadExperiment(selectedFile);
			placeManager.revealPlace(Token.TOKEN_LOADED_PLATE);
		} catch (IOException | PlateLoadingException e) {
			getView().setDisplayError(e);
			e.printStackTrace();
		}

	}

}
