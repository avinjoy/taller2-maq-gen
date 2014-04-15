package compiler;

public abstract class Instruction {

	public String[] param = new String[3];
	public String args = "";	
	public String toHex(){return null;};
	public String toAsm(){return null;};
	protected Boolean hasParams = true;
	
}
