package com.ferra13671.BThack.api.SoundSystem.yaw;

public interface Music {


	/**
	 * Play this Music and loop if specified.
	 * @param loop if this Music should loop
	 */
	void play(boolean loop);


	/**
	 * Play this Music at the specified volume and loop if specified.
	 * @param loop if this Music should loop
	 * @param volume the volume to play the this Music
	 */
	void play(boolean loop, double volume);


	/**
	 * Play this Music at the specified volume and pan, and loop if specified.
	 * @param loop if this Music should loop
	 * @param volume the volume to play the this Music
	 * @param pan the pan at which to play this Music [-1.0,1.0], values outside
	 * the valid range will be ignored
	 */
	void play(boolean loop, double volume, double pan);


	/**
	 * Stop playing this Music and set its position to the beginning.
	 */
	void stop();


	/**
	 * Stop playing this Music and keep its current position.
	 */
	void pause();


	/**
	 * Play this Music from its current position.
	 */
	void resume();


	/**
	 * Set this Music's position to the beginning.
	 */
    void rewind();


	/**
	 * Set this Music's position to the loop position.
	 */
    void rewindToLoopPosition();


	/**
	 * Determine if this Music is playing.
	 * @return true if this Music is playing
	 */
    boolean playing();


	/**
	 * Determine if this Music has reached its end and is done playing.
	 * @return true if this Music is at the end and is done playing
	 */
    boolean done();


	/**
	 * Determine if this Music will loop.
	 * @return true if this Music will loop
	 */
    boolean loop();


	/**
	 * Set whether this Music will loop.
	 * @param loop whether this Music will loop
	 */
    void setLoop(boolean loop);


	/**
	 * Get the loop position of this Music by sample frame.
	 * @return loop position by sample frame
	 */
    int getLoopPositionByFrame();


	/**
	 * Get the loop position of this Music by seconds.
	 * @return loop position by seconds
	 */
    double getLoopPositionBySeconds();


	/**
	 * Set the loop position of this Music by sample frame.
	 * @param frameIndex sample frame loop position to set
	 */
    void setLoopPositionByFrame(int frameIndex);


	/**
	 * Set the loop position of this Music by seconds.
	 * @param seconds loop position to set by seconds
	 */
    void setLoopPositionBySeconds(double seconds);


	/**
	 * Get the volume of this Music.
	 * @return volume of this Music
	 */
    double getVolume();


	/**
	 * Set the volume of this Music.
	 * @param volume the desired volume of this Music
	 */
    void setVolume(double volume);


	/**
	 * Get the pan of this Music.
	 * @return pan of this Music
	 */
    double getPan();


	/**
	 * Set the pan of this Music.  Must be between -1.0 (full pan left) and 1.0
	 * (full pan right).  Values outside the valid range will be ignored.
	 * @param pan the desired pan of this Music
	 */
    void setPan(double pan);


	/**
	 * Unload this Music from the system.  Attempts to use this Music after
	 * unloading will result in error.
	 */
    void unload();
	
}
