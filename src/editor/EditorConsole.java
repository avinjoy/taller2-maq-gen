package editor;

import domain.Console;
import domain.Observer;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class EditorConsole extends JTextArea implements Console, Observer {

    @Override
    public String input() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void output(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

