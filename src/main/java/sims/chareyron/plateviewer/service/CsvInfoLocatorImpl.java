package sims.chareyron.plateviewer.service;

import java.awt.Point;

import org.springframework.stereotype.Service;

@Service
public class CsvInfoLocatorImpl implements CsvInfoLocator {

	@Override
	public Point getXpTitle() {
		return new Point(1, 1);
	}

	@Override
	public Point getXpDescription() {
		return new Point(1, 3);
	}

	@Override
	public Point getXpDate() {
		return new Point(1, 2);
	}

}
