package org.usfirst.frc.team5669.powerup;

import java.util.ArrayList;

import org.usfirst.frc.team5669.hardware.HardwareModule;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousInstructor2018 implements HardwareModule {
	public enum StartSide {
		LEFT, MID, RIGHT;
	}
	public enum Target {
		NEAR_LEFT, NEAR_RIGHT, MID_LEFT, MID_RIGHT;
	}
	private SendableChooser<StartSide> startSideChooser = new SendableChooser<>();
	private ArrayList<SendableChooser<Target>> priorityChoosers = new ArrayList<>();

	@Override
	public void setup() {
		startSideChooser.addDefault("Leftmost Side", StartSide.LEFT);
		startSideChooser.addObject("Middle Side", StartSide.MID);
		startSideChooser.addObject("Rightmost Side", StartSide.RIGHT);
		SmartDashboard.putData("AutonomousInstructor2018.startSide", startSideChooser);
		for(int i = 0; i < 4; i++) {
			SendableChooser<Target> chooser = new SendableChooser<>();
			chooser.addObject("Left Plate of Alliance Switch", Target.NEAR_LEFT);
			chooser.addObject("Right Plate of Alliance Switch", Target.NEAR_RIGHT);
			chooser.addObject("Left Plate of Middle Switch", Target.MID_LEFT);
			chooser.addObject("Right Plate of Middle Switch", Target.MID_RIGHT);
			SmartDashboard.putData("AutonomousInstructor2018.priority" + Integer.toString(i+1), chooser);
		}
	}

	@Override
	public void periodic() {
		
	}

	@Override
	public void stop() {
		
	}
	
	public StartSide getStartSide() {
		return startSideChooser.getSelected();
	}
	
	public Target getPriorityTarget(int priorityLevel) {
		return priorityChoosers.get(priorityLevel).getSelected();
	}
}
