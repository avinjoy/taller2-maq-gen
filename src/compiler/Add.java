package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import editor.exceptions.InvalidArgumentException;

public class Add extends Instruction {
	
	public Add(String args){
		this.args = args;
		this.parameters = new ArrayList<Integer>();
		this.argumentExceptions = new ArrayList<InvalidArgumentException>();
		this.validate();
	}
	
	@Override
	public String toHex() {
		String asm = "5";
		Iterator<Integer> it = this.parameters.iterator();
		while (it.hasNext()){			
			asm += Integer.toHexString(it.next()).toUpperCase();
		}								
		return asm;
	}

	@Override
	public String toAsm() {
		return null;
	}

	@Override
	public void validate() {
		StringTokenizer token = new StringTokenizer(this.args, ",");
		this.valid = true;
		Integer iArgument = 0;
		
		// Cantidad de Parámetros // Deben ser 3
		if (token.countTokens() != 3){
			this.valid = false;
			this.argumentExceptions.add(new InvalidArgumentException("Cantidad Erronea de Argumentos"));
		}
		else{
			while (token.hasMoreTokens()){
				try {
					iArgument++;
					this.parameters.add(Integer.parseInt(token.nextToken()));					
				} catch (Exception ex) {
					this.valid = false;
					this.argumentExceptions.add(new InvalidArgumentException("El argumento # "+ iArgument.toString() +"invalido"));
				}
			}			
			
		}
			
	}

}
