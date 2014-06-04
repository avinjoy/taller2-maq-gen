package compiler;

import compiler.Parameter.Type;
import domain.MemoryController;
import domain.RegisterController;

public class Xor extends Instruction {

	final int CANT_PARAMETROS = 3;
	final static String HEXA = "9";
	final static String ASSEMBLER = "xor";
	
	public Xor(int lineNumber, String args, Language lang){
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

         public void execute(RegisterController regCtrl, MemoryController memCtrl){				
		Integer regOneNumber = Integer.parseInt(this.parameters.get(1).getValue());
		Integer regTwoNumber = Integer.parseInt(this.parameters.get(2).getValue());
                Integer regDestinationNumber = Integer.parseInt(this.parameters.get(0).getValue());
                Byte xorOperator = (byte)(regCtrl.getRegisterValue(regTwoNumber) ^ regCtrl.getRegisterValue(regOneNumber));
                regCtrl.setRegisterValue(regDestinationNumber, xorOperator);
	}
         
     @Override
 	public String toString() {
 		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
 	}
}
