package sims.chareyron.plateviewer.javafx.controller;

import java.util.function.Function;

public interface ExecutorFS {

	<T, R> void executeLongOp(Function<T, R> function, T input, Function<R, R> callback, String loadingMessage);

}
