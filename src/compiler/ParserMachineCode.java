package compiler;

import java.util.Iterator;
import java.util.List;

import compiler.Instruction.Language;
import compiler.exceptions.CompilationtException;

public class ParserMachineCode extends Parser {

	public ParserMachineCode(List<String> tokens){
		super(tokens);
		this.parse();
	}
		
	public Instruction parseInstruction(int lnNumber, String instr){
		Instruction instruction = null;
		int indexSpace = instr.indexOf(" ")!=-1?instr.indexOf(" ")+1:instr.length()-1;
		this.currentInstruction = instr.substring(indexSpace, indexSpace+1);
		String args = instr.substring(indexSpace+1).trim();

		// Verifica que las instrucciones tengan 2 bytes
		if (instr.substring(indexSpace).length() == 4){
			if (this.currentInstruction.compareTo(Ldm.HEXA) == 0){
				instruction = new Ldm(lnNumber, args, Language.MACHINE,console);                                
			}
			if (this.currentInstruction.compareTo(Ldi.HEXA) == 0){
				instruction = new Ldi(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(Stm.HEXA) == 0){
				instruction = new Stm(lnNumber, args, Language.MACHINE, console);
			}
			if (this.currentInstruction.compareTo(Cpy.HEXA) == 0){
				instruction = new Cpy(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(Add.HEXA) == 0){
				instruction = new Add(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(Adm.HEXA) == 0){
				instruction = new Adm(lnNumber, args, Language.MACHINE);
			}			
			if (this.currentInstruction.compareTo(Oor.HEXA) == 0){
				instruction = new Oor(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(And.HEXA) == 0){
				instruction = new And(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(Xor.HEXA) == 0){
				instruction = new Xor(lnNumber, args, Language.MACHINE);
			}
			if (this.currentInstruction.compareTo(Rrr.HEXA) == 0){
				instruction = new Rrr(lnNumber, args, Language.MACHINE);
			}			
			if (this.currentInstruction.compareTo(Jmp.HEXA) == 0){
				instruction = new Jmp(lnNumber, args, Language.MACHINE);
			}			
			if (this.currentInstruction.compareTo(End.HEXA) == 0){
				instruction = new End(lnNumber);
			}
		}
		return instruction;
	}

	public void compile() {		
		Iterator<Instruction> it = this.instructions.iterator();
		Iterator<CompilationtException> itex = this.invalid.iterator();
		while (it.hasNext()){
			System.out.printf(((Instruction)it.next()).toAsm()+"\n");
		}
		while (itex.hasNext()){
			System.out.printf(((CompilationtException)itex.next()).getMessage()+"\n");			
		}
	}
}
