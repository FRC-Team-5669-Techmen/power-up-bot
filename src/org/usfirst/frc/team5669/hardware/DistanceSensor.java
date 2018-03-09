package org.usfirst.frc.team5669.hardware;

public interface DistanceSensor extends HardwareModule {
	/**
	 * Returns the distance measured by this sensor in inches.
	 * 
	 * @return The distance measured by this sensor in inches.
	 */
	double getDistance();
}
