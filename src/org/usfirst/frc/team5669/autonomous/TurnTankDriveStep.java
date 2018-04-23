package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TurnTankDriveStep extends AutonomousStep {
	private static final double COUNTS_PER_DEGREE = 41.64;
	private TankDrive tankDrive;
	private double speed;
	private boolean clockwise;
	private int leftTarget, rightTarget;

	/* Positive degrees = clockwise, negative degrees = counterclockwise. */
	public TurnTankDriveStep(TankDrive tankDrive, double speed, double degrees) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		leftTarget = (int) (degrees * COUNTS_PER_DEGREE);
		rightTarget = (int) (-degrees * COUNTS_PER_DEGREE);
		clockwise = degrees > 0;
	}
	
	/* Positive degrees = clockwise, negative degrees = counterclockwise. */
	public TurnTankDriveStep(TankDrive tankDrive, double speed, double degrees, long timeLimit) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		leftTarget = (int) (degrees * COUNTS_PER_DEGREE);
		rightTarget = (int) (-degrees * COUNTS_PER_DEGREE);
		clockwise = degrees > 0;
		this.maxDuration = timeLimit;
	}

	@Override
	public void start() {
		super.start();
		tankDrive.resetEncoders();
		if(clockwise) {
			this.tankDrive.set(0.0, speed);
		} else {
			this.tankDrive.set(0.0, -speed);
		}
	}
	
	@Override
	public void periodic() {/*
		if((Math.abs(tankDrive.getLeftEncoder()) > 100) && (Math.abs(tankDrive.getRightEncoder()) > 100)) {
			double leftPercent = Math.abs((((double) tankDrive.getLeftEncoder()) - ((double) leftTarget))) / Math.abs(leftTarget);
			double rightPercent = Math.abs((((double) tankDrive.getRightEncoder()) - ((double) rightTarget))) / Math.abs(rightTarget);
			System.out.println("Percents");
			System.out.println(leftPercent);
			System.out.println(rightPercent);
			double multFac = (Math.abs(leftTarget) + Math.abs(rightTarget)) / 2.0;
			System.out.println("MultFac");
			System.out.println(multFac);
			leftPercent *= multFac;
			rightPercent *= multFac;
			double difference = leftPercent - rightPercent; // > 0 means left is ahead, < 0 means right is ahead.
			final double MIN_COMPENSATION_THRESHOLD = 100.0, // Minimum distance divergence that requires compensation.
					MAX_COMPENSATION_THRESHOLD = 500.0; // When maximum compensation (full, in-place turn) will be applied.
			double compensation = (Math.abs(difference) - MIN_COMPENSATION_THRESHOLD) / MAX_COMPENSATION_THRESHOLD;
			compensation = Math.min(Math.max(compensation, 0.0), 1.0); // Clamp to 0 - 1 range.
			compensation *= (0.5 - speed); // Make it so that none of the tires go over 0.5 when compensated.
			int smult = (clockwise) ? 1 : -1;
			if(difference > 0) { // Left side is ahead
				this.tankDrive.setRaw(smult * (speed) - compensation, smult * (-speed) - compensation);
			} else { // Right side is ahead.
				this.tankDrive.setRaw(smult * (speed) + compensation, smult * (-speed) + compensation);
			}
		}*/
		double left = 0.0, right = 0.0;
		if(Math.abs(tankDrive.getLeftEncoder()) <= Math.abs(leftTarget)) {
			if(clockwise) {
				left = speed;
			} else {
				left = -speed;
			}
		}
		if(Math.abs(tankDrive.getRightEncoder()) <= Math.abs(rightTarget)) {
			if(clockwise) {
				right = -speed;
			} else {
				right = speed;
			}
		}
		this.tankDrive.setRaw(left, right);
	}

	@Override
	public boolean isDone() {
		return (((Math.abs(tankDrive.getLeftEncoder())) >= Math.abs(leftTarget)) &&
				((Math.abs(tankDrive.getRightEncoder())) >= Math.abs(rightTarget)))
				|| super.isDone();
	}
	
	@Override
	public void finish() {
		this.tankDrive.set(0.0, 0.0);
	}
}

