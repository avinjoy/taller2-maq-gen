package editor;

import java.io.File;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class NataliusErrorParser extends AbstractErrorParser {

    private String configPath = "src\\editor\\res\\natalius.xml";
    
    public NataliusErrorParser(JTextComponent c) {

        initialize(new File(configPath));
        component = c;

    }

}
