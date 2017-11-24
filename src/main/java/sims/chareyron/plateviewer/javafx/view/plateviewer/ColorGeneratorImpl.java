package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.paint.Color;

public class ColorGeneratorImpl implements ColorGenerator {

	private final AtomicInteger currentColorIndex = new AtomicInteger(0);
	private final List<Color> colors = Arrays.asList(Color.LAWNGREEN, Color.DEEPPINK, Color.AQUA, Color.RED,
			Color.BLUEVIOLET, Color.GOLD, Color.YELLOWGREEN, Color.CORAL, Color.DEEPPINK, Color.BLACK, Color.PINK,
			Color.GREENYELLOW, Color.GREEN, Color.TOMATO, Color.BEIGE, Color.TEAL, Color.AQUAMARINE, Color.YELLOW,
			Color.RED, Color.ROYALBLUE);

	@Override
	public Color getNextColor() {
		return colors.get(getNextColorIndex());
	}

	@Override
	public Color getNextColorReverse() {
		return colors.get(getReverseIndex());
	}

	private int getNextColorIndex() {
		if (currentColorIndex.get() + 1 >= colors.size()) {
			restartColor();
		}
		return currentColorIndex.getAndIncrement();
	}

	private int getReverseIndex() {
		if (isOutOfRangeLowBound()) {
			restartColor();
		}
		return colors.size() - 1 - currentColorIndex.getAndIncrement();
	}

	private boolean isOutOfRangeLowBound() {
		return colors.size() - 1 - currentColorIndex.get() <= 0;
	}

	@Override
	public void restartColor() {
		currentColorIndex.set(0);
	}

}
