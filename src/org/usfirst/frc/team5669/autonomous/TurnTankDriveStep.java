package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TurnTankDriveStep extends AutonomousStep {
	private static final double COUNTS_PER_DEGREE = -41.3;
	private TankDrive tankDrive;
	private double speed, degrees;
	private int leftTarget, rightTarget;

	/* Positive degrees = clockwise, negative degrees = counterclockwise. */
	public TurnTankDriveStep(TankDrive tankDrive, double speed, double degrees) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		leftTarget = (int) (degrees * COUNTS_PER_DEGREE);
		rightTarget = (int) (degrees * COUNTS_PER_DEGREE);
	}

	@Override
	public void start() {
		tankDrive.resetEncoders();
		if(degrees > 0) {
			this.tankDrive.set(0.0, speed);
		} else {
			this.tankDrive.set(0.0, -speed);
		}
	}
	
	@Override
	public void periodic() {
		double leftPercent = (((double) tankDrive.getLeftEncoder()) / ((double) leftTarget));
		double rightPercent = (((double) tankDrive.getRightEncoder()) / ((double) rightTarget));
		double leftActual = leftPercent / rightPercent; // How much faster left is than right.
		if(degrees > 0) {
			this.tankDrive.setRaw(speed / leftActual, -speed);
		} else {
			this.tankDrive.setRaw(-speed / leftActual, speed);
		}
	}

	@Override
	public boolean isDone() {
		return (Math.abs((double) tankDrive.getLeftEncoder() / COUNTS_PER_DEGREE) >= Math.abs(degrees)) &&
				(Math.abs((double) tankDrive.getLeftEncoder() / COUNTS_PER_DEGREE) >= Math.abs(degrees));
	}
	
	@Override
	public void finish() {
		this.tankDrive.set(0.0, 0.0);
	}
}

