package Interfaz;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import FuncionamientoAplicacion.Arista;
import FuncionamientoAplicacion.Grafo;
import FuncionamientoAplicacion.Kruskal;
import FuncionamientoAplicacion.UnionFind;
import FuncionamientoAplicacion.UtilidadesGrafo;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JMapViewer mapa;
    
    private Grafo<String> grafo = new Grafo<>();
    private Map<String, Point> posiciones = new HashMap<>();
    private Map<String, Integer> regionDe = new HashMap<>();

    private GraphPanel panelGrafo;
    private JTextField txtK;
    private JLabel lblStatus;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }

    public MainFrame() {
        setTitle("Partición de Provincias en k Regiones – Árbol Generador Mínimo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 820);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(contentPane);

        // ==================== BARRA SUPERIOR ====================
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        contentPane.add(barra, BorderLayout.NORTH);

        JButton btnCargar = new JButton("Cargar desde archivo");
        //btnCargar.addActionListener(e -> cargarDesdeArchivo());
        barra.add(btnCargar);

        JButton btnProvincia = new JButton("Agregar provincia");
        btnProvincia.addActionListener(e -> agregarProvincia());
        barra.add(btnProvincia);

        JButton btnSimilaridad = new JButton("Agregar similaridad");
        btnSimilaridad.addActionListener(e -> agregarSimilaridad());
        barra.add(btnSimilaridad);

        JButton btnBorrarProvincia = new JButton("Borrar provincia");
        btnBorrarProvincia.addActionListener(e -> borrarProvincia());
        barra.add(btnBorrarProvincia);
        
        JButton btnLimpiar = new JButton("Limpiar grafo");
        btnLimpiar.addActionListener(e -> limpiar());
        barra.add(btnLimpiar);

        barra.add(Box.createHorizontalStrut(15));
        barra.add(new JLabel("k ="));
        txtK = new JTextField("4", 3);
        barra.add(txtK);

        JButton btnEjecutar = new JButton("Ejecutar algoritmo");
        btnEjecutar.setFont(btnEjecutar.getFont().deriveFont(Font.BOLD));
        btnEjecutar.addActionListener(e -> ejecutarAlgoritmo());
        barra.add(btnEjecutar);

        JButton btnArbol = new JButton("Ver Árbol Mínimo");
        btnArbol.addActionListener(e -> mostrarArbolMinimo());
        barra.add(btnArbol);

        // ==================== PANEL DE DIBUJO ====================
        panelGrafo = new GraphPanel();
        contentPane.add(panelGrafo, BorderLayout.CENTER);

        // ==================== STATUS BAR ====================
        lblStatus = new JLabel(" Listo. Usá \"Cargar desde archivo\" o agregá provincias manualmente.");
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(lblStatus, BorderLayout.SOUTH);
        
        mapa = new JMapViewer();
		mapa.setSize(550, 400);
		mapa.setZoomControlsVisible(false);
		mapa.setLayout(new BorderLayout());
		
		//frame.getContentPane().add(mapa, BorderLayout.WEST);
		add(mapa, BorderLayout.WEST);
    }

    // ==================== CARGA DESDE ARCHIVO ====================
    /*private void cargarDesdeArchivo() {
        Grafo<String> cargado = CargadorArchivo.cargarDesdeArchivo(this);
        if (cargado == null) return; // canceló o error

        grafo = cargado;
        posiciones.clear();
        regionDe.clear();
        asignarPosicionesAutomaticas();

        panelGrafo.mostrarArbol = false;
        panelGrafo.repaint();

        lblStatus.setText(String.format(
                " Archivo cargado correctamente → %d provincias, %d aristas",
                grafo.tamanio(),
                UtilidadesGrafo.contarAristasUnicas(grafo)));
    }*/

    // ==================== Dentro de GraphPanel ====================
    private List<Arista<String>> obtenerAristasUnicas() {
        return UtilidadesGrafo.obtenerAristasUnicas(grafo);
    }

    private void asignarPosicionesAutomaticas() {
        List<String> vertices = new ArrayList<>(grafo.obtenerVertices());
        int columnas = 8;
        for (int i = 0; i < vertices.size(); i++) {
            int x = 90 + (i % columnas) * 130;
            int y = 80 + (i / columnas) * 110;
            posiciones.put(vertices.get(i), new Point(x, y));
        }
    }

    // ==================== CARGA MANUAL ====================
    private void agregarProvincia() {
        String nombre = AgregadorProvincia.agregarProvincia(this, grafo);
        if (nombre == null || nombre.trim().isEmpty()) return;

        int n = grafo.tamanio() - 1;
        int x = 90 + (n % 8) * 130;
        int y = 80 + (n / 8) * 110;
        posiciones.put(nombre, new Point(x, y));

        regionDe.clear();
        panelGrafo.repaint();
        lblStatus.setText(" Provincia agregada: " + nombre);
    }

    private void agregarSimilaridad() {
        Set<String> verts = grafo.obtenerVertices();
        if (verts.size() < 2) {
            JOptionPane.showMessageDialog(this, "Necesitás al menos 2 provincias.");
            return;
        }

        String[] nombres = verts.toArray(new String[0]);
        JComboBox<String> c1 = new JComboBox<>(nombres);
        JComboBox<String> c2 = new JComboBox<>(nombres);
        JTextField txtSim = new JTextField(8);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Provincia 1:"));
        panel.add(c1);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Provincia 2:"));
        panel.add(c2);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Similaridad (número real):"));
        panel.add(txtSim);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                "Agregar similaridad entre provincias limítrofes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) return;

        String u = (String) c1.getSelectedItem();
        String v = (String) c2.getSelectedItem();

        if (u.equals(v)) {
            JOptionPane.showMessageDialog(this, "Las provincias deben ser distintas.");
            return;
        }

        double sim;
        try {
            sim = Double.parseDouble(txtSim.getText().trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El valor de similaridad no es válido.");
            return;
        }

        grafo.agregarArista(u, v, sim);
        regionDe.clear();
        panelGrafo.repaint();
        lblStatus.setText(String.format(" Similaridad agregada: %s – %s = %.2f", u, v, sim));
    }

    private void borrarProvincia() {
        String eliminada = BorradorProvincia.borrarProvincia(this, grafo);

        if (eliminada == null) return; // canceló o no había provincias

        // Limpiar estructuras de la interfaz
        posiciones.remove(eliminada);
        regionDe.remove(eliminada);

        panelGrafo.mostrarArbol = false;
        panelGrafo.repaint();

        lblStatus.setText(" Provincia eliminada: " + eliminada);
    }
    
    private void limpiar() {
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Seguro que querés borrar todo el grafo actual?",
                "Confirmar limpieza", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            grafo = new Grafo<>();
            posiciones.clear();
            regionDe.clear();
            panelGrafo.mostrarArbol = false;
            panelGrafo.repaint();
            lblStatus.setText(" Grafo limpiado.");
        }
    }

    // ==================== ALGORITMO ====================
    private void ejecutarAlgoritmo() {
        if (grafo.tamanio() == 0) {
            JOptionPane.showMessageDialog(this, "El grafo está vacío.");
            return;
        }

        int k;
        try {
            k = Integer.parseInt(txtK.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "k debe ser un entero positivo.");
            return;
        }

        if (k < 1 || k > grafo.tamanio()) {
            JOptionPane.showMessageDialog(this,
                    "k debe estar entre 1 y " + grafo.tamanio());
            return;
        }

        // 1. Árbol Generador Mínimo
        List<Arista<String>> mst = Kruskal.ejecutar(grafo);

        // 2. Ordenar de mayor a menor peso y eliminar las k-1 más pesadas
        mst.sort((a, b) -> Double.compare(b. obtenerPeso(), a. obtenerPeso()));

        List<Arista<String>> aristasRestantes = new ArrayList<>();
        for (int i = k - 1; i < mst.size(); i++) {
            aristasRestantes.add(mst.get(i));
        }

        // 3. Componentes conexas con UnionFind
        List<String> vertices = new ArrayList<>(grafo.obtenerVertices());
        Map<String, Integer> indice = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            indice.put(vertices.get(i), i);
        }

        UnionFind uf = new UnionFind(vertices.size());

        for (Arista<String> a : aristasRestantes) {
            Integer u = indice.get(a.obtenerOrigen());
            Integer v = indice.get(a.obtenerDestino());
            if (u != null && v != null) {
                uf.union(u, v);
            }
        }

        // 4. Asignar regiones
        regionDe.clear();
        Map<Integer, Integer> raizARegion = new HashMap<>();
        int idRegion = 0;

        for (String v : vertices) {
            int raiz = uf.root(indice.get(v));
            if (!raizARegion.containsKey(raiz)) {
                raizARegion.put(raiz, idRegion++);
            }
            regionDe.put(v, raizARegion.get(raiz));
        }

        panelGrafo.mostrarArbol = false;
        panelGrafo.repaint();

        lblStatus.setText(String.format(
                " Algoritmo ejecutado → %d regiones formadas (k = %d)", idRegion, k));
    }

    private void mostrarArbolMinimo() {
        if (grafo.tamanio() == 0) return;

        panelGrafo.arbolActual = Kruskal.ejecutar(grafo);
        panelGrafo.mostrarArbol = true;
        panelGrafo.repaint();
        lblStatus.setText(" Mostrando Árbol Generador Mínimo");
    }

    // ==================== PANEL DE DIBUJO ====================
    private class GraphPanel extends JPanel {

        boolean mostrarArbol = false;
        List<Arista<String>> arbolActual = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (grafo.tamanio() == 0) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
                g2.drawString("Cargá un archivo (formato: Origen,Destino,Peso) o agregá provincias manualmente", 40, 50);
                return;
            }

            // Dibujar aristas
            Collection<Arista<String>> paraDibujar = mostrarArbol ? arbolActual : obtenerAristasUnicas();

            for (Arista<String> a : paraDibujar) {
                Point p1 = posiciones.get(a.obtenerOrigen());
                Point p2 = posiciones.get(a.obtenerDestino());
                if (p1 == null || p2 == null) continue;

                if (mostrarArbol) {
                    g2.setColor(new Color(0, 100, 180));
                    g2.setStroke(new BasicStroke(3.0f));
                } else {
                    g2.setColor(new Color(170, 170, 170));
                    g2.setStroke(new BasicStroke(1.6f));
                }

                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Etiqueta del peso
                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString(String.format("%.1f", a. obtenerPeso()), mx + 5, my - 4);
            }

            // Dibujar vértices (coloreados por región)
            Color[] palette = {
                new Color(65, 105, 225), new Color(220, 70, 70), new Color(46, 160, 80),
                new Color(240, 150, 30), new Color(140, 80, 190), new Color(40, 170, 170),
                new Color(190, 90, 140), new Color(100, 100, 100), new Color(180, 120, 40)
            };

            for (String v : grafo.obtenerVertices()) {
                Point p = posiciones.get(v);
                if (p == null) continue;

                Color color = new Color(100, 149, 237);
                if (!regionDe.isEmpty() && !mostrarArbol) {
                    color = palette[regionDe.getOrDefault(v, 0) % palette.length];
                }

                g2.setColor(color);
                g2.fillOval(p.x - 22, p.y - 22, 44, 44);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(p.x - 22, p.y - 22, 44, 44);

                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int ancho = fm.stringWidth(v);
                g2.drawString(v, p.x - ancho / 2, p.y + 5);
            }
        }
    }
}