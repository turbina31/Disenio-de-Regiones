package Interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import FuncionamientoApp.Arista;
import FuncionamientoApp.Coordenada;
import FuncionamientoApp.CoordenadasJSON;
import FuncionamientoApp.Grafo;
import FuncionamientoApp.Kregiones;
import FuncionamientoApp.Kruskal;
import FuncionamientoApp.NormalizadorNombres;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class VentanaAplicacion extends JFrame {

	private static final long serialVersionUID = 1L;

	private Grafo<String> grafo = new Grafo<>();
	private final Map<String, Point> posiciones = new HashMap<>();
	private final Map<String, Integer> regionDe = new HashMap<>();
	private NormalizadorNombres normalizar = new NormalizadorNombres();
	private final Map<String, Coordenada> coordenadasCargadas = new HashMap<>();

	private EstiloGrafo panelGrafo;
	
	private Mapa mapa;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {
			}
			new VentanaAplicacion().setVisible(true);
		});
	}

	public VentanaAplicacion() {
		setTitle("Partición de Provincias en k Regiones – Árbol Generador Mínimo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 768);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		setContentPane(contentPane);

		JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
		contentPane.add(barra, BorderLayout.NORTH);

		JButton btnProvincia = EstiloBoton.crearBoton("Agregar provincia", false);
		btnProvincia.addActionListener(e -> agregarProvincia());
		barra.add(btnProvincia);

		JButton btnBorrarProvincia = EstiloBoton.crearBoton("Borrar provincia", false);
		btnBorrarProvincia.addActionListener(e -> borrarProvincia());
		barra.add(btnBorrarProvincia);

		JButton btnSimilaridad = EstiloBoton.crearBoton("Agregar similaridad", false);
		btnSimilaridad.addActionListener(e -> agregarSimilaridad());
		barra.add(btnSimilaridad);

		JButton btnLimpiar = EstiloBoton.crearBoton("Limpiar grafo", false);
		btnLimpiar.addActionListener(e -> limpiar());
		barra.add(btnLimpiar);

		barra.add(Box.createHorizontalStrut(12));

		JButton btnCargarCoords = EstiloBoton.crearBoton("Cargar coordenadas", false);
		btnCargarCoords.addActionListener(e -> cargarCoordenadas());
		barra.add(btnCargarCoords);

		JButton btnAgregarCoord = EstiloBoton.crearBoton("Agregar coordenada", false);
		btnAgregarCoord.addActionListener(e -> agregarCoordenada());
		barra.add(btnAgregarCoord);

		barra.add(Box.createHorizontalStrut(12));

		JButton btnArbol = EstiloBoton.crearBoton("Ver Árbol Mínimo", true);
		btnArbol.addActionListener(e -> mostrarArbolMinimo());
		barra.add(btnArbol);

		JButton btnEjecutar = EstiloBoton.crearBoton("Ejecutar algoritmo", true);
		btnEjecutar.addActionListener(e -> ejecutarAlgoritmo());
		barra.add(btnEjecutar);
		
		mapa = new Mapa();
		mapa.setBorder(new LineBorder(new Color(139, 92, 246)));
		panelGrafo = new EstiloGrafo(grafo, posiciones, regionDe);
		panelGrafo.setBorder(new LineBorder(new Color(139, 92, 246)));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapa, panelGrafo);
		split.setDividerLocation(600);
		split.setResizeWeight(0.5);
		contentPane.add(split, BorderLayout.CENTER);

	}

	private void refrescarVistas() {
		panelGrafo.actualizarReferencias(grafo, posiciones, regionDe);
		panelGrafo.repaint();
		mapa.mostrarProvincias(grafo, coordenadasDelGrafo());
	}

	private Map<String, Coordenada> coordenadasDelGrafo() {
		Map<String, Coordenada> porNombre = new HashMap<>();
		for (Coordenada c : coordenadasCargadas.values()) {
			porNombre.put(normalizar.normalizar(c.getNombre()), c);
		}
		Map<String, Coordenada> resultado = new HashMap<>();
		for (String provincia : grafo.obtenerVertices()) {
			Coordenada c = porNombre.get(normalizar.normalizar(provincia));
			if (c != null) {
				resultado.put(provincia, c);
			}
		}
		return resultado;
	}

	private void invalidarResultados() {
		regionDe.clear();
		panelGrafo.mostrarArbol = false;
		panelGrafo.arbolActual = new ArrayList<>();
	}

	private void agregarProvincia() {
		String nombre = AgregadorProvincia.agregarProvincia(this, grafo);
		if (nombre == null)
			return;

		int n = posiciones.size();
		posiciones.put(nombre, new Point(90 + (n % 8) * 130, 80 + (n / 8) * 110));

		invalidarResultados();
		refrescarVistas();
	}

	private void borrarProvincia() {
		String eliminada = BorradorProvincia.borrarProvincia(this, grafo);
		if (eliminada == null)
			return;

		posiciones.remove(eliminada);
		invalidarResultados();
		refrescarVistas();
	}

	private void agregarSimilaridad() {
		if (AgregadorSimilaridad.agregarSimilaridad(this, grafo)) {
	        invalidarResultados();
	        refrescarVistas();
	    }
	}

	private void limpiar() {
		int borrar = JOptionPane.showConfirmDialog(this, "¿Seguro que querés borrar todo el grafo actual?",
				"Confirmar limpieza", JOptionPane.YES_NO_OPTION);
		if (borrar != JOptionPane.YES_OPTION)
			return;

		grafo = new Grafo<>();
		posiciones.clear();
		invalidarResultados();
		refrescarVistas();
	}

	private void cargarCoordenadas() {

		String ruta = SelectorArchivoJSON.elegir(this);

		try {
			CoordenadasJSON datos = CoordenadasJSON.leerJSON(ruta);

			if (datos == null || datos.tamanio() == 0) {
				JOptionPane.showMessageDialog(this,
						"No se pudo leer el JSON o está vacío.\n" + "Verificá que tenga el formato:\n"
								+ "{ \"coordenadas\": [ {\"nombre\":\"...\", \"lat\":0, \"lon\":0} ] }");
				return;
			}

			coordenadasCargadas.clear();
			for (int i = 0; i < datos.tamanio(); i++) {
				Coordenada c = datos.dame(i);
				coordenadasCargadas.put(c.getNombre().trim(), c);
			}

			Set<String> existentes = new java.util.HashSet<>();
			for (String v : grafo.obtenerVertices()) {
				existentes.add(normalizar.normalizar(v));
			}
			int agregadas = 0;
			for (Coordenada c : coordenadasCargadas.values()) {
				String nombre = c.getNombre().trim();
				if (existentes.add(normalizar.normalizar(nombre))) {
					grafo.agregarVertice(nombre);
					int n = posiciones.size();
					posiciones.put(nombre, new Point(90 + (n % 5) * 130, 80 + (n / 5) * 110));
					agregadas++;
				}
			}
			if (agregadas > 0) {
				invalidarResultados();
			}

			refrescarVistas();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al cargar coordenadas:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarCoordenada() {
		if (AgregadorCoordenada.agregarCordenanda(this, grafo, coordenadasCargadas)) {
	        refrescarVistas();
	    }
	}

	private void mostrarArbolMinimo() {
		if (grafo.tamanio() == 0)
			return;

		List<Arista<String>> agm = Kruskal.ejecutar(grafo);
		panelGrafo.arbolActual = agm;
		panelGrafo.mostrarArbol = true;
		panelGrafo.repaint();
		mapa.mostrarArbolMinimo(grafo, coordenadasDelGrafo(), agm);

		String texto = " Árbol Generador Mínimo: " + agm.size() + " aristas, peso total "
				+ String.format("%.2f", Kruskal.pesoTotal(grafo));
		if (agm.size() < grafo.tamanio() - 1) {
			texto += " (el grafo no es conexo: es un bosque)";
		}
	}
	
	private void ejecutarAlgoritmo() {
	    if (grafo.tamanio() == 0) {
	        JOptionPane.showMessageDialog(this, "El grafo está vacío.");
	        return;
	    }

	    int k = 1;

	    try {
	        Map<String, Integer> resultado = Kregiones.separarEnKRegiones(grafo, k);
	        regionDe.clear();
	        regionDe.putAll(resultado);

	        panelGrafo.mostrarArbol = false;
	        panelGrafo.arbolActual = new ArrayList<>();
	        refrescarVistas();

	    } catch (IllegalArgumentException ex) {
	        JOptionPane.showMessageDialog(this, ex.getMessage());
	    }
	}
}