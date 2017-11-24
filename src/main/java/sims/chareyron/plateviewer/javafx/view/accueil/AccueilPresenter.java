package sims.chareyron.plateviewer.javafx.view.accueil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.stage.Stage;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractPresenter;
import sims.chareyron.plateviewer.javafx.framework.mvp.PlaceManager;
import sims.chareyron.plateviewer.javafx.framework.mvp.Slot;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;
import sims.chareyron.plateviewer.javafx.view.MainPresenter;

@Component
public class AccueilPresenter extends AbstractPresenter<AccueilPresenter.MyView> {

	public interface MyView extends View {
		public void setViewBindings(Stage stage);
	}

	private PlaceManager placeManager;

	@Autowired
	public AccueilPresenter(MyView view, PlaceManager placeManager) {
		super(view);
		this.view = view;
		this.placeManager = placeManager;
	}

	public Slot revealedInSlot() {
		return MainPresenter.BODY_SLOT;
	}

	@Override
	public String getToken() {

		return "default";
	}

	@Override
	public void onBind() {
		getView().setViewBindings(placeManager.getStage());

	}

	@Override
	public void onReveal() {
		// TODO Auto-generated method stub

	}

}
