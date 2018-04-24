package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.DistanceSensor;

public class DistanceLessThanWait extends AutonomousStep {
	private DistanceSensor sensor;
	private double threshold;

	public DistanceLessThanWait(DistanceSensor sensor, double threshold) {
		this.sensor = sensor;
		this.threshold = threshold;
	}

	public boolean isDone() {
		System.out.println(sensor.getDistance());
		return this.sensor.getDistance() < threshold;
	}
}
