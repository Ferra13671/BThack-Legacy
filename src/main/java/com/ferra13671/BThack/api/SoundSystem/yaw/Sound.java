package com.ferra13671.BThack.api.SoundSystem.yaw;

public interface Sound {

	/**
	 * Plays this Sound.
	 */
    void play();
	
	/**
	 * Plays this Sound with a specified volume.
	 * @param volume the volume at which to play this Sound
	 */
    void play(double volume);
	
	/**
	 * Plays this Sound with a specified volume and pan.
	 * @param volume the volume at which to play this Sound
	 * @param pan the pan value to play this Sound [-1.0,1.0], values outside
	 * the valid range will assume no panning (0.0)
	 */
    void play(double volume, double pan);
	
	/**
	 * Stops this Sound from playing.  Note that if this Sound was played
	 * repeatedly in an overlapping fashion, all instances of this Sound still
	 * playing will be stopped.
	 */
    void stop();
	
	/**
	 * Unloads this Sound from the system.  Attempts to use this Sound after
	 * unloading will result in error.
	 */
    void unload();
	
}
