package compiler;

public class End extends Instruction {

	final int CANT_PARAMETROS = 0;
	final static String HEXA = "C";
	final static String ASSEMBLER = "end";
	
	public End(int lineNumber){	
		super(lineNumber);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.asmInstruction = ASSEMBLER;
		this.validate();		
	}
	
	public String toHex() {
		String hex = this.getMemoryAddress();
		hex += " " + this.hexaInstruction + "000";		
		return hex;
	}

	public String toAsm() {
		return this.asmInstruction;
	}

}
