package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.autonomous.AutonomousStep;

public class SpitStep extends AutonomousStep {
	private static final long DURATION = 2000; // How many milliseconds to spit for.
	private Lift lift;
	private long startTime;
	
	public SpitStep(Lift lift) {
		this.lift = lift;
	}
	
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	public void periodic() {
		lift.spit();
	}
	
	@Override
	public boolean isDone() {
		return (System.currentTimeMillis() > startTime + DURATION);
	}
}
