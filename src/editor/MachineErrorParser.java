package editor;

import java.io.File;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Oscar Bertran <oabertran@yahoo.com.ar>
 */
public class MachineErrorParser extends AbstractErrorParser {

    private String configPath = "src\\editor\\res\\machine.xml";

    public MachineErrorParser(JTextComponent c) {

        initialize(new File(configPath));
        component = c;

    }

}
