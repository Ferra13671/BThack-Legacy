package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

import com.ferra13671.BThack.api.SoundSystem.yaw.Sound;

public class MemSound implements Sound {
	
	private byte[] left;
	private byte[] right;
	private Mixer mixer;
	private final int ID; //unique ID to match references
	
	/**
	 * Construct a new MemSound with the given data and Mixer which will handle
	 * this MemSound.
	 * @param left left channel of sound data
	 * @param right right channel of sound data
	 * @param mixer Mixer that will handle this MemSound
	 * @param id unique ID of this MemSound
	 */
	public MemSound(byte[] left, byte[] right, Mixer mixer, int id) {
		this.left = left;
		this.right = right;
		this.mixer = mixer;
		this.ID = id;
	}
	
	/**
	 * Plays this MemSound.
	 */
	@Override
	public void play() {
		this.play(1.0);
	}
	
	/**
	 * Plays this MemSound with a specified volume.
	 * @param volume the volume at which to play this MemSound
	 */
	@Override
	public void play(double volume) {
		this.play(volume, 0.0);
	}

	/**
	 * Plays this MemSound with a specified volume and pan.
	 * @param volume the volume at which to play this MemSound
	 * @param pan the pan value to play this MemSound [-1.0,1.0], values outside
	 * the valid range will assume no panning (0.0)
	 */
	@Override
	public void play(double volume, double pan) {
		//dispatch a sound refence to the mixer
		SoundReference ref = new MemSoundReference(this.left, this.right,
				volume, pan, this.ID);
		this.mixer.registerSoundReference(ref);
	}
	
	/**
	 * Stops this MemSound from playing.  Note that if this MemSound was played
	 * repeatedly in an overlapping fashion, all instances of this MemSound
	 * still playing will be stopped.
	 */
	@Override
	public void stop() {
		this.mixer.unRegisterSoundReference(this.ID);
	}
	
	/**
	 * Unloads this MemSound from the system.  Attempts to use this MemSound
	 * after unloading will result in error.
	 */
	@Override
	public void unload() {
		this.mixer.unRegisterSoundReference(this.ID);
		this.mixer = null;
		this.left = null;
		this.right = null;
	}
	
	/////////////
	//Reference//
	/////////////
	
	/**
	 * The MemSoundReference is an implementation of the SoundReference
	 * interface.
	 * 
	 * @author Finn Kuusisto
	 */
	private static class MemSoundReference implements SoundReference {

		public final int SOUND_ID; //parent MemSound
		
		private byte[] left;
		private byte[] right;
		private int position;
		private final double volume;
		private final double pan;
		
		/**
		 * Construct a new MemSoundReference with the given reference data.
		 * @param left left channel of sound data
		 * @param right right channel of sound data
		 * @param volume volume at which to play the sound
		 * @param pan pan at which to play the sound
		 * @param soundID ID of the MemSound for which this is a reference
		 */
		public MemSoundReference(byte[] left, byte[] right, double volume,
				double pan, int soundID) {
			this.left = left;
			this.right = right;
			this.volume = (volume >= 0.0) ? volume : 1.0;
			this.pan = (pan >= -1.0 && pan <= 1.0) ? pan : 0.0;
			this.position = 0;
			this.SOUND_ID = soundID;
		}

		/**
		 * Get the ID of the MemSound that produced this MemSoundReference.
		 * @return the ID of this MemSoundReference's parent MemSound
		 */
		@Override
		public int getSoundID() {
			return this.SOUND_ID;
		}
		
		/**
		 * Gets the volume of this MemSoundReference.
		 * @return volume of this MemSoundReference
		 */
		@Override
		public double getVolume() {
			return this.volume;
		}

		/**
		 * Gets the pan of this MemSoundReference.
		 * @return pan of this MemSoundReference
		 */
		@Override
		public double getPan() {
			return this.pan;
		}
		
		/**
		 * Get the number of bytes remaining for each channel.
		 * @return number of bytes remaining for each channel
		 */
		@Override
		public long bytesAvailable() {
			return this.left.length - this.position;
		}
		
		/**
		 * Skip a specified number of bytes of the audio data.
		 * @param num number of bytes to skip
		 */
		@Override
		public synchronized void skipBytes(long num) {
			this.position += (int) num;
		}
		
		/**
		 * Get the next two bytes from the sound data in the specified
		 * endianness.
		 * @param data length-2 array to write in next two bytes from each
		 * channel
		 * @param bigEndian true if the bytes should be read big-endian
		 */
		@Override
		public void nextTwoBytes(int[] data, boolean bigEndian) {
			if (bigEndian) {
				//left
				data[0] = ((this.left[this.position] << 8) |
						(this.left[this.position + 1] & 0xFF));
				//right
				data[1] = ((this.right[this.position] << 8) |
						(this.right[this.position + 1] & 0xFF));
			}
			else {
				//left
				data[0] = ((this.left[this.position + 1] << 8) |
						(this.left[this.position] & 0xFF));
				//right
				data[1] = ((this.right[this.position + 1] << 8) |
						(this.right[this.position] & 0xFF));
			}
			this.position += 2;
		}

		/**
		 * Does any cleanup necessary to dispose of resources in use by this
		 * MemSoundReference.
		 */
		@Override
		public void dispose() {
			this.position = this.left.length + 1;
			this.left = null;
			this.right = null;
		}
		
	}

}
