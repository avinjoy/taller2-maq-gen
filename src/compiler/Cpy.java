package compiler;

import compiler.Parameter.Type;

public class Cpy extends Instruction {

	final int CANT_PARAMETROS = 2;
	final String HEXA = "40";
	
	public Cpy(int lineNumber, String args){
		super(lineNumber,args);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.validate();
	}
	
	public void validateArgument(Integer iArgument, String arg){
		if (this.validateRegisterNumber(iArgument, arg))
			this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
	};

}
