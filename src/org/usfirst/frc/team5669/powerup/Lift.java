package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.hardware.HardwareModule;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;

public class Lift implements HardwareModule {
	public static final double COUNTS_PER_REV = 4096.0, // How many counts the sensor returns per revolution. 
			GEAR_RATIO = 1.0, // The gear ratio between the sensor and the final axle.
			INCHES_PER_REV = 1.0, // How many inches the lift moves per revolution of the final axle.
			K = COUNTS_PER_REV * GEAR_RATIO * INCHES_PER_REV; // Conversion constant.
	private TalonSRX liftMotor;
	private int target = 0;
	
	public Lift(TalonSRX liftMotor) {
		this.liftMotor = liftMotor;
	}
	
	/**
	 * Converts sensor counts into how high the lift is in inches.
	 * @param counts The counts returned by the sensor.
	 * @return The height of the sensor in inches.
	 */
	private double countsToInches(int counts) {
		return counts * K;
	}
	
	/**
	 * Converts inches to sensor counts.
	 * @param inches The desired height in inches of the lift.
	 * @return What the sensor will return at that height.
	 */
	private int inchesToCounts(double inches) {
		return (int) (inches / K);
	}
	
	/**
	 * Sets the lift to move to a particular height in inches.
	 * @param inches The desired height of the lift in inches.
	 */
	public void moveTo(double inches) {
		target = inchesToCounts(inches);
	}
	
	public void setLiftSpeed(double speed) {
		liftMotor.set(ControlMode.PercentOutput, speed);
	}
	
	/**
	 * Returns the current height of the lift in inches.
	 * @return the current height of the lift in inches.
	 */
	public double getCurrentPos() {
		return countsToInches(liftMotor.getSelectedSensorPosition(0));
	}
	
	public void setup() { }
	
	public void periodic() {
		//liftMotor.set(ControlMode.Position, target);
	}
	
	public void stop() {
		target = liftMotor.getSelectedSensorPosition(0);
	}
}
