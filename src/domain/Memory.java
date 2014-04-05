package domain;

import java.util.Vector;

public class Memory {
	
	private Vector<Byte> mem;
	private Byte input;
	private Byte output;
	
		
	public Memory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Vector<Byte> getMem() {
		return mem;
	}
	public void setMem(Vector<Byte> mem) {
		this.mem = mem;
	}
	public Byte getInput() {
		return input;
	}
	public void setInput(Byte input) {
		this.input = input;
	}
	public Byte getOutput() {
		return output;
	}
	public void setOutput(Byte output) {
		this.output = output;
	}

	

}
