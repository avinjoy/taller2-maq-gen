package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class PortPanel  extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int CANT_CELDAS = 1;
	
	JPanel panelPort;
	private ArrayList<JPanel> row;
	private ArrayList<JLabel> labelPort;
	private ArrayList<JLabel> arrowPort;
	private ArrayList<JLabel> valorPort;
	private ArrayList<JLabel> arrowFlag;
	private ArrayList<JLabel> valorFlag;

	public PortPanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		//this.setPreferredSize(new Dimension(340, this.getHeight()));
		this.setPreferredSize(new Dimension(340, 80));

		GridBagConstraints c = new GridBagConstraints();

		JLabel labelReg = new JLabel("Puertos");
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		this.add(labelReg, c);

		panelPort = agregarPanelPuertos(this);
		
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

	private JPanel agregarPanelPuertos(JPanel contenedor) {
		// CREA UN PANEL PARA LOS REGISTROS
		JPanel panelPort = new JPanel(new GridBagLayout()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4409720857557537617L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelPort.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		contenedor.add(panelPort, c);

		//Crea los registros
		row = new ArrayList<JPanel>();
		labelPort = new ArrayList<JLabel>();
		arrowPort = new ArrayList<JLabel>();
		valorPort = new ArrayList<JLabel>();
		arrowFlag = new ArrayList<JLabel>();
		valorFlag = new ArrayList<JLabel>();
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.weighty = 0.0;
		c1.weightx = 1.0;
		c1.anchor = GridBagConstraints.WEST;
		c1.fill = GridBagConstraints.HORIZONTAL;

		//ENTRADA
		JPanel panelRow = new JPanel(new GridBagLayout());
		panelRow.setBackground(this.getColorRow(0));
		row.add(panelRow);
		
		c1.gridy = 0;
		panelPort.add(panelRow, c1);
		labelPort.add(agregarLabelPuerto(panelRow, "ENTRADA", 0));
		arrowPort.add(agregarArrowPuerto(panelRow, 0));
		valorPort.add(agregarValorPuerto(panelRow, "", 0));
		arrowFlag.add(agregarArrowFlag(panelRow, 0));
		valorFlag.add(agregarValorFlag(panelRow, "", 0));
		
		//SALIDA
		panelRow = new JPanel(new GridBagLayout());
		panelRow.setBackground(this.getColorRow(0));
		row.add(panelRow);
		
		c1.gridy = 1;
		panelPort.add(panelRow, c1);
		labelPort.add(agregarLabelPuerto(panelRow, "SALIDA", 1));
		arrowPort.add(agregarArrowPuerto(panelRow, 1));
		valorPort.add(agregarValorPuerto(panelRow, "", 1));
		arrowFlag.add(agregarArrowFlag(panelRow, 1));
		valorFlag.add(agregarValorFlag(panelRow, "", 1));		


		return panelPort;
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

	private JLabel agregarLabelPuerto(JPanel contenedor, String nombre,
			int fila) {

		JLabel label = new JLabel(nombre);
		label.setPreferredSize(new Dimension(70, 10));

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

	private JLabel agregarValorPuerto(JPanel contenedor, String value,
			int fila) {

		JLabel valor = new JLabel(value);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 4; // aligned with button 2
		c.gridy = fila; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		contenedor.add(valor, c);

		return valor;
	}

	private JLabel agregarArrowPuerto(JPanel contenedor, int fila) {

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

	private JLabel agregarValorFlag(JPanel contenedor, String value,
			int fila) {

		JLabel valor = new JLabel(value);
		valor.setPreferredSize(new Dimension(30, 10));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 2; // aligned with button 2
		c.gridy = fila; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 0.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		contenedor.add(valor, c);

		return valor;
	}

	private JLabel agregarArrowFlag(JPanel contenedor, int fila) {

		JLabel arrow = new JLabel("=>");
		arrow.setPreferredSize(new Dimension(30, 10));
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 3;
		c.gridy = fila;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		contenedor.add(arrow, c);

		return arrow;
	}
	
	public void setValorAndMark(String valor_port, String valor_flag, int index) {
		JLabel label_port_aux = null;
		JLabel arrow_port_aux = null;
		JLabel valor_port_aux = null;
		JLabel arrow_flag_aux = null;
		JLabel valor_flag_aux = null;
		
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_port_aux = labelPort.get(i);
			arrow_port_aux = arrowPort.get(i);
			valor_port_aux = valorPort.get(i);
			arrow_flag_aux = arrowFlag.get(i);
			valor_flag_aux = valorFlag.get(i);
			

			if (i == index) {
				label_port_aux.setForeground(Color.RED);
				arrow_port_aux.setForeground(Color.RED);
				valor_port_aux.setForeground(Color.RED);		
				valor_port_aux.setText(valor_port);
				arrow_flag_aux.setForeground(Color.RED);
				valor_flag_aux.setForeground(Color.RED);		
				valor_flag_aux.setText(valor_flag);
				
			} else {
				label_port_aux.setForeground(Color.BLACK);
				arrow_port_aux.setForeground(Color.BLACK);
				valor_port_aux.setForeground(Color.BLACK);	
				arrow_flag_aux.setForeground(Color.BLACK);
				valor_flag_aux.setForeground(Color.BLACK);		
			}
			
			label_port_aux.repaint();
			arrow_port_aux.repaint();
			valor_port_aux.repaint();	
			arrow_flag_aux.repaint();
			valor_flag_aux.repaint();				
		}
	}
	
	public void resetValor(String valorinicialport, String valorinicialflag) {
		JLabel label_port_aux = null;
		JLabel arrow_port_aux = null;
		JLabel valor_port_aux = null;
		JLabel arrow_flag_aux = null;
		JLabel valor_flag_aux = null;
		
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_port_aux = labelPort.get(i);
			arrow_port_aux = arrowPort.get(i);
			valor_port_aux = valorPort.get(i);
			arrow_flag_aux = arrowFlag.get(i);
			valor_flag_aux = valorFlag.get(i);		

			label_port_aux.setForeground(Color.BLACK);
			arrow_port_aux.setForeground(Color.BLACK);
			valor_port_aux.setForeground(Color.BLACK);		
			valor_port_aux.setText(valorinicialport);
			arrow_flag_aux.setForeground(Color.BLACK);
			valor_flag_aux.setForeground(Color.BLACK);		
			valor_flag_aux.setText(valorinicialflag);
			
			label_port_aux.repaint();
			arrow_port_aux.repaint();
			valor_port_aux.repaint();	
			arrow_flag_aux.repaint();
			valor_flag_aux.repaint();	
		}
	}
	
}
