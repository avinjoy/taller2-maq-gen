package editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class NataliusErrorParser implements ErrorParser {

    List<String> patterns = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add("^ldi[ ]*r[1-16]{1,2},[\\d]{3}$");
            add("^ldm[ ]*r[1-16]{1,2},[\\d]{3}$");
            add("^stm[ ]*r[1-16]{1,2},[\\d]{3}$");
            add("^cpy[ ]*r[1-16]{1,2},r[1-16]{1,2}$");
            add("^add[ ]*r[1-16]{1,2},r[1-16]{1,2},r[1-16]{1,2}$");
            add("^adm[ ]*r[1-16]{1,2},r[1-16]{1,2},r[1-16]{1,2}$");
            add("^oor[ ]*r[1-16]{1,2},r[1-16]{1,2},r[1-16]{1,2}$");
            add("^and[ ]*r[1-16]{1,2},r[1-16]{1,2},r[1-16]{1,2}$");
            add("^xor[ ]*r[1-16]{1,2},r[1-16]{1,2},r[1-16]{1,2}$");
            add("^rrr[ ]*r[1-16]{1,2},[\\d]{3}$");
            add("^jmp[ ]*r[1-16]{1,2},[\\d]{3}$");
            add("^end[ ]$");
        }
    };

    @Override
    public void checkError(JTextComponent ide) {

        StringTokenizer t = new StringTokenizer(ide.getText(), "\r\n", true);

        Integer position = 0;

        ide.getHighlighter().removeAllHighlights();
        
        ide.repaint();

        while (t.hasMoreElements()) {

            String s = t.nextToken();

            Iterator<String> it = patterns.iterator();

            boolean match = false;

            while (it.hasNext() && !match) {

                match = s.matches(it.next());

            }

            if (!match) {
                try {
                    ide.getHighlighter().addHighlight(position, position + s.length(), new JaggedHighlighterPainter(Color.red, "Error de sintaxis"));
                } catch (BadLocationException ex) {
                    Logger.getLogger(NataliusErrorParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            position += s.length();
        }
    }

}
