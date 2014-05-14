package domain;

import java.util.Iterator;
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
		this.reg.elementAt(Pos-1).setValue(value);
	}
	
	public Byte getRegisterValue(Integer Pos){
		return this.reg.elementAt(Pos-1).getValue();		
	}
	
	public void getRecordValues(){
		Iterator<Register> it = this.reg.iterator();
		while (it.hasNext()){
			System.out.printf(((Register)it.next()).toString()+"\n");
		}		
	}
}
