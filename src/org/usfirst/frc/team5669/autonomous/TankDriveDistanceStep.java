package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TankDriveDistanceStep extends AutonomousStep {
	//private static final double COUNTS_PER_INCH = 2518.0 / 12.0;
	private static final double TURN_COMPENSATION = 0.0; // Was 0.025
	private TankDrive tankDrive;
	private double speed;
	private int leftTarget, rightTarget;

	public TankDriveDistanceStep(TankDrive tankDrive, double speed, double distance) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		this.leftTarget = (int) (distance * TankDrive.COUNTS_PER_INCH);
		this.rightTarget = (int) (distance * TankDrive.COUNTS_PER_INCH);
	}

	@Override
	public void start() {
		tankDrive.resetEncoders();
		System.out.println(tankDrive.getLeftEncoder());
		System.out.println(((double) tankDrive.getLeftEncoder() / TankDrive.COUNTS_PER_INCH));
		this.tankDrive.set(this.speed, 0.0);
	}
	
	@Override
	public void periodic() {
		double leftPercent = (((double) tankDrive.getLeftEncoder()) / ((double) leftTarget));
		double rightPercent = (((double) tankDrive.getRightEncoder()) / ((double) rightTarget));
		double multFac = (Math.abs(leftTarget) + Math.abs(rightTarget)) / 2.0;
		leftPercent *= multFac;
		rightPercent *= multFac;
		double difference = leftPercent - rightPercent; // > 0 means left is ahead, < 0 means right is ahead.
		final double MIN_COMPENSATION_THRESHOLD = 100.0, // Minimum distance divergence that requires compensation.
				MAX_COMPENSATION_THRESHOLD = 500.0; // When maximum compensation (full, in-place turn) will be applied.
		double compensation = (Math.abs(difference) - MIN_COMPENSATION_THRESHOLD) / MAX_COMPENSATION_THRESHOLD;
		compensation = Math.min(Math.max(compensation, 0.0), 1.0); // Clamp to 0 - 1 range.
		compensation *= (0.7 - speed); // Make it so that none of the tires go over 0.5 when compensated.
		if(difference > 0) { // Left side is ahead
			this.tankDrive.setRaw(speed - compensation, speed + compensation);
		} else { // Right side is ahead.
			this.tankDrive.setRaw(speed + compensation, speed - compensation);
		}
	}

	@Override
	public boolean isDone() {
		//System.out.println("DISTANCE: " + (tankDrive.getTurnCounter() / COUNTS_PER_INCH) + "in.");
		return ((Math.abs(tankDrive.getLeftEncoder())) >= Math.abs(leftTarget)) &&
				((Math.abs(tankDrive.getRightEncoder())) >= Math.abs(rightTarget));
	}
	
	@Override
	public void finish() {
		//System.out.println("FINISHED");
		this.tankDrive.set(0.0, 0.0);
	}
}
