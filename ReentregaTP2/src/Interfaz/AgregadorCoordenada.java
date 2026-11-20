package Interfaz;

import java.util.Map;
import java.util.Set;
import javax.swing.*;
import FuncionamientoApp.Coordenada;
import FuncionamientoApp.Grafo;

public class AgregadorCoordenada {

    /**
     * Diálogo para agregar o modificar lat/lon de una provincia del grafo.
     * @return true si se guardó la coordenada
     */
    public static boolean agregarCordenanda(java.awt.Component componente, Grafo<String> grafo, Map<String, Coordenada> coordenadasCargadas) {

        Set<String> vertices = grafo.obtenerVertices();
        if (vertices.isEmpty()) {
            JOptionPane.showMessageDialog(componente, "Primero agregá provincias.");
            return false;
        }

        String[] nombres = vertices.toArray(new String[0]);
        java.util.Arrays.sort(nombres);

        JComboBox<String> combo = new JComboBox<>(nombres);
        JTextField txtLat = new JTextField(10);
        JTextField txtLon = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Provincia:"));
        panel.add(combo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Latitud (ej: -34.6037):"));
        panel.add(txtLat);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Longitud (ej: -58.3816):"));
        panel.add(txtLon);

        int opcion = JOptionPane.showConfirmDialog(
                componente, panel, "Agregar / modificar coordenada",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return false;
        }

        String provincia = (String) combo.getSelectedItem();
        double latitud, longitud;
        try {
            latitud = Double.parseDouble(txtLat.getText().trim().replace(',', '.'));
            longitud = Double.parseDouble(txtLon.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(componente, "Latitud o longitud inválida.");
            return false;
        }

        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            JOptionPane.showMessageDialog(componente,
                    "Latitud (-90..90) o longitud (-180..180) fuera de rango.");
            return false;
        }

        coordenadasCargadas.put(provincia, new Coordenada(provincia, latitud, longitud));
        return true;
    }
}