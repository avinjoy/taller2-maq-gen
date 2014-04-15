package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import editor.exceptions.InvalidInstruccionException;

public class Parser {

	protected List<String> tokens;
	protected List<Instruction> instructions;
	protected List<InvalidInstruccionException> invalid;
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
		this.invalid = new ArrayList<InvalidInstruccionException>();
		this.parse();
	}
	
	public void parse(){
		String instr = "";
		Instruction instruction = null;
		Iterator<String> it = this.tokens.iterator();
		int indexSpace = 0;
		Boolean bValid = false;
    	while (it.hasNext()){
    		bValid = false;
    		instr=(String)it.next();
    		indexSpace = instr.indexOf(" ")!=-1?instr.indexOf(" "):instr.length();
			if (instr.substring(0, indexSpace).compareTo("add") == 0){
				instruction = new Add();
				bValid = true;
			}
			if (instr.substring(0, indexSpace).compareTo("ldi") == 0){
				instruction = new Ldi();
				bValid = true;
			}
			if (instr.substring(0, indexSpace).compareTo("end") == 0){
				instruction = new End();
				bValid = true;
			}			
			if (bValid){
				if (instruction.hasParams)
					instruction.args = instr.substring(instr.indexOf(" ")).trim();
				this.instructions.add(instruction);
				this.index++;
			}	
			else			
				this.invalid.add(new InvalidInstruccionException("La instrucción: " + instr.substring(0, instr.indexOf(" ")) + " es invalida"));
    	}			
	};
	
	public void compile(){};
	public void transalate(){};
	
}
