package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TurnTankDriveStep extends AutonomousStep {
	private static final double COUNTS_PER_DEGREE = 41.3;
	private TankDrive tankDrive;
	private double speed, degrees;

	/* Positive degrees = clockwise, negative degrees = counterclockwise. */
	public TurnTankDriveStep(TankDrive tankDrive, double speed, double degrees) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		this.degrees = degrees;
	}

	@Override
	public void start() {
		tankDrive.resetTurnCounter();
		if(degrees > 0) {
			this.tankDrive.set(0.0, speed);
		} else {
			this.tankDrive.set(0.0, -speed);
		}
		System.out.println(tankDrive.getTurnCounter());
		System.out.println(((double) -tankDrive.getTurnCounter() / COUNTS_PER_DEGREE));
	}

	@Override
	public boolean isDone() {
		return ((double) -tankDrive.getTurnCounter() / COUNTS_PER_DEGREE) >= Math.abs(degrees);
	}
	
	@Override
	public void finish() {
		this.tankDrive.set(0.0, 0.0);
	}
}

