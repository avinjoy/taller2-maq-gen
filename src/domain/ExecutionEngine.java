package domain;

import javax.swing.text.html.parser.Parser;

public class ExecutionEngine {

	public ExecutionEngine() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	private Byte currentInstruction;
	private Integer programCounter;
	private MemoryController memControl;
	private RegisterController regControl;
	private Parser parser;
	private Byte nextInstruction;
	
	public Byte getCurrentInstruction() {
		return currentInstruction;
	}
	public void setCurrentInstruction(Byte currentInstruction) {
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
	public Parser getParser() {
		return parser;
	}
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	public Byte getNextInstruction() {
		return nextInstruction;
	}
	public void setNextInstruction(Byte nextInstruction) {
		this.nextInstruction = nextInstruction;
	}
	
	public void nextIntruction(){
		
	}
	
	public void showMachineState(){
		
	}

}
