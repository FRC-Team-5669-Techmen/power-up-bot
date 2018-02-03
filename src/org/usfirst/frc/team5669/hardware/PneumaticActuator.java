package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticActuator implements HardwareModule {
	private static final double THRESHOLD = 0.5;
	private Solenoid forward, backward = null;
	
	public PneumaticActuator(int canId, int pcmId) {
		forward = new Solenoid(canId, pcmId);
	}
	
	public PneumaticActuator(int canId, int pcmIdForward, int pcmIdBackward) {
		forward = new Solenoid(canId, pcmIdForward);
		backward = new Solenoid(canId, pcmIdBackward);
	}
	
	public void set(double direction) {
		if(backward == null) {
			if(direction > THRESHOLD) {
				forward.set(true);
			} else {
				forward.set(false);
			}
		} else {
			if(direction > THRESHOLD) {
				forward.set(true);
				backward.set(false);
			} else if(direction < -THRESHOLD) {
				forward.set(false);
				backward.set(true);
			} else {
				forward.set(false);
				backward.set(false);
			}
		}
	}

	@Override
	public void setup() {
	}

	@Override
	public void periodic() {
	}

	@Override
	public void stop() {
	}
}
