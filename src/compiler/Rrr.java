package compiler;

import compiler.Parameter.Type;

public class Rrr extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "A";
	final static String ASSEMBLER = "rrr";

	public Rrr(int lineNumber, String args, Language lang){
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
			// Hay que hacer una validacion de un numero de 1 byte
			if (this.validateRegisterNumber(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.BYTEINTEGER, arg));
	};

	private void translateArguments(){
		String reg = this.args.substring(0,1);
		Integer regInt = Integer.parseInt(reg, 16);
		String integer = this.args.substring(2, 3);		
		this.args = regInt.toString() + "," + integer;		
	}

}
