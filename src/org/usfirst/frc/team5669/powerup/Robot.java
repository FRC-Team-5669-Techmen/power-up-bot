/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5669.powerup;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team5669.autonomous.AutonomousMode;
import org.usfirst.frc.team5669.autonomous.AutonomousQueue;
import org.usfirst.frc.team5669.autonomous.AutonomousStep;
import org.usfirst.frc.team5669.autonomous.PriorityObjective;
import org.usfirst.frc.team5669.autonomous.StartPos;
import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.DistanceSensor;
import org.usfirst.frc.team5669.hardware.HardwareModule;
import org.usfirst.frc.team5669.hardware.RS232DistanceSensor;
import org.usfirst.frc.team5669.hardware.ReevesDrive;
import org.usfirst.frc.team5669.hardware.TankDrive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
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
	private TankDrive drive = new TankDrive(l1, l2, r1, r2);
	private Lift lift = new Lift(new TalonSRX(20));
	private PneumaticClaw claw = new PneumaticClaw(0, 0, 2);
	// TODO: Replace back distance sensor with RS232 implementation
	private DistanceSensor frontDist = new AnalogDistanceSensor(3, "front"),
			rightDist = new AnalogDistanceSensor(2, "right"), backDist = new RS232DistanceSensor(),
			leftDist = new AnalogDistanceSensor(0, "left");
	private FMS2018 fms = new FMS2018();
	private AutonomousInstructor2018 autoInst = new AutonomousInstructor2018();
	private HardwareModule[] modules = { drive, fms, autoInst, frontDist, rightDist, backDist, leftDist, claw, lift };
	
	private AutonomousQueue queue = new AutonomousQueue();
	private Joystick stick = new Joystick(0);
	private PriorityObjective objective;
	private StartPos start;
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
		for (HardwareModule module : modules) {
			module.setup();
		}
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Autonomous init called!");
		System.out.println("The nearest switch has our team on the " + fms.getNearPlate() + " side.");
		System.out.println("The central switch has our team on the " + fms.getMidPlate() + " side.");

		start = StartPos.LEFT; // I don't know how to initialize this variable depending on where we start.

		objective = new PriorityObjective(autoInst.getStartSide(), fms.getNearPlate(), fms.getMidPlate());


		autoMode.addHardware(drive, lift, claw);
		autoMode.addSensors(frontDist, backDist, leftDist, rightDist);
		autoMode.addFMS(fms);
		autoMode.addStart(start);

		List<AutonomousStep> steps = new ArrayList<>();

		// Figure out which objective is most efficient
		/*
		switch (objective.getPriority()) {
		case SCALE:
			steps = autoMode.getScaleSteps();
			break;
		case SWITCH:
			steps = autoMode.getSwitchSteps();
			break;
		default:
			// IDK Yet...
			break;
		}*/
		
		// For testing purposes...
		steps = autoMode.getSwitchSteps();
		queue.clear();
		for (AutonomousStep step : steps) {
			queue.add(step);
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		queue.periodic();
		for (HardwareModule module : modules) {
			module.periodic();
		}
		//System.out.println(backDist.getDistance());
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
	}

	boolean liftWasMoving = false;
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		// Y axis is upside-down, -1.0 is up and 1.0 is down.
		
		// I tried to program the POV to allow for slow, perfectly straight movement at constant speed. (I don't know if this works.)
		/* There seems to be problems with this code, but I want to communicate with Shane about what it is supposed
		 * to do before I try to fix it, so I don't break anything.
		if(stick.getY() == 0)
		{
			if(stick.getPOV() == 360)
			{
				drive.set(-(stick.getThrottle() - 1) / 2, stick.getTwist() / 4);
			}
			else if(stick.getPOV() == 180)
			{
				drive.set((stick.getThrottle() - 1) / 2, stick.getTwist() / 4);
			}
			else
			{
				drive.set(stick.getY(), stick.getTwist() / 4); // I DO NOT THINK THIS PART WAS UPLOADED CORRECTLY TO THE ROBORIO. DO NOT RUN THE ROBOT WITHOUT UPLOADING THIS CODE.
			}
		}
		else
		{
			drive.set(stick.getY() * (stick.getThrottle() - 1) / 2, stick.getTwist() / 4);
		} */
		
		// Allows the robot's controls to be inverted when button 10 is pressed
		if (stick.getRawButton(10)) {
			invert = !invert;
		}
		if(invert)
		{
			invNum = -1;
		}
		else
		{
			invNum = 1;
		}
		drive.set(invNum * stick.getY() * (stick.getThrottle() - 1) / 2, stick.getTwist() / 4);
		
		System.out.println("LIFT POSITION: " + lift.getCurrentPos());
		// I changed the claw controls to trigger and 2
		if (stick.getRawButton(2)) {
			claw.expandClaw();
		} else if (stick.getRawButton(1)) {
			claw.contractClaw();
		}
		
		// I changed the lift controls to 5 and 3
		if (stick.getRawButton(5)) {
			lift.setLiftSpeed(1.0);
			liftWasMoving = true;
		} else if (stick.getRawButton(3)) {
			lift.setLiftSpeed(-1.0);
			liftWasMoving = true;
		} else if(liftWasMoving) {
			lift.holdCurrentPosition();
			liftWasMoving = false;
		}
		
		
		for (HardwareModule module : modules) {
			module.periodic();
		}
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		for (HardwareModule module : modules) {
			module.periodic();
		}
	}
}
