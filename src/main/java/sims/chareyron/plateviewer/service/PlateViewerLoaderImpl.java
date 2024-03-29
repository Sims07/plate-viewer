package sims.chareyron.plateviewer.service;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import sims.chareyron.plateviewer.model.Animal;
import sims.chareyron.plateviewer.model.Cage;
import sims.chareyron.plateviewer.model.Experiment;
import sims.chareyron.plateviewer.model.Lot;
import sims.chareyron.plateviewer.model.Plate;
import sims.chareyron.plateviewer.model.Sample;
import sims.chareyron.plateviewer.model.Strain;
import sims.chareyron.plateviewer.model.Traitement;
import sims.chareyron.plateviewer.model.Well;

@Service
public class PlateViewerLoaderImpl implements PlateViewerLoader {
	private static final String DATE_PATTERN = "dd/MM/yyyy";
	private static final String CONTENT_SEPARATOR = "_";
	private static final String PLATE = "Plate";
	private static final String ANIMAL = "Animal";
	private static final Logger logger = LoggerFactory.getLogger(PlateViewerLoader.class);
	private static final String SEPARATOR_CHAR = ";";

	@Autowired
	private CsvInfoLocator csvInfoLocator;
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_PATTERN);
	private final Map<String, Animal> animalsMap = new HashMap<>();
	private final Map<String, Cage> cagesMap = new HashMap<>();
	private final Map<String, Strain> strainsMap = new HashMap<>();
	private Experiment currentExperiment;

	@Override
	public Experiment loadExperiment(final File csvInput) throws IOException, PlateLoadingException {
		final String xpFileContent = FileUtils.readFileToString(csvInput, "Cp1252");
		final String[] lines = xpFileContent.split("\\R");

		// extract experiment info
		final Experiment xp = new Experiment(extractValue(lines, csvInfoLocator.getXpTitle()),
				extractValue(lines, csvInfoLocator.getXpDescription()), extractDate(lines, csvInfoLocator.getXpDate()));

		// find animal index
		final int speciesIndex = extractIndex(lines, 1, ANIMAL) + 1;
		final String speciesValue = extractValue(lines, new Point(1, speciesIndex));

		// search list of this species of animal
		final int speciesValueIndex = extractIndex(lines, speciesIndex, speciesValue);
		final List<Animal> animals = extractAnimals(speciesValueIndex, speciesValue, lines);
		animals.stream().forEach(animal -> animalsMap.put(animal.getName(), animal));
		xp.addAll(animals);

		// get plates
		final List<Plate> plates = extractPlates(lines);
		xp.setPlates(plates);
		this.currentExperiment = xp;
		return xp;
	}

	private List<Plate> extractPlates(final String[] linesCsv) throws PlateLoadingException {
		final List<Plate> resPlates = new ArrayList<>();

		final long nbPlates = Arrays.asList(linesCsv).parallelStream().filter(line -> {
			return line.contains(PLATE);
		}).count();
		logger.info("Get {} plates", nbPlates);
		int nextPLateIndex = 0;
		for (int i = 0; i < nbPlates; i++) {
			final int plateIndex = extractIndex(linesCsv, nextPLateIndex, PLATE);
			resPlates.add(extractPlate(linesCsv, plateIndex));
			nextPLateIndex = plateIndex + 1;
		}
		return resPlates;
	}

	/**
	 * Well;Nb echantillon;Ilots;Glucose Conc (Mmol);TTT;Lot TTT;;;;
	 * 
	 * @param linesCsv
	 * @param plateIndex
	 * @return
	 * @throws PlateLoadingException
	 * @throws NumberFormatException
	 */
	private Plate extractPlate(final String[] linesCsv, final int plateIndex)
			throws NumberFormatException, PlateLoadingException {
		// plateIndex pointe sur le titre Plate
		final String name = linesCsv[plateIndex + 1].split(SEPARATOR_CHAR)[1];
		final Plate plate = new Plate(name);
		int indexCurrentWell = plateIndex + 3;
		do {
			plate.addWell(extractWell(linesCsv[indexCurrentWell], plate));
			indexCurrentWell++;
		} while (indexCurrentWell < linesCsv.length && linesCsv[indexCurrentWell].split(SEPARATOR_CHAR).length > 0);
		return plate;
	}

	/**
	 * Well;Nb echantillon;Ilots;Glucose Conc (Mmol);T1;Lot T1;Tn;Lot Tn;;
	 * 
	 * @param lineWell
	 * @param plate
	 * @return
	 * @throws PlateLoadingException
	 * @throws NumberFormatException
	 */
	private Well extractWell(final String lineWell, final Plate plate)
			throws NumberFormatException, PlateLoadingException {
		Well well = null;
		try {
			final String extractValue = extractValue(lineWell, 0);
			final Integer wellIndex = Integer.valueOf(extractValue);
			logger.info("Load well {}", wellIndex);
			if (lineWell.split(SEPARATOR_CHAR).length > 2) {
				well = new Well(wellIndex, Double.valueOf(extractValue(lineWell, 3)), plate,
						extractTraitements(lineWell), extractSamples(lineWell));
			} else {
				well = new Well(wellIndex);
			}
		} catch (final NumberFormatException ne) {
			throw new PlateLoadingException("Decimal format must used '.'.\n\n-Value:" + extractValue(lineWell, 3)
					+ "\n\n-Locale:" + Locale.getDefault());
		}
		return well;
	}

	/**
	 * sample1-sample2-..
	 * 
	 * @param lineWell
	 * @return
	 * @throws PlateLoadingException
	 */
	private List<Sample> extractSamples(final String lineWell) throws PlateLoadingException {
		final List<Sample> res = new ArrayList<>();
		String sampleId;
		try {
			sampleId = extractValue(lineWell, 1);
		} catch (final PlateLoadingException e) {
			throw new PlateLoadingException("Sample id is missing.\n\n" + e.getMessage());
		}
		String samples;
		try {
			samples = extractValue(lineWell, 2);
		} catch (final PlateLoadingException e) {
			throw new PlateLoadingException("Sample is missing.\n\n" + e.getMessage());
		}
		logger.info("Extract samples from {}", samples);
		final Sample sample = new Sample(sampleId);
		res.add(sample);
		Arrays.asList(samples.split(CONTENT_SEPARATOR)).forEach(animal -> sample.addInpuAnimal(animalsMap.get(animal)));
		return res;
	}

	/**
	 * tt begin at the column index 4 nom-dose-duree
	 * 
	 * @param lineWell
	 * @return
	 * @throws PlateLoadingException
	 */
	private List<Traitement> extractTraitements(final String lineWell) throws PlateLoadingException {
		final List<Traitement> tts = new ArrayList<>();

		final int nbTraitement = (lineWell.split(SEPARATOR_CHAR).length - 4) / 2;
		logger.info("Nb traitement to get: {} from {}", nbTraitement, lineWell);
		int currentTtindex = 4;
		int currentTtLotindex = 5;
		for (int i = 0; i < nbTraitement; i++) {
			final String ttFlatInfo = extractValue(lineWell, currentTtindex);
			if (!CONTENT_SEPARATOR.equals(ttFlatInfo)) {
				final String ttLotFlatInfo = extractValue(lineWell, currentTtLotindex);
				final String[] ttFlatInfoArray = ttFlatInfo.split(CONTENT_SEPARATOR);
				try {
					final Traitement tt = new Traitement(extract(ttFlatInfoArray, 0), extract(ttFlatInfoArray, 1),
							extract(ttFlatInfoArray, 2), extractLot(ttLotFlatInfo));
					tts.add(tt);
				} catch (final Exception e) {

					System.err.println("Treatement could not be loaded");
					e.printStackTrace();
					throw new PlateLoadingException("The following treatment could not be parsed :\nTreatment:"
							+ ttFlatInfo + "\nLine:" + lineWell + "\nDue to :" + e.getMessage());
				}
			}
			currentTtindex += 2;
			currentTtLotindex += 2;

		}
		return tts;
	}

	private String extract(final String[] ttFlatInfoArray, final int i) {
		// TODO Auto-generated method stub
		return ttFlatInfoArray.length > i ? ttFlatInfoArray[i].trim() : "";
	}

	private Lot extractLot(final String ttLotFlatInfo) throws PlateLoadingException {
		logger.info("Get tt lot from {}", ttLotFlatInfo);
		final String[] ttLotFlatInfoArray = ttLotFlatInfo.split(CONTENT_SEPARATOR);

		Date peremptionDate = null;
		try {
			final String peremptionDateStr = ttLotFlatInfoArray[2];
			peremptionDate = peremptionDateStr != null ? dateFormatter.parse(peremptionDateStr) : null;
		} catch (final Exception e) {
			peremptionDate = new Date(0);
			// throw new PlateLoadingException("Date format invalid it must
			// follow the pattern:" + DATE_PATTERN);
		}
		return new Lot(extract(ttLotFlatInfoArray, 0), peremptionDate, extract(ttLotFlatInfoArray, 1));
	}

	/**
	 * Souche;Cage;Nom;ID souris;DDN;R�gime/Traitement;BW (g);BL
	 * (cm);Description; s1;c1;souris1;30547;08/05/2016;fat;32;56;des;
	 * 
	 * @param speciesValueIndex
	 * @param speciesValue
	 * @param lines
	 * @return
	 * @throws PlateLoadingException
	 */
	private List<Animal> extractAnimals(final int speciesValueIndex, final String speciesValue, final String[] lines)
			throws PlateLoadingException {
		final List<Animal> animals = new ArrayList<>();
		final int lastIndex = extractFirstEmptyRawIndex(lines, speciesValueIndex);

		final List<String> linesList = Arrays.asList(lines).subList(speciesValueIndex + 2, lastIndex);
		try {
			linesList.forEach(lineAnimal -> {
				if (StringUtils.isEmpty(lineAnimal.split(SEPARATOR_CHAR)[0])) {
					throw new BreakException();
				}
				try {
					animals.add(extractAnimal(lineAnimal, speciesValue));
				} catch (final PlateLoadingException e) {
					throw new BreakException(e);
				}
			});
		} catch (final BreakException be) {
			logger.info("Animals are loaded");
			if (be.getCause() != null) {
				throw new PlateLoadingException(be.getPlEx().getMessage());
			}
		}
		return animals;
	}

	private int extractFirstEmptyRawIndex(final String[] lines, final int from) {
		int index = from;
		final boolean found = false;
		while (!found) {
			final String[] checkLine = lines[index].split(SEPARATOR_CHAR);
			if (checkLine.length == 0) {
				break;
			} else {
				index++;
			}
		}
		return index;
	}

	/**
	 * Souche 0;Cage1;Nom2;ID souris3;DDN4;R�gime/Traitement5;BW (g)6;BL7
	 * (cm);Description8;
	 * 
	 * s1;c1;souris1;30547;08/05/2016;fat;32;56;des;
	 * 
	 * @param lineAnimal
	 * @param speciesValue
	 * @return
	 * @throws PlateLoadingException
	 */
	private Animal extractAnimal(final String lineAnimal, final String speciesValue) throws PlateLoadingException {
		Animal animalExtracted;
		try {
			animalExtracted = new Animal(extractDate(lineAnimal, 4), speciesValue, extractValue(lineAnimal, 2),
					extractValue(lineAnimal, 7), extractValue(lineAnimal, 8), extractValue(lineAnimal, 9),
					extractValue(lineAnimal, 5), extractValue(lineAnimal, 9), extractValue(lineAnimal, 3));
		} catch (final PlateLoadingException e1) {
			throw new PlateLoadingException("Animal's value is missing.\n\n" + e1.getMessage()
					+ "\nExpected Values:Strain;Cage;Name;ID;Date of Birth;Age;Sex;BW;BL;Description");
		}
		// get cage
		Cage cage;
		try {
			cage = extractCage(extractValue(lineAnimal, 1));
		} catch (final PlateLoadingException e1) {
			throw new PlateLoadingException("Cage value is missing.\n\n" + e1.getMessage());
		}
		cage.addAnimalInCage(animalExtracted);

		// get strain
		Strain strain;
		try {
			strain = extractStrain(extractValue(lineAnimal, 0));
		} catch (final PlateLoadingException e) {
			throw new PlateLoadingException("Strain value is missing.\n\n" + e.getMessage());
		}
		strain.addAnimalInStrain(animalExtracted);
		return animalExtracted;
	}

	private Strain extractStrain(final String extractValue) {
		Strain extractedStrain = null;
		final boolean strainExisted = strainsMap.containsKey(extractValue);
		if (strainExisted) {
			extractedStrain = strainsMap.get(extractValue);
		} else {
			extractedStrain = new Strain(extractValue);
			strainsMap.put(extractValue, extractedStrain);
		}
		return extractedStrain;
	}

	private Cage extractCage(final String extractValue) {
		Cage res = null;
		final boolean cageExisted = cagesMap.containsKey(extractValue);
		if (cageExisted) {
			// add animal in cage
			res = cagesMap.get(extractValue);
		} else {
			res = new Cage(extractValue);
			cagesMap.put(extractValue, res);
		}
		return res;
	}

	private String extractValue(final String line, final int index) throws PlateLoadingException {

		logger.info("TRY TO FIND VALUE AT {} INDEX IN {}", index, line);
		String res = "?";
		try {
			res = line.split(SEPARATOR_CHAR)[index].trim();
		} catch (final Exception e) {
			System.err.println("value not found from:" + line + "," + index);
			e.printStackTrace();
			throw new PlateLoadingException(
					"Used '_' to declare an empty value.\n\nA value is requested but not found :\n\n-Index:" + index
							+ "\n\n-Line:" + line);
		}
		return res;
	}

	private Date extractDate(final String line, final int indexValue) throws PlateLoadingException {
		logger.info("TRY TO FIND DATE AT {} INDEX IN {}", indexValue, line);

		try {
			final String date = line.split(SEPARATOR_CHAR)[indexValue];
			return date != null && !date.trim().equals("_") ? dateFormatter.parse(date) : null;
		} catch (final Exception e) {
			System.err.println("date not found from:" + line + "," + indexValue);
			e.printStackTrace();
			throw new PlateLoadingException("A date is not correctly formated :\n\n-Index:" + indexValue + "\n\n-Line:"
					+ line + "\n\n-Pattern:" + DATE_PATTERN);
		}
	}

	private int extractIndex(final String[] lines, final int from, final String titleToFind) {
		int index = from;
		final boolean found = false;
		while (!found) {
			final String[] checkLine = lines[index].split(SEPARATOR_CHAR);
			if (checkLine.length > 0) {
				final String checkString = checkLine[0];
				if (checkString.equals(titleToFind)) {
					break;
				} else {
					index++;
				}
			} else {
				index++;
			}
		}
		return index;
	}

	private Date extractDate(final String[] lines, final Point indexValue) throws PlateLoadingException {
		logger.info("TRY TO FIND DATE AT {} INDEX IN {}", indexValue.x, lines[indexValue.y]);
		final String date = lines[indexValue.y].split(SEPARATOR_CHAR)[indexValue.x];
		try {
			return date != null ? dateFormatter.parse(date) : new Date();
		} catch (final ParseException e) {
			throw new PlateLoadingException("The date :" + date + " must follow the pattern:" + DATE_PATTERN);

		}
	}

	private String extractValue(final String[] lines, final Point indexValue) throws PlateLoadingException {
		logger.info("TRY TO FIND VALUE AT {} INDEX IN {}", indexValue.x, lines[indexValue.y]);
		try {
			return lines[indexValue.y].split(SEPARATOR_CHAR)[indexValue.x];
		} catch (final Exception e) {
			throw new PlateLoadingException(
					"Check the line is not emprty because a value is requested but not found at the line:\n\n-line:"
							+ (int) (1 + indexValue.getY()) + "\n\n-item:" + (int) indexValue.getX());
		}
	}

	@Override
	public Experiment getLoadedExperiment() {
		return currentExperiment;
	}

}
