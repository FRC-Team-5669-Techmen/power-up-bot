package org.usfirst.frc.team5669.hardware;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TankDrive implements HardwareModule {
	// 3210.0 3247.0 3340.0
	public static final double COUNTS_PER_INCH = 204.711;
	private SpeedController l1, l2, r1, r2;
	private TalonSRX leftEncoder, rightEncoder;
	private double left, right, speed, turn;
	private int leftOffset = 0, rightOffset = 0;
	private int prevLeft = 0, prevRight = 0;
	private static final int BUFFER_SIZE = 30;
	private double[] leftBuffer = new double[BUFFER_SIZE], rightBuffer = new double[BUFFER_SIZE];
	private int leftPointer = 0, rightPointer = 0;
	private boolean mechanicalCompensationActive = true;
	private int elapsedSamples = 0;
	
	public void enableMechanicalCompensation() {
		mechanicalCompensationActive = true;
	}
	
	public void disableMechanicalCompensation() {
		mechanicalCompensationActive = false;
	}

	public TankDrive(SpeedController l1, SpeedController l2, SpeedController r1, SpeedController r2,
			TalonSRX leftEncoder, TalonSRX rightEncoder) {
		this.l1 = l1;
		this.l2 = l2;
		this.r1 = r1;
		this.r2 = r2;
		this.leftEncoder = leftEncoder;
		this.rightEncoder = rightEncoder;
	}

	public void set(double speed, double turn) {
		this.speed = speed;
		this.turn = turn;
		left = speed;
		right = speed;
		left += turn;
		right -= turn;
	}
	
	public void setRaw(double leftSpeed, double rightSpeed) {
		left = leftSpeed;
		right = rightSpeed;
	}
	
	public void resetEncoders() {
		prevLeft = 0;
		prevRight = 0;
		leftOffset = getLeftEncoder() + leftOffset;
		rightOffset = getRightEncoder() + rightOffset;
	}
	
	public int getLeftEncoder() {
		return (-leftEncoder.getSelectedSensorPosition(0)) - leftOffset;
	}
	
	public int getRightEncoder() {
		return rightEncoder.getSelectedSensorPosition(0) - rightOffset;
	}

	@Override
	public void setup() {
		for(int i = 0; i < BUFFER_SIZE; i++) {
			leftBuffer[i] = 0.0;
			rightBuffer[i] = 0.0;
		}
	}
	
	private double countsToInches(int counts) {
		return ((double) counts) / COUNTS_PER_INCH;
	}
	
	private int inchesToCounts(double inches) {
		return (int) (inches * COUNTS_PER_INCH);
	}

	public void periodic(double dt) {
		SmartDashboard.putNumber("TankDrive.left", left);
		SmartDashboard.putNumber("TankDrive.right", right);
		SmartDashboard.putNumber("TankDrive.speed", Math.abs(speed * 100.0));
		SmartDashboard.putNumber("TankDrive.turn", turn);
		SmartDashboard.putBoolean("TankDrive.reverse", speed < -0.05);
		SmartDashboard.putBoolean("TankDrive.forward", speed > 0.03);
		SmartDashboard.putBoolean("TankDrive.turning", Math.abs(turn) > 0.05);
		SmartDashboard.putNumber("TankDrive.leftEncoder", getLeftEncoder());
		
		double leftError = 1.0;
		if(mechanicalCompensationActive && ((Math.abs(left) > 0.05) || (Math.abs(right) > 0.05))) {
			double leftInstSpeed = (countsToInches(getLeftEncoder() - prevLeft) * dt) / left;
			double rightInstSpeed = (countsToInches(getRightEncoder() - prevRight) * dt) / right;
			prevLeft = getLeftEncoder();
			prevRight = getRightEncoder();
			if((Math.abs(leftInstSpeed) > 0.02) && (!Double.isNaN(leftInstSpeed) && (!Double.isInfinite(leftInstSpeed)))) {
				leftBuffer[leftPointer] = leftInstSpeed;
				leftPointer = (leftPointer + 1) % BUFFER_SIZE;
				//System.out.print("left adding ");
				//System.out.println(leftInstSpeed);
			}
			if((Math.abs(rightInstSpeed) > 0.02) && (!Double.isNaN(rightInstSpeed) && (!Double.isInfinite(rightInstSpeed)))) {
				rightBuffer[rightPointer] = rightInstSpeed;
				rightPointer = (rightPointer + 1) % BUFFER_SIZE;
//				System.out.print("right adding ");
//				System.out.println(rightInstSpeed);
			}
			// Moving averages, to be used to compute how far off of their target value they are.
			double leftSpeedMA = 0.0, rightSpeedMA = 0.0;
			for(int i = 0; i < BUFFER_SIZE; i++) {
				leftSpeedMA += leftBuffer[i];
			}
			leftSpeedMA /= (double) BUFFER_SIZE;
			for(int i = 0; i < BUFFER_SIZE; i++) {
				rightSpeedMA += rightBuffer[i];
			}
			rightSpeedMA /= (double) BUFFER_SIZE;
			leftError = leftSpeedMA / rightSpeedMA; // If left is consistently going faster, leftError > 1.
//			System.out.print("left errror ");
//			System.out.println(leftError);
//			System.out.println(leftSpeedMA);
//			System.out.println(rightSpeedMA);
			elapsedSamples++;
		}
		if(elapsedSamples <= BUFFER_SIZE) {
			leftError = 1.0;
		}
		
		l1.set(left / leftError);
		l2.set(left / leftError);
		r1.set(right);
		r2.set(right);
	}

	public void stop() {
		left = 0;
		right = 0;
	}
}
