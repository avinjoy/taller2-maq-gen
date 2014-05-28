package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class RegisterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int CANT_CELDAS = 15;
	
	JPanel panelReg;
	private ArrayList<JPanel> row;
	private ArrayList<JLabel> labelReg;
	private ArrayList<JLabel> arrowReg;
	private ArrayList<JLabel> valorReg;

	public RegisterPanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(340, this.getHeight()));
		//this.setPreferredSize(new Dimension(340, 380));

		GridBagConstraints c = new GridBagConstraints();

		JLabel labelReg = new JLabel("Registros");
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		this.add(labelReg, c);

		panelReg = agregarPanelRegistros(this);
		
		JLabel labelAux = new JLabel("");
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 2; // third row
		c.weighty = 1.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(labelAux, c);
	}

	private JPanel agregarPanelRegistros(JPanel contenedor) {
		// CREA UN PANEL PARA LOS REGISTROS
		JPanel panelReg = new JPanel(new GridBagLayout()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6356007198983737348L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelReg.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		contenedor.add(panelReg, c);

		//Crea los registros
		row = new ArrayList<JPanel>();
		labelReg = new ArrayList<JLabel>();
		arrowReg = new ArrayList<JLabel>();
		valorReg = new ArrayList<JLabel>();
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.weighty = 0.0;
		c1.weightx = 1.0;
		c1.anchor = GridBagConstraints.WEST;
		c1.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i <= CANT_CELDAS; i++) {
			JPanel panelRow = new JPanel(new GridBagLayout());
			panelRow.setBackground(this.getColorRow(i));
			row.add(panelRow);
			
			c1.gridy = i;
			panelReg.add(panelRow, c1);
			
			
			labelReg.add(agregarLabelRegistros(panelRow,
					"R" + Integer.toHexString(i).toUpperCase(), i));
			arrowReg.add(agregarArrowRegistros(panelRow, i));
			valorReg.add(agregarValorRegistros(panelRow, "", i));

		}

		return panelReg;
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

	private JLabel agregarLabelRegistros(JPanel contenedor, String nombre,
			int fila) {

		JLabel label = new JLabel(nombre);
		label.setPreferredSize(new Dimension(25, 10));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0; // aligned with button 2
		c.gridy = fila; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 0.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		contenedor.add(label, c);

		return label;
	}

	private JLabel agregarValorRegistros(JPanel contenedor, String value,
			int fila) {

		JLabel valor = new JLabel(value);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 2; // aligned with button 2
		c.gridy = fila; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		contenedor.add(valor, c);

		return valor;
	}

	private JLabel agregarArrowRegistros(JPanel contenedor, int fila) {

		JLabel arrow = new JLabel("=>");
		arrow.setPreferredSize(new Dimension(30, 10));
		
		//arrowReg.setForeground(Color.red);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 1;
		c.gridy = fila;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		contenedor.add(arrow, c);

		return arrow;
	}

	public void setValorAndMark(String valor, int index) {
		JLabel label_aux = null;
		JLabel arrow_aux = null;
		JLabel valor_aux = null;
		
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_aux = labelReg.get(i);
			arrow_aux = arrowReg.get(i);
			valor_aux = valorReg.get(i);

			if (i == index) {
				label_aux.setForeground(Color.RED);
				arrow_aux.setForeground(Color.RED);
				valor_aux.setForeground(Color.RED);		
				valor_aux.setText(valor);
				
			} else {
				label_aux.setForeground(Color.BLACK);
				arrow_aux.setForeground(Color.BLACK);
				valor_aux.setForeground(Color.BLACK);		
			}
			
			label_aux.repaint();
			arrow_aux.repaint();
			valor_aux.repaint();	
		}
	}
	
	public void resetValor(String valorinicial) {
		JLabel label_aux = null;
		JLabel arrow_aux = null;
		JLabel valor_aux = null;
		
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_aux = labelReg.get(i);
			arrow_aux = arrowReg.get(i);
			valor_aux = valorReg.get(i);

			label_aux.setForeground(Color.BLACK);
			arrow_aux.setForeground(Color.BLACK);
			valor_aux.setForeground(Color.BLACK);		
			valor_aux.setText(valorinicial);
			
			label_aux.repaint();
			arrow_aux.repaint();
			valor_aux.repaint();	
		}
	}
	
	public void loadRegisterValues(Vector<Byte> values) {
		JLabel label_aux = null;
		JLabel arrow_aux = null;
		JLabel valor_aux = null;
		
		for (int i = 0; i < values.size(); i++) {
			label_aux = labelReg.get(i);
			arrow_aux = arrowReg.get(i);
			valor_aux = valorReg.get(i);

			label_aux.setForeground(Color.BLACK);
			arrow_aux.setForeground(Color.BLACK);
			valor_aux.setForeground(Color.BLACK);		
			valor_aux.setText(values.get(i).toString());
			
			label_aux.repaint();
			arrow_aux.repaint();
			valor_aux.repaint();	
		}
	}
	
}
