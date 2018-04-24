package org.usfirst.frc.team5669.autonomous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.usfirst.frc.team5669.powerup.FMS2018.Side;
import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.DistanceSensor;
import org.usfirst.frc.team5669.hardware.TankDrive;
import org.usfirst.frc.team5669.powerup.FMS2018;
import org.usfirst.frc.team5669.powerup.Lift;
import org.usfirst.frc.team5669.powerup.PneumaticClaw;
import org.usfirst.frc.team5669.powerup.SpitStep;

public class AutonomousMode {
	private static final double AUTO_SPEED = 0.4;
	private static final double TURN_SPEED = 0.4;
	private static final long WAIT_TIME = 200;
	private static final double NINETY_DEGREES = 90.0; // This seems really unnecessary, but I was trying to compensate for motor problems on the test bot. // WAS 60.0 Degrees
	public static final double LIFT_MAX_HEIGHT = -1.42e8;
	public static final double LIFT_MID_HEIGHT = -10.0e7;
	public static final double LIFT_WING_HEIGHT = -1.32e8; // TODO: MEASURE THIS VALUE!
	public static final double LIFT_START_HEIGHT = -3.3e7 + 1.5e7;
	public static final double LIFT_LOWEST_HEIGHT = -1.5e7 + 1.5e7; 
	private TankDrive drive;
	private Lift lift;
	private PneumaticClaw claw;
	private DistanceSensor frontDist, backDist, leftDist, rightDist;
	private FMS2018 fms;

	private Side switchSide; // 1 for Left, 2 for Right
	private Side scaleSide; // 1 for Left, 2 for Right
	private Side startSide; // 1 for Left, 2 for Right, 0 for center

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
		switchSide = fms.getNearPlate();
		scaleSide = fms.getMidPlate();
	}

	public void addStart(Side s) {
		startSide = s;
	}
	
	// Helpers
	private List<AutonomousStep> centerSwitchSteps() {
		int turnMod = 1;
		if(switchSide == Side.RIGHT)
		{
			turnMod = 1;
		}
		else if(switchSide == Side.LEFT) {
			turnMod = -1;
		}
		return Arrays.asList(//new SetLiftHeightStep(lift, LIFT_START_HEIGHT),
				 			 new LiftWhileDrivingStep(lift, LIFT_MID_HEIGHT),
				 			 new TankDriveDistanceStep(drive, AUTO_SPEED, 90.0),
				 			 new WaitStep(WAIT_TIME),
				 			 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod, 4000),
				 			 new WaitForLiftStep(lift, LIFT_MID_HEIGHT),
				 			 new WaitStep(WAIT_TIME),
				 			 new TankDriveDistanceStep(drive, AUTO_SPEED, 40.0),
				 			 new TurnTankDriveStep(drive, TURN_SPEED, -NINETY_DEGREES * turnMod, 4000),
				 			 new TankDriveDistanceStep(drive, AUTO_SPEED, 50.0),
				 			 new SpitStep(claw));
	}
	private List<AutonomousStep> straightSwitchSteps() {
		int turnMod = 1; // Changes the angle from positive to negative depending on the start.
		if(startSide == Side.LEFT)
		{
			turnMod = 1;
		}
		else if(startSide == Side.RIGHT)
		{
			turnMod = -1;
		}
		return Arrays.asList(//new SetLiftHeightStep(lift, LIFT_START_HEIGHT),
				            new LiftWhileDrivingStep(lift, LIFT_MID_HEIGHT),
							new TankDriveDistanceStep(drive, AUTO_SPEED, 146.0), // Used to be 155.0
							new WaitStep(WAIT_TIME),
							new WaitForLiftStep(lift, LIFT_MID_HEIGHT),
							new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod, 4000),
							new WaitStep(WAIT_TIME),
							new TankDriveDistanceStep(drive, AUTO_SPEED, 35.0, 3000),
							new SpitStep(claw)  // Drop Box into Switch
							);
	};
	
	private List<AutonomousStep> farSideSwitchSteps() {
		int turnMod = 1; // Changes the angle from positive to negative depending on the start.
		if(startSide == Side.LEFT)
		{
			turnMod = 1;
		}
		else if(startSide == Side.RIGHT)
		{
			turnMod = -1;
		}
		/*return Arrays.asList(new SetLiftHeightStep(lift, LIFT_START_HEIGHT),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 235.0), // 146" for base, 40" for robot, 13" for cubes, 12" for buffer zone, 24" because it's not far enough.
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod),
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 260.0), // 156" for width of switch, 40" for robot, 12" for buffer, 12" for buffer, 40" for robot.
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod),
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 89.0), // 235" - 146" for base
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod),
							 new WaitStep(WAIT_TIME),
							 new SetLiftHeightStep(lift, LIFT_MID_HEIGHT),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 25.0), // A lot extra for comfort.
							 new SpitStep(claw)
							 );*/
		return Arrays.asList(new TankDriveDistanceStep(drive, AUTO_SPEED, 240.0));
	}

	public List<AutonomousStep> straightScaleSteps() {
		int turnMod = 1; // Changes the angle from positive to negative depending on the start.
		if(startSide == Side.LEFT)
		{
			turnMod = 1;
		}
		else if(startSide == Side.RIGHT)
		{
			turnMod = -1;
		}
		return Arrays.asList(//new SetLiftHeightStep(lift, LIFT_START_HEIGHT),
				             new LiftWhileDrivingStep(lift, LIFT_MAX_HEIGHT),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 310.0), // - 168.0 if starting from switch position
							 new WaitForLiftStep(lift, LIFT_MAX_HEIGHT),
							 new WaitStep(WAIT_TIME),
							 //new SetLiftHeightStep(lift, LIFT_MAX_HEIGHT),
							 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod, 4000),// * turnMod)
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 28.0, 3000),
							 new SpitStep(claw)
							);
	}
	
	private List<AutonomousStep> farSideScaleSteps() {
		int turnMod = 1; // Changes the angle from positive to negative depending on the start.
		if(startSide == Side.LEFT)
		{
			turnMod = 1;
		}
		else if(startSide == Side.RIGHT)
		{
			turnMod = -1;
		}
		return Arrays.asList(new TankDriveDistanceStep(drive, AUTO_SPEED, 200.0)); // This should never, NEVER be used. But just in case there is a bug...
		/*return Arrays.asList(new SetLiftHeightStep(lift, LIFT_START_HEIGHT),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 240.0),
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod),
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 232.0),
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, -NINETY_DEGREES  * turnMod),
							 new WaitStep(WAIT_TIME),
							 new SetLiftHeightStep(lift, LIFT_MAX_HEIGHT),
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 46.0),
							 new WaitStep(WAIT_TIME),
							 new TurnTankDriveStep(drive, TURN_SPEED, -NINETY_DEGREES * turnMod),
							 new WaitStep(WAIT_TIME),
							 new TankDriveDistanceStep(drive, AUTO_SPEED, 28.0),
							 new SpitStep(claw)
							 );*/
		/*
		return Arrays.asList(new TankDriveDistanceStep(drive, AUTO_SPEED, 220.0),
				new TurnTankDriveStep(drive, TURN_SPEED, NINETY_DEGREES * turnMod));
				*/
	}
	
	// Public Returns 
	
	public List<AutonomousStep> getSwitchSteps() {
		System.out.println("Going to the " + fms.getNearPlate() + " switch");
		if (startSide == switchSide) {
			return straightSwitchSteps();
		} else if (startSide == Side.MID) {
			// Go to the other side of the switch
			return centerSwitchSteps();
		}
		else
		{
			return farSideSwitchSteps();
		}
	}

	public List<AutonomousStep> getScaleSteps() {
		System.out.println("Going to the " + fms.getNearPlate() + " scale");
		if (startSide == scaleSide) {
			return straightScaleSteps();
		} else {
			return farSideScaleSteps();
		}
	}
}
