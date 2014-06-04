package domain;

import java.util.Iterator;

import compiler.Instruction;
import compiler.Parser;
import compiler.ParserMachineCode;

public class ExecutionEngine {

	private static ExecutionEngine instance = new ExecutionEngine();
	
	public static ExecutionEngine getInstance(){
		return instance;
	}
	
	private ExecutionEngine() {
		super();
		this.regControl = new RegisterController();
		this.memControl = new MemoryController();
		//Genera en memoria la lista de instrucciones (ver en donde va realmente)						
	}
	
	private Short currentInstruction;
	private Integer programCounter;
	private MemoryController memControl;
	private RegisterController regControl;
	private Parser parser;
	private Short nextInstruction;
	private Integer totalProgramInsturctions = 0;
	
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
	public Parser getParser() {
		return parser;
	}
	public void setParser(Parser parser) {
		this.parser = parser;
	}
	public Short getNextInstruction() {
		return nextInstruction;
	}
	public void setNextInstruction(Short nextInstruction) {
		this.nextInstruction = nextInstruction;
	}
	
	public void nextInstruction(){
		
	}
	
	public void executeProgram(){
		Instruction curInst = null;
		this.programCounter = 1;
		Parser programParser = new ParserMachineCode();
		
		while (this.programCounter<=this.totalProgramInsturctions){
			String memAddr = "00";
			String instr = Integer.toHexString(this.getMemControl().getValue((2*this.programCounter)-1)).toUpperCase();				
			String instr2 = Integer.toHexString(this.getMemControl().getValue((2*this.programCounter))).toUpperCase();
			if (instr2.length() == 1)
				instr2 = "0"+instr2;
			
			curInst = programParser.parseInstruction(this.programCounter, memAddr+" "+instr+instr2);
			curInst.execute(this.regControl, this.memControl);
			this.programCounter++;
	}

		this.regControl.getRecordValues();
		this.memControl.showCurrentState();
		}
	
	public void executeProgramOnDebugMode(int pc){
		Instruction curInst = null;
		this.programCounter = pc;
		Parser programParser = new ParserMachineCode();
		
		if (this.programCounter<=this.totalProgramInsturctions){
			String memAddr = "00";
			String instr = Integer.toHexString(this.getMemControl().getValue((2*this.programCounter)-1)).toUpperCase();				
			String instr2 = Integer.toHexString(this.getMemControl().getValue((2*this.programCounter))).toUpperCase();
			if (instr2.length() == 1)
				instr2 = "0"+instr2;
			
			curInst = programParser.parseInstruction(this.programCounter, memAddr+" "+instr+instr2);
			curInst.execute(this.regControl, this.memControl);
			this.programCounter++;
		}
		
		this.regControl.getRecordValues();
		this.memControl.showCurrentState();
	}
	
	public void loadProgram(){
		this.totalProgramInsturctions = 0;
		Iterator<Instruction> it = this.parser.getInstructions().iterator();
		while (it.hasNext()){
			Instruction curInst =((Instruction)it.next()); 
			this.memControl.setValue((2*curInst.getLineNumber())-1, Short.valueOf(curInst.toHex().substring(3, 5),16));			
			this.memControl.setValue(2*curInst.getLineNumber(), Short.valueOf(curInst.toHex().substring(5, 7),16));
			this.totalProgramInsturctions++;
		}
	}
	
	public MachineState getCurrentMachineState(){
		return new MachineState(this.currentInstruction, this.programCounter, this.memControl, this.regControl, this.nextInstruction, this.totalProgramInsturctions);
	}
        
    public void setConsole(Console c){
        this.memControl.setObserver(c);
       
    }
    
    public void cleanMachine(){
    	this.currentInstruction=0;
    	this.memControl.cleanMemory();
    	this.nextInstruction=0;
    	this.regControl=new RegisterController();
    }
}
