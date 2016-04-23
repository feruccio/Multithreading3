package com.haurylenka.projects.multithreading3;

import com.haurylenka.projects.multithreading3.beans.Artist;
import com.haurylenka.projects.multithreading3.beans.Eraser;
import com.haurylenka.projects.multithreading3.beans.Pencil;
import com.haurylenka.projects.multithreading3.beans.StringBuilderCanvas;
import com.haurylenka.projects.multithreading3.interfaces.Canvas;

public class Runner {

	public static void main(String[] args) throws InterruptedException {
		Canvas canvas = new StringBuilderCanvas();
		Artist artist = new Artist(canvas);
		artist.addInstrument(Pencil.NAME, new Pencil());
		artist.addInstrument(Eraser.NAME, new Eraser());
		Thread artistThread = new Thread(artist);
		artistThread.start();
		//terminating drawing process in 30 secs
		Thread.sleep(30 * 1000L);
		artistThread.interrupt();
	}

}
