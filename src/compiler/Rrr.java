package compiler;

import compiler.Parameter.Type;
import domain.MemoryController;
import domain.RegisterController;

public class Rrr extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "A";
	final static String ASSEMBLER = "rrr";

	public Rrr(int lineNumber, String args, Language lang){
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
			// Hay que hacer una validacion de un numero de 1 byte
			if (this.validateRegisterNumber(iArgument, arg))
				this.parameters.add(new Parameter(iArgument, Type.BYTEINTEGER, arg));
	};

	private void parseMachineArguments(){
		if (lang == Language.MACHINE)
			this.args = this.args.substring(0,1) + "," + this.args.substring(2,3);
	}
	
	@Override
	public String toString() {
		return "Línea: " + this.lineNumber + " " + this.getClass().getSimpleName() + " " + showParameters();
	}

    
    public void execute(RegisterController regCtrl, MemoryController memCtrl) {
        
        Integer regNumber = this.parameters.get(0).getValueInt();
        Integer times = this.parameters.get(1).getValueInt();
        
        Integer regValue = regCtrl.getRegisterValue(regNumber).intValue();
        regValue&=0xff;
        Integer lastBit=0;
        for (int i=times;i>0;i--){
            
            lastBit = regValue & 0x01;
            regValue >>>= 1;
            lastBit <<= 7;
            regValue |= lastBit;
        }
        
        System.out.println(regValue);
        System.out.println(lastBit);
        System.out.println(regValue.byteValue());
                
        regCtrl.setRegisterValue(regNumber, regValue.byteValue());
        
    } 

        
}

