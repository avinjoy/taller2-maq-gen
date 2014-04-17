package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import editor.exceptions.InvalidArgumentException;

public class Instruction {

	protected List<Parameter> parameters; 
	protected boolean valid = false;
	protected String args;
	protected List<InvalidArgumentException> argumentExceptions;
	protected int qParameters;
	protected String hexaInstruction;
	protected int lineNumber;

	public Instruction(){}
	
	public Instruction (int lineNumber){
		this.lineNumber = lineNumber;
		this.parameters = new ArrayList<Parameter>();
		this.argumentExceptions = new ArrayList<InvalidArgumentException>();		
	}
	
	public Instruction (int lineNumber, String args){
		this.args = args;
		this.parameters = new ArrayList<Parameter>();
		this.argumentExceptions = new ArrayList<InvalidArgumentException>();
	}
	
	public Boolean isValid() {
		return valid;
	}
	
	public void validate(){
		if (this.qParameters > 0){
			StringTokenizer token = new StringTokenizer(this.args, ",");
			this.valid = true;
			Integer iArgument = 0;
			String arg = "";
		
			if (token.countTokens() != this.qParameters)
				this.argumentExceptions.add(new InvalidArgumentException("Cantidad erronea de argumentos"));
			else{
				while (token.hasMoreTokens()){
					iArgument++;
					arg = token.nextToken();
					this.validateArgument(iArgument, arg);
				}						
			}
			if (this.argumentExceptions.size() !=0)
				this.valid = false;
		}
		else
			this.valid = true;
	}		
			
	public void validateArgument(Integer iArgument, String arg){};
	
	protected boolean validateRegisterNumber(Integer iArgument, String arg){
		boolean valid = true;
		try {
			Integer intArg = Integer.parseInt(arg);
			if (intArg < 0 || intArg > 15){				
				this.argumentExceptions.add(new InvalidArgumentException("El valor del argumento #"+ iArgument.toString() + " (" + arg + ") es inválido"));
				valid = false;
			}
		} catch (Exception ex) {
			this.argumentExceptions.add(new InvalidArgumentException("El argumento #"+ iArgument.toString() + " (" + arg + ") es inválido"));
			valid = false;
		}
		return valid;
	};
	
	protected boolean validateMemoryAddress(Integer iArgument, String arg){
		boolean valid = true;
		if (arg.length() != 2){
			this.argumentExceptions.add(new InvalidArgumentException("La dirección de memoria "+ arg + " es inválida"));
			valid = false;
		}
		else{
			try{
				Integer.parseInt(arg, 16);				
				if (arg.compareTo("00") == 0 || arg.compareTo("FF") == 0 || arg.compareTo("FE") == 0 || arg.compareTo("FD") == 0){
					this.argumentExceptions.add(new InvalidArgumentException("La dirección de memoria "+ arg + " no puede ser utilizada"));
					valid = false;
				}
			}
			catch(Exception ex){
				// Falla si pongo MM
				this.argumentExceptions.add(new InvalidArgumentException("La dirección de memoria "+ arg + " es inválida"));
				valid = false;
			}
		}
		return valid;
	};

	public String toHex() {
		String asm = this.hexaInstruction;
		Iterator<Parameter> it = this.parameters.iterator();
		while (it.hasNext()){
			asm += it.next().getHexaValue();			
		}								
		return asm.toUpperCase();
	}
	
	public String toAsm(){return null;};

}
