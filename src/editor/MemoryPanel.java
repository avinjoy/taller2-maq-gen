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
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import domain.Console;
import domain.Observer;

public class MemoryPanel extends JPanel implements Console, Observer {
	private static final long serialVersionUID = 1L;
	private static final int CANT_CELDAS = 255;
	private static final int CANT_FILA = 15;
	private static final int CANT_COL = 15;
	
	JPanel panelMem;
	private ArrayList<JPanel> row;
	private ArrayList<JLabel> labelMem;
	private ArrayList<JLabel> arrowMem;
	private ArrayList<JLabel> valorMem;

	public MemoryPanel() {
		super();

		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(130, 200));//this.getHeight()));

		GridBagConstraints c = new GridBagConstraints();

		JLabel labelMem = new JLabel("Memoria");
		c.insets = new Insets(7, 5, 7, 5); // top padding
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		this.add(labelMem, c);

		panelMem = agregarPanelMemory(this);

	}

	private JPanel agregarPanelMemory(JPanel contenedor) {
		// CREA UN PANEL PARA LOS REGISTROS
		JPanel panelMemory = new JPanel(new GridBagLayout()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4166309657225245203L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelMemory.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		JScrollPane scrollPane = new JScrollPane(panelMemory);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		contenedor.add(scrollPane, c);

		//Crea los registros
		row = new ArrayList<JPanel>();
		labelMem = new ArrayList<JLabel>();
		arrowMem = new ArrayList<JLabel>();
		valorMem = new ArrayList<JLabel>();
		
		//Datos comunes para cada una de las celdas de la grilla
		GridBagConstraints c1 = new GridBagConstraints();
		c1.weighty = 0.0;
		c1.weightx = 1/(CANT_COL + 1);
		c1.anchor = GridBagConstraints.WEST;
		c1.fill = GridBagConstraints.HORIZONTAL;

		for (int fila = 0; fila <= CANT_FILA + 1; fila++) {
			Color color = this.getColorRow(fila);
			for (int col = 0; col <= CANT_COL + 1; col++) {
				
				//Completa los datos de la celda en particular
				JPanel panelRow = new JPanel(new GridBagLayout());
				panelRow.setBackground(color);
				row.add(panelRow);
				c1.gridy = fila;
				c1.gridx = col;
				panelMemory.add(panelRow, c1);
				
				if (fila == 0 && col > 0)
					labelMem.add(agregarLabelMemory(panelRow, Integer.toHexString(col - 1).toUpperCase()));
				else if (fila > 0 && col == 0)
					labelMem.add(agregarLabelMemory(panelRow, Integer.toHexString(fila - 1).toUpperCase()));
				else if (fila > 0 && col > 0)
					valorMem.add(agregarValorMemory(panelRow, ""));
				//arrowMem.add(agregarArrowMemory(panelRow, i));
				
			}
		}
		
		/*
		for (int i = 0; i <= CANT_CELDAS; i++) {
			JPanel panelRow = new JPanel(new GridBagLayout());
			panelRow.setBackground(this.getColorRow(i));
			row.add(panelRow);
			
			c1.gridy = i;
			panelMemory.add(panelRow, c1);
			
			
			labelMem.add(agregarLabelMemory(panelRow,
					"M" + Integer.toHexString(i).toUpperCase(), i));
			arrowMem.add(agregarArrowMemory(panelRow, i));
			valorMem.add(agregarValorMemory(panelRow, "", i));

		}
		*/

		return panelMemory;
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

	private JLabel agregarLabelMemory(JPanel contenedor, String nombre) {

		JLabel label = new JLabel(nombre);
		label.setPreferredSize(new Dimension(13, 9));

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 0.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		contenedor.add(label, c);

		return label;
	}

	private JLabel agregarValorMemory(JPanel contenedor, String value) {

		JLabel valor = new JLabel(value);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0; // aligned with button 2
		c.gridy = 0; // third row
		c.weighty = 0.0; // La fila 1 debe estirarse, le ponemos 1.0
		c.weightx = 1.0; // Restauramos el valor por defecto.
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		contenedor.add(valor, c);

		return valor;
	}

	private JLabel agregarArrowMemory(JPanel contenedor, int fila) {

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

	/*
	public void setValorAndMark(String valor, int index) {
		JLabel label_aux = null;
		JLabel arrow_aux = null;
		JLabel valor_aux = null;
		
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_aux = labelMem.get(i);
			arrow_aux = arrowMem.get(i);
			valor_aux = valorMem.get(i);

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
	*/
	
	public void resetValor(String valorinicial) {
		JLabel label_aux = null;
		JLabel arrow_aux = null;
		JLabel valor_aux = null;
		
		for (int i = 0; i <= CANT_FILA; i++) {
			for (int j = 0; j <= CANT_COL; j++) {
				int position = i * (CANT_FILA + 1) + j;
				valor_aux = valorMem.get(position);
	
				valor_aux.setForeground(Color.BLACK);		
				valor_aux.setText("0");
				valor_aux.repaint();
			}
		}

		/*
		for (int i = 0; i <= CANT_CELDAS; i++) {
			label_aux = labelMem.get(i);
			arrow_aux = arrowMem.get(i);
			valor_aux = valorMem.get(i);

			label_aux.setForeground(Color.BLACK);
			arrow_aux.setForeground(Color.BLACK);
			valor_aux.setForeground(Color.BLACK);		
			valor_aux.setText(valorinicial);

			label_aux.repaint();
			arrow_aux.repaint();
			valor_aux.repaint();	
		}
		*/
	}
	
	public void loadMemoryValues(Vector<Short> values) {
		//JLabel label_aux = null;
		//JLabel arrow_aux = null;
		JLabel label_valor = null;

		for (int i = 0; i <= CANT_FILA; i++) {
			for (int j = 0; j <= CANT_COL; j++) {
				int position = i * (CANT_FILA + 1) + j;
				label_valor = valorMem.get(position);
				String valor_act = Integer.toHexString(values.get(position) & 0xffff);
				
				Color color = Color.RED;
				if (label_valor.getText().equalsIgnoreCase(valor_act)) 
					color = Color.BLACK;
				
				label_valor.setForeground(color);		
				label_valor.setText(valor_act); // Máscara para mostrar los shorts
				
				label_valor.repaint();	
			}
		}

		
		/*
		for (int i = 0; i < values.size(); i++) {
			label_aux = labelMem.get(i);
			arrow_aux = arrowMem.get(i);
			valor_aux = valorMem.get(i);

			label_aux.setForeground(Color.BLACK);
			arrow_aux.setForeground(Color.BLACK);
			valor_aux.setForeground(Color.BLACK);		
			valor_aux.setText(Integer.toHexString(values.get(i) & 0xffff)); // Máscara para mostrar los shorts
			
			label_aux.repaint();
			arrow_aux.repaint();
			valor_aux.repaint();	
		}
		*/
	}
	
    @Override
    public String input() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void output(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

