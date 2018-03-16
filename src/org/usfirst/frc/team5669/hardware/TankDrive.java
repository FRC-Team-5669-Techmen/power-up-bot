package org.usfirst.frc.team5669.hardware;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TankDrive implements HardwareModule {
	private SpeedController l1, l2, r1, r2;
	private TalonSRX leftEncoder, rightEncoder;
	private double left, right, speed, turn;
	private int leftOffset = 0, rightOffset = 0;

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
		leftOffset = getLeftEncoder() + leftOffset;
		rightOffset = getRightEncoder() + rightOffset;
	}
	
	public int getLeftEncoder() {
		return leftEncoder.getSelectedSensorPosition(0) - leftOffset;
	}
	
	public int getRightEncoder() {
		return -rightEncoder.getSelectedSensorPosition(0) - rightOffset;
	}

	public void setup() {
	}

	public void periodic() {
		l1.set(left);
		l2.set(left);
		r1.set(right);
		r2.set(right);
		SmartDashboard.putNumber("TankDrive.left", left);
		SmartDashboard.putNumber("TankDrive.right", right);
		SmartDashboard.putNumber("TankDrive.speed", Math.abs(speed * 100.0));
		SmartDashboard.putNumber("TankDrive.turn", turn);
		SmartDashboard.putBoolean("TankDrive.reverse", speed < -0.05);
		SmartDashboard.putBoolean("TankDrive.forward", speed > 0.03);
		SmartDashboard.putBoolean("TankDrive.turning", Math.abs(turn) > 0.05);
		SmartDashboard.putNumber("TankDrive.leftEncoder", getLeftEncoder());
		System.out.println("Encoders");
		System.out.println(getLeftEncoder());
		System.out.println(getRightEncoder());
	}

	public void stop() {
		left = 0;
		right = 0;
	}
}
