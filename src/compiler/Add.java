package compiler;

import compiler.Parameter.DataDir;
import compiler.Parameter.Type;
import domain.ALU;
import domain.MemoryController;
import domain.RegisterController;

public class Add extends Instruction {
	 
	final int CANT_PARAMETROS = 3;
	final static String HEXA = "5";
	final static String ASSEMBLER = "add";
	
	public Add(int lineNumber, String args, Language lang){
		super(lineNumber,args,lang);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		this.parseMachineArguments();				
		this.validate();
	}
	
        @Override
	public void validateArgument(Integer iArgument, String arg){
		Parameter param;
		if (this.validateRegisterNumber(iArgument, arg)){
			param = new Parameter(iArgument, Type.REGISTER, arg);
			this.parameters.add(param);
			if (iArgument == 1)
				param.setDataDirection(DataDir.OUTPUT);
			else
				param.setDataDirection(DataDir.INPUT);
		}
	}

	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args = this.args.substring(0,1) + "," + this.args.substring(1,2) + "," + this.args.substring(2,3);
	}

    @Override
    public void execute(RegisterController regCtrl, MemoryController memCtrl) {
        
        Integer reg1 = this.parameters.get(1).getValueInt();
        Integer reg2 = this.parameters.get(2).getValueInt();
        Integer regDestino = this.parameters.get(0).getValueInt();
        regCtrl.setRegisterValue(regDestino, ALU.ALU().addInteger(regCtrl.getRegisterValue(reg1), regCtrl.getRegisterValue(reg2)));
        
    }
	
	@Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}
	
}
