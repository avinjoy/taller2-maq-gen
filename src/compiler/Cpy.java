package compiler;

import java.util.Iterator;

import compiler.Parameter.Type;
import domain.MemoryController;
import domain.RegisterController;

public class Cpy extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "4";
	final static String ASSEMBLER = "cpy";
	
	public Cpy(int lineNumber, String args, Language lang){
		super(lineNumber,args,lang);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		this.parseMachineArguments();
		this.validate();
	}
	
	public void validateArgument(Integer iArgument, String arg){
		if (this.validateRegisterNumber(iArgument, arg))
			this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
	};

	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args =  this.args.substring(1,2) + "," + this.args.substring(2,3);
	}	
	
	public String toHex() {
		String hex = this.getMemoryAddress();
		hex += " " + this.hexaInstruction + "0";		
		Iterator<Parameter> it = this.parameters.iterator();
		while (it.hasNext()){
			hex += it.next().getHexaValue();			
		}								
		return hex.toUpperCase();
	}

	public void execute(RegisterController regCtrl, MemoryController memCtrl){				
		Integer regSourceNumber = Integer.parseInt(this.parameters.get(0).getValue());
		Integer regDestinationNumber = Integer.parseInt(this.parameters.get(1).getValue());
		regCtrl.setRegisterValue(regDestinationNumber, regCtrl.getRegisterValue(regSourceNumber));
	}
	
	@Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}

}
