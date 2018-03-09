package org.usfirst.frc.team5669.hardware;

/**
 * A common interface for modules that abstract away pieces of hardware (e.g.
 * mecanum drive, tank drive, claw, etc.)
 */
public interface HardwareModule {
	/**
	 * This is called when the robot first comes online. Use this to set up
	 * parameters of devices included in the module. This will only be called once
	 * for the lifespan of the robot.
	 */
	void setup();

	/**
	 * Called periodically whenever the robot is functional. This should do things
	 * like set motor speeds to fulfill whatever this module has been requested to
	 * do.
	 */
	void periodic();

	/**
	 * This is called when the module should stop doing whatever it is doing. For
	 * example, if this was a claw module and it was previously told to open itself,
	 * this method should make it so the claw stops trying to do this. periodic()
	 * will be called after this.
	 */
	void stop();
}
