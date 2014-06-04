package compiler;

import compiler.Parameter.Type;

import domain.MemoryController;
import domain.Observer;
import domain.RegisterController;

public class Stm extends Instruction{

    final int CANT_PARAMETROS = 2;
    final static String HEXA = "3";
    final static String ASSEMBLER = "stm";
    Observer observer;

    public Stm(int lineNumber, String args, Language lang) {
        super(lineNumber, args, lang);
        this.qParameters = CANT_PARAMETROS;
        this.hexaInstruction = HEXA;
        this.asmInstruction = ASSEMBLER;
        this.parseMachineArguments();
        this.validate();
    }
    
    public Stm(int lineNumber, String args, Language lang,Observer o) {
        super(lineNumber, args, lang);
        this.qParameters = CANT_PARAMETROS;
        this.hexaInstruction = HEXA;
        this.asmInstruction = ASSEMBLER;
        this.parseMachineArguments();
        this.validate();
        observer = o;
    }

    public void validateArgument(Integer iArgument, String arg) {
        if (iArgument == 1) {
            if (this.validateRegisterNumber(iArgument, arg)) {
                this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
            }
        } else if (this.validateMemoryAddress(iArgument, arg)) {
            this.parameters.add(new Parameter(iArgument, Type.ADRRESS, arg));
        }
    }

    ;	

	private void parseMachineArguments() {
        if (lang == Language.MACHINE) {
            this.args = this.args.substring(0, 1) + "," + this.args.substring(1, 3);
        }
    }

    public void execute(RegisterController regCtrl, MemoryController memCtrl) {
        Integer regNumber = Integer.parseInt(this.parameters.get(0).getValue());
        Integer memAddr = Integer.parseInt(this.parameters.get(1).getValue(),16);
        memCtrl.setValue(memAddr, regCtrl.getRegisterValue(regNumber).shortValue());
    }
    
    @Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}

}
