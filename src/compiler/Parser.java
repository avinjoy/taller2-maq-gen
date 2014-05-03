package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import compiler.exceptions.CompilationtException;

public class Parser {

	protected List<String> tokens;
	protected List<Instruction> instructions;
	protected List<CompilationtException> invalid;
	protected int index;
	protected String currentInstruction;
	
	public List<String> getTokens() {
		return tokens;
	}

	public List<CompilationtException> getExceptions() {
		return invalid;
	}
	
	public List<Instruction> getInstructions() {
		return instructions;
	}

	public Parser(List<String> tokens){
		this.tokens = tokens;
		this.index = 0;
		this.instructions = new ArrayList<Instruction>();
		this.invalid = new ArrayList<CompilationtException>();
	}
	
	public void parse(){
		String instr = "";
		Instruction instruction = null;
		Iterator<String> it = this.tokens.iterator();
		Integer lnNumber = 0;
    	while (it.hasNext()){
    		lnNumber++;
    		instr=(String)it.next().trim();
    		instruction = this.parseInstruction(lnNumber, instr);
			if (instruction instanceof Instruction){
				if (instruction.isValid()){
					this.instructions.add(instruction);
					this.index++;
				}
				else
					this.invalid.addAll(instruction.argumentExceptions);	
			}	
			else{
				this.invalid.add(new CompilationtException("La instrucción " + this.currentInstruction + " es invalida", lnNumber));
			}
				
    	}			
	};
	
	public Instruction parseInstruction(int lnNumber, String instr){return null;};
	public void compile(){};
	public void transalate(){};
	
}

