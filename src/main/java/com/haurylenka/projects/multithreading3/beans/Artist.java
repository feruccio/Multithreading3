package com.haurylenka.projects.multithreading3.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.haurylenka.projects.multithreading3.interfaces.Canvas;
import com.haurylenka.projects.multithreading3.interfaces.Instrument;

public class Artist implements Runnable {
	
	private final static Logger LOGGER = Logger.getLogger(Artist.class);
	private static final String MESSAGE = "\nHere's the result:\n";
	public static final long CHAR_DRAW_TIMEOUT = 500L;
	private Canvas canvas;
	private Map<String, Instrument> instruments;
	private boolean goOnFlag = true;
	
	public Artist(Canvas canvas) {
		this.canvas = canvas;
		this.instruments = new HashMap<>();
	}

	@Override
	public void run() {
		Map<String, Thread> instrThreads = null;
		try {
			instrThreads = init();
			Thread pencil = instrThreads.get(Pencil.NAME);
			if (pencil != null) {
				pencil.start();
			}
			Random rnd = new Random();
			while (true) {
				//random instruments switching timeout
				long switchTimeOut = (2 + rnd.nextInt(5)) * 1000;
				Thread.sleep(switchTimeOut);
				switchInstruments(instrThreads);
			}
		} catch (InterruptedException e) {
			goOnFlag = false;
			if (instrThreads != null) {
				for (Thread t : instrThreads.values()) {
					t.interrupt();
				}
				synchronized (canvas) {
					canvas.notify();
				}
			}
			LOGGER.info(MESSAGE);
			LOGGER.info(canvas.show());
		}
		
	}
	
	/*
	 * Returns a map of Thread objects, each corresponds
	 * to an instrument, i.e. a Pencil or an Eraser.
	 */
	private Map<String, Thread> init() {
		Map<String, Thread> instrThreads = new HashMap<>();
		for (String instrName : instruments.keySet()) {
			Instrument instr = instruments.get(instrName);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (canvas) {
						try {
							while (goOnFlag) {
								instr.apply(canvas);
								/*
								 * resume working until another 
								 * instrument notifies its suspending
								 */
								canvas.wait();
							} 
						} catch (InterruptedException e) {
							/*
							 * Interruption means instruments change,
							 * just returning
							 */
						}
					}
				}
			});
			instrThreads.put(instrName, t);
		}
		return instrThreads;
	}

	/*
	 * Suspends Pencil's thread and resumes Eraser's one 
	 * and vice versa
	 */
	private void switchInstruments(Map<String, Thread> instrThreads) 
			throws InterruptedException {
		for (Thread t : instrThreads.values()) {
			if (t.getState() != Thread.State.WAITING) {
				t.interrupt();
			}
		}
		//starting Eraser thread if it's not yet
		Thread eraser = instrThreads.get(Eraser.NAME);
		if (eraser != null && eraser.getState() == Thread.State.NEW) {
			eraser.start();
		}
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Map<String, Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(Map<String, Instrument> instruments) {
		this.instruments = instruments;
	}
	
	public void addInstrument(String name, Instrument instrument) {
		instruments.put(name, instrument);
	}
	
}
