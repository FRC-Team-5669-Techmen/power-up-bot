package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogDistanceSensor implements HardwareModule, DistanceSensor {
	private static final double VOLTAGE = 5.0; // Voltage the sensor runs at.
	// The range of the internal digital buffer of the device.
	private static final double CM_PER_VOLT = 1024.0 * 5.0 / VOLTAGE;	
	private static final double IN_PER_VOLT = CM_PER_VOLT / 2.73;
	
	private AnalogInput distanceInput;
	
	public AnalogDistanceSensor(int analogPort) {
		distanceInput = new AnalogInput(analogPort);
	}
	
	@Override
	public void setup() {
	}

	@Override
	public void periodic() {
	}

	@Override
	public void stop() {
	}
	
	public double getDistance() {
		return distanceInput.getVoltage() * IN_PER_VOLT;
	}
}