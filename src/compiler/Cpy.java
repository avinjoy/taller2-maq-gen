package compiler;

import java.util.Iterator;

import compiler.Parameter.Type;

public class Cpy extends Instruction {

	final int CANT_PARAMETROS = 2;
	final static String HEXA = "4";
	final static String ASSEMBLER = "cpy";
	
	public Cpy(int lineNumber, String args, Language lang){
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
		String reg1 = this.args.substring(1,2);
		Integer reg1Int = Integer.parseInt(reg1, 16);
		String reg2 = this.args.substring(2,3);
		Integer reg2Int = Integer.parseInt(reg2, 16);

		this.args = reg1Int.toString() + "," + reg2Int.toString();		
	}

	public String toHex() {
		String hex = this.getMemoryAddress();
		hex += " " + this.hexaInstruction + "0";		
		Iterator<Parameter> it = this.parameters.iterator();
		while (it.hasNext()){
			hex += it.next().getHexaValue();			
		}								
		return hex.toUpperCase();
	}

}
