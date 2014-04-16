package compiler;

import java.util.StringTokenizer;

import editor.exceptions.InvalidArgumentException;

public class Ldi extends Instruction {

	public String toHex() {
		String asm = "4";
		StringTokenizer token = new StringTokenizer(this.args, ",");
		String param;
		while (token.hasMoreTokens()){
			try {
				param = Integer.toHexString(Integer.parseInt(token.nextToken()));
				asm += param.toUpperCase();
			} catch (InvalidArgumentException ex) {
				
			}
		}
		return asm;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		
	}

}
