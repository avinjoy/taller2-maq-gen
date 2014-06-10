package compiler;

public class Parameter {

	public enum Type{
		REGISTER, ADRRESS,INTEGER,FLOAT,BYTEINTEGER
	}
	
	public enum DataDir{
		INPUT,OUTPUT
	} 
	
	public Type parameterType;
	private String value;
	public int number;
	private DataDir dataDirection;
	
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
		if (this.parameterType == Type.ADRRESS || this.parameterType == Type.INTEGER)
			value = this.value;		
		return value;
	}
	
	public String getValue(){
		String value = "";
		if (this.parameterType == Type.BYTEINTEGER || this.parameterType == Type.INTEGER){
			Integer sValue = Integer.parseInt(this.value, 16);
			value = sValue.toString();
		}
		if (this.parameterType == Type.ADRRESS || this.parameterType == Type.REGISTER)
			value = this.value;				
		return value;		
	}
        
        public Integer getValueInt(){
            
            return Integer.parseInt(this.getValue());
        }

	public DataDir getDataDirection() {
		return dataDirection;
	}

	public void setDataDirection(DataDir dataDirection) {
		this.dataDirection = dataDirection;
	}

	@Override
	public String toString() {
		return getTypeOfParameter(this.parameterType) + this.number + this.value;
	}

	private String getTypeOfParameter(Type parameterType2) {
		String ret="";
		switch(parameterType2){
			case ADRRESS: {
				ret= "Address: ";
				break;
			}
			case REGISTER: {
				ret = "Register: ";
				break;
			}
			case INTEGER: {
				ret = "Integer: ";
				break;
			}
			case FLOAT: {
				ret = "Float: ";
				break;
			}
			case BYTEINTEGER: {
				ret = "ByteInt: ";
				break;
			}
		default:
			break;
		}
		return ret;
	}
	
	
}
