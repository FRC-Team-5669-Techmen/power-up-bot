package org.usfirst.frc.team5669.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.DistanceSensor;
import org.usfirst.frc.team5669.hardware.TankDrive;
import org.usfirst.frc.team5669.powerup.FMS2018;
import org.usfirst.frc.team5669.powerup.Lift;
import org.usfirst.frc.team5669.powerup.PneumaticClaw;
import org.usfirst.frc.team5669.powerup.SpitStep;

public class AutonomousMode {
	private TankDrive drive;
	private Lift lift;
	private PneumaticClaw claw;
	private DistanceSensor frontDist, backDist, leftDist, rightDist;
	private FMS2018 fms;

	private int switchSide; // 1 for Left, 2 for Right
	private int scaleSide; // 1 for Left, 2 for Right
	private int startSide; // 1 for Left, 2 for Right, 0 for center

	public void addHardware(TankDrive d, Lift l, PneumaticClaw c) {
		drive = d;
		lift = l;
		claw = c;
	}

	public void addSensors(DistanceSensor f, DistanceSensor b, DistanceSensor l, DistanceSensor r) {
		frontDist = f;
		backDist = b;
		leftDist = l;
		rightDist = r;
	}

	public void addFMS(FMS2018 f) {
		fms = f;
		// Convert swtch to an int
		switch (fms.getNearPlate()) {
		case LEFT:
			switchSide = 1;
			break;
		case RIGHT:
			switchSide = 2;
			break;
		default:
			switchSide = 0;
			break;
		}
		// Convert mid to an int
		switch (fms.getMidPlate()) {
		case LEFT:
			scaleSide = 1;
			break;
		case RIGHT:
			scaleSide = 2;
			break;
		default:
			scaleSide = 0;
			break;
		}

	}

	public void addStart(StartPos s) {
		switch (s) {
		case LEFT:
			startSide = 1;
			break;
		case RIGHT:
			startSide = 2;
			break;
		case CENTER:
			startSide = 0;
			break;
		default:
			startSide = 0;
			break;
		}
	}

	private List<AutonomousStep> straightSwitchSteps() {
		return Arrays.asList(//new SetLiftHeightStep(lift, -3.3e7), 
							new StartTankDriveStep(drive, 0.45, 0.0), // Go Straight
							new DistanceGreaterThanWait(backDist, 60.0), 
							new StopTankDriveStep(drive));
							//new SpitStep(claw)); // Drop Box into Switch
	};
	
	private List<AutonomousStep> leftToRightSwitchSteps() {
		return Arrays.asList(new StartTankDriveStep(drive, 0.3, 0.0),
							 new DistanceGreaterThanWait(backDist, 30.0),
							 new StopTankDriveStep(drive),
							 new TurnTankDriveStep(drive, 0.2, 110.0),
							 new StartTankDriveStep(drive, 0.3, 0.0),
							 new DistanceLessThanWait(frontDist, 60.0),
							 new StopTankDriveStep(drive)
							 //new SpitStep(claw)
							 );
	}

	private List<AutonomousStep> straightScaleSteps() {
		return Arrays.asList(new SetLiftHeightStep(lift, -3.3e7), 
							new StartTankDriveStep(drive, 0.2, 0.0), // Go Straight
							new DistanceLessThanWait(frontDist, 24.0), 
							new DistanceGreaterThanWait(backDist, 80.0),
							new StopTankDriveStep(drive)
							
							);
	}

	public List<AutonomousStep> getSwitchSteps() {
		if (startSide == switchSide) {
			return leftToRightSwitchSteps();
		} else {
			// Go to the other side of the switch
			return new ArrayList<AutonomousStep>();
		}
	}

	public List<AutonomousStep> getScaleSteps() {
		if (startSide == scaleSide) {
			return new ArrayList<AutonomousStep>();
		} else {
			return new ArrayList<AutonomousStep>();
		}
	}
}
