package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.hardware.HardwareModule;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FMS2018 implements HardwareModule {
	public enum Side {
		LEFT, RIGHT, MID, UNKNOWN;
	}

	private class GetterThread extends Thread {
		public void run() {
			while (true) {
				String data = DriverStation.getInstance().getGameSpecificMessage();
				if (data.length() < 3) {
					// Wait and try again.
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else { // Set the data.
					System.out.println("Collected game specific data!");
					System.out.println("Contains: " + data);
					nearPlate = (data.charAt(0) == 'L') ? Side.LEFT : Side.RIGHT;
					midPlate = (data.charAt(1) == 'L') ? Side.LEFT : Side.RIGHT;
					farPlate = (data.charAt(2) == 'L') ? Side.LEFT : Side.RIGHT;
					break;
				}
			}
		}
	}

	private Side nearPlate = Side.UNKNOWN, midPlate = Side.UNKNOWN, farPlate = Side.UNKNOWN;
	private GetterThread thread = null;

	public FMS2018() {
	}

	private void startGetterThread() {
		if (thread == null) {
			thread = new GetterThread();
			thread.start();
		}
	}

	public void setup() {
		startGetterThread();
	}

	public void periodic() {
		SmartDashboard.putBoolean("FMS2018.nearLeft", nearPlate == Side.LEFT);
		SmartDashboard.putBoolean("FMS2018.nearRight", nearPlate == Side.RIGHT);
		SmartDashboard.putBoolean("FMS2018.midLeft", midPlate == Side.LEFT);
		SmartDashboard.putBoolean("FMS2018.midRight", midPlate == Side.RIGHT);
		SmartDashboard.putBoolean("FMS2018.farLeft", farPlate == Side.LEFT);
		SmartDashboard.putBoolean("FMS2018.farRight", farPlate == Side.RIGHT);
	}

	public void stop() {
	}

	public Side getNearPlate() {
		// TODO: This is just for testing in the back of IDEA
		return Side.LEFT; //return nearPlate;
	}

	public Side getMidPlate() {
		// TODO: This is just for testing in the back of IDEA
		return Side.LEFT; //return midPlate;
	}

	public Side getFarPlate() {
		// TODO: This is just for testing in the back of IDEA
		return Side.LEFT; //return farPlate;
	}
}
