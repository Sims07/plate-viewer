package sims.chareyron.plateviewer.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sims.chareyron.plateviewer.PlateViewerApplication;
import sims.chareyron.plateviewer.model.Experiment;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PlateViewerApplication.class)
public class PlateViewerLoaderTest {

	@Autowired
	private PlateViewerLoader plateViewerLoader;

	@Test
	public void testLoading() throws FileNotFoundException, IOException, PlateLoadingException {
		Experiment xp = plateViewerLoader.loadExperiment(new File("src/test/resources/plqteviewer-input.csv"));
		System.out.println(xp);
	}
}
