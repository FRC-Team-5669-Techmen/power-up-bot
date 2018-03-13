package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class TankDriveDistanceStep extends AutonomousStep {
	private static final double COUNTS_PER_INCH = 1;
	private TankDrive tankDrive;
	private double speed, distance;

	public TankDriveDistanceStep(TankDrive tankDrive, double speed, double distance) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		this.distance = distance;
	}

	@Override
	public void start() {
		tankDrive.resetTurnCounter();
		if(distance > 0) {
			this.tankDrive.set(speed, 0.0);
		} else {
			this.tankDrive.set(-speed, 0.0);
		}
		System.out.println(tankDrive.getTurnCounter());
		System.out.println(((double) -tankDrive.getTurnCounter() / COUNTS_PER_INCH));
	}

	@Override
	public boolean isDone() {
		return ((double) -tankDrive.getTurnCounter() / COUNTS_PER_INCH) >= Math.abs(distance);
	}
	
	@Override
	public void finish() {
		this.tankDrive.set(0.0, 0.0);
	}
}
