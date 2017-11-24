package sims.chareyron.plateviewer.javafx.framework.mvp;

public abstract class AbstractWidgetPresenter<V extends View> extends AbstractPresenter<V> implements Presenter<V> {

	public AbstractWidgetPresenter(V view) {
		super(view);
	}

	@Override
	public String getToken() {
		return null;
	}

}
