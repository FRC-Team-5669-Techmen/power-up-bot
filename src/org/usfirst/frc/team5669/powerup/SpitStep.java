package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.autonomous.AutonomousStep;

public class SpitStep extends AutonomousStep {
	private PneumaticClaw claw;

	public SpitStep(PneumaticClaw claw) {
		this.claw = claw;
	}

	@Override
	public void start() {
		claw.expandClaw();
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
}
