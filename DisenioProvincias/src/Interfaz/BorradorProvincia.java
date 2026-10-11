package Interfaz;

import java.util.Set;

import javax.swing.JOptionPane;

import FuncionamientoAplicacion.Grafo;

public class BorradorProvincia {
	public static String borrarProvincia(java.awt.Component componente, Grafo<String> grafo) {
		Set<String> vertise = grafo.obtenerVertices();
		
		if (vertise.isEmpty()) {
			JOptionPane.showMessageDialog(componente, "No hay provincia para borrar.");
			return null;
		}
		
		String[] nombres = vertise.toArray(new String[0]);
		
		String seleccion = (String) JOptionPane.showInputDialog(
				componente,
				"Seleccioná la provincia a borrar: ",
				"Borrar provincia",
				JOptionPane.QUESTION_MESSAGE,
				null,
				nombres,
				nombres[0]
						);
		if (seleccion == null) {
			return null;
		}
		
		int confirmar = JOptionPane.showConfirmDialog(
				componente,
				"¿Seguro que querés eliminar la provincia" + seleccion + "?\n" + 
				"También se eliminarán todas sus conexion.",
				"Confirmar eliminación",
				JOptionPane.YES_NO_OPTION
				);
		
		if (confirmar != JOptionPane.YES_NO_OPTION) {
			return null;
		}
		
		grafo.eliminarVertice(seleccion);
		
		return seleccion;
	}
}
