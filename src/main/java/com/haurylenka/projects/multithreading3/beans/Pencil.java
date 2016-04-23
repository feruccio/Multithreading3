package com.haurylenka.projects.multithreading3.beans;

import java.util.Random;

import org.apache.log4j.Logger;

import com.haurylenka.projects.multithreading3.interfaces.Canvas;
import com.haurylenka.projects.multithreading3.interfaces.Instrument;

public class Pencil implements Instrument {
	
	private final static Logger LOGGER = Logger.getLogger(Pencil.class);
	private static String MESSAGE = "Pencil is working: ";
	public static final String NAME = "pencil";
	private final Random RND;
	private final int MAX_CHAR;

	public Pencil() {
		RND = new Random();
		MAX_CHAR = (int) Character.MAX_HIGH_SURROGATE + 1;
	}

	@Override
	public void apply(Canvas canvas) {
		LOGGER.info(MESSAGE + canvas.show());
		try {
			while (true) {
				char[] charContent = Character.toChars(RND.nextInt(MAX_CHAR));
				String stringContent = new String(charContent);
				canvas.addContent(stringContent);
				LOGGER.info(stringContent);
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
