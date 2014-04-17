package compiler;

import compiler.Parameter.Type;

public class Stm extends Instruction {

	final int CANT_PARAMETROS = 2;
	final String HEXA = "3";

	public Stm(int lineNumber, String args){
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
			if (this.validateMemoryAddress(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.ADRRESS, arg));
	};	

}
