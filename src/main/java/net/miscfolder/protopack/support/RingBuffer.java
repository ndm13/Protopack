package net.miscfolder.protopack.support;

public class RingBuffer{
	private final int size;
	private int[] buffer;
	private volatile int index = 0;

	public RingBuffer(int size){
		this.size = size;
		this.buffer = new int[size];
	}

	public int feed(int input){
		int old = buffer[index];
		buffer[index] = input;

		int nextIndex = index + 1;
		index = nextIndex % size;

		return old;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<size;i++)
			sb.append((char)buffer[(index + i) % size]);
		return sb.toString();
	}
}
