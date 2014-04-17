package editor.exceptions;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
@SuppressWarnings("serial")
public class InvalidInstruccionException extends IllegalArgumentException {
    
	private int lineNumber;
	
    public InvalidInstruccionException (String s) {
        super (s);
    }

    public InvalidInstruccionException (String s, int lnNumber) {
        super (s);
        this.lineNumber = lnNumber;
    }

	public int getLineNumber() {
		return lineNumber;
	}    
}
