package sims.chareyron.plateviewer.javafx.framework.mvp;

import sims.chareyron.plateviewer.javafx.framework.AbstractFxmlView;

public abstract class AbstractViewWithUiHandlers<T extends UiHandlers> extends AbstractFxmlView
		implements ViewWithUiHandlers<T> {
	private T uiHandlers;

	@Override
	public T getUiHandlers() {
		return uiHandlers;
	}

	@Override
	public void setUiHandlers(T uih) {
		this.uiHandlers = uih;

	}

}
