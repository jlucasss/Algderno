package com.algderno.util.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public abstract class AbstractLog {

	protected Deque<AtomicLog> list;
	private List<ListenerLogger> listeners;

	public abstract void show();

	public AbstractLog() {
		list = new ArrayDeque<>();
		this.listeners = new ArrayList<>();
	}

	public AbstractLog add(String str) {
		
		AtomicLog atomic = new AtomicLog(currentDate(), str);
		
		list.addLast(atomic);
		
		for (ListenerLogger listener : this.listeners)
			listener.call(atomic);
		
		return this;
	}

	public AtomicLog getLast() {
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
	
	protected String currentDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString();
	}
	

	public void addListener(ListenerLogger listener) {
		listeners.add(listener);
	}
	
	public List<ListenerLogger> getListeners() {
		return this.listeners;
	}

}


