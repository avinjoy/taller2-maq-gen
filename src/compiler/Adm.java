package compiler;

import compiler.Parameter.Type;
import domain.ALU;
import domain.MemoryController;
import domain.RegisterController;

public class Adm extends Instruction {

	final int CANT_PARAMETROS = 3;
	final static String HEXA = "6";
	final static String ASSEMBLER = "adm";
	
	public Adm(int lineNumber, String args, Language lang){
		super(lineNumber,args,lang);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		this.parseMachineArguments();
		this.validate();
	}
	
        @Override
	public void validateArgument(Integer iArgument, String arg){
		if (this.validateRegisterNumber(iArgument, arg))
			this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
	};
	
	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args = this.args.substring(0,1) + "," + this.args.substring(1,2) + "," + this.args.substring(2,3);
	}	
        
        @Override
        public void execute(RegisterController regCtrl, MemoryController memCtrl){
            
            Byte valor1 = regCtrl.getRegisterValue(this.parameters.get(1).getValueInt());
            Byte valor2 = regCtrl.getRegisterValue(this.parameters.get(2).getValueInt());
            Byte result = ALU.ALU().addFloat(valor1, valor2);
            regCtrl.setRegisterValue(this.parameters.get(0).getValueInt(), result);
        }
	
	@Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}
	
}
