package compiler;

import java.util.Iterator;
import java.util.List;

import compiler.Instruction.Language;
import compiler.exceptions.CompilationtException;

public class ParserAssembler extends Parser {

	public ParserAssembler(List<String> tokens){
		super(tokens);
		this.parse();
	}
	
	public Instruction parseInstruction(int lnNumber, String instr){

		Instruction instruction = null;
		int indexSpace = instr.indexOf(" ")!=-1?instr.indexOf(" "):instr.length();
		this.currentInstruction = instr.substring(0, indexSpace);
		String args = instr.substring(indexSpace).trim();
		
		if (this.currentInstruction.compareTo(Ldm.ASSEMBLER) == 0){
			instruction = new Ldm(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Ldi.ASSEMBLER) == 0){
			instruction = new Ldi(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Stm.ASSEMBLER) == 0){
			instruction = new Stm(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Cpy.ASSEMBLER) == 0){
			instruction = new Cpy(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Add.ASSEMBLER) == 0){
			instruction = new Add(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Adm.ASSEMBLER) == 0){
			instruction = new Adm(lnNumber, args, Language.ASSEMBLER);
		}		
		if (this.currentInstruction.compareTo(Oor.ASSEMBLER) == 0){
			instruction = new Oor(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(And.ASSEMBLER) == 0){
			instruction = new And(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Xor.ASSEMBLER) == 0){
			instruction = new Xor(lnNumber, args, Language.ASSEMBLER);
		}
		if (this.currentInstruction.compareTo(Rrr.ASSEMBLER) == 0){
			instruction = new Rrr(lnNumber, args, Language.ASSEMBLER);
		}			
		if (this.currentInstruction.compareTo(Jmp.ASSEMBLER) == 0){
			instruction = new Jmp(lnNumber, args, Language.ASSEMBLER);
		}			
		if (this.currentInstruction.compareTo(End.ASSEMBLER) == 0){
			instruction = new End(lnNumber);
		}		
		return instruction;
	}

	public void compile() {		
		Iterator<Instruction> it = this.instructions.iterator();
		Iterator<CompilationtException> itex = this.invalid.iterator();
		while (it.hasNext()){
			System.out.printf(((Instruction)it.next()).toHex()+"\n");
		}
		while (itex.hasNext()){
			System.out.printf(((CompilationtException)itex.next()).getMessage()+"\n");			
		}
	}

}
