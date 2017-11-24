package sims.chareyron.plateviewer.javafx.view;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractPresenter;
import sims.chareyron.plateviewer.javafx.framework.mvp.PlaceManager;
import sims.chareyron.plateviewer.javafx.framework.mvp.Slot;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;
import sims.chareyron.plateviewer.javafx.view.header.HeaderPresenter;

@Component
public class MainPresenter extends AbstractPresenter<MainPresenter.MyView> {

	private HeaderPresenter headerPresenter;

	private PlaceManager placeManager;

	public final static Slot HEADER_SLOT = new Slot("header");
	public final static Slot BODY_SLOT = new Slot("body");

	@Autowired
	public MainPresenter(MyView view, HeaderPresenter headerPresenter, PlaceManager placeManager) {
		super(view);
		this.view = view;
		this.placeManager = placeManager;
		this.headerPresenter = headerPresenter;
	}

	public interface MyView extends View {
		void setViewBindings(Stage stage);
	}

	@Override
	public void onBind() {
		getView().setViewBindings(placeManager.getStage());
		setInSlot(HEADER_SLOT, headerPresenter);
		placeManager.getStage().addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				headerPresenter.displayInputSelection();
			}
		});
	}

	@Override
	public List<Slot> getSlotList() {
		return Arrays.asList(HEADER_SLOT, BODY_SLOT);
	}

	@Override
	public void onReveal() {
		System.out.println("Main presenter on start");

	}

	@Override
	public String getToken() {
		return null;
	}
}
