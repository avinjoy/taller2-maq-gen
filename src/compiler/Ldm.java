package compiler;

import compiler.Parameter.Type;

public class Ldm extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "1";
	final static String ASSEMBLER = "ldm";
	
	public Ldm(int lineNumber, String args, Language lang){
		super(lineNumber,args);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		if (lang == Language.MACHINE)
			this.translateArguments();
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
	
	private void translateArguments(){
		String reg = this.args.substring(0,1);
		Integer regInt = Integer.parseInt(reg, 16);
		String memAddress = this.args.substring(1, 3);		
		this.args = regInt.toString() + "," + memAddress;		
	}
}
