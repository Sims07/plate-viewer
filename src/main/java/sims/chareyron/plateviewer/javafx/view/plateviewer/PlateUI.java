package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import sims.chareyron.plateviewer.model.Animal;
import sims.chareyron.plateviewer.model.Plate;
import sims.chareyron.plateviewer.model.Well;

public class PlateUI extends Pane {
	private final PlateTypeFactory plateTypeFactory = new PlateTypeFactory();
	private double rayon;
	private double height;
	private final PlatePreferenceUI platePreferenceUI;
	private final PoolGenerator poolGenerator;

	private final ObjectProperty<Font> fontTTproperty;
	private final ObjectProperty<Font> samplenumproperty;

	private final ExperimentalConditionExtractor experimentalConditionExtractor;

	public PlateUI(final Plate pl, final PlatePreferenceUI pref, final double plateWidth,
			final PoolGenerator poolGenerator, final ObjectProperty<Font> attFont, final ObjectProperty<Font> asfont,
			final ExperimentalConditionExtractor experimentalConditionExtractor) {
		super();
		fontTTproperty = attFont;
		samplenumproperty = asfont;
		this.platePreferenceUI = pref;
		this.poolGenerator = poolGenerator;
		this.experimentalConditionExtractor = experimentalConditionExtractor;
		initPool(pl);
		drawPlate(pl, pref, plateWidth);
	}

	private void initPool(final Plate pl) {

		pl.getWells().forEach(w -> {
			if (!w.isEmpty()) {
				w.getSamples().forEach(sample -> {
					final List<Animal> pool = sample.getInputAnimals();
					final MicePoolInfo mcpool = new MicePoolInfo(w, pool);
					poolGenerator.addPoolInfo(mcpool);
				});
			}
		});

	}

	public void drawPlate(final Plate plate, final PlatePreferenceUI pref, final double plateWidth) {
		final PlateType plateType = plateTypeFactory.buildPlateType(plate.getNbWells());
		getChildren().clear();
		final int nbEcart = plateType.getNbWellX() - 1;
		final int nbRayon = plateType.getNbWellX() * 2;
		rayon = (plateWidth - 2 * pref.getPadding() - nbEcart * pref.getOffset()) / nbRayon;
		height = pref.getPadding() * 2 + plateType.getNbWellY() * rayon * 2 + 3 * pref.getOffset();
		final Rectangle plateRect = new Rectangle(0, 0, plateWidth, height);
		plateRect.setFill(Color.WHITESMOKE);
		plateRect.setArcHeight(5);
		plateRect.setArcWidth(5);
		plateRect.setStroke(Color.BLACK);
		getChildren().add(plateRect);
		drawWells(plate.getWells());

	}

	private void drawWells(final List<Well> wells) {
		final PlateType plateType = plateTypeFactory.buildPlateType(wells.size());
		for (int y = 0; y < plateType.getNbWellY(); y++) {
			for (int x = 0; x < plateType.getNbWellX(); x++) {
				drawWell(findWellByIndex(wells, y * plateType.getNbWellX() + x), x, y);
			}
		}

	}

	private void drawWell(final Well well, final int x, final int y) {
		final double centerX = platePreferenceUI.getPadding() + rayon * (2 * x + 1) + x * platePreferenceUI.getOffset();
		final double centerY = platePreferenceUI.getPadding() + rayon * (2 * y + 1) + y * platePreferenceUI.getOffset();

		final Circle wc = new Circle(centerX, centerY, rayon);
		wc.setStroke(Color.BLACK);

		wc.setFill(experimentalConditionExtractor.getColorForWell(well));
		if (!well.isEmpty()) {
			wc.setStrokeWidth(2);
		}

		final BorderPane borderPane = new BorderPane();

		final VBox content = new VBox();
		borderPane.setCenter(content);
		borderPane.setMaxWidth(rayon * 2);
		borderPane.setMaxHeight(rayon * 2);

		// content.setSpacing(5);
		content.setAlignment(Pos.CENTER);
		// VBox vbox = new VBox();
		final Label krbh = new Label(String.valueOf(well.getKRBH()));
		final List<Label> ttsLabel = getDisplayWellContent(well);

		krbh.fontProperty().bind(fontTTproperty);
		krbh.setStyle("-fx-font-weight: bold;");
		content.setMaxWidth(rayon * 9);

		final Label nbEchantillon = new Label();
		content.setSpacing(0);
		if (!well.isEmpty()) {
			content.getChildren().add(krbh);
		}
		content.getChildren().addAll(ttsLabel);
		content.setAlignment(Pos.CENTER);
		// content.getChildren().add(content);
		if (!well.isEmpty()) {
			nbEchantillon.setText(well.getSamples().get(0).getId());
			nbEchantillon.setTextFill(Color.FIREBRICK);
			nbEchantillon.setAlignment(Pos.BOTTOM_RIGHT);
			nbEchantillon.setStyle("-fx-font-family:Calibri;-fx-font-size:13;-fx-font-color:red;");
			nbEchantillon.fontProperty().bind(samplenumproperty);
			wc.strokeProperty().bind(well.colorProperty());
			BorderPane.setAlignment(nbEchantillon, Pos.CENTER);
			BorderPane.setMargin(nbEchantillon, new Insets(0, 0, 10, 0));
			borderPane.setBottom(nbEchantillon);
		} else {
			wc.setFill(Color.LIGHTGRAY);
		}
		final StackPane stack = new StackPane(wc, borderPane);
		stack.setLayoutX(centerX - rayon);
		stack.setLayoutY(centerY - rayon);
		getChildren().add(stack);

	}

	private List<Label> getDisplayWellContent(final Well well) {
		final List<Label> contentBuilder = new ArrayList<>();
		if (!well.isEmpty()) {
			// contentBuilder.append(well.getKRBH());
			well.getTts().forEach(tt -> {
				final Label ttsLabel = new Label("+" + (tt.getName() + tt.getDose() + "\n"));
				ttsLabel.fontProperty().bind(fontTTproperty);
				ttsLabel.setStyle("-fx-font-weight: bold;");
				ttsLabel.setBorder(Border.EMPTY);
				ttsLabel.setWrapText(true);
				contentBuilder.add(ttsLabel);
			});
		}
		return contentBuilder;
	}

	private Well findWellByIndex(final List<Well> wells, final int index) {
		return wells.parallelStream().filter(w -> w.getIndex() == (index + 1)).findFirst().get();
	}

	public Font getTtFont() {
		return fontTTproperty.get();
	}

	public void setTtFont(final Font ttFont) {
		fontTTproperty.set(ttFont);
	}

}
