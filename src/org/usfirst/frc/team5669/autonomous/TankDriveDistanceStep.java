package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TankDriveDistanceStep extends AutonomousStep {
	private static final double COUNTS_PER_INCH = -2518 / 12;
	private static final double TURN_COMPENSATION = 0.0; // Was 0.025
	private TankDrive tankDrive;
	private double speed;
	private int leftTarget, rightTarget;

	public TankDriveDistanceStep(TankDrive tankDrive, double speed, double distance) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		this.leftTarget = (int) (distance * COUNTS_PER_INCH);
		this.rightTarget = (int) (distance * COUNTS_PER_INCH);
	}

	@Override
	public void start() {
		tankDrive.resetEncoders();
		System.out.println(tankDrive.getLeftEncoder());
		System.out.println(((double) -tankDrive.getLeftEncoder() / COUNTS_PER_INCH));
		this.tankDrive.set(this.speed, 0.0);
	}
	
	@Override
	public void periodic() {
		double leftPercent = (((double) tankDrive.getLeftEncoder()) / ((double) leftTarget));
		double rightPercent = (((double) tankDrive.getRightEncoder()) / ((double) rightTarget));
		double leftActual = leftPercent / rightPercent; // How much faster left is than right.
		this.tankDrive.setRaw(speed / leftActual, speed);
	}

	@Override
	public boolean isDone() {
		//System.out.println("DISTANCE: " + (tankDrive.getTurnCounter() / COUNTS_PER_INCH) + "in.");
		return (((double) tankDrive.getLeftEncoder() / COUNTS_PER_INCH) >= Math.abs(leftTarget)) &&
				(((double) tankDrive.getRightEncoder() / COUNTS_PER_INCH) >= Math.abs(rightTarget));
	}
	
	@Override
	public void finish() {
		//System.out.println("FINISHED");
		this.tankDrive.set(0.0, 0.0);
	}
}
