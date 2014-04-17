package compiler;

import java.util.Iterator;
import java.util.List;

public class ParserAssembler extends Parser {

	public ParserAssembler(List<String> tokens){
		super(tokens);
	}

	@Override
	public void compile() {		
		Iterator<Instruction> it = this.instructions.iterator();
		Iterator<IllegalArgumentException> itex = this.invalid.iterator();
		while (it.hasNext()){
			System.out.printf(((Instruction)it.next()).toHex()+"\n");
		}
		while (itex.hasNext()){
			System.out.printf(((IllegalArgumentException)itex.next()).getMessage()+"\n");			
		}

	} 

	@Override
	public void transalate() {
		// TODO Auto-generated method stub		
	}

}
