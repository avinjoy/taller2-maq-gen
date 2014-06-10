package compiler;

import compiler.Parameter.Type;
import domain.MemoryController;
import domain.RegisterController;

public class Ldi extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "2";
	final static String ASSEMBLER = "ldi";

	public Ldi(int lineNumber, String args, Language lang){
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
			if (this.validateInteger(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.INTEGER, arg));
	};
	
	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args = this.args.substring(0,1) + "," + this.args.substring(1,3);
	}
	
	public void execute(RegisterController regCtrl, MemoryController memCtrl){				
		Integer regNumber = Integer.parseInt(this.parameters.get(0).getValue());		
		Integer intValue = Integer.parseInt(this.parameters.get(1).getValue());
                
		short value = Short.valueOf(intValue.toString());
		regCtrl.setRegisterValue(regNumber, (byte)value);
	}
	
	@Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}
	
}
