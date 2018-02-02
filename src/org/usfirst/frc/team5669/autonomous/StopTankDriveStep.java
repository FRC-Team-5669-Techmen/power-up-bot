package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.TankDrive;

public class StopTankDriveStep extends AutonomousStep {
	private TankDrive tankDrive;
	
	public StopTankDriveStep(TankDrive tankDrive) {
		this.tankDrive = tankDrive;
	}
	
	@Override
	public void start() {
		this.tankDrive.set(0, 0);
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
}
