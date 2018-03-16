package org.usfirst.frc.team5669.autonomous;
public class WaitStep extends AutonomousStep {
	private long start, duration;
	
	public WaitStep(long duration) {
		this.duration = duration;
	}
	
	@Override
	public void start() {
		start = System.currentTimeMillis();
	}
	
	@Override
	public boolean isDone() {
		return (start + duration) <= System.currentTimeMillis();
	}
}
