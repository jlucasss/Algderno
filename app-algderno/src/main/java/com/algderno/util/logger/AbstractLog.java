package com.algderno.util.logger;

import java.util.Deque;
import java.util.ArrayDeque;

public abstract class AbstractLog {

	protected Deque<String> list;

	public abstract void show();

	public AbstractLog() {
		list = new ArrayDeque<>();
	}

	public AbstractLog add(String str) {
		list.addLast(str);
		return this;
	}

	public String getLast() {
		return list.getLast();
	}

	public int size() {
		return list.size();
	}

	public String[] getAndClearAll() {

		String[] copy = (String[]) list.toArray();

		list.clear();

		return copy;

	}

}


