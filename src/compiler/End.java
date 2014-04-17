package compiler;

public class End extends Instruction {

	final int CANT_PARAMETROS = 0;
	final String HEXA = "C";

	public End(int lineNumber){	
		super(lineNumber);
		this.qParameters = CANT_PARAMETROS;
		this.hexaInstruction = HEXA;
		this.validate();		
	}
	
	public String toHex() {
		return "C000";
	}
}
