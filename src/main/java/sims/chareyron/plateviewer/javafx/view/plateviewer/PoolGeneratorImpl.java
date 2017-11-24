package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

public class PoolGeneratorImpl implements PoolGenerator {
	private ColorGenerator colorGenerator;
	private AtomicInteger currentPoolIndex;
	private Map<MicePoolInfo, ObservableValue<Color>> poolInfo;

	public PoolGeneratorImpl(ColorGenerator colorGenerator) {
		super();
		this.colorGenerator = colorGenerator;
		this.currentPoolIndex = new AtomicInteger(0);
		this.poolInfo = new HashMap<MicePoolInfo, ObservableValue<Color>>();
	}

	@Override
	public Color addPoolInfo(MicePoolInfo toAdd) {
		ObservableValue<Color> nextColor = null;
		if (!poolInfo.containsKey(toAdd)) {
			toAdd.setIndex(currentPoolIndex.incrementAndGet());

			Color nextReelColor = colorGenerator.getNextColor();
			toAdd.getWell().setColor(nextReelColor);
			nextColor = toAdd.getWell().colorProperty();
			poolInfo.put(toAdd, nextColor);

		} else {
			nextColor = poolInfo.get(toAdd);
			toAdd.getWell().setColorProperty((ObjectProperty<Color>) nextColor);
		}
		return nextColor.getValue();

	}

	@Override
	public Map<MicePoolInfo, ObservableValue<Color>> getPool() {

		return poolInfo;
	}

}
