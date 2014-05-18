package domain;

import java.util.Iterator;
import java.util.Vector;

public class Memory {
	
	private Vector<Short> mem;
	private Byte input;
	private Byte output;
	private static Memory INSTANCE = new Memory();
	
	private Memory() {
		this.mem = new Vector<Short>();
		this.cleanMemory();
	}

    public static Memory getInstance() {
        return INSTANCE;
    }
    
	public Vector<Short> getMem() {
		return mem;
	}
	public void setMem(Vector<Short> mem) {
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

	public Short getValue(Integer pos){
		return this.mem.elementAt(pos);
	}

	public void setValue(Integer pos, Short value){
		this.mem.set(pos, value);
	}
	
	private void cleanMemory(){
		Short bt = 0;
		for (int i = 0; i < 255; i++) {
			this.mem.add(bt);
		}		
	}

	public void getRecordValues(){
		Iterator<Short> it = this.mem.iterator();
		Short celd = null;
		while (it.hasNext()){
			celd = (Short)it.next();
			System.out.printf(Integer.toHexString(celd.intValue()).toUpperCase());
		}		
	}
}
