package sims.chareyron.plateviewer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sims.chareyron.plateviewer.javafx.framework.mvp.PlaceManager;
import sims.chareyron.plateviewer.javafx.view.loader.SplashScreen;

@Lazy
@SpringBootApplication
public class PlateViewerApplication extends AbstractJavaFxSpring {

	@Autowired
	PlaceManager placeManager;
	public static Stage MAIN_STAGE;
	Stage stage;

	public static void main(final String[] args) {
		launchApp(PlateViewerApplication.class, args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		MAIN_STAGE = stage;
		stage.getIcons().add(new Image(SplashScreen.class.getResource("plate-viewer.jpg").toExternalForm()));
		placeManager.setStage(stage);
		placeManager.revealDefaultPlace();
		placeManager.init();
		afterLoadingSpring();
		stage.getScene().getStylesheets().add("style.css");
	}

	@Override
	protected void beforeLoadingSpring() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				final FXMLLoader loader = new FXMLLoader(SplashScreen.class.getResource("SplashScreen.fxml"), null);
				try {
					final Node splashScreen = loader.load();
					final FadeTransition ft = new FadeTransition(Duration.millis(1000), splashScreen);
					ft.setFromValue(0.0);
					ft.setToValue(1.0);
					ft.play();
					final Scene scene = new Scene((Parent) splashScreen);
					stage = new Stage();
					stage.setScene(scene);
					stage.setResizable(true);
					stage.centerOnScreen();
					stage.initStyle(StageStyle.UNDECORATED);
					stage.show();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	protected void afterLoadingSpring() {

		stage.close();

	}

}
