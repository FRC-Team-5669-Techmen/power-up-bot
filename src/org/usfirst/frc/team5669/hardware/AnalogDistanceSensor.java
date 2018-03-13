package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AnalogDistanceSensor implements HardwareModule, DistanceSensor {
	private static final double VOLTAGE = 5.0; // Voltage the sensor runs at.
	// The range of the internal digital buffer of the device.
	private static final double MM_PER_VOLT = 1024.0 * 5.0 / VOLTAGE;
	private static final double IN_PER_VOLT = MM_PER_VOLT / 25.4;

	private AnalogInput distanceInput;
	private String name;

	public AnalogDistanceSensor(int analogPort, String name) {
		distanceInput = new AnalogInput(analogPort);
		this.name = name;
	}

	@Override
	public void setup() {
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("AnalogDistanceSensor." + name + ".inches", getDistance());
	}

	@Override
	public void stop() {
	}

	public double getDistance() {
		return distanceInput.getVoltage() * IN_PER_VOLT;
	}
}
