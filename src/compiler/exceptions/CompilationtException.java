package compiler.exceptions;

@SuppressWarnings("serial")
public class CompilationtException extends IllegalArgumentException {

	private Integer lineNumber;

    public CompilationtException (String s, int lnNumber) {
        super (s);
        this.lineNumber = lnNumber;
    }

	public int getLineNumber() {
		return lineNumber;
	}
	
	public String getMessage(){		
		return "Línea " + this.lineNumber.toString() + ": " + super.getMessage();
	}
	
}
