package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import editor.exceptions.InvalidInstruccionException;

public class Parser {

	protected List<String> tokens;
	protected List<Instruction> instructions;
	protected List<IllegalArgumentException> invalid;
	protected int index;
	
	public List<String> getTokens() {
		return tokens;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public Parser(List<String> tokens){
		this.tokens = tokens;
		this.index = 0;
		this.instructions = new ArrayList<Instruction>();
		this.invalid = new ArrayList<IllegalArgumentException>();
		this.parse();
	}
	
	public void parse(){
		String instr = "";
		Instruction instruction = null;
		Iterator<String> it = this.tokens.iterator();
		int indexSpace = 0;
		Integer lnNumber = 0;
    	while (it.hasNext()){
    		lnNumber++;
    		instr=(String)it.next();
    		indexSpace = instr.indexOf(" ")!=-1?instr.indexOf(" "):instr.length();
    		instruction = null;
			if (instr.substring(0, indexSpace).compareTo("ldm") == 0){
				instruction = new Ldm(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("ldi") == 0){
				instruction = new Ldi(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("stm") == 0){
				instruction = new Ldi(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("cpy") == 0){
				instruction = new Cpy(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("add") == 0){
				instruction = new Add(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("orr") == 0){
				instruction = new Oor(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("and") == 0){
				instruction = new And(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("xor") == 0){
				instruction = new Xor(lnNumber, instr.substring(indexSpace).trim());
			}
			if (instr.substring(0, indexSpace).compareTo("rrr") == 0){
				instruction = new Rrr(lnNumber, instr.substring(indexSpace).trim());
			}			
			if (instr.substring(0, indexSpace).compareTo("jmp") == 0){
				instruction = new Jmp(lnNumber, instr.substring(indexSpace).trim());
			}			
			if (instr.substring(0, indexSpace).compareTo("end") == 0){
				instruction = new End(lnNumber);
			}
			if (instruction instanceof Instruction){
				if (instruction.isValid()){
					this.instructions.add(instruction);
					this.index++;
				}
				else
					this.invalid.addAll(instruction.argumentExceptions);	
			}	
			else			
				this.invalid.add(new InvalidInstruccionException("Ln " + lnNumber.toString() + ": La instrucción " + instr.substring(0, indexSpace) + " es invalida"));
    	}			
	};
	
	public void compile(){};
	public void transalate(){};
	
}

