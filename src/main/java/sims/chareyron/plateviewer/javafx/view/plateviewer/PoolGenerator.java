package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.Map;

import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

public interface PoolGenerator {
	Color addPoolInfo(MicePoolInfo toAdd);

	Map<MicePoolInfo, ObservableValue<Color>> getPool();
}
