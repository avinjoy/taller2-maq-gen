package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import compiler.exceptions.CompilationtException;

public class ErrorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private List<CompilationtException> errores;
	private JPanel panelError;
	private JScrollPane scrollPane;
	private ArrayList<JPanel> row;
	private ArrayList<JLabel> labelError;

	public ErrorPanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(this.getWidth(), 200));

		JLabel label = new JLabel("Errores");

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		this.add(label, c);
		
		panelError = new JPanel(new GridBagLayout()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3438850775900238569L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				//g.setColor(Color.WHITE);
				g.setColor(Color.cyan);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelError.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		scrollPane = new JScrollPane(panelError);

		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, c);		
		
	}

	private void agregarPanelError() {
		
		//Crea los registros
		row = new ArrayList<JPanel>();
		labelError = new ArrayList<JLabel>();
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.weighty = 0.0;
		c1.weightx = 1.0;
		c1.anchor = GridBagConstraints.NORTH;
		c1.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i < errores.size(); i++) {
			JPanel panelRow = new JPanel(new GridBagLayout());
			panelRow.setBackground(this.getColorRow(i));
			row.add(panelRow);
			
			c1.gridy = i + 1;
			panelError.add(panelRow, c1);
			
			CompilationtException exception = errores.get(i);
			labelError.add(agregarLabelError(panelRow, exception.getMessage(), i));
		}
		
		//Completa el panel
		JPanel panelRow = new JPanel(new GridBagLayout());
		panelRow.setBackground(this.getColorRow(errores.size()));
		row.add(panelRow);
		
		c1.weighty = 1.0;
		c1.gridy = errores.size() + 1;
		c1.fill = GridBagConstraints.BOTH;
		panelError.add(panelRow, c1);		
	}
	
	private Color getColorRow(int index) {
		Color color;
		if (index % 2 == 0) {
			color = new Color(240,240,240);
		} else {
			color = Color.WHITE;
		}
		
		return color;
	}

	private JLabel agregarLabelError(JPanel contenedor, String nombre,
			int fila) {

		JLabel label = new JLabel(nombre);
		//label.setPreferredSize(new Dimension(0, 10));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0; // aligned with button 2
		c.gridy = fila; // third row
		c.weighty = 1.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		contenedor.add(label, c);

		return label;
	}

	public void loadError(List<CompilationtException> errores) {
		this.errores = errores;
		
		if (panelError != null)
			panelError.removeAll();
		
		agregarPanelError();
		this.getParent().validate();
	}
	    
}

