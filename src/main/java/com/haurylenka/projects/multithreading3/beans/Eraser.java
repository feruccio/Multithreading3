package com.haurylenka.projects.multithreading3.beans;

import org.apache.log4j.Logger;

import com.haurylenka.projects.multithreading3.interfaces.Canvas;
import com.haurylenka.projects.multithreading3.interfaces.Instrument;

public class Eraser implements Instrument {

	private final static Logger LOGGER = Logger.getLogger(Eraser.class);
	public static final String NAME = "eraser";
	private static String MESSAGE = "Eraser is working";
	private static String PROGRESS = ".";
	
	@Override
	public void apply(Canvas canvas) {
		LOGGER.info(MESSAGE);
		try {
			while (true) {
				canvas.deleteLatestContent();
				LOGGER.info(PROGRESS);
				Thread.sleep(Artist.CHAR_DRAW_TIMEOUT);
			}
		} catch (InterruptedException e) {
			//suspending to give way another instrument
			LOGGER.info("\n");
			synchronized (canvas) {
				canvas.notify();
			}
		}
	}
	
}
