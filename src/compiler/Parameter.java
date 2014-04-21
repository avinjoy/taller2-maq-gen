package compiler;

public class Parameter {

	public enum Type{
		REGISTER, ADRRESS,INTEGER,FLOAT,BYTEINTEGER
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
		if (this.parameterType == Type.BYTEINTEGER)
			value = "0" + Integer.toHexString(Integer.parseInt(this.value));
		if (this.parameterType == Type.ADRRESS)
			value = this.value;		
		return value;
	}
	
	public String getValue(){
		String value = "";
		if (this.parameterType == Type.REGISTER || this.parameterType == Type.BYTEINTEGER){
			Integer sValue = Integer.parseInt(this.value, 16);
			value = sValue.toString();
		}
		if (this.parameterType == Type.ADRRESS)
			value = this.value;				
		return value;		
	}
}
