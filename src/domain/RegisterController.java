package domain;

import java.util.Vector;

public class RegisterController {

	private Vector<Register> reg;

	public RegisterController() {
		this.reg = new Vector<Register>();
		for (int i = 1; i < 16; i++) {			
			this.reg.add(new Register(i));
		}		
	}
	
	public void setRegisterValue(Integer Pos, Byte value){
		this.reg.elementAt(Pos).setValue(value);
	}
	
	public Byte getRegisterValue(Integer Pos){
		return this.reg.elementAt(Pos).getValue();		
	}
}
