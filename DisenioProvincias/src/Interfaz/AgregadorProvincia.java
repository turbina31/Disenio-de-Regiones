package Interfaz;

import javax.swing.JOptionPane;

import FuncionamientoAplicacion.Grafo;

public class AgregadorProvincia {
    /** Devuelve el nombre agregado, o null si se canceló o ya existía. */
    public static String agregarProvincia(java.awt.Component componente, Grafo<String> grafo) {
        String nombre = JOptionPane.showInputDialog(componente, "Nombre de la provincia:");

        if (nombre == null) {
            return null;
        }
        //nombre = nombre.trim();
        if (nombre.isEmpty()) {
            return null;
        }
        if (grafo.existeVertice(nombre)) {
            JOptionPane.showMessageDialog(componente, "La provincia ya existe: " + nombre);
            return null;
        }
        grafo.agregarVertice(nombre);
        return nombre;
    }
}