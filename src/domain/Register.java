package domain;

public class Register {

	private Integer number;
	private Byte value;
	
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
	
	
}
