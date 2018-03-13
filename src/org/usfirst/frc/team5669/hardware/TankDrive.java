package org.usfirst.frc.team5669.hardware;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TankDrive implements HardwareModule {
	private SpeedController l1, l2, r1, r2;
	private TalonSRX turnCounter;
	private double left, right, speed, turn;
	private int offset = 0;

	public TankDrive(SpeedController l1, SpeedController l2, SpeedController r1, SpeedController r2, TalonSRX turnCounter) {
		this.l1 = l1;
		this.l2 = l2;
		this.r1 = r1;
		this.r2 = r2;
		this.turnCounter = turnCounter;
	}

	public void set(double speed, double turn) {
		this.speed = speed;
		this.turn = turn;
		left = speed;
		right = speed;
		left += turn;
		right -= turn;
	}
	
	public void resetTurnCounter() {
		offset = getTurnCounter() + offset;
	}
	
	public int getTurnCounter() {
		return turnCounter.getSelectedSensorPosition(0) - offset;
	}

	public void setup() {
	}

	public void periodic() {
		l1.set(left);
		l2.set(left);
		// TODO: Remove - signs
		r1.set(-right);
		r2.set(-right);
		SmartDashboard.putNumber("TankDrive.left", left);
		SmartDashboard.putNumber("TankDrive.right", right);
		SmartDashboard.putNumber("TankDrive.speed", Math.abs(speed * 100.0));
		SmartDashboard.putNumber("TankDrive.turn", turn);
		SmartDashboard.putBoolean("TankDrive.reverse", speed < -0.05);
		SmartDashboard.putBoolean("TankDrive.forward", speed > 0.03);
		SmartDashboard.putBoolean("TankDrive.turning", Math.abs(turn) > 0.05);
		SmartDashboard.putNumber("TankDrive.turnCounter", getTurnCounter());
	}

	public void stop() {
		left = 0;
		right = 0;
	}
}
