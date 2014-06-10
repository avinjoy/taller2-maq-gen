package compiler;

import compiler.Parameter.Type;
import domain.MemoryController;
import domain.RegisterController;

public class Jmp extends Instruction {

    final int CANT_PARAMETROS = 2;
    final static String HEXA = "B";
    final static String ASSEMBLER = "jmp";

    public Jmp(int lineNumber, String args, Language lang) {
        super(lineNumber, args, lang);
        this.qParameters = CANT_PARAMETROS;
        this.hexaInstruction = HEXA;
        this.asmInstruction = ASSEMBLER;
        this.parseMachineArguments();
        this.validate();
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

    @Override
    public String toString() {
        return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
    }

    @Override
    public void execute(RegisterController regCtrl, MemoryController memCtrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
