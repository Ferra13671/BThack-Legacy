package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

public interface SoundReference {

	/**
	 * Get the ID of the Sound that produced this SoundReference.
	 * @return the ID of this SoundReference's parent Sound
	 */
    int getSoundID();
	
	/**
	 * Gets the volume of this SoundReference.
	 * @return volume of this SoundReference
	 */
    double getVolume();
	
	/**
	 * Gets the pan of this SoundReference.
	 * @return pan of this SoundReference
	 */
    double getPan();
	
	/**
	 * Get the number of bytes remaining for each channel.
	 * @return number of bytes remaining for each channel
	 */
    long bytesAvailable();
	
	/**
	 * Skip a specified number of bytes of the audio data.
	 * @param num number of bytes to skip
	 */
    void skipBytes(long num);
	
	/**
	 * Get the next two bytes from the sound data in the specified endianness.
	 * @param data length-2 array to write in next two bytes from each channel
	 * @param bigEndian true if the bytes should be read big-endian
	 */
    void nextTwoBytes(int[] data, boolean bigEndian);
	
	/**
	 * Does any cleanup necessary to dispose of resources in use by this
	 * SoundReference.
	 */
    void dispose();
	
}
