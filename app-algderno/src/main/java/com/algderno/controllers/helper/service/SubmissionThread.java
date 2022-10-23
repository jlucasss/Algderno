package com.algderno.controllers.helper.service;

import com.algderno.controllers.MainController;
import com.algderno.models.Group;
import com.algderno.models.Workbook;
import com.algderno.util.logger.SimpleLogger;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeTableView;

public class SubmissionThread extends SubmissionService {

	private volatile boolean suspended = false, stopped = true;

	public SubmissionThread(SimpleLogger logger,
			MainController main,
			ProgressBar progressBar,
			TreeTableView<Group<?>> resultsTTV,
			Workbook[] arrayWorkbooks) {
		super(logger, main, progressBar, resultsTTV, arrayWorkbooks);
	}

	@Override
	protected synchronized void startThread() {

		Thread thisThread = Thread.currentThread();

		stopped = false;

		while (threadTask == thisThread) {
			try {
System.out.println("Starting thread");
				//Thread.sleep(100);
				threadTask.start();

				synchronized(this.threadTask) {//this) {
					while (suspended && threadTask == thisThread)
						wait();
					if (stopped) {
						stopAll();
						break;
					}
				}
			} catch (InterruptedException e) {

				logger.getExceptions().add("Error in init thread"
						/*main.getResources().getString("exception.")*/, e).show();
				e.printStackTrace();

			}
			//repaint();
		}

	}

	public synchronized void suspendThread() {

		this.suspended = true;

	}

	public synchronized void resumeThread() {

		this.suspended = false;
		notify();

	}

	public synchronized void stopThread() {
		threadTask = null;
		this.stopped = true;
		this.suspended = false;
		notify();
	}

	public synchronized boolean stopAll() {

		boolean result = true;

		stopThread();

		if (this.task != null)
			result = this.task.cancel();

		if (this.isRunning())
			result = result && this.cancel();

		return result;

	}

	public boolean isStopped() {
		return this.stopped;
	}

	public boolean isSuspended() {
		return this.suspended;
	}

}

