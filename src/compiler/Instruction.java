package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import compiler.exceptions.CompilationtException;

public class Instruction {

	public enum Language {ASSEMBLER,MACHINE};
	protected List<Parameter> parameters; 
	protected boolean valid = false;
	protected String args;
	protected List<CompilationtException> argumentExceptions;
	protected int qParameters;
	protected String hexaInstruction;
	protected String asmInstruction;
	protected int lineNumber;
	protected Language lang;

	public Instruction(){}
	
	public Instruction (int lineNumber){
		this.lineNumber = lineNumber;
		this.parameters = new ArrayList<Parameter>();
		this.argumentExceptions = new ArrayList<CompilationtException>();		
	}
	
	public Instruction (int lineNumber, String args){
		this.lang = Language.ASSEMBLER;
		this.args = args;
		this.lineNumber = lineNumber;
		this.parameters = new ArrayList<Parameter>();
		this.argumentExceptions = new ArrayList<CompilationtException>();
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
				this.argumentExceptions.add(new CompilationtException("Cantidad erronea de argumentos"));
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
				this.argumentExceptions.add(new CompilationtException("El valor del argumento #"+ iArgument.toString() + " (" + arg + ") es inválido"));
				valid = false;
			}
		} catch (Exception ex) {
			this.argumentExceptions.add(new CompilationtException("El argumento #"+ iArgument.toString() + " (" + arg + ") es inválido"));
			valid = false;
		}
		return valid;
	};
	
	protected boolean validateMemoryAddress(Integer iArgument, String arg){
		boolean valid = true;
		if (arg.length() != 2){
			this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " es inválida"));
			valid = false;
		}
		else{
			try{
				Integer.parseInt(arg, 16);
				if (arg.compareTo("00") == 0 || arg.compareTo("FF") == 0 || arg.compareTo("FE") == 0 || arg.compareTo("FD") == 0){
					this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " no puede ser utilizada"));
					valid = false;
				}
			}
			catch(Exception ex){
				// Falla si pongo MM
				this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " es inválida"));
				valid = false;
			}
		}
		return valid;
	};

	protected String getMemoryAddress(){
		
		String memAddress = "";
		memAddress = Integer.toHexString((this.lineNumber-1)*2).toUpperCase();
		if (memAddress.length() == 1)
			memAddress = "0"+memAddress.toUpperCase();
		
		return memAddress;		
	} 

	public String toHex() {
		String hex = this.getMemoryAddress();
		hex += " " + this.hexaInstruction;
		Iterator<Parameter> it = this.parameters.iterator();
		while (it.hasNext()){
			hex += it.next().getHexaValue();			
		}								
		return hex.toUpperCase();
	}
		
	public String toAsm(){
		String asm = this.asmInstruction + " ";
		Iterator<Parameter> it = this.parameters.iterator();
		while (it.hasNext()){
			asm += it.next().getValue().toUpperCase() + ",";			
		}
		if (this.parameters.size() > 0)
			asm = asm.substring(0, asm.length()-1);
		return asm;		
	};

}
