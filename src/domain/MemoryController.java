package domain;


public class MemoryController {
	
	private Memory mem;
		
	public MemoryController() {
		this.mem = Memory.getInstance();
	}

	public Memory getMem() {
		return mem;
	}

	public void setMem(Memory mem) {
		this.mem = mem;
	}
	
	public Byte getValue(Integer memPos){
		return this.mem.getValue(memPos);
	}
	
	public void setValue(Integer memAddr, Byte value){
		this.mem.setValue(memAddr, value);
	}
	
	public void showCurrentState(){
		this.mem.getRecordValues();
	}

}
