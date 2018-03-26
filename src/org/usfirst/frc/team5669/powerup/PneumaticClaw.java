package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.hardware.HardwareModule;
import org.usfirst.frc.team5669.hardware.PneumaticActuator;
import org.usfirst.frc.team5669.hardware.PneumaticCircuit;

public class PneumaticClaw implements HardwareModule {
	private PneumaticCircuit pneumatics;
	private PneumaticActuator clawActuator;

	public PneumaticClaw(int pcmId, int expandId, int contractId) {
		pneumatics = new PneumaticCircuit(pcmId);
		clawActuator = pneumatics.add(expandId, contractId);
	}

	public void expandClaw() {
		clawActuator.set(-1.0);
	}

	public void contractClaw() {
		clawActuator.set(1.0);
	}

	@Override
	public void setup() {
		pneumatics.setup();
	}

	@Override
	public void periodic(double dt) {
		pneumatics.periodic(dt);
	}

	@Override
	public void stop() {
		pneumatics.stop();
	}
}
