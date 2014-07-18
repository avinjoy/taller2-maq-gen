package domain;

import java.util.Vector;

public class MemoryController implements Observable {

    private Memory mem;

    private Observer observer;

    public MemoryController() {
        this.mem = Memory.getInstance();
    }

    public Memory getMem() {
        return mem;
    }

    public void setMem(Memory mem) {
        this.mem = mem;
    }
    
    public Short getPortValue(Integer memPos){
        
        return this.mem.getValue(memPos);
    }

    public Short getValue(Integer memPos) {

        if (memPos == 253 && mem.getValue(memPos - 1) == 0) {

            String value = observer.input();
            mem.setValue(memPos, Short.parseShort(value,16));
            mem.setValue(memPos - 1, (short) 1);
        }

        return this.mem.getValue(memPos);
    }

    public void setValue(Integer memAddr, Short value) {

        if (memAddr == 255 && mem.getValue(memAddr - 1) == 0) {
            observer.output(Integer.toHexString(0x0FF&value));
            this.mem.setValue(memAddr-1, (short) 1);
        }

        this.mem.setValue(memAddr, value);
    }

    public void showCurrentState() {
        this.mem.getRecordValues();
    }

	public Vector<Short> getMemoryState() {
		return mem.getMemoryState();
	}
    @Override
    public void setObserver(Observer o) {
        observer = o;
    }

	public void cleanMemory() {
		this.mem.cleanMemory();
		
	}
        
        public static MemoryController inst(){
            
            return new MemoryController();
        }
    
}
