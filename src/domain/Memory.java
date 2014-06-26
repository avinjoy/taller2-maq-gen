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
	
	public void cleanMemory(){
		this.mem=null;
		this.mem=new Vector<Short>();
		Short bt = 0;
		this.mem.clear();
		for (int i = 0; i <= 256; i++) {
			this.mem.add(bt);
		}		
	}

	public void getRecordValues(){
		Iterator<Short> it = this.mem.iterator();
		Short celd = null;
                int i=0;
		while (it.hasNext()){
			celd = (Short)it.next();
			System.out.printf(Integer.toHexString(i)+":"+Integer.toHexString(celd.intValue()).toUpperCase()+" ");
                        i++;
		}		
	}
	
	public Vector<Short> getMemoryState(){
		return this.mem;
	}

}
