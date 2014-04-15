package compiler;

public class End extends Instruction {

	public End(){
		this.hasParams = false;
	}
	
	public String toHex() {
		return "C000";
	}	
}
