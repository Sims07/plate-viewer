package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import sims.chareyron.plateviewer.model.Experiment;
import sims.chareyron.plateviewer.model.ExperimentalCondition;
import sims.chareyron.plateviewer.model.Well;

public class ExperimentalConditionExtractor {
	private final ColorGenerator colorGenerator = new ColorGeneratorImpl();
	private final Map<ExperimentalCondition, Color> mapExperimentColor = new HashMap<>();

	public Color getColorForWell(final Well well) {

		final ExperimentalCondition experimentalCondition = well.getExperimentalCondition();
		if (isExistColor(experimentalCondition)) {
			return mapExperimentColor.get(experimentalCondition).deriveColor(1, 1, 1, 0.5);
		}
		return Color.WHITE;

	}

	private boolean isExistColor(final ExperimentalCondition experimentalCondition) {
		return mapExperimentColor.containsKey(experimentalCondition);
	}

	public void extract(final Experiment xp) {
		mapExperimentColor.clear();
		colorGenerator.restartColor();
		xp.getPlates().forEach(pl -> pl.getWells().forEach(w -> {
			final ExperimentalCondition xpCondition = extractExperimentalCondition(w);
			generateColorForXpCondition(xpCondition);
		}));
	}

	private void generateColorForXpCondition(final ExperimentalCondition xpCondition) {
		if (couleurAGenerer(xpCondition)) {
			mapExperimentColor.put(xpCondition, colorGenerator.getNextColorReverse());
		}

	}

	private boolean couleurAGenerer(final ExperimentalCondition xpCondition) {
		return xpCondition != null && !isExistColor(xpCondition);
	}

	private ExperimentalCondition extractExperimentalCondition(final Well w) {

		return w.getExperimentalCondition();
	}
}
