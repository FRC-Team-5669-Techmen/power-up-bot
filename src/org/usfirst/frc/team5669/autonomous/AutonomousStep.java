package org.usfirst.frc.team5669.autonomous;

public class AutonomousStep {
	private long startTime;
	protected long maxDuration = -1;
	
	public void start() {
		startTime = System.currentTimeMillis(); 
	}

	public void periodic() {
	}

	public boolean isDone() {
		return (maxDuration > 0) && (System.currentTimeMillis() >= startTime + maxDuration);
	}

	public void finish() {
	}
}
