package org.usfirst.frc.team5669.hardware;

import edu.wpi.first.wpilibj.SerialPort;

public class RS232DistanceSensor implements HardwareModule, DistanceSensor {
	private static final double IN_PER_MM = 1.0 / (10.0 * 2.54);
	private SerialPort serial = new SerialPort(9600, SerialPort.Port.kOnboard, 8, SerialPort.Parity.kNone);
	private int currentValue;

	@Override
	public double getDistance() {
		return ((double) currentValue) * IN_PER_MM;
	}

	@Override
	public void setup() {
	}

	@Override
	public void periodic() {
		String next = serial.readString();
		// Content of message is R[1-4 digit number][carriage return]
		if((next.length() >= 3) && (next.length() <= 6) && (next.charAt(0) == 'R')) {
			String number = next.substring(1, next.length() - 1);
			currentValue = Integer.parseInt(number);
		}
	}

	@Override
	public void stop() {
	}
}
