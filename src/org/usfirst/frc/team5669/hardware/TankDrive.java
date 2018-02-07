package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TankDrive implements HardwareModule {
	private SpeedController l1, l2, r1, r2;
	private double left, right, speed, turn;
	
	public TankDrive(SpeedController l1, SpeedController l2, SpeedController r1, SpeedController r2) {
		this.l1 = l1;
		this.l2 = l2;
		this.r1 = r1;
		this.r2 = r2;
	}
	
	public void set(double speed, double turn) {
		this.speed = speed;
		this.turn = turn;
		left = speed;
		right = speed;
		left += turn;
		right -= turn;
	}
	
	public void setup() { }
	
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
	}
	
	public void stop() {
		left = 0;
		right = 0;
	}
}
