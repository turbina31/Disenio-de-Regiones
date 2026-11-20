package Interfaz;

import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import FuncionamientoApp.Grafo;

public class AgregadorSimilaridad {
	public static boolean agregarSimilaridad(java.awt.Component componente, Grafo<String> grafo) {
		Set<String> vertise = grafo.obtenerVertices();
		if (vertise.size() < 2) {
			JOptionPane.showMessageDialog(componente, "Necesitás al menos 2 provincias.");
			return false;
		}

		String[] nombres = vertise.toArray(new String[0]);
		java.util.Arrays.sort(nombres);
		JComboBox<String> provincia1 = new JComboBox<>(nombres);
		JComboBox<String> provincia2 = new JComboBox<>(nombres);
		JTextField txtSim = new JTextField(8);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Provincia 1:"));
		panel.add(provincia1);
		panel.add(Box.createVerticalStrut(8));
		panel.add(new JLabel("Provincia 2:"));
		panel.add(provincia2);
		panel.add(Box.createVerticalStrut(8));
		panel.add(new JLabel("Similaridad (número real):"));
		panel.add(txtSim);

		int opcion = JOptionPane.showConfirmDialog(componente, panel, "Agregar similaridad", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (opcion != JOptionPane.OK_OPTION)
			return false;

		String u = (String) provincia1.getSelectedItem();
		String v = (String) provincia2.getSelectedItem();
		if (u.equals(v)) {
			JOptionPane.showMessageDialog(componente, "Las provincias deben ser distintas.");
			return false;
		}

		double sim;
		try {
			sim = Double.parseDouble(txtSim.getText().trim().replace(',', '.'));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(componente, "Valor de similaridad inválido.");
			return false;
		}
		
		grafo.agregarArista(u, v, sim);
		return true;
	}
}
