package compiler;

import java.util.List;

import editor.exceptions.InvalidArgumentException;

public abstract class Instruction {

	protected List<Integer> parameters; 
	protected Boolean valid = false;
	protected String args;
	protected List<InvalidArgumentException> argumentExceptions;
	
	public Boolean isValid() {
		return valid;
	}
	
	public abstract void validate();
	public String toHex(){return null;};
	public String toAsm(){return null;};
}
