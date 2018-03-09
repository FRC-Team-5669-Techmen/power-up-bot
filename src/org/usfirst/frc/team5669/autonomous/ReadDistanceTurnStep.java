package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.DistanceSensor;
import org.usfirst.frc.team5669.hardware.TankDrive;

public class ReadDistanceTurnStep extends AutonomousStep{
	private final double TOLERANCE = 0.15;
	private TankDrive drive;
	private DistanceSensor d1, d2;
	private double speed;
	private double turn;
	private double d1Dist;
	public ReadDistanceTurnStep(TankDrive drive, DistanceSensor d1, DistanceSensor d2, double speed, double turn)
	{
		this.drive = drive;
		this.d1 = d1;
		this.d2 = d2;
		this.speed = speed;
		this.turn = turn;
	}
	
	@Override
	public void start() {
		d1Dist = d1.getDistance();
		this.drive.set(this.speed, this.turn);
	}
	
	@Override
	public boolean isDone() {
		boolean done = false;
		if(Math.abs((this.d2.getDistance() - d1Dist) / this.d2.getDistance()) <= TOLERANCE)
		{
			this.drive.set(0, 0);
			done = true;
		}
		return done;
	}
}
