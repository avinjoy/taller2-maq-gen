package editor;

import editor.exceptions.InvalidInstruccionException;
import java.awt.Color;
import java.util.StringTokenizer;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class MachineErrorParser implements ErrorParser {

    @Override
    public void checkError(JTextComponent ide) {
        
        StringTokenizer t = new StringTokenizer(ide.getText(), "\r\n", true);

        Integer position = 0;

        ide.getHighlighter().removeAllHighlights();
        ide.repaint();
        
        while (t.hasMoreElements()) {

            String s = t.nextToken();

            StringTokenizer ln = new StringTokenizer(s, " ", true);

            Integer amountWord = 0;

            while (ln.hasMoreElements()) {

                String w = ln.nextToken();

                if (w.equals(" ") || w.equals("\r") || w.equals("\n")) {
                    position += w.length();
                    continue;
                }
                amountWord++;

                try {


                    if (!w.matches("^[\\da-fA-F]*$") && amountWord <3){
                        
                        throw new InvalidInstruccionException("No es un valor hexadecimal válido");
                    }
                    switch (amountWord) {
                        case 1:
                            if (w.length() != 2) {
                             
                                throw new InvalidInstruccionException("El primer argumento corresponde a la posición de memoria y solo hay 256 direcciones disponibles");
                            }
                            break;
                        case 2:
                            if (w.length() != 4) {

                                throw new InvalidInstruccionException("El segundo argumento corresponde a la instrucción y solo puede tener 4 caracteres en hexadecimal");
                            }
                            break;
                    }
                } catch (NumberFormatException ex) {
                    throw new InvalidInstruccionException("No es un formato hexadecimal válido");
                    
                }catch(InvalidInstruccionException ex1){
                    try {
                        ide.getHighlighter().addHighlight(position, position + w.length(), new JaggedHighlighterPainter(Color.red,ex1.getMessage()));

                    } catch (BadLocationException ex2) {
                        System.out.println(ex2.getMessage());
                    }
                }
                position += w.length();
            }
        }
    }

}
