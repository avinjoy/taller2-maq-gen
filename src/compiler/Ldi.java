package compiler;

import java.util.StringTokenizer;

public class Ldi extends Instruction {

	public String toHex() {
		String asm = "4";
		StringTokenizer token = new StringTokenizer(this.args, ",");
		String param;
		while (token.hasMoreTokens()){
			param = Integer.toHexString(Integer.parseInt(token.nextToken()));
			asm += param.toUpperCase();
		}
		return asm;
	}

}
