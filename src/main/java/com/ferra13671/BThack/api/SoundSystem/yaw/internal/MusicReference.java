package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

public interface MusicReference {

	/**
	 * Get the playing setting of this MusicReference.
	 * @return true if this MusicReference is set to play
	 */
    boolean getPlaying();
	
	/**
	 * Get the loop setting of this MusicReference.
	 * @return true if this MusicReference is set to loop
	 */
    boolean getLoop();
	
	/**
	 * Get the byte index of this MusicReference.
	 * @return byte index of this MusicReference
	 */
    long getPosition();
	
	/**
	 * Get the loop-position byte index of this MusicReference.
	 * @return loop-position byte index of this MusicReference
	 */
    long getLoopPosition();
	
	/**
	 * Get the volume of this MusicReference.
	 * @return volume of this MusicReference
	 */
    double getVolume();
	
	/**
	 * Get the pan of this MusicReference.
	 * @return pan of this MusicReference
	 */
    double getPan();
	
	/**
	 * Set whether this MusicReference is playing.
	 * @param playing whether this MusicReference is playing
	 */
    void setPlaying(boolean playing);
	
	/**
	 * Set whether this MusicReference will loop.
	 * @param loop whether this MusicReference will loop
	 */
    void setLoop(boolean loop);
	
	/**
	 * Set the byte index of this MusicReference.
	 * @param position the byte index to set
	 */
    void setPosition(long position);
	
	/**
	 * Set the loop-position byte index of this MusicReference.
	 * @param loopPosition the loop-position byte index to set
	 */
    void setLoopPosition(long loopPosition);
	
	/**
	 * Set the volume of this MusicReference.
	 * @param volume the desired volume of this MusicReference
	 */
    void setVolume(double volume);
	
	/**
	 * Set the pan of this MusicReference.  Must be between -1.0 (full pan left)
	 * and 1.0 (full pan right).
	 * @param pan the desired pan of this MusicReference
	 */
    void setPan(double pan);
	
	/**
	 * Get the number of bytes remaining for each channel until the end of this
	 * Music.
	 * @return number of bytes remaining for each channel
	 */
    long bytesAvailable();
	
	/**
	 * Determine if there are no bytes remaining and play has stopped.
	 * @return true if there are no bytes remaining and the reference is no
	 * longer playing
	 */
    boolean done();
	
	/**
	 * Skip a specified number of bytes of the audio data.
	 * @param num number of bytes to skip
	 */
    void skipBytes(long num);
	
	/**
	 * Get the next two bytes from the music data in the specified endianness.
	 * @param data length-2 array to write in next two bytes from each channel
	 * @param bigEndian true if the bytes should be read big-endian
	 */
    void nextTwoBytes(int[] data, boolean bigEndian);
	
	/**
	 * Does any cleanup necessary to dispose of resources in use by this
	 * MusicReference.
	 */
    void dispose();
	
}
