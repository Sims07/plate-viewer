package sims.chareyron.plateviewer.javafx.framework.mvp;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPresenter<V extends View> implements Presenter<V> {
	protected boolean bound = false;

	protected V view;

	public AbstractPresenter(V view) {
		super();
		this.view = view;
	}

	@SuppressWarnings("rawtypes")
	protected List<Presenter<?>> childrenPresenters = new ArrayList<>();

	public V getView() {
		return view;
	}

	public List<Slot> getSlotList() {
		return new ArrayList<>();
	}

	public Slot revealedInSlot() {
		return null;
	}

	protected void setInSlot(Slot slot, AbstractWidgetPresenter<?> childPresenter) {
		getView().setInSlot(slot, childPresenter.getView());
		if (!childrenPresenters.contains(childPresenter)) {
			childrenPresenters.add(childPresenter);
		}
	}

	public List<Presenter<?>> childrenPresenter() {
		return childrenPresenters;
	}

	@Override
	public void bind() {
		onBind();
		bound = true;

	}

	@Override
	public void reveal() {
		onReveal();

	}

	public boolean isBound() {
		return bound;
	}

}
