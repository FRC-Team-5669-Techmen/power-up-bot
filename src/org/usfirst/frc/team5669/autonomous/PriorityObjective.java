package org.usfirst.frc.team5669.autonomous;
import java.util.ArrayList;

import org.usfirst.frc.team5669.powerup.SpitStep;
import org.usfirst.frc.team5669.hardware.AnalogDistanceSensor;
import org.usfirst.frc.team5669.hardware.TankDrive;
import org.usfirst.frc.team5669.powerup.FMS2018.Side;
import org.usfirst.frc.team5669.powerup.Lift;

public class PriorityObjective {
	
	private Priority priority;
	private int switchSide; // 1 for Left, 2 for Right
	private int midSide; // 1 for Left, 2 for Right
	private int startSide; // 1 for Left, 2 for Right, 0 for center
	private int modifier; // 0 for nothing, 1 always prioritize switch, 2 for always prioritize middle
	
	
	public PriorityObjective(StartPos start, Side swtch, Side mid)
	{
		initSides(start, swtch, mid);
		modifier = 0;
		determinePriority();
	}
	public PriorityObjective(StartPos start, Side swtch, Side mid, int mod)
	{
		initSides(start, swtch, mid);
		modifier = mod;
		determinePriority();
	}
	public Priority getPriority()
	{
		return priority;
	}
	
	private void initSides(StartPos start, Side swtch, Side mid)
	{
		// Convert swtch to an int
		switch(swtch) {
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
		switch(mid) {
		case LEFT:
			midSide = 1;
			break;
		case RIGHT:
			midSide = 2;
			break;
		default:
			midSide = 0;
			break;
		}
		switch(start) {
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
	private void determinePriority()
	{
		if(modifier == 1)
		{
			priority = Priority.SWITCH;
		}
		else if(modifier == 2)
		{
			priority = Priority.SCALE;
		}
		else
		{
			if(switchSide == startSide)
			{
				priority = Priority.SWITCH;
			}
			else if(midSide == startSide)
			{
				priority = Priority.SCALE;
			}
			else
			{
				priority = Priority.SWITCH;
			}
		}
		
	}
	
}
