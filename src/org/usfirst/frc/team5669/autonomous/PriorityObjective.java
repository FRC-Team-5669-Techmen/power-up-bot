package org.usfirst.frc.team5669.autonomous;

import org.usfirst.frc.team5669.powerup.FMS2018.Side;

public class PriorityObjective {

	private Priority priority;
	private Side switchSide, scaleSide, startSide; // 1 for Left, 2 for Right
	private int modifier; // 0 for nothing, 1 always prioritize switch, 2 for always prioritize middle

	public PriorityObjective(Side start, Side swtch, Side mid) {
		initSides(start, swtch, mid);
		modifier = 0;
		determinePriority();
	}

	public PriorityObjective(Side start, Side swtch, Side mid, int mod) {
		initSides(start, swtch, mid);
		modifier = mod;
		determinePriority();
	}

	public Priority getPriority() {
		return priority;
	}

	private void initSides(Side start, Side swtch, Side mid) {
		switchSide = swtch;
		scaleSide = mid;
		startSide = start;
	}

	private void determinePriority() {
		if (modifier == 1) {
			priority = Priority.SWITCH;
		} else if (modifier == 2) {
			priority = Priority.SCALE;
		} else {
			//TODO: I changed this to prioritize switch if both sw and sc are owned
			if (switchSide == startSide) {
				priority = Priority.SWITCH;
			} else if (scaleSide == startSide) {
				priority = Priority.SCALE;
			} else {
				priority = Priority.SWITCH;
			}
		}

	}

}
