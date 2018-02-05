package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class StartTankDriveStep extends AutonomousStep {
	private TankDrive tankDrive;
	private double speed, turn;
	
	public StartTankDriveStep(TankDrive tankDrive, double speed, double turn) {
		this.tankDrive = tankDrive;
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void start() {
		this.tankDrive.set(speed, turn);
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
}
