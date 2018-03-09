package org.usfirst.frc.team5669.autonomous;

import java.util.ArrayList;

public class AutonomousQueue {
	private ArrayList<AutonomousStep> queue = new ArrayList<>();

	public void clear() {
		if (queue.size() > 0) {
			queue.get(0).finish();
		}
		queue.clear();
	}

	public void add(AutonomousStep toAdd) {
		if (queue.size() == 0) {
			toAdd.start();
		}
		queue.add(toAdd);
	}

	public void periodic() {
		if (queue.size() == 0) {
			return;
		}
		while (true) {
			queue.get(0).periodic();
			if (queue.get(0).isDone()) {
				queue.get(0).finish();
				queue.remove(0);
				if (queue.size() > 0) {
					queue.get(0).start();
				}
			} else {
				return;
			}
		}
	}
}
