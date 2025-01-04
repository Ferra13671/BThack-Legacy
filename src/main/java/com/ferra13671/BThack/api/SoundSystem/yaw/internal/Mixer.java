package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

import java.util.ArrayList;
import java.util.List;

public class Mixer {
	
	private final List<MusicReference> musics;
	private final List<SoundReference> sounds;
	private double globalVolume;
	private final int[] dataBuf; //buffer for reading sound data
	
	/**
	 * Construct a new Mixer for TinySound system.
	 */
	public Mixer() {
		this.musics = new ArrayList<>();
		this.sounds = new ArrayList<>();
		this.globalVolume = 1.0;
		this.dataBuf = new int[2]; //2-channel
	}
	
	/**
	 * Get the global volume for this Mixer.
	 * @return the global volume
	 */
	public synchronized double getVolume() {
		return this.globalVolume;
	}
	
	/**
	 * Set the global volume for this Mixer.
	 * @param volume the global volume to set
	 */
	public synchronized void setVolume(double volume) {
		if (volume >= 0.0) {
			this.globalVolume = volume;
		}
	}
	
	/**
	 * Registers a MusicReference with this Mixer.
	 * @param music MusicReference to be registered
	 */
	public synchronized void registerMusicReference(MusicReference music) {
		this.musics.add(music);
	}
	
	/**
	 * Registers a SoundReference with this Mixer.
	 * @param sound SoundReference to be registered
	 */
	public synchronized void registerSoundReference(SoundReference sound) {
		this.sounds.add(sound);
	}
	
	/**
	 * Unregisters a MusicReference with this Mixer.
	 * @param music MusicReference to be unregistered
	 */
	public synchronized void unRegisterMusicReference(MusicReference music) {
		this.musics.remove(music);
	}
	
	/**
	 * Unregisters all SoundReferences with a given soundID.
	 * @param soundID ID of SoundReferences to be unregistered
	 */
	public synchronized void unRegisterSoundReference(int soundID) {
		//removal working backward is easier
		for (int i = this.sounds.size() - 1; i >= 0; i--) {
			if (this.sounds.get(i).getSoundID() == soundID) {
				this.sounds.remove(i).dispose();
			}
		}
	}
	
	/**
	 * Unregister all Music registered with this Mixer.
	 */
	public synchronized void clearMusic() {
		this.musics.clear();
	}
	
	/**
	 * Unregister all Sounds registered with this Mixer.
	 */
	public synchronized void clearSounds() {
		for (SoundReference s : this.sounds) {
			s.dispose();
		}
		this.sounds.clear();
	}
	
	/**
	 * Read bytes from this Mixer.
	 * @param data the buffer to read the bytes into
	 * @param offset the start index to read bytes into
	 * @param length the maximum number of bytes that should be read
	 * @return number of bytes read into buffer
	 */
	public synchronized int read(byte[] data, int offset, int length) {
		//************************************************//
		//assume little-endian, stereo, 16-bit, signed PCM//
		//************************************************//
		int numRead = 0;
		boolean bytesRead = true; //terminate early if out of bytes
		for (int i = offset; i < (length + offset) && bytesRead; i += 4) {
			//first assume we are done
			bytesRead = false;
			//need to track value across audio sources
			double leftValue = 0.0;
			double rightValue = 0.0;
			//go through all the music first
            for (MusicReference music : this.musics) {
                //is the music playing and are their bytes available
                if (music.getPlaying() && music.bytesAvailable() > 0) {
                    //add this music to the mix by volume (and global volume)
                    music.nextTwoBytes(this.dataBuf, false);
                    double volume = music.getVolume() * this.globalVolume;
                    double leftCurr = (this.dataBuf[0] * volume);
                    double rightCurr = (this.dataBuf[1] * volume);
                    //do panning
                    double pan = music.getPan();
                    if (pan != 0.0) {
                        double ll = (pan <= 0.0) ? 1.0 : (1.0 - pan);
                        double lr = (pan <= 0.0) ? Math.abs(pan) : 0.0;
                        double rl = Math.max(pan, 0.0);
                        double rr = (pan >= 0.0) ? 1.0 : (1.0 - Math.abs(pan));
                        double tmpL = (ll * leftCurr) + (lr * rightCurr);
                        double tmpR = (rl * leftCurr) + (rr * rightCurr);
                        leftCurr = tmpL;
                        rightCurr = tmpR;
                    }
                    //update the final left and right channels
                    leftValue += leftCurr;
                    rightValue += rightCurr;
                    //we know we aren't done yet now
                    bytesRead = true;
                }
            }
			//then go through all the sounds (backwards to remove completed)
			for (int s = this.sounds.size() - 1; s >= 0; s--) {
				SoundReference sound = this.sounds.get(s);
				//are their bytes available
				if (sound.bytesAvailable() > 0) {
					//add this sound to the mix by volume (and global volume)
					sound.nextTwoBytes(this.dataBuf, false);
					double volume = sound.getVolume() * this.globalVolume;
					double leftCurr = (this.dataBuf[0] * volume);
					double rightCurr = (this.dataBuf[1] * volume);
					//do panning
					//do panning
					double pan = sound.getPan();
					if (pan != 0.0) {
						double ll = (pan <= 0.0) ? 1.0 : (1.0 - pan);
						double lr = (pan <= 0.0) ? Math.abs(pan) : 0.0;
						double rl = Math.max(pan, 0.0);
						double rr = (pan >= 0.0) ? 1.0 : (1.0 - Math.abs(pan));
						double tmpL = (ll * leftCurr) + (lr * rightCurr);
						double tmpR = (rl * leftCurr) + (rr * rightCurr);
						leftCurr = tmpL;
						rightCurr = tmpR;
					}
					//update the final left and right channels
					leftValue += leftCurr;
					rightValue += rightCurr;
					//we know we aren't done yet now
					bytesRead = true;
					//remove the reference if done
					if (sound.bytesAvailable() <= 0) {
						this.sounds.remove(s).dispose();
					}
				}
				else { //otherwise remove this reference
					this.sounds.remove(s).dispose();
				}
			}
			//if we actually read bytes, store in the buffer
			if (bytesRead) {
				int finalLeftValue = (int)leftValue;
				int finalRightValue = (int)rightValue;
				//clipping
				if (finalLeftValue > Short.MAX_VALUE) {
					finalLeftValue = Short.MAX_VALUE;
				}
				else if (finalLeftValue < Short.MIN_VALUE) {
					finalLeftValue = Short.MIN_VALUE;
				}
				if (finalRightValue > Short.MAX_VALUE) {
					finalRightValue = Short.MAX_VALUE;
				}
				else if (finalRightValue < Short.MIN_VALUE) {
					finalRightValue = Short.MIN_VALUE;
				}
				//left channel bytes
				data[i + 1] = (byte)((finalLeftValue >> 8) & 0xFF); //MSB
				data[i] = (byte)(finalLeftValue & 0xFF); //LSB
				//then right channel bytes
				data[i + 3] = (byte)((finalRightValue >> 8) & 0xFF); //MSB
				data[i + 2] = (byte)(finalRightValue & 0xFF); //LSB
				numRead += 4;
			}
		}
		return numRead;
	}
	
	/**
	 * Skip specified number of bytes of all audio in this Mixer.
	 * @param numBytes the number of bytes to skip
	 */
	public synchronized void skip(int numBytes) {
		//go through all the music first
        for (MusicReference music : this.musics) {
            //is the music playing and are their bytes available
            if (music.getPlaying() && music.bytesAvailable() > 0) {
                //skip the bytes
                music.skipBytes(numBytes);
            }
        }
		//then go through all the sounds (backwards to remove completed)
		for (int s = this.sounds.size() - 1; s >= 0; s--) {
			SoundReference sound = this.sounds.get(s);
			//are their bytes available
			if (sound.bytesAvailable() > 0) {
				//skip the bytes
				sound.skipBytes(numBytes);
				//remove the reference if done
				if (sound.bytesAvailable() <= 0) {
					this.sounds.remove(s).dispose();
				}
			}
			else { //otherwise remove this reference
				this.sounds.remove(s).dispose();
			}
		}
	}

}
