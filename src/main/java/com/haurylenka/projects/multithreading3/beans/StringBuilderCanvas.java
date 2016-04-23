package com.haurylenka.projects.multithreading3.beans;

import com.haurylenka.projects.multithreading3.interfaces.Canvas;

public class StringBuilderCanvas implements Canvas {

	private final StringBuilder CONTENT;

	public StringBuilderCanvas() {
		this.CONTENT = new StringBuilder();
	}

	@Override
	public void addContent(Object content) {
		if (!(content instanceof String)) {
			throw new IllegalArgumentException();
		}
		String str = (String) content;
		CONTENT.append(str);
	}

	@Override
	public void deleteLatestContent() {
		if (CONTENT.length() == 0) {
			return;
		}
		CONTENT.deleteCharAt(CONTENT.length() - 1);
	}

	@Override
	public Object show() {
		return CONTENT.toString();
	}
	
}
