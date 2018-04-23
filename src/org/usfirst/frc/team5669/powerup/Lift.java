package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.hardware.HardwareModule;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift implements HardwareModule {
	private enum Mode {
		SPEED, POSITION
	}
	
	public static final double COUNTS_PER_REV = 4096.0, // How many counts the sensor returns per revolution.
			GEAR_RATIO = 1.0, // The gear ratio between the sensor and the final axle.
			INCHES_PER_REV = 1.0, // How many inches the lift moves per revolution of the final axle.
			K = COUNTS_PER_REV * GEAR_RATIO * INCHES_PER_REV; // Conversion constant.
	private TalonSRX liftMotor;
	private Mode mode = Mode.SPEED;
	private int target = 0;
	private double speed = 0.0;
	private int offset = 0;

	public Lift(TalonSRX liftMotor) {
		this.liftMotor = liftMotor;
	}

	/**
	 * Converts sensor counts into how high the lift is in inches.
	 * 
	 * @param counts
	 *            The counts returned by the sensor.
	 * @return The height of the sensor in inches.
	 */
	private double countsToInches(int counts) {
		return counts * K;
	}

	/**
	 * Converts inches to sensor counts.
	 * 
	 * @param inches
	 *            The desired height in inches of the lift.
	 * @return What the sensor will return at that height.
	 */
	private int inchesToCounts(double inches) {
		return (int) (inches / K);
	}

	/**
	 * Sets the lift to move to a particular height in inches.
	 * 
	 * @param inches
	 *            The desired height of the lift in inches.
	 */
	public void moveTo(double inches) {
		target = inchesToCounts(inches) + offset;
		mode = Mode.POSITION;
	}
	
	public boolean isMovingToTarget() {
		return mode == Mode.POSITION;
	}
	
	public void holdCurrentPosition() {
		target = liftMotor.getSelectedSensorPosition(0);
		mode = Mode.POSITION;
	}

	public void setLiftSpeed(double speed) {
		this.speed = speed;
		if (speed != 0.0) {
			mode = Mode.SPEED;
		}
	}

	/**
	 * Returns the current height of the lift in inches.
	 * 
	 * @return the current height of the lift in inches.
	 */
	public double getCurrentPos() {
		return countsToInches(liftMotor.getSelectedSensorPosition(0) - offset);
	}
	
	public void resetEncoder() {
		offset = liftMotor.getSelectedSensorPosition(0);
	}

	public void setup() {
		liftMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
	}
	
	public boolean forwardLimitPressed() {
		return liftMotor.getSensorCollection().isFwdLimitSwitchClosed();
	}

	public void periodic(double dt) {
		SmartDashboard.putNumber("Lift Position (Counts)", liftMotor.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Lift Position (Inches)", getCurrentPos());
		SmartDashboard.putNumber("Lift Target (Counts)", target);
		SmartDashboard.putNumber("Lift Speed", speed);
		SmartDashboard.putBoolean("Position Mode?", mode == Mode.POSITION);
		SmartDashboard.putBoolean("Speed Mode?", mode == Mode.SPEED);
		if(forwardLimitPressed()) {// || liftMotor.getSensorCollection().isRevLimitSwitchClosed()) {
			//holdCurrentPosition(); // If we trip a limit switch, stop trying to move. Just hold the current position.
		}
		if(mode == Mode.POSITION) {
			double speedDecayFactor = 4000.0; // If the encoder is [speedDecayFactor] counts off of the target, motor will go full speed. All other positions are linearly related to that speed.
			double correctionSpeed = ((double) (target - liftMotor.getSelectedSensorPosition(0))) / speedDecayFactor;
			if(correctionSpeed > 1.0) correctionSpeed = 1.0;
			if(correctionSpeed < -1.0) correctionSpeed = -1.0;
			liftMotor.set(ControlMode.PercentOutput, -correctionSpeed);
		} else {
			liftMotor.set(ControlMode.PercentOutput, speed);
		}
	}

	public void stop() {
		mode = mode.SPEED;
		speed = 0.0;
		liftMotor.set(ControlMode.PercentOutput, 0.0);
		target = liftMotor.getSelectedSensorPosition(0);
	}
}
