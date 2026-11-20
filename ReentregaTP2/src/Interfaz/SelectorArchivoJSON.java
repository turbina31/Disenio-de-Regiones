package Interfaz;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectorArchivoJSON {
	public static String elegir(Component padre) {
		JFileChooser elegirArchivo = new JFileChooser();
		elegirArchivo.setDialogTitle("Seleccionar archivo de coordenadas (JSON)");
		elegirArchivo.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

		if (elegirArchivo.showOpenDialog(padre) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		return elegirArchivo.getSelectedFile().getAbsolutePath();
	}
}
