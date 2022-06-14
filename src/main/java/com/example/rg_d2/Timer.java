package com.example.rg_d2;

import javafx.animation.AnimationTimer;

public class Timer extends AnimationTimer {
	public static interface TimerSubscriber {
		public void update ( long dms );
	}
	
	private long time;
	private TimerSubscriber timerSubscriber;
	
	public Timer ( TimerSubscriber timerSubscriber ) {
		this.timerSubscriber = timerSubscriber;
	}
	
	@Override public void handle ( long now ) {
		if ( this.time != 0 ) {
			long dns = now - this.time;
			
			this.timerSubscriber.update ( dns );
		}
		
		this.time = now;
	}
}
