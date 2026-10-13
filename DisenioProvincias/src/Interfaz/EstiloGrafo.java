package Interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import FuncionamientoAplicacion.Arista;
import FuncionamientoAplicacion.Grafo;
import FuncionamientoAplicacion.UtilidadesGrafo;

public class EstiloGrafo extends JPanel {

    private Grafo<String> grafo;
    private Map<String, Point> posiciones;
    private Map<String, Integer> regionDe;

    boolean mostrarArbol = false;
    List<Arista<String>> arbolActual = new ArrayList<>();

    public EstiloGrafo(Grafo<String> grafo,
                      Map<String, Point> posiciones,
                      Map<String, Integer> regionDe) {
        this.grafo = grafo;
        this.posiciones = posiciones;
        this.regionDe = regionDe;
        setBackground(EstiloBoton.FONDO_PANEL);
    }

    public void actualizarReferencias(Grafo<String> grafo,
                                      Map<String, Point> posiciones,
                                      Map<String, Integer> regionDe) {
        this.grafo = grafo;
        this.posiciones = posiciones;
        this.regionDe = regionDe;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g2.setColor(EstiloBoton.FONDO_PANEL);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (grafo == null || grafo.tamanio() == 0) {
            g2.setColor(EstiloBoton.TEXTO_OSCURO);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2.drawString("Agregá provincias manualmente o cargá coordenadas de un archivo.json", 40, 50);
            return;
        }

        // ---------- Aristas ----------
        Collection<Arista<String>> paraDibujar = mostrarArbol
                ? arbolActual
                : UtilidadesGrafo.obtenerAristasUnicas(grafo);

        for (Arista<String> a : paraDibujar) {
            Point p1 = posiciones.get(a.obtenerOrigen());
            Point p2 = posiciones.get(a.obtenerDestino());
            if (p1 == null || p2 == null) continue;

            if (mostrarArbol) {
                g2.setColor(EstiloBoton.VIOLETA_BOTON);
                g2.setStroke(new BasicStroke(3.2f));
            } else {
                g2.setColor(EstiloBoton.VIOLETA_FONDO);
                g2.setStroke(new BasicStroke(2.0f));
            }

            g2.drawLine(p1.x, p1.y, p2.x, p2.y);

            // Peso
            int mx = (p1.x + p2.x) / 2;
            int my = (p1.y + p2.y) / 2;
            g2.setColor(EstiloBoton.TEXTO_OSCURO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString(String.format("%.1f", a.obtenerPeso()), mx + 6, my - 4);
        }

        // ---------- Vértices ----------
        for (String v : grafo.obtenerVertices()) {
            Point p = posiciones.get(v);
            if (p == null) continue;

            Color color = EstiloBoton.VIOLETA_BOTON;
            if (regionDe != null && !regionDe.isEmpty() && !mostrarArbol) {
                color = EstiloBoton.PALETA_REGIONES[
                        regionDe.getOrDefault(v, 0) % EstiloBoton.PALETA_REGIONES.length];
            }

            // Círculo
            g2.setColor(color);
            g2.fillOval(p.x - 24, p.y - 24, 48, 48);

            // Borde blanco
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(p.x - 24, p.y - 24, 48, 48);

            // Nombre
            g2.setColor(EstiloBoton.TEXTO_OSCURO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int ancho = fm.stringWidth(v);
            g2.drawString(v, p.x - ancho / 2, p.y + 5);
        }
    }
}
