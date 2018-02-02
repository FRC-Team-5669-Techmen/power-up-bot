package org.usfirst.frc.team5669.powerup.autonomous;

import org.usfirst.frc.team5669.powerup.DistanceSensor;

public class DistanceLessThanWait extends AutonomousStep {
	private DistanceSensor sensor;
	private double threshold;
	
	public DistanceLessThanWait(DistanceSensor sensor, double threshold) {
		this.sensor = sensor;
		this.threshold = threshold;
	}
	
	public boolean isDone() {
		return this.sensor.getDistance() < threshold;
	}
}
