package org.usfirst.frc.team5669.hardware;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Compressor;

public class PneumaticCircuit implements HardwareModule {
	private int canId;
	private Compressor compressor;
	private ArrayList<PneumaticActuator> actuators = new ArrayList<>();;
	
	public PneumaticCircuit(int canId) {
		this.canId = canId;
		compressor = new Compressor(canId);
	}
	
	public PneumaticCircuit(int canId, int[][] actuatorPorts) {
		this.canId = canId;
		compressor = new Compressor(canId);
		for(int[] ports : actuatorPorts) {
			if(ports.length == 1) {
				actuators.add(new PneumaticActuator(canId, ports[0]));
			} else if(ports.length == 2) {
				actuators.add(new PneumaticActuator(canId, ports[0], ports[1]));				
			}
		}
	}
	
	public PneumaticActuator add(int pcmId) {
		PneumaticActuator actuator = new PneumaticActuator(canId, pcmId);
		actuators.add(actuator);
		return actuator;
	}
	
	public PneumaticActuator add(int pcmIdForward, int pcmIdReverse) {
		PneumaticActuator actuator = new PneumaticActuator(canId, pcmIdForward, pcmIdReverse);
		actuators.add(actuator);
		return actuator;
	}
	
	public PneumaticActuator add(PneumaticActuator actuator) {
		actuators.add(actuator);
		return actuator;
	}
	
	public PneumaticActuator get(int index) {
		return actuators.get(index);
	}
	
	public void startCompressor() {
		compressor.start();
	}
	
	public void stopCompressor() {
		compressor.stop();
	}

	@Override
	public void setup() {
		compressor.setClosedLoopControl(true);
		compressor.start();
		for(PneumaticActuator actuator : actuators) {
			actuator.setup();
		}
	}

	@Override
	public void periodic() {
		compressor.start();
		for(PneumaticActuator actuator : actuators) {
			actuator.periodic();
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
