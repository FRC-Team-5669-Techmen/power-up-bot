package org.usfirst.frc.team5669.powerup;

import edu.wpi.first.wpilibj.DriverStation;

public class FMS2018 implements HardwareModule {
	public enum Side {
		LEFT, RIGHT, UNKNOWN;
	}
	
	private class GetterThread extends Thread {
		public void run() {
			while(true) {
				String data = DriverStation.getInstance().getGameSpecificMessage();
				if(data.length() < 3) {
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
		if(thread == null) {
			thread = new GetterThread();
			thread.start();
		}
	}
	
	public void setup() {
		startGetterThread();
	}
	
	public void periodic() {
	}
	
	public void stop() {
	}
	
	public Side getNearPlate() {
		return nearPlate;
	}
	
	public Side getMidPlate() {
		return midPlate;
	}
	
	public Side getFarPlate() {
		return farPlate;
	}
}
