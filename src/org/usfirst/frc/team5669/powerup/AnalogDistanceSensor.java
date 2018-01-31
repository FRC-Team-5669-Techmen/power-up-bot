package org.usfirst.frc.team5669.powerup;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogDistanceSensor implements HardwareModule {
	private static final double VOLTAGE = 5.0; // Voltage the sensor runs at.
	// The range of the internal digital buffer of the device.
	private static final double CM_PER_VOLT = 1024.0 * 5.0 / VOLTAGE;	
	
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
	
	/**
	 * Returns the distance measured by this sensor in centimeters.
	 * @return The distance measured by this sensor in centimeters.
	 */
	public double getDistance() {
		return distanceInput.getVoltage() * CM_PER_VOLT;
	}
}
