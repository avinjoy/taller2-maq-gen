package compiler;

import compiler.Parameter.Type;

public class Oor extends Instruction {

	final int CANT_PARAMETROS = 3;
	final static String HEXA = "7";
	final static String ASSEMBLER = "oor";
	
	public Oor(int lineNumber, String args, Language lang){
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
			this.args = this.args.substring(0,1) + "," + this.args.substring(1,2) + "," + this.args.substring(2,3);
	}	
}
