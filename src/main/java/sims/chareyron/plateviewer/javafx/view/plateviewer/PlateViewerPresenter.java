package sims.chareyron.plateviewer.javafx.view.plateviewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.stage.Stage;
import sims.chareyron.plateviewer.PlateViewerApplication;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractPresenter;
import sims.chareyron.plateviewer.javafx.framework.mvp.Slot;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;
import sims.chareyron.plateviewer.javafx.framework.mvp.ViewWithUiHandlers;
import sims.chareyron.plateviewer.javafx.view.MainPresenter;
import sims.chareyron.plateviewer.javafx.view.Token;
import sims.chareyron.plateviewer.model.Experiment;
import sims.chareyron.plateviewer.service.PlateViewerLoader;

@Component
public class PlateViewerPresenter extends AbstractPresenter<PlateViewerPresenter.MyView>
		implements PlateViewerUiHandlers {
	private final PlateViewerLoader plateViewerLoader;

	@Autowired
	public PlateViewerPresenter(MyView view, PlateViewerLoader plateViewerLoader) {
		super(view);
		this.plateViewerLoader = plateViewerLoader;
	}

	public interface MyView extends View, ViewWithUiHandlers<PlateViewerUiHandlers> {
		void setViewBindings(Stage stage);

		void setExperiment(Experiment xp);
	}

	@Override
	public String getToken() {

		return Token.TOKEN_LOADED_PLATE;
	}

	@Override
	public void onBind() {
		getView().setUiHandlers(this);
		getView().setViewBindings(PlateViewerApplication.MAIN_STAGE);
		getView().setExperiment(plateViewerLoader.getLoadedExperiment());

	}

	public Slot revealedInSlot() {
		return MainPresenter.BODY_SLOT;
	}

	@Override
	public void onReveal() {
		getView().setExperiment(plateViewerLoader.getLoadedExperiment());

	}
}
