package sims.chareyron.plateviewer.javafx.framework.mvp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sims.chareyron.plateviewer.javafx.framework.AbstractFxmlView;

@Component
public class PlaceManagerImpl implements PlaceManager {

	public Stage stage;
	@SuppressWarnings("rawtypes")
	private final Map<Presenter, Boolean> boundPresentersMap = new HashMap<>();
	private Presenter<? extends View> currentPresenter;
	@Autowired
	@Lazy
	private List<Presenter<? extends View>> presenters;
	private boolean sceneBuilt = false;

	@Override
	public void init() {

		stage.setResizable(true);
		stage.centerOnScreen();
		stage.minHeightProperty().set(800);
		stage.minWidthProperty().set(1200);
		final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
		stage.initStyle(StageStyle.UNIFIED);
		stage.setFullScreen(true);
		stage.show();
	}

	@Override
	public void revealPlace(final String token) {

		final Presenter<? extends View> presenterWithToken = presenters.parallelStream()
				.filter(p -> p.getToken() != null && p.getToken().equals(token)).findFirst().get();
		Presenter<?> parentPresenter = null;
		if (!presenterWithToken.isBound()) {
			boundPresentersMap.put(presenterWithToken, true);
			parentPresenter = onBind(presenterWithToken);

		} else {
			parentPresenter = onReveal(presenterWithToken);
		}
		currentPresenter = presenterWithToken;
		revealInScene(parentPresenter);

	}

	private Presenter<?> onReveal(final Presenter<? extends View> presenterToReveal) {

		// appeler on reveal sur tous ses enfants
		presenterToReveal.childrenPresenter().forEach(p -> p.reveal());
		// reveler dans le parent
		final Slot revealedInSlot = presenterToReveal.revealedInSlot();
		// trouver ou se trouve le slot
		final Presenter<?> parentPresenter = presenters.stream().filter(p -> {
			return p.getSlotList().contains(revealedInSlot);
		}).findFirst().get();
		// initialiser la vue java FX
		((AbstractFxmlView) parentPresenter.getView()).getParent();
		// appeler on reveal sur tout ses enfants
		parentPresenter.reveal();
		parentPresenter.childrenPresenter().forEach(p -> p.reveal());
		parentPresenter.getView().setInSlot(revealedInSlot, presenterToReveal.getView());
		// bind du presenter courant
		presenterToReveal.reveal();
		return parentPresenter;

	}

	private Presenter<?> onBind(final Presenter<? extends View> presenterToBind) {
		boundPresentersMap.put(presenterToBind, true);

		// reveler dans le parent
		final Slot revealedInSlot = presenterToBind.revealedInSlot();
		// trouver ou se trouve le slot
		final Presenter<?> parentPresenter = presenters.stream().filter(p -> {
			return p.getSlotList().contains(revealedInSlot);
		}).findFirst().get();
		((AbstractFxmlView) parentPresenter.getView()).getParent();
		// appeler on bind sur tout ses enfants
		if (!parentPresenter.isBound()) {
			parentPresenter.bind();
		}
		parentPresenter.childrenPresenter().forEach(p -> {
			// init java fx view
			((AbstractFxmlView) p.getView()).getParent();
			if (!p.isBound()) {
				p.bind();
			}
		});
		parentPresenter.getView().setInSlot(revealedInSlot, presenterToBind.getView());
		// bind du presenter courant
		if (!presenterToBind.isBound()) {
			((AbstractFxmlView) presenterToBind.getView()).getParent();
			presenterToBind.bind();
		}
		// appeler on bind sur tous ses enfants
		presenterToBind.childrenPresenter().forEach(p -> {
			// init java fx view
			((AbstractFxmlView) p.getView()).getParent();
			if (!p.isBound()) {
				p.bind();
			}
		});

		return parentPresenter;
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace("default");

	}

	private void revealInScene(final Presenter<? extends View> defaultPresenter) {
		if (!sceneBuilt) {
			final Scene scene = new Scene(defaultPresenter.getView().getParent());
			defaultPresenter.reveal();
			stage.setScene(scene);
			sceneBuilt = true;
		}
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@Override
	public void setStage(final Stage stage) {
		this.stage = stage;

	}

}
