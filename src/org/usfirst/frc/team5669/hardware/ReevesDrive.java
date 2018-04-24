package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * Code to run Will's Reeves drive on the 2018 (Power Up) robot.
 * A Reeves drive is a device that uses two v-shaped plates that move closer to or apart from each other to provide a
 * continuously variable gear ratio. In this particular instance, there is one pair of such plates that are
 * spring-loaded such that the more tension there is on the belt, the farther down the v the belts go (thus providing
 * a faster gear ratio). When tension is lowered, the belt migrates to the outer edge of the wheel, resulting in a
 * torquier gear ratio. The tension is controlled by moving the motor driving the belt with a linear actuator that is
 * moved by another motor.
 */
public class ReevesDrive implements HardwareModule {
	// The voltages the potentiometer will read at the highest and lowest gear ratio the reeves drive can provide.
	private static final double MIN_VOLTAGE = 0.0, MAX_VOLTAGE = 0.80;
	private SpeedController linearActuatorMotor;
	private AnalogInput potentiometer;
	private double speed = 0.0;
	
	public ReevesDrive(SpeedController linearActuatorMotor, AnalogInput potentiometer) {
		this.linearActuatorMotor = linearActuatorMotor;
		this.potentiometer = potentiometer;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	

	@Override
	public void setup() {
		
	}

	@Override
	public void periodic(double dt) {
		// Only move forwards or backwards if the potentiometer is not at its limit yet.
		
		if(((speed > 0) && (potentiometer.getVoltage() < MAX_VOLTAGE))
				|| ((speed < 0) && (potentiometer.getVoltage() > MIN_VOLTAGE))) {
			linearActuatorMotor.set(speed);
		} else {
			linearActuatorMotor.set(0);
		}
		
	}

	@Override
	public void stop() {

	}
}
