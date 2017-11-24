package sims.chareyron.plateviewer.javafx.framework.mvp;

import javafx.scene.Parent;

public interface View {
	void setInSlot(Slot slot, View view);

	Parent getParent();
}
