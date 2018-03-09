package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.hardware.DistanceSensor;

public class DistanceGreaterThanWait extends AutonomousStep {
	private DistanceSensor sensor;
	private double threshold;

	public DistanceGreaterThanWait(DistanceSensor sensor, double threshold) {
		this.sensor = sensor;
		this.threshold = threshold;
	}

	public boolean isDone() {
		return this.sensor.getDistance() > threshold;
	}
}
