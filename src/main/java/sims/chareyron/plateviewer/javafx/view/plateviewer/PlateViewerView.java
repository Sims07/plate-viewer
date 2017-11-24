package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.controlsfx.dialog.FontSelectorDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sims.chareyron.plateviewer.javafx.controller.ExecutorFS;
import sims.chareyron.plateviewer.javafx.framework.mvp.AbstractViewWithUiHandlers;
import sims.chareyron.plateviewer.javafx.framework.mvp.Slot;
import sims.chareyron.plateviewer.javafx.framework.mvp.View;
import sims.chareyron.plateviewer.model.Experiment;
import sims.chareyron.plateviewer.model.Plate;

@Component
public class PlateViewerView extends AbstractViewWithUiHandlers<PlateViewerUiHandlers>
		implements PlateViewerPresenter.MyView {
	private static final String PLATE_EXPORT_FILE_PREFIX = "Plate_";
	private static final String POOL_EXPORT_FILE_NAME_EXTENSION = "Pool.png";
	private static final int PLATE_OFFSET = 2;
	private static final int PADDING_PLATE = 10;
	private static final double ZOOM_INT_VALUE = 50.0;
	private static final int LEFT_MENU_WIDTH = 40;
	@FXML
	VBox plateContainer;
	@FXML
	Pane xpPane;
	@FXML
	Pane body;
	@FXML
	Label xpTitle;
	@FXML
	Label xpDate;
	@FXML
	Label xpDescription;
	@FXML
	ScrollPane scrollPaneBody;
	@FXML
	Slider slider;

	PlatePreferenceUI platePref;
	DirectoryChooser outputExport;
	Experiment xp;
	PlatePoolUi poolUi;
	SimpleDateFormat spf = new SimpleDateFormat("YYYY-MM-dd");
	List<PlatePaneDecorator> platePanes;
	@Autowired
	ExecutorFS executorFS;
	Double zoom = 50.0;
	Font selectedFont = new Font("Calibri BOLD", 20);
	Font selectedSampleFont = new Font("Calibri BOLD", 18);
	private final ObjectProperty<Double> zoomProperty = new SimpleObjectProperty<>(this, "zoom");
	private final ObjectProperty<Font> fontTTproperty = new SimpleObjectProperty<>(this, "selectedFont");
	private final ObjectProperty<Font> fontSampleproperty = new SimpleObjectProperty<>(this, "selectedSampleFont");

	private final ExperimentalConditionExtractor experimentalConditionExtractor = new ExperimentalConditionExtractor();

	public Double getZoom() {
		return zoomProperty.getValue();
	}

	public void setZoom(final Double zoom) {
		zoomProperty.set(zoom);
	}

	public Font getSelectedFont() {
		return fontTTproperty.get();
	}

	public void setSelectedFont(final Font selectedFont) {
		this.fontTTproperty.set(selectedFont);
	}

	public Font getSelectedSampleFont() {
		return fontSampleproperty.get();
	}

	public void setSelectedSampleFont(final Font selectedFont) {
		this.fontSampleproperty.set(selectedFont);
	}

	@Override
	public void setInSlot(final Slot slot, final View view) {

	}

	@Override
	public void setViewBindings(final Stage stage) {
		setZoom(ZOOM_INT_VALUE);

		fontTTproperty.set(selectedFont);
		fontSampleproperty.set(selectedSampleFont);
		outputExport = new DirectoryChooser();
		platePref = new PlatePreferenceUI(PADDING_PLATE, PLATE_OFFSET);
		platePanes = new ArrayList<>();
		body.prefWidthProperty().bind(stage.widthProperty());
		body.prefHeightProperty().bind(stage.heightProperty());
		slider.setValue(zoom);
		slider.valueProperty().addListener(
				(final ObservableValue<? extends Number> ov, final Number old_val, final Number new_val) -> {
					setZoom((Double) new_val);
					refreshExperiment();
				});
	}

	public void refreshExperiment() {
		setExperiment(xp);
	}

	@Override
	public void setExperiment(final Experiment xp) {
		experimentalConditionExtractor.extract(xp);

		platePanes.clear();
		this.xp = xp;
		xpTitle.setText(xp.getTitle());
		xpDate.setText(formatDate(xp.getXpDate()));
		xpDescription.setText(xp.getDescription());
		plateContainer.getChildren().clear();

		final PoolGenerator poolGenerator = new PoolGeneratorImpl(new ColorGeneratorImpl());

		poolUi = new PlatePoolUi();
		plateContainer.getChildren().add(poolUi);
		VBox.setMargin(poolUi, new Insets(5, 0, 0, 10));

		final double widthPlates = body.getPrefWidth() * getZoom() / 100;
		xpPane.setMaxWidth(widthPlates);
		poolUi.setMaxWidth(widthPlates);
		xp.getPlates().stream().filter(p -> !p.isEmpty()).forEach(addPlateUI(xp, poolGenerator, widthPlates));
		poolUi.setPoolModel(poolGenerator.getPool());
		final Pane footerPane = new Pane();
		footerPane.setMinHeight(40);
		plateContainer.getChildren().add(footerPane);

	}

	private Consumer<? super Plate> addPlateUI(final Experiment xp, final PoolGenerator poolGenerator,
			final double widthPlates) {
		return pl -> {
			final GridPane plateXp = new GridPane();
			final VBox plateVb = buildPlateUI(poolGenerator, widthPlates, pl, plateXp);
			platePanes.add(new PlatePaneDecorator(plateVb, "Plate:" + pl.getId(), xp, pl));
			VBox.setMargin(plateXp, new Insets(5, 5, 5, 10));
			plateContainer.getChildren().add(plateVb);
		};
	}

	private VBox buildPlateUI(final PoolGenerator poolGenerator, final double widthPlates, final Plate pl,
			final GridPane plateXp) {
		final ColumnConstraints firstCol = new ColumnConstraints();
		firstCol.setPercentWidth(100);
		firstCol.setHalignment(HPos.CENTER);
		final ColumnConstraints miceCol = new ColumnConstraints();
		miceCol.setPercentWidth(100);
		miceCol.setHalignment(HPos.CENTER);

		plateXp.getColumnConstraints().clear();
		plateXp.getColumnConstraints().addAll(firstCol, miceCol);
		final PlateUI plateUi = new PlateUI(pl, platePref, widthPlates - LEFT_MENU_WIDTH, poolGenerator, fontTTproperty,
				fontSampleproperty, experimentalConditionExtractor);

		plateXp.add(plateUi, 0, 0);
		final VBox plateVb = new VBox();
		final Label plateLabel = new Label("Plate:" + pl.getId());
		plateLabel.setFont(new Font("Calibria Bold", 20));
		plateVb.getChildren().add(plateLabel);
		plateVb.getChildren().add(plateXp);
		return plateVb;
	}

	public void onChangeFontClicked() {
		final FontSelectorDialog selecto = new FontSelectorDialog(getSelectedFont());

		final Optional<Font> newFont = selecto.showAndWait();
		if (newFont.isPresent()) {
			setSelectedFont(newFont.get());
		}

	}

	public void onChangeSampleFontClicked() {
		final FontSelectorDialog selecto = new FontSelectorDialog(getSelectedSampleFont());

		final Optional<Font> newFont = selecto.showAndWait();
		if (newFont.isPresent()) {
			setSelectedSampleFont(newFont.get());
		}

	}

	public void onPrintClicked() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				final File exportDir = new File("output");
				if (!exportDir.exists()) {
					exportDir.mkdirs();
				}

				// generate xp output dir
				final File xpDir = new File(exportDir,
						formatDate(xp.getXpDate()) + "_" + xp.getTitle().replace(" ", ""));
				if (!xpDir.exists()) {
					xpDir.mkdirs();
				}

				// generate pool ui
				final WritableImage snapshot = poolUi.snapshot(getParams(), null);
				final File filePool = new File(xpDir, POOL_EXPORT_FILE_NAME_EXTENSION);
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", filePool);
				} catch (final IOException e) {
					e.printStackTrace();
				}

				// generate image by plate
				platePanes.stream().forEach(pl -> {
					final WritableImage snapshotPl = pl.getDecorated().snapshot(getParams(), null);
					final File filePl = new File(xpDir, PLATE_EXPORT_FILE_PREFIX + pl.getPlate().getId() + ".png");
					try {
						ImageIO.write(SwingFXUtils.fromFXImage(snapshotPl, null), "png", filePl);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				});

				final Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Experiment has been exported at the following path:\n" + xpDir.getAbsolutePath());
				alert.show();
			}
		});

	}

	private String formatDate(final Date xpDate2) {

		return spf.format(xpDate2);
	}

	private SnapshotParameters getParams() {
		final SnapshotParameters snapshotParameters = new SnapshotParameters();
		snapshotParameters.setDepthBuffer(true);
		snapshotParameters.setTransform(new Scale(2, 2));
		return snapshotParameters;
	}

}
