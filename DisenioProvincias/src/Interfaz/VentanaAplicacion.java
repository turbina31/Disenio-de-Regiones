package Interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.text.Normalizer;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import FuncionamientoAplicacion.Arista;
import FuncionamientoAplicacion.Coordenada;
import FuncionamientoAplicacion.CoordenadasJSON;
import FuncionamientoAplicacion.Grafo;
import FuncionamientoAplicacion.Kruskal;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class VentanaAplicacion extends JFrame {

	private static final long serialVersionUID = 1L;

	private Grafo<String> grafo = new Grafo<>();
	private final Map<String, Point> posiciones = new HashMap<>();
	private final Map<String, Integer> regionDe = new HashMap<>();
	private final Map<String, Coordenada> coordenadasCargadas = new HashMap<>();

	private EstiloGrafo panelGrafo;
	//private JTextField txtK;
	//private JLabel lblStatus;
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

		// ==================== BARRA SUPERIOR ====================
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

		/*barra.add(new JLabel("k ="));
		txtK = new JTextField("4", 3);
		barra.add(txtK);*/

		/*JButton btnEjecutar = EstiloBoton.crearBoton("Ejecutar algoritmo", true);
		btnEjecutar.addActionListener(e -> ejecutarAlgoritmo());
		barra.add(btnEjecutar);*/

		JButton btnArbol = EstiloBoton.crearBoton("Ver Árbol Mínimo", true);
		btnArbol.addActionListener(e -> mostrarArbolMinimo());
		barra.add(btnArbol);

		// ==================== PANEL CENTRAL ====================
		mapa = new Mapa();
		mapa.setBorder(new LineBorder(new Color(139, 92, 246)));
		panelGrafo = new EstiloGrafo(grafo, posiciones, regionDe);
		panelGrafo.setBorder(new LineBorder(new Color(139, 92, 246)));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapa, panelGrafo);
		split.setDividerLocation(600);
		split.setResizeWeight(0.5);
		contentPane.add(split, BorderLayout.CENTER);

	}

	// ==================== AUXILIARES ====================

	private void refrescarVistas() {
		panelGrafo.actualizarReferencias(grafo, posiciones, regionDe);
		panelGrafo.repaint();
		mapa.mostrarProvincias(grafo, coordenadasDelGrafo());
	}

	/** Minúsculas, sin tildes y sin espacios sobrantes: "Entre Ríos " == "entre rios". */
	private static String normalizar(String s) {
		String t = Normalizer.normalize(s.trim(), Normalizer.Form.NFD);
		return t.replaceAll("\\p{M}", "").replaceAll("\\s+", " ").toLowerCase();
	}

	/** Asocia cada provincia del grafo con su coordenada, comparando nombres normalizados. */
	private Map<String, Coordenada> coordenadasDelGrafo() {
		Map<String, Coordenada> porNombre = new HashMap<>();
		for (Coordenada c : coordenadasCargadas.values()) {
			porNombre.put(normalizar(c.getNombre()), c);
		}
		Map<String, Coordenada> resultado = new HashMap<>();
		for (String provincia : grafo.obtenerVertices()) {
			Coordenada c = porNombre.get(normalizar(provincia));
			if (c != null) {
				resultado.put(provincia, c);
			}
		}
		return resultado;
	}

	/** Texto con las provincias del grafo que no tienen coordenada. */
	private String provinciasSinCoordenada() {
		Map<String, Coordenada> coordenada = coordenadasDelGrafo();
		List<String> faltan = new ArrayList<>();
		for (String vertice : grafo.obtenerVertices()) {
			if (!coordenada.containsKey(vertice)) {
				faltan.add(vertice);
			}
		}
		return faltan.isEmpty() ? "" : " | Sin coordenada: " + String.join(", ", faltan);
	}

	private void invalidarResultados() {
		regionDe.clear();
		panelGrafo.mostrarArbol = false;
		panelGrafo.arbolActual = new ArrayList<>();
	}

	// ==================== PROVINCIAS ====================

	private void agregarProvincia() {
		String nombre = AgregadorProvincia.agregarProvincia(this, grafo);
		if (nombre == null)
			return;

		int n = posiciones.size();
		posiciones.put(nombre, new Point(90 + (n % 8) * 130, 80 + (n / 8) * 110));

		invalidarResultados();
		refrescarVistas();
		//lblStatus.setText(" Provincia agregada: " + nombre);
	}

	private void borrarProvincia() {
		String eliminada = BorradorProvincia.borrarProvincia(this, grafo);
		if (eliminada == null)
			return;

		posiciones.remove(eliminada);
		invalidarResultados();
		refrescarVistas();
		//lblStatus.setText(" Provincia eliminada: " + eliminada);
	}

	private void agregarSimilaridad() {
		Set<String> vertise = grafo.obtenerVertices();
		if (vertise.size() < 2) {
			JOptionPane.showMessageDialog(this, "Necesitás al menos 2 provincias.");
			return;
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

		int opcion = JOptionPane.showConfirmDialog(this, panel, "Agregar similaridad", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (opcion != JOptionPane.OK_OPTION)
			return;

		String u = (String) provincia1.getSelectedItem();
		String v = (String) provincia2.getSelectedItem();
		if (u.equals(v)) {
			JOptionPane.showMessageDialog(this, "Las provincias deben ser distintas.");
			return;
		}

		double sim;
		try {
			sim = Double.parseDouble(txtSim.getText().trim().replace(',', '.'));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Valor de similaridad inválido.");
			return;
		}

		grafo.agregarArista(u, v, sim); // si ya existía, actualiza el peso
		invalidarResultados();
		refrescarVistas();
		//lblStatus.setText(String.format(" Similaridad: %s – %s = %.2f", u, v, sim));
	}

	private void limpiar() {
		int borrar = JOptionPane.showConfirmDialog(this, "¿Seguro que querés borrar todo el grafo actual?",
				"Confirmar limpieza", JOptionPane.YES_NO_OPTION);
		if (borrar != JOptionPane.YES_OPTION)
			return;

		grafo = new Grafo<>();
		posiciones.clear();
		invalidarResultados();
		refrescarVistas(); // actualiza la referencia del panel y redibuja el mapa
		//lblStatus.setText(" Grafo limpiado.");
	}

	// ==================== COORDENADAS ====================

	private void cargarCoordenadas() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Seleccionar archivo de coordenadas (JSON)");
		fc.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;

		String ruta = fc.getSelectedFile().getAbsolutePath();

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

			// Agregar al grafo las provincias del JSON que todavía no existen
			Set<String> existentes = new java.util.HashSet<>();
			for (String v : grafo.obtenerVertices()) {
				existentes.add(normalizar(v));
			}
			int agregadas = 0;
			for (Coordenada c : coordenadasCargadas.values()) {
				String nombre = c.getNombre().trim();
				if (existentes.add(normalizar(nombre))) {
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
			//lblStatus.setText(" Coordenadas cargadas: " + datos.tamanio() + " provincias, " + agregadas
				//	+ " agregadas al grafo" + provinciasSinCoordenada());

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al cargar coordenadas:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarCoordenada() {
		Set<String> vertise = grafo.obtenerVertices();
		if (vertise.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Primero agregá provincias.");
			return;
		}

		String[] nombres = vertise.toArray(new String[0]);
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

		int opcion = JOptionPane.showConfirmDialog(this, panel, "Agregar / modificar coordenada",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (opcion != JOptionPane.OK_OPTION)
			return;

		String provincia = (String) combo.getSelectedItem();
		double latitud, longitud;
		try {
			latitud = Double.parseDouble(txtLat.getText().trim().replace(',', '.'));
			longitud = Double.parseDouble(txtLon.getText().trim().replace(',', '.'));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Latitud o longitud inválida.");
			return;
		}
		if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
			JOptionPane.showMessageDialog(this, "Latitud (-90..90) o longitud (-180..180) fuera de rango.");
			return;
		}

		coordenadasCargadas.put(provincia, new Coordenada(provincia, latitud, longitud));
		refrescarVistas();
		//lblStatus.setText(String.format(" Coordenada agregada: %s → (%.4f, %.4f)", provincia, latitud, longitud));
	}

	// ==================== ALGORITMO ====================

	/*private void ejecutarAlgoritmo() {
		int n = grafo.tamanio();
		if (n == 0) {
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
		if (k < 1 || k > n) {
			JOptionPane.showMessageDialog(this, "k debe estar entre 1 y " + n);
			return;
		}

		// 1. Árbol (o bosque) generador mínimo
		List<Arista<String>> mst = new ArrayList<>(Kruskal.ejecutar(grafo));

		// Si el grafo no es conexo, ya hay (n - |mst|) componentes de entrada
		int componentesIniciales = n - mst.size();
		if (k < componentesIniciales) {
			JOptionPane.showMessageDialog(this,
					"El grafo no es conexo: ya tiene " + componentesIniciales + " componentes.\n"
							+ "No se puede formar menos regiones que eso. Se usarán " + componentesIniciales + ".");
			k = componentesIniciales;
		}

		// 2. Quitar las (k - componentesIniciales) aristas más pesadas
		mst.sort((a, b) -> Double.compare(b.obtenerPeso(), a.obtenerPeso()));
		int aQuitar = k - componentesIniciales;
		List<Arista<String>> restantes = new ArrayList<>(mst.subList(aQuitar, mst.size()));

		// 3. Componentes con UnionFind
		List<String> vertices = new ArrayList<>(grafo.obtenerVertices());
		Map<String, Integer> indice = new HashMap<>();
		for (int i = 0; i < vertices.size(); i++) {
			indice.put(vertices.get(i), i);
		}

		UnionFind uf = new UnionFind(vertices.size());
		for (Arista<String> a : restantes) {
			uf.union(indice.get(a.obtenerOrigen()), indice.get(a.obtenerDestino()));
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
		panelGrafo.actualizarReferencias(grafo, posiciones, regionDe);
		panelGrafo.repaint();
		mapa.mostrarGrafo(grafo, coordenadasDelGrafo(), restantes, true);

		lblStatus.setText(String.format(" Algoritmo ejecutado → %d regiones formadas (k = %d)", idRegion, k));
	}*/

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
		//lblStatus.setText(texto);
	}
}