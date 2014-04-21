package compiler;

import compiler.Parameter.Type;

public class Ldm extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "1";
	final static String ASSEMBLER = "ldm";
	
	public Ldm(int lineNumber, String args, Language lang){
		super(lineNumber,args,lang);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		this.parseMachineArguments();
		this.validate();
	}

	public void validateArgument(Integer iArgument, String arg){
		if (iArgument == 1){
			if (this.validateRegisterNumber(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
		}
		else
			if (this.validateMemoryAddress(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.ADRRESS, arg));
	};
	
	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args = this.args.substring(0,1) + "," + this.args.substring(1,3);
	}
}
