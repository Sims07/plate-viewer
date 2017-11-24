package sims.chareyron.plateviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;

/**
 * @author Thomas Darimont
 */
public abstract class AbstractJavaFxSpring extends Application {

	private static String[] savedArgs;

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void init() throws Exception {
		beforeLoadingSpring();
		applicationContext = SpringApplication.run(getClass(), savedArgs);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
	}

	protected abstract void beforeLoadingSpring();

	@Override
	public void stop() throws Exception {

		super.stop();
		applicationContext.close();
	}

	protected static void launchApp(Class<? extends AbstractJavaFxSpring> appClass, String[] args) {

		AbstractJavaFxSpring.savedArgs = args;
		Application.launch(appClass, args);
	}
}
