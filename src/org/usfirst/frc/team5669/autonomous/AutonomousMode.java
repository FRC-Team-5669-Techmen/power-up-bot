package org.usfirst.frc.team5669.autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.TankDrive;
import org.usfirst.frc.team5669.powerup.FMS2018;
import org.usfirst.frc.team5669.powerup.Lift;
import org.usfirst.frc.team5669.powerup.SpitStep;

public class AutonomousMode {
	private TankDrive drive;
	private Lift lift;
	private AnalogDistanceSensor frontDist, backDist, leftDist, rightDist;
	private FMS2018 fms;
	
	private int switchSide; // 1 for Left, 2 for Right
	private int scaleSide; // 1 for Left, 2 for Right
	private int startSide; // 1 for Left, 2 for Right, 0 for center
	
	
	public void addHardware(TankDrive d, Lift l)
	{
		drive = d;
		lift = l;
	}
	public void addSensors(AnalogDistanceSensor f, AnalogDistanceSensor b, AnalogDistanceSensor l, AnalogDistanceSensor r)
	{
		frontDist = f;
		backDist = b;
		leftDist = l;
		rightDist = r;
	}
	public void addFMS(FMS2018 f)
	{
		fms = f;
		// Convert swtch to an int
		switch(fms.getNearPlate()) {
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
		switch(fms.getMidPlate()) {
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
	public void addStart(StartPos s)
	{
		switch(s) {
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
	
	public ArrayList<AutonomousStep> getSwitchSteps()
	{
		ArrayList<AutonomousStep> steps = new ArrayList<AutonomousStep>();
		if(startSide == switchSide)
		{
			steps.add(new StartTankDriveStep(drive, 0.4, 0.0));
			steps.add(new DistanceLessThanWait(frontDist, 24.0));
			steps.add(new StopTankDriveStep(drive));
			steps.add(new SpitStep(lift));
		}
		else
		{
			// Go to the other side of the switch
		}
		
		return steps;
	}
	public ArrayList<AutonomousStep> getScaleSteps()
	{
		ArrayList<AutonomousStep> steps = new ArrayList<AutonomousStep>();
		if(startSide == scaleSide)
		{
			// Go to our side of the scale
		}
		else
		{
			// Go to the other side of the scale
		}
		
		return steps;
	}
}
