package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.powerup.Lift;

public class LiftWhileDrivingStep extends AutonomousStep {
	Lift lift;
	double height;
	public LiftWhileDrivingStep(Lift lift, double height) {
		this.lift = lift;
		this.height = height;
	}
	
	@Override
	public void start() {
		lift.moveTo(height);
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
}
