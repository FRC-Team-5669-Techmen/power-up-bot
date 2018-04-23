/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.powerup.FMS2018.Side;
import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team5669.autonomous.AutonomousMode;
import org.usfirst.frc.team5669.autonomous.AutonomousQueue;
import org.usfirst.frc.team5669.autonomous.AutonomousStep;
import org.usfirst.frc.team5669.autonomous.LiftWhileDrivingStep;
import org.usfirst.frc.team5669.autonomous.PriorityObjective;
import org.usfirst.frc.team5669.autonomous.SetLiftHeightStep;
import org.usfirst.frc.team5669.autonomous.StartTankDriveStep;
import org.usfirst.frc.team5669.autonomous.StopTankDriveStep;
import org.usfirst.frc.team5669.autonomous.TankDriveDistanceStep;
import org.usfirst.frc.team5669.autonomous.TurnTankDriveStep;
import org.usfirst.frc.team5669.autonomous.WaitStep;
import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.DistanceSensor;
import org.usfirst.frc.team5669.hardware.HardwareModule;
import org.usfirst.frc.team5669.hardware.RS232DistanceSensor;
import org.usfirst.frc.team5669.hardware.ReevesDrive;
import org.usfirst.frc.team5669.hardware.TankDrive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	// WPI_TalonSRX is compatible with other WPILib motor controllers. TalonSRX has
	// more functions but is less compatible.
	private WPI_TalonSRX l1 = new WPI_TalonSRX(3), l2 = new WPI_TalonSRX(4), r1 = new WPI_TalonSRX(1),
			r2 = new WPI_TalonSRX(2);
	private TalonSRX leftTankEncoder = new TalonSRX(4), rightTankEncoder = new TalonSRX(1);
	private TankDrive drive = new TankDrive(l1, l2, r1, r2, leftTankEncoder, rightTankEncoder);
	private Lift lift = new Lift(new TalonSRX(20));
	private PneumaticClaw claw = new PneumaticClaw(0, 2, 0);
	private DistanceSensor frontDist = new AnalogDistanceSensor(3, "front"),
			rightDist = new AnalogDistanceSensor(2, "right"), backDist = new AnalogDistanceSensor(1, "back"),
			leftDist = new AnalogDistanceSensor(0, "left");
	private FMS2018 fms = new FMS2018();
	private AutonomousInstructor2018 autoInst = new AutonomousInstructor2018();
	private HardwareModule[] modules = { drive, fms, autoInst, frontDist, rightDist, backDist, leftDist, claw, lift };

	private AutonomousQueue queue = new AutonomousQueue();
	private Joystick leftStick = new Joystick(0), rightStick = new Joystick(1), gamepad = new Joystick(2), throttle = new Joystick(3), stick = new Joystick(4);
	private PriorityObjective objective;
	private Side start;
	private AutonomousMode autoMode = new AutonomousMode();

	private boolean invert = false;
	private int invNum = 1;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("Robot init called!");
		drive.resetEncoders();
		for (HardwareModule module : modules) {
			module.setup();
		}
		rightTankEncoder.configForwardSoftLimitEnable(false, 0);
		rightTankEncoder.configReverseSoftLimitEnable(false, 0);
		rightTankEncoder.overrideLimitSwitchesEnable(false);
		rightTankEncoder.overrideSoftLimitsEnable(false);
		r1.overrideSoftLimitsEnable(false);
		lift.resetEncoder();
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		drive.enableMechanicalCompensation();

		System.out.println("Autonomous init called!");
		System.out.println("The nearest switch has our team on the " + fms.getNearPlate() + " side.");
		System.out.println("The central scale has our team on the " + fms.getMidPlate() + " side.");

		start = Side.LEFT; // TODO: CHange this for each match!

		// To always go to switch, include 1 at the end; to always go to scale, include
		// 2.
		objective = new PriorityObjective(autoInst.getStartSide(), fms.getNearPlate(), fms.getMidPlate());

		autoMode.addHardware(drive, lift, claw);
		autoMode.addSensors(frontDist, backDist, leftDist, rightDist);
		autoMode.addFMS(fms);
		autoMode.addStart(start);

		List<AutonomousStep> steps = new ArrayList<>();

		// Determine which objective is most efficient

		switch (objective.getPriority()) {
		case SCALE:
			steps = autoMode.getScaleSteps();
			break;
		case SWITCH:
			steps = autoMode.getSwitchSteps();
			break;
		default:
			steps = autoMode.getSwitchSteps();
			break;
		}

		// For testing purposes...
		// steps = autoMode.straightScaleSteps();
		queue.clear();
		for (AutonomousStep step : steps) {
			//queue.add(step);
		}
		// queue.add(new StartTankDriveStep(drive, 0.0, 0.3));
		// queue.add(new WaitStep(8000));
		// queue.add(new StopTankDriveStep(drive));
//		 queue.add(new TankDriveDistanceStep(drive, 0.3, 250.0));
		// queue.add(new TurnTankDriveStep(drive, 0.3, 90.0));
		// queue.add(new SetLiftHeightStep(lift, AutonomousMode.LIFT_MID_HEIGHT));
		// queue.add(new SpitStep(claw));
		// queue.add(new LiftWhileDrivingStep(lift, AutonomousMode.LIFT_MAX_HEIGHT));
		// queue.add(new TankDriveDistanceStep(drive, 0.3, 50.0));
		// queue.add(new SetLiftHeightStep(lift, AutonomousMode.LIFT_MAX_HEIGHT));
	}

	long previousTime = System.currentTimeMillis();

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		queue.periodic();
		double dt = ((double) (System.currentTimeMillis() - previousTime)) / 1000.0;
		previousTime = System.currentTimeMillis();
		for (HardwareModule module : modules) {
			module.periodic(dt);
		}
		// System.out.println(backDist.getDistance());
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		drive.disableMechanicalCompensation();
	}

	boolean liftWasMoving = false;
	double previousSpeed = 0.0;
	double MAX_ACCELERATION = 2.0; // Accelerate to full speed in 1 / MAX_ACCELERATION seconds.
	long prevTime = System.currentTimeMillis();

	boolean triggerWasPressed = false;
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		double secondsSinceLastUpdate = ((double) System.currentTimeMillis() - prevTime) / 1000.0;
		prevTime = System.currentTimeMillis();
		//System.out.println("The nearest switch has our team on the " + fms.getNearPlate() + " side.");
		//System.out.println("The central scale has our team on the " + fms.getMidPlate() + " side.");
		// Y axis is upside-down, -1.0 is up and 1.0 is down.		
		double newSpeed = invNum * (0.5 - throttle.getRawAxis(0) * 0.5) * stick.getRawAxis(1) * -0.7; //(leftStick.getY() * (leftStick.getThrottle() - 1) / 2); // Left stick y / throttle controls speed
		/* if(throttle.getRawButton(23)) {
			newSpeed *= -1;
		} else if(!throttle.getRawButton(21)) {
			newSpeed = 0.0;
		}*/
		/* 0 at top, 1 at bottom. */
		MAX_ACCELERATION = (/*lift.getCurrentPos()*/ 1.2e8 / 1.2e8 + 1.0) * 0.4 + 0.8;
		if (rightStick.getRawButton(11)) {
			//MAX_ACCELERATION = 999.0;
		}
		if (newSpeed > previousSpeed) {
			newSpeed = Math.min(newSpeed, previousSpeed + MAX_ACCELERATION * secondsSinceLastUpdate);
		} else if (newSpeed < previousSpeed) {
			newSpeed = Math.max(newSpeed, previousSpeed - MAX_ACCELERATION * secondsSinceLastUpdate);
		}
		
		// TODO: REPLACE THIS WITH A SPECIFIC BUTTON!
		double turnSpeed = (stick.getRawButton(5) ? 0.25 : 0.45) * stick.getRawAxis(0); //(-throttle.getRawAxis(0) + 1.0) / 2.0;
		drive.set(newSpeed, turnSpeed);//gamepad.getRawAxis(2) * turnSpeed); // Right stick x controls turn
		previousSpeed = newSpeed;

		if (stick.getRawButton(1)) { // Trigger toggles claw
			if(!triggerWasPressed) {
				claw.toggleClaw();
			}
			triggerWasPressed = true;
		} else {
			triggerWasPressed = false;
		}

		if (stick.getRawButton(11)) { // Go up when left 6 is pressed
			lift.setLiftSpeed(1.0);
			liftWasMoving = true;
		} else if (stick.getRawButton(13)) { // Go down when left 4 is pressed
			lift.setLiftSpeed(-1.0);
			liftWasMoving = true;
		}/* else if(stick.getRawButton(13)) { // TODO: Set this to a specific button!
			lift.moveTo(AutonomousMode.LIFT_LOWEST_HEIGHT);
		} else if(stick.getRawButton(11)) { // TODO: Set this to a specific button!
			lift.moveTo(AutonomousMode.LIFT_MAX_HEIGHT);
		} else if(stick.getRawButton(3) || stick.getRawButton(12) || stick.getRawButton(14)) { // TODO: Set this to a specific button!
			lift.moveTo(AutonomousMode.LIFT_WING_HEIGHT);
		} */else if (!lift.isMovingToTarget()) {
			lift.holdCurrentPosition();
			liftWasMoving = false;
		}
		System.out.println(lift.getCurrentPos());

		double dt = ((double) (System.currentTimeMillis() - previousTime)) / 1000.0;
		previousTime = System.currentTimeMillis();
		for (HardwareModule module : modules) {
			module.periodic(dt);
		}
	}

	@Override
	public void testInit() {
		drive.enableMechanicalCompensation();
		drive.resetEncoders();
	}
	
	DigitalInput input = new DigitalInput(9);
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		double dt = ((double) (System.currentTimeMillis() - previousTime)) / 1000.0;
		previousTime = System.currentTimeMillis();
		System.out.println(lift.forwardLimitPressed());
		for (HardwareModule module : modules) {
			module.periodic(dt);
		}
//		for(int i = 0; i < throttle.getButtonCount(); i++) {
//			System.out.print(i);
//			System.out.print(": ");
//			System.out.print(throttle.getRawButton(i));
//			System.out.print
//		}
	}
}
