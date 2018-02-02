/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5669.powerup;

import org.usfirst.frc.team5669.autonomous.AutonomousQueue;
import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.HardwareModule;
import org.usfirst.frc.team5669.hardware.TankDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	// WPI_TalonSRX is compatible with other WPILib motor controllers. TalonSRX has more functions but is less compatible.
	private WPI_TalonSRX l1 = new WPI_TalonSRX(1), l2 = new WPI_TalonSRX(2), r3 = new WPI_TalonSRX(3), r4 = new WPI_TalonSRX(4);
	private TankDrive drive = new TankDrive(l1, l2, r3, r4);
	private AnalogDistanceSensor frontDist = new AnalogDistanceSensor(0), rightDist = new AnalogDistanceSensor(1), 
			backDist = new AnalogDistanceSensor(2), leftDist = new AnalogDistanceSensor(3);
	private FMS2018 fms = new FMS2018();
	private HardwareModule[] modules = { drive, fms, frontDist, rightDist, backDist, leftDist };
	private AutonomousQueue queue = new AutonomousQueue();
	private Joystick stick = new Joystick(0);
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("Robot init called!");
		for(HardwareModule module : modules) {
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
		
		queue.clear();
		// Insert code to build the autonomous queue here.
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		queue.periodic();
		for(HardwareModule module : modules) {
			module.periodic();
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		// Y axis is upside-down, -1.0 is up and 1.0 is down.
		drive.set(-stick.getY(), stick.getX());
		
		for(HardwareModule module : modules) {
			module.periodic();
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		for(HardwareModule module : modules) {
			module.periodic();
		}
	}
}
