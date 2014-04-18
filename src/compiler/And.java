package compiler;

import compiler.Parameter.Type;

public class And extends Instruction {

	final int CANT_PARAMETROS = 3;
	final String HEXA = "8";
	
	public And(int lineNumber, String args){
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
