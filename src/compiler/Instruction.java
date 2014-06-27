package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import compiler.exceptions.CompilationtException;
import domain.MemoryController;
import domain.RegisterController;

public abstract class Instruction {

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
	
	public Instruction (int lineNumber, String args, Language lang){
		this.lang = lang;
		this.args = args;
		this.lineNumber = lineNumber;
		this.parameters = new ArrayList<Parameter>();
		this.argumentExceptions = new ArrayList<CompilationtException>();
	}
	
	public Boolean isValid() {
		return valid;
	}

	public Integer getLineNumber() {
		return this.lineNumber;
	}

	public void validate(){
		if (this.qParameters > 0){
                        
			StringTokenizer token = new StringTokenizer(this.args.split("//")[0], ",");
			this.valid = true;
			Integer iArgument = 0;
			String arg = "";
		
			if (token.countTokens() != this.qParameters)
				this.argumentExceptions.add(new CompilationtException("Cantidad erronea de argumentos", this.lineNumber));
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
			Integer intArg = 0;
			if (this.lang == Language.ASSEMBLER)
				intArg = Integer.parseInt(arg);
			else
				intArg = Integer.parseInt(arg,16);
			
			if (intArg < 0 || intArg > 15){				
				this.argumentExceptions.add(new CompilationtException("El valor del argumento #"+ iArgument.toString() + " (" + arg + ") es inválido", this.lineNumber));
				valid = false;
			}
		} catch (Exception ex) {
			this.argumentExceptions.add(new CompilationtException("El argumento #"+ iArgument.toString() + " (" + arg + ") es inválido", this.lineNumber));
			valid = false;
		}
		return valid;
	};
	
	protected boolean validateMemoryAddress(Integer iArgument, String arg){
		boolean valid = true;
		if (arg.length() != 2){
			this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " es inválida", this.lineNumber));
			valid = false;
		}
		else{
			try{
				Integer.parseInt(arg, 16);
				if (arg.compareTo("00") == 0){
					this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " no puede ser utilizada", this.lineNumber));
					valid = false;
				}
			}
			catch(Exception ex){
				// Falla si pongo MM
				this.argumentExceptions.add(new CompilationtException("La dirección de memoria "+ arg + " es inválida", this.lineNumber));
				valid = false;
			}
		}
		return valid;
	};
	
	protected boolean validateInteger(Integer iArgument, String arg){
		boolean valid = true;
		Integer value = 0;
		if (arg.length() != 2){
			this.argumentExceptions.add(new CompilationtException("El valor ingresado "+ arg + " es inválido", this.lineNumber));
			valid = false;
		}
		else{
			try{
				value = Integer.parseInt(arg, 16);
				if (value < Byte.MIN_VALUE || value > 255){
					this.argumentExceptions.add(new CompilationtException("El entero ingresado "+ value.toString() + " es inválido", this.lineNumber));
					valid = false;										
				}					
			}
			catch(Exception ex){
				// Falla si pongo MM
				this.argumentExceptions.add(new CompilationtException("El valor ingresado "+ arg + " es inválido", this.lineNumber));				valid = false;
			}
		}
		return valid;
	} 
	
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

	public abstract void execute(RegisterController regCtrl, MemoryController memCtrl);
	
	public String showParameters(){
		String params="";
		for (Parameter par : this.parameters){
			params+=par.toString()+ " ";
		}
		return params;
	}
}
