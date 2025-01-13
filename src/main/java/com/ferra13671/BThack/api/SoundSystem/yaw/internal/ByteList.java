package com.ferra13671.BThack.api.SoundSystem.yaw.internal;

import java.util.Arrays;

public class ByteList {
	
	private int numBytes;
	private byte[] data;
	
	/**
	 * Create a new ByteList of default starting size.
	 */
	public ByteList() {
		this(10);
	}
	
	/**
	 * Create a new ByteList of a specified starting size.  If the size is not
	 * valid, the default starting size is used.
	 * @param startSize the desired start size for the backing array
	 */
	public ByteList(int startSize) {
		startSize = startSize >= 0 ? startSize : 10;
		this.data = new byte[startSize];
		this.numBytes = 0;
	}
	
	/**
	 * Add a byte to the end of this ByteList.
	 * @param b the byte to add
	 */
	public void add(byte b) {
		if (this.numBytes >= this.data.length
				&& this.numBytes == Integer.MAX_VALUE) {
			throw new RuntimeException("Array reached maximum size");
		}
		else if (this.numBytes >= this.data.length) {
			//grow the backing array
			long tmp = this.data.length * 2L;
			int newSize = tmp > Integer.MAX_VALUE ?
					Integer.MAX_VALUE : (int)tmp;
			this.data = Arrays.copyOf(this.data, newSize);
		}
		this.data[this.numBytes] = b;
		this.numBytes++;
	}
	
	/**
	 * Get a byte at a specified index in this ByteList.
	 * @param i the index of the byte to get
	 * @return the byte at index i
	 */
	public byte get(int i) {
		if (i < 0 || i > this.numBytes) {
			throw new ArrayIndexOutOfBoundsException(i);
		}
		return this.data[i];
	}
	
	/**
	 * Get the number of bytes that have been added to this ByteList.
	 * @return the number of bytes added to this ByteList
	 */
	public int size() {
		return this.numBytes;
	}
	
	/**
	 * Get an array of all the bytes added to this ByteList.  This does not
	 * affect the backing array.
	 * @return an array of the bytes added to this ByteList
	 */
	public byte[] asArray() {
		return Arrays.copyOf(this.data, this.numBytes);
	}
	
	/**
	 * Clear this ByteList of all added bytes.
	 */
	public void clear() {
		this.data = new byte[10];
		this.numBytes = 0;
	}

}
