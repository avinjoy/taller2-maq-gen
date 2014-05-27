package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import domain.Console;
import domain.Observer;


public class ConsolePanel extends JPanel implements Console, Observer {

	private static final long serialVersionUID = 1L;
	private static final String PROMPT = "> ";
	private static final String NEW_LINE = "\n";
	
	JPanel panelConsole;
	JTextArea textArea;
	JScrollPane scrollPane;
	
	String inputText;
	
	public ConsolePanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(400, 200));

		GridBagConstraints c = new GridBagConstraints();

		JLabel labelReg = new JLabel("Consola");
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0;
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		this.add(labelReg, c);
		

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		
		c.insets = new Insets(2, 5, 0, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 1; // second row
		c.weighty = 1.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 0.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, c);
		
		configTextArea();
	}

	private void configTextArea() {
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);


        
        textArea.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				int limit = textArea.getText().lastIndexOf(PROMPT) + PROMPT.length();
				int caret = textArea.getCaretPosition();
				int max = textArea.getText().length();

				if (caret <= limit) {
					if (limit <= max) {
						textArea.setCaretPosition(limit);
					}
				}					
			}
		});
		
		textArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				int code = event.getKeyCode();
				int limit = textArea.getText().lastIndexOf(PROMPT) + PROMPT.length();

				if (code == KeyEvent.VK_ENTER) {
					inputText = textArea.getText().subSequence(limit, textArea.getText().length()).toString();
					textArea.append(NEW_LINE);
					setTextAreaEditable(false);
				} else {
					int caret = textArea.getCaretPosition();
					if (caret <= limit) {
						if (code == KeyEvent.VK_BACK_SPACE) {
							textArea.append(" ");
							textArea.setCaretPosition(limit + 1);
						}
					}
				}
			}
		});
		setTextAreaEditable(false);
	}
	
	private void setTextAreaEditable(boolean edit) {
		textArea.setEditable(edit);
		textArea.setEnabled(edit);
	}
	
	public void clear() {
		textArea.setText("");
	}

    @Override
    public String input() {
    	inputText = null;
    	textArea.append(PROMPT);
    	
    	setTextAreaEditable(true);
    	//((AbstractDocument)textArea.getDocument()).setDocumentFilter(new NonEditableLineDocumentFilter());
        
    	return "";
    	//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void output(String s) {
    	if (!s.trim().endsWith(NEW_LINE))
    		s += NEW_LINE;
    	
    	textArea.append(s);
    }
    
}

