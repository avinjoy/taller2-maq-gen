package compiler.exceptions;

@SuppressWarnings("serial")
public class CompilationtException extends IllegalArgumentException {

	private int lineNumber;

	public CompilationtException (String s) {
        super (s);
    }

    public CompilationtException (String s, int lnNumber) {
        super (s);
        this.lineNumber = lnNumber;
    }

	public int getLineNumber() {
		return lineNumber;
	}
}
