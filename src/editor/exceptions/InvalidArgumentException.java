package editor.exceptions;

@SuppressWarnings("serial")
public class InvalidArgumentException extends IllegalArgumentException {

	private int lineNumber;

	public InvalidArgumentException (String s) {
        super (s);
    }

    public InvalidArgumentException (String s, int lnNumber) {
        super (s);
        this.lineNumber = lnNumber;
    }

	public int getLineNumber() {
		return lineNumber;
	}
}
