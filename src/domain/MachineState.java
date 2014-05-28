package domain;


public class MachineState {
	
	private Short currentInstruction;
	private Integer programCounter;
	private MemoryController memControl;
	private RegisterController regControl;
	private Short nextInstruction;
	private Integer totalProgramInsturctions;
	
	
	
	public MachineState(Short currentInstruction, Integer programCounter,
			MemoryController memControl, RegisterController regControl,
			Short nextInstruction, Integer totalProgramInsturctions) {
		super();
		this.currentInstruction = currentInstruction;
		this.programCounter = programCounter;
		this.memControl = memControl;
		this.regControl = regControl;
		this.nextInstruction = nextInstruction;
		this.totalProgramInsturctions = totalProgramInsturctions;
	}
	
	
	public Short getCurrentInstruction() {
		return currentInstruction;
	}
	public void setCurrentInstruction(Short currentInstruction) {
		this.currentInstruction = currentInstruction;
	}
	public Integer getProgramCounter() {
		return programCounter;
	}
	public void setProgramCounter(Integer programCounter) {
		this.programCounter = programCounter;
	}
	public MemoryController getMemControl() {
		return memControl;
	}
	public void setMemControl(MemoryController memControl) {
		this.memControl = memControl;
	}
	public RegisterController getRegControl() {
		return regControl;
	}
	public void setRegControl(RegisterController regControl) {
		this.regControl = regControl;
	}
	public Short getNextInstruction() {
		return nextInstruction;
	}
	public void setNextInstruction(Short nextInstruction) {
		this.nextInstruction = nextInstruction;
	}
	public Integer getTotalProgramInsturctions() {
		return totalProgramInsturctions;
	}
	public void setTotalProgramInsturctions(Integer totalProgramInsturctions) {
		this.totalProgramInsturctions = totalProgramInsturctions;
	}
	
	

}
