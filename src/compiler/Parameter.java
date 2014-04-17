package compiler;

public class Parameter {

	public enum Type{
		REGISTER, ADRRESS,INTEGER,FLOAT
	}
	
	public Type parameterType;
	private String value;
	public int number;
	
	public Parameter(int number, Type pt, String val){
		this.number = number;
		this.parameterType = pt;
		this.value = val;
	}
	
	public String getHexaValue() {
		String value = "";
		if (this.parameterType == Type.REGISTER)
			value = Integer.toHexString(Integer.parseInt(this.value));
		if (this.parameterType == Type.ADRRESS)
			value = this.value;
		return value;
	}
}
