package sims.chareyron.plateviewer.javafx.view.plateviewer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sims.chareyron.plateviewer.PlateViewerApplication;
import sims.chareyron.plateviewer.model.Animal;
import sims.chareyron.plateviewer.model.Well;

public class PlatePoolUi extends GridPane {
	private static final int WIDTH_EDIT_INPUTS = 100;
	private static final SimpleDateFormat FORMATTER_DATE_BIRTHDAY = new SimpleDateFormat("dd/MM/yyyy");
	public static final Font calibri = new Font("Calibri BOLD", 12);
	final ContextMenu contextMenu = new ContextMenu();
	private Well wellColorToUpdate;
	private Pane paneToUpdate;
	private Label labelToUpdate;

	enum Column {
		COLOR("Color", 0), STRAIN("Strain", 1), SEX("Sex", 2), BIRTHDAY("Birthday", 3), AGE("Age", 4);
		public String title;
		public int index;

		public boolean isFirstColumn() {
			return index == 0;
		}

		Column(final String title, final int index) {
			this.title = title;
			this.index = index;
		}
	}

	public void setPoolModel(final Map<MicePoolInfo, ObservableValue<Color>> pool) {
		addColorMenuOnColorCell();
		buildPoolTable(pool);
	}

	private void buildPoolTable(final Map<MicePoolInfo, ObservableValue<Color>> pool) {
		final AtomicInteger rawIndex = new AtomicInteger(0);
		final AtomicInteger columnIndex = new AtomicInteger(0);
		buildColumnHeaders(rawIndex, columnIndex);

		rawIndex.incrementAndGet();
		feedPoolTable(pool, rawIndex);

		adjustColumnSizeProportion();
	}

	private void adjustColumnSizeProportion() {
		final ColumnConstraints firstCol = new ColumnConstraints();
		firstCol.setPercentWidth(20);
		firstCol.setHalignment(HPos.CENTER);
		final ColumnConstraints strainCol = new ColumnConstraints();
		strainCol.setPercentWidth(25);
		strainCol.setHalignment(HPos.CENTER);
		final ColumnConstraints sexeCol = new ColumnConstraints();
		sexeCol.setPercentWidth(15);
		sexeCol.setHalignment(HPos.CENTER);
		final ColumnConstraints birthDayCol = new ColumnConstraints();
		birthDayCol.setPercentWidth(25);
		birthDayCol.setHalignment(HPos.CENTER);
		final ColumnConstraints ageCol = new ColumnConstraints();
		ageCol.setPercentWidth(10);
		ageCol.setHalignment(HPos.CENTER);

		getColumnConstraints().clear();
		getColumnConstraints().addAll(firstCol, strainCol, sexeCol, birthDayCol, ageCol);
	}

	private void buildColumnHeaders(final AtomicInteger rawIndex, final AtomicInteger columnIndex) {
		add(wrap(new Label("Pools"), Color.LIGHTGRAY, Pos.CENTER, true, true), columnIndex.get(), rawIndex.get(), 5, 1);
		// add row title
		rawIndex.incrementAndGet();
		Arrays.stream(Column.values())
				.forEach(col -> add(wrap(new Label(col.title), Color.LIGHTGRAY, Pos.CENTER, col.isFirstColumn(), false),
						col.index, rawIndex.get()));
	}

	private void feedPoolTable(final Map<MicePoolInfo, ObservableValue<Color>> pool, final AtomicInteger rawIndex) {
		final TreeSet<MicePoolInfo> orderedpool = buildOrderedSetByPoolIndex(pool);
		orderedpool.forEach(key -> {
			final List<Animal> mices = key.getMices();

			add(wrap(new Label("Pool-" + key.getIndex()), key.getWell(), Pos.CENTER, true, false), 0, rawIndex.get(), 1,
					mices.size());
			// display mouse names

			mices.forEach(mice -> {
				feedMiceLine(rawIndex, mice);
				rawIndex.incrementAndGet();
			});

		});
	}

	private void feedMiceLine(final AtomicInteger rawIndex, final Animal mice) {
		add(wrap(mice.getStrain().getStrain(), Color.WHITE, Pos.CENTER), Column.STRAIN.index, rawIndex.get());
		add(wrap(FORMATTER_DATE_BIRTHDAY.format(mice.getBirthday()), Color.WHITE, Pos.CENTER), Column.BIRTHDAY.index,
				rawIndex.get());
		add(wrap(mice.getAge(), Color.WHITE, Pos.CENTER), Column.AGE.index, rawIndex.get());
		add(wrap(mice.getSex(), Color.WHITE, Pos.CENTER), Column.SEX.index, rawIndex.get());
	}

	private Node wrap(final String textToWrap, final Color color, final Pos pos) {
		final Label wrapLabel = new Label(textToWrap);
		wrapLabel.setWrapText(true);
		return wrap(wrapLabel, color, pos);
	}

	private TreeSet<MicePoolInfo> buildOrderedSetByPoolIndex(final Map<MicePoolInfo, ObservableValue<Color>> pool) {
		final Set<MicePoolInfo> poolKey = pool.keySet();
		final TreeSet<MicePoolInfo> orderedpool = new TreeSet<>(new Comparator<MicePoolInfo>() {

			@Override
			public int compare(final MicePoolInfo o1, final MicePoolInfo o2) {
				return o1.getIndex() - o2.getIndex();
			}
		});
		orderedpool.addAll(poolKey);
		return orderedpool;
	}

	private void addColorMenuOnColorCell() {
		final MenuItem editProperty = new MenuItem("Edit");

		contextMenu.getItems().addAll(editProperty);
		editProperty.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {

				onEditClicked();
			}

			private void onEditClicked() {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(PlateViewerApplication.MAIN_STAGE);
				final Pane pane = buildEditContent();
				final Scene dialogScene = new Scene(pane, 200, 80);
				dialog.setScene(dialogScene);
				dialog.show();
			}

			private Pane buildEditContent() {
				final GridPane editPanel = new GridPane();
				editPanel.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
				final int fieldColumnIndex = 0;
				final int colorRawIndex = 1;
				final int nameRawIndex = 0;

				addPoolNameFormToGrid(editPanel, fieldColumnIndex, nameRawIndex);

				addColorFormToGrid(editPanel, fieldColumnIndex, colorRawIndex);

				return editPanel;
			}

			private void addColorFormToGrid(final GridPane editPanel, final int fieldColumnIndex,
					final int colorRawIndex) {
				final ColorPicker cp = new ColorPicker(wellColorToUpdate.getColor());
				cp.setMinWidth(WIDTH_EDIT_INPUTS);
				cp.setMaxWidth(WIDTH_EDIT_INPUTS);
				cp.valueProperty().addListener(new ChangeListener<Color>() {

					@Override
					public void changed(final ObservableValue<? extends Color> observable, final Color oldValue,
							final Color newValue) {

						paneToUpdate.setStyle(paneToUpdate.getStyle() + "-fx-background-color: "
								+ newValue.toString().replace("0x", "#") + ";");
						wellColorToUpdate.setColor(newValue);

					}
				});
				editPanel.add(new Label("Pool Color: "), fieldColumnIndex, colorRawIndex);
				editPanel.add(cp, 1, colorRawIndex);
			}

			private void addPoolNameFormToGrid(final GridPane editPanel, final int fieldColumnIndex,
					final int colorRawIndex) {
				final TextField txtf = new TextField();
				txtf.setMinWidth(WIDTH_EDIT_INPUTS);
				txtf.setMaxWidth(WIDTH_EDIT_INPUTS);
				txtf.textProperty().bindBidirectional(labelToUpdate.textProperty());
				editPanel.add(new Label("Pool Name: "), fieldColumnIndex, colorRawIndex);
				editPanel.add(txtf, 1, colorRawIndex);
			}
		});
	}

	private Node wrap(final Label c, final Color color, final Pos alignement) {
		c.setFont(calibri);
		final StackPane p = new StackPane(c);
		p.setStyle("-fx-border-width:0 1 1 0;-fx-border-color:black;-fx-background-color: "
				+ color.toString().replace("0x", "#") + ";");
		StackPane.setAlignment(c, alignement);
		return p;
	}

	private Node wrap(final Label c, final Well well, final Pos alignement, final boolean firstCol,
			final boolean firstRaw) {
		c.setFont(calibri);
		final StackPane p = new StackPane(c);

		p.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					wellColorToUpdate = well;
					paneToUpdate = p;
					labelToUpdate = c;
					contextMenu.show(p, event.getScreenX(), event.getScreenY());
				}
			}
		});
		p.setStyle("-fx-border-width:" + (firstRaw ? "1" : "0") + " 1 1 " + (firstCol ? "1" : "0")
				+ ";-fx-border-color:black;-fx-background-color: " + well.getColor().toString().replace("0x", "#")
				+ ";");
		StackPane.setAlignment(c, alignement);
		return p;
	};

	private Node wrap(final Label c, final Color color, final Pos alignement, final boolean firstCol,
			final boolean firstRaw) {
		c.setFont(calibri);
		final StackPane p = new StackPane(c);

		p.setStyle("-fx-border-width:" + (firstRaw ? "1" : "0") + " 1 1 " + (firstCol ? "1" : "0")
				+ ";-fx-border-color:black;-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		StackPane.setAlignment(c, alignement);
		return p;
	};

}
