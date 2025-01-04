package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

import com.ferra13671.BThack.api.SoundSystem.yaw.TinySound;

import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateRunner implements Runnable {
		
		private final AtomicBoolean running;
		private final SourceDataLine outLine;
		private final Mixer mixer;
		
		/**
		 * Constructs a new UpdateRunner to update the TinySound system.
		 * @param mixer the mixer to read audio data from
		 * @param outLine the line to write audio data to
		 */
		public UpdateRunner(Mixer mixer, SourceDataLine outLine) {
			this.running = new AtomicBoolean();
			this.mixer = mixer;
			this.outLine = outLine;
		}
		
		/**
		 * Stop this UpdateRunner from updating the TinySound system.
		 */
		public void stop() {
			this.running.set(false);
		}

		@Override
		public void run() {
			//mark the updater as running
			this.running.set(true);
			//1-sec buffer
			int bufSize = (int)TinySound.FORMAT.getFrameRate() *
				TinySound.FORMAT.getFrameSize();
			byte[] audioBuffer = new byte[bufSize];
			//only buffer some maximum number of frames each update (25ms)
			int maxFramesPerUpdate = 
				(int)((TinySound.FORMAT.getFrameRate() / 1000) * 25);
			int numBytesRead = 0;
			double framesAccrued = 0;
			long lastUpdate = System.nanoTime();
			//keep running until told to stop
			while (this.running.get()) {
				//check the time
				long currTime = System.nanoTime();
				//accrue frames
				double delta = currTime - lastUpdate;
				double secDelta = (delta / 1000000000L);
				framesAccrued += secDelta * TinySound.FORMAT.getFrameRate(); 
				//read frames if needed
				int framesToRead = (int)framesAccrued;
				int framesToSkip = 0;
				//check if we need to skip frames to catch up
				if (framesToRead > maxFramesPerUpdate) {
					framesToSkip = framesToRead - maxFramesPerUpdate;
					framesToRead = maxFramesPerUpdate;
				}
				//skip frames
				if (framesToSkip > 0) {
					int bytesToSkip = framesToSkip *
						TinySound.FORMAT.getFrameSize();
					this.mixer.skip(bytesToSkip);
				}
				//read frames
				if (framesToRead > 0) {
					//read from the mixer
					int bytesToRead = framesToRead *
						TinySound.FORMAT.getFrameSize();
					int tmpBytesRead = this.mixer.read(audioBuffer,
							numBytesRead, bytesToRead);
					numBytesRead += tmpBytesRead; //mark how many read
					//fill rest with zeroes
					int remaining = bytesToRead - tmpBytesRead;
					for (int i = 0; i < remaining; i++) {
						audioBuffer[numBytesRead + i] = 0;
					}
					numBytesRead += remaining; //mark zeroes read
				}
				//mark frames read and skipped
				framesAccrued -= (framesToRead + framesToSkip);
				//write to speakers
				if (numBytesRead > 0) {
					this.outLine.write(audioBuffer, 0, numBytesRead);
					numBytesRead = 0;
				}
				//mark last update
				lastUpdate = currTime;
				//give the CPU back to the OS for a bit
				try {
					Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		}
		
	}