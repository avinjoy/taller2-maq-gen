package editor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public abstract class AbstractErrorParser implements ErrorParser {

    JTextComponent component;
    List<List<Element>> arguments;

    public void initialize(File f){

        arguments = new ArrayList<List<Element>>();

        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {
            doc = builder.build(f);
            List<Element> listOfCommands = doc.getRootElement().getChildren("command");

            for (int i = 0; i < listOfCommands.size(); i++) {
                Element node = listOfCommands.get(i);

                List<Element> listOfParameters = node.getChildren("parameter");

                arguments.add(listOfParameters);

            }
        } catch (JDOMException ex) {
            Logger.getLogger(AbstractErrorParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AbstractErrorParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void checkError() {
        StringTokenizer t = new StringTokenizer(component.getText(), "\r\n", true);
        Integer position = 0;
        Integer startErrorPosition = 0;
        Integer endErrorPosition = 0;
        for (Highlighter.Highlight highlight:component.getHighlighter().getHighlights()){
            
            if (highlight.getPainter() instanceof JaggedHighlighterPainter){
            
                component.getHighlighter().removeHighlight(highlight);
            }
            
        }
        component.repaint();

        while (t.hasMoreElements()) {
            String s = t.nextToken();
            if (s.equals("\r") || s.contentEquals("\n")) {
                position += 1;
                continue;
            }
            Iterator<List<Element>> it = arguments.iterator();
            boolean match = false;
            boolean find = false;
            String errorMessage = null;
            while (it.hasNext() && !find) {
                int i = 1;
                Pattern pattern = Pattern.compile("^");

                match = true;
                int linePosition = 0;
                for (Element e : it.next()) {

                    pattern = Pattern.compile(pattern.pattern() + e.getChildText("pattern"));

                    Matcher m = pattern.matcher(s);
                    if (m.find()) {

                        linePosition = m.end() - 1;
                    } else {

                        startErrorPosition = position + linePosition;
                        endErrorPosition = position + linePosition + (s.length() - linePosition);
                        errorMessage = e.getChildText("message");
                        match = false;
                        break;
                    }
                    i++;
                    find = true;
                }

            }
            position += s.length();
            if (!match) {
                try {
//                    System.out.println(startErrorPosition + " * "+ endErrorPosition);
                    component.getHighlighter().addHighlight(startErrorPosition, endErrorPosition, new JaggedHighlighterPainter(Color.red, errorMessage));
                } catch (BadLocationException ex) {
                    Logger.getLogger(NataliusErrorParser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
