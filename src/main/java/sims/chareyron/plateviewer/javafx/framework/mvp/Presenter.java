package sims.chareyron.plateviewer.javafx.framework.mvp;

import java.util.List;

public interface Presenter<V extends View> {
	V getView();

	String getToken();

	void onBind();

	void onReveal();

	void reveal();

	boolean isBound();

	public List<Slot> getSlotList();

	public Slot revealedInSlot();

	List<Presenter<?>> childrenPresenter();

	void bind();
}
