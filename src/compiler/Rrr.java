package compiler;

import compiler.Parameter.Type;

public class Rrr extends Instruction {

	final int CANT_PARAMETROS = 2;
	final String HEXA = "A";
	
	public Rrr(int lineNumber, String args){
		super(lineNumber,args);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
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

}
