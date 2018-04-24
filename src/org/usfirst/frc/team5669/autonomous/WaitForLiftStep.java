package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.powerup.Lift;

public class WaitForLiftStep extends AutonomousStep {
	private static final double TOLERANCE = 2.0e7; // If the lift height is within this many inches of target, consider success.
	private Lift lift;
	private double height;	
	
	public WaitForLiftStep(Lift lift, double height) {
		this.lift = lift;
		this.height = height;
	}
	
	@Override
	public boolean isDone() {
		boolean done = (Math.abs(lift.getCurrentPos() - height) <= TOLERANCE) || lift.forwardLimitPressed();
		if(done) {
			lift.holdCurrentPosition();
		}
		return done;
	}
}
