package org.usfirst.frc.team5669.powerup;

import java.util.ArrayList;

import org.usfirst.frc.team5669.hardware.HardwareModule;
import org.usfirst.frc.team5669.powerup.FMS2018.Side;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousInstructor2018 implements HardwareModule {
	public enum Target {
		NEAR_LEFT, NEAR_RIGHT, MID_LEFT, MID_RIGHT;
	}

	private SendableChooser<Side> startSideChooser = new SendableChooser<>();
	private ArrayList<SendableChooser<Target>> priorityChoosers = new ArrayList<>();

	@Override
	public void setup() {
		startSideChooser.addDefault("Leftmost Side", Side.LEFT);
		startSideChooser.addObject("Middle Side", Side.MID);
		startSideChooser.addObject("Rightmost Side", Side.RIGHT);
		SmartDashboard.putData("AutonomousInstructor2018.startSide", startSideChooser);
		for (int i = 0; i < 4; i++) {
			SendableChooser<Target> chooser = new SendableChooser<>();
			chooser.addObject("Left Plate of Alliance Switch", Target.NEAR_LEFT);
			chooser.addObject("Right Plate of Alliance Switch", Target.NEAR_RIGHT);
			chooser.addObject("Left Plate of Middle Switch", Target.MID_LEFT);
			chooser.addObject("Right Plate of Middle Switch", Target.MID_RIGHT);
			SmartDashboard.putData("AutonomousInstructor2018.priority" + Integer.toString(i + 1), chooser);
		}
	}

	@Override
	public void periodic(double dt) {

	}

	@Override
	public void stop() {

	}

	public Side getStartSide() {
		return startSideChooser.getSelected();
	}

	public Target getPriorityTarget(int priorityLevel) {
		return priorityChoosers.get(priorityLevel).getSelected();
	}
}
