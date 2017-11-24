package sims.chareyron.plateviewer.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import sims.chareyron.plateviewer.model.Experiment;

public interface PlateViewerLoader {
	Experiment loadExperiment(File csvInput) throws FileNotFoundException, IOException, PlateLoadingException;

	Experiment getLoadedExperiment();
}
