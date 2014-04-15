package compiler;

import java.util.StringTokenizer;

public class Add extends Instruction {

	@Override
	public String toHex() {
		String asm = "5";
		StringTokenizer token = new StringTokenizer(this.args, ",");
		String param;
		while (token.hasMoreTokens()){
			param = Integer.toHexString(Integer.parseInt(token.nextToken()));
			asm += param.toUpperCase();
		}
		return asm;
	}

	@Override
	public String toAsm() {
		return null;
	}

}
