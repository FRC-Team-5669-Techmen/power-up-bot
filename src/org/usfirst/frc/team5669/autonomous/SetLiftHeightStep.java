package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.powerup.Lift;

public class SetLiftHeightStep extends AutonomousStep {
	private static final double TOLERANCE = 2.0e7; // If the lift height is within this many inches of target, consider success.
	private Lift lift;
	private double height;
	
	public SetLiftHeightStep(Lift lift, double height) {
		this.lift = lift;
		this.height = height;
	}
	
	@Override
	public void start() {
		lift.moveTo(height);
	}
	
	@Override
	public boolean isDone() {
		return (Math.abs(lift.getCurrentPos() - height) <= TOLERANCE);
	}
}