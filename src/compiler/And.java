package compiler;

import compiler.Parameter.Type;

public class And extends Instruction {

	final int CANT_PARAMETROS = 3;
	final static String HEXA = "8";
	final static String ASSEMBLER = "and";
	
	public And(int lineNumber, String args, Language lang){
		super(lineNumber,args);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		if (lang == Language.MACHINE)
			this.translateArguments();		
		this.validate();
	}
	
	public void validateArgument(Integer iArgument, String arg){
		if (this.validateRegisterNumber(iArgument, arg))
			this.parameters.add(new Parameter(iArgument, Type.REGISTER, arg));
	};
	
	private void translateArguments(){
		String reg1 = this.args.substring(0,1);
		Integer reg1Int = Integer.parseInt(reg1, 16);
		String reg2 = this.args.substring(1,2);
		Integer reg2Int = Integer.parseInt(reg2, 16);
		String reg3 = this.args.substring(2,3);
		Integer reg3Int = Integer.parseInt(reg3, 16);

		this.args = reg1Int.toString() + "," + reg2Int.toString() + "," + reg3Int.toString();		
	}

}
