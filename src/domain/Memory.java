package domain;

import java.util.Vector;

public class Memory {
	
	private Vector<Byte> mem;
	private Byte input;
	private Byte output;
	private static Memory INSTANCE = new Memory();
	
	private Memory() {
		this.mem = new Vector<Byte>();
		this.cleanMemory();
	}

    public static Memory getInstance() {
        return INSTANCE;
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

	public Byte getValue(Integer pos){
		return this.mem.elementAt(pos);
	}

	public void setValue(Integer pos, Byte value){
		this.mem.set(pos, value);
	}
	
	private void cleanMemory(){
		Byte bt = 0;
		for (int i = 0; i < 255; i++) {
			this.mem.add(bt);
		}
		
	}

}
