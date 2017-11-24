package sims.chareyron.plateviewer.javafx.controller;

import java.io.IOException;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sims.chareyron.plateviewer.PlateViewerApplication;
import sims.chareyron.plateviewer.javafx.view.LoadingView;
import sims.chareyron.plateviewer.javafx.view.MainView;

@Service
public class ExecutorFSImpl implements ExecutorFS {
	private final GlassPaneRunnable glassPaneRunnable = new GlassPaneRunnable();

	class GlassPaneRunnable implements Runnable {
		Stage dialog;
		FXMLLoader loader;

		public Stage getStage() {
			return dialog;
		}

		public void run(String message) {
			if (loader == null) {
				dialog = new Stage();
				dialog.initStyle(StageStyle.UNDECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(PlateViewerApplication.MAIN_STAGE);
				loader = new FXMLLoader(MainView.class.getResource("Loading.fxml"));
				Node res;
				try {
					res = loader.load();

					Scene dialogScene = new Scene((Parent) res);
					dialog.setScene(dialogScene);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LoadingView l = loader.getController();
			l.setLoadingMessage(message);
			dialog.show();

		}

		@Override
		public void run() {
			if (loader == null) {
				dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(PlateViewerApplication.MAIN_STAGE);
				FXMLLoader loader = new FXMLLoader(MainView.class.getResource("Loading.fxml"));
				Node res;
				try {
					res = loader.load();
					Scene dialogScene = new Scene((Parent) res, 300, 200);
					dialog.setScene(dialogScene);
					dialog.show();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				dialog.show();
			}

		}
	}

	@Override
	public <T, R> void executeLongOp(Function<T, R> function, T input, Function<R, R> callback, String loadingMessage) {

		glassPaneRunnable.run(loadingMessage);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				R result = function.apply(input);
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						callback.apply(result);
						Platform.runLater(new Runnable() {

							@Override
							public void run() {

								glassPaneRunnable.getStage().hide();
							}
						});
					}
				});

			}
		});
		t.start();

	}

}
