package org.usfirst.frc.team5669.powerup;

import edu.wpi.first.wpilibj.SpeedController;

public class TankDrive {
	private SpeedController l1, l2, r1, r2;
	
	public TankDrive(SpeedController l1, SpeedController l2, SpeedController r1, SpeedController r2) {
		this.l1 = l1;
		this.l2 = l2;
		this.r1 = r1;
		this.r2 = r2;
	}
	
	public void set(double speed, double turn) {
		double left = speed, right = speed;
		left += turn;
		right -= turn;
		l1.set(left);
		l2.set(left);
		r1.set(right);
		r2.set(right);
	}
}
