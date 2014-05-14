package domain;

public class Register {

	private Integer number;
	private Byte value = 0;
	
	public Register(Integer registerNumber) {
		this.number = registerNumber;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Byte getValue() {
		return value;
	}

	public void setValue(Byte value) {
		this.value = value;
	}
	
	public String toString(){		
		return "Reg #" + this.number.toString() + " " + this.value.toString();
	}
}
