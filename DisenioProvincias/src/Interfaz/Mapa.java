package Interfaz;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import FuncionamientoAplicacion.Arista;
import FuncionamientoAplicacion.Coordenada;
import FuncionamientoAplicacion.Grafo;
import FuncionamientoAplicacion.UtilidadesGrafo;

public class Mapa extends JPanel {

	private final JMapViewer vistaMapa;
	private final Map<String, MapMarkerDot> marcadores = new HashMap<>();

	public Mapa() {
		setLayout(new BorderLayout());

		vistaMapa = new JMapViewer();
		vistaMapa.setTileSource(new OsmTileSource.Mapnik());
		vistaMapa.setDisplayPosition(new Coordinate(-40.0, -64.0), 4); // Argentina
		vistaMapa.setZoomControlsVisible(true);

		add(vistaMapa, BorderLayout.CENTER);
	}

	/**
	 * Dibuja provincias (marcadores) y aristas (líneas) del grafo.
	 */
	public void mostrarProvincias(Grafo<String> grafo, Map<String, Coordenada> coordenadas) {
		mostrarGrafo(grafo, coordenadas, null, false);
	}

	/**
	 * Dibuja el grafo completo o solo el MST.
	 *
	 * @param grafo       grafo original
	 * @param coordenadas mapa nombre → Coordenada
	 * @param aristasMST  si no es null, dibuja solo esas aristas (MST)
	 * @param soloMST     true = solo aristas del MST
	 */
	public void mostrarGrafo(Grafo<String> grafo, Map<String, Coordenada> coordenadas, List<Arista<String>> aristas,
			boolean soloAGM) {

// Limpiar
		vistaMapa.removeAllMapMarkers();
		vistaMapa.removeAllMapPolygons();
		marcadores.clear();

		if (grafo == null || coordenadas == null || coordenadas.isEmpty()) {
			vistaMapa.repaint();
			return;
		}

// ---------- 1. Marcadores ----------
		for (String provincia : grafo.obtenerVertices()) {
			Coordenada c = coordenadas.get(provincia);
			if (c == null)
				continue;

			Coordinate coordenada = new Coordinate(c.getLatitud(), c.getLongitud());
			MapMarkerDot marker = new MapMarkerDot(provincia, coordenada);
			vistaMapa.addMapMarker(marker);
			marcadores.put(provincia, marker);
		}

// ---------- 2. Aristas ----------
		List<Arista<String>> aristasADibujar;

		if (soloAGM && aristas != null) {
			aristasADibujar = aristas;
		} else {
			aristasADibujar = UtilidadesGrafo.obtenerAristasUnicas(grafo);
		}

		for (Arista<String> a : aristasADibujar) {
// Probar ambos nombres de método por si tu Arista usa get o obtener
			String origen = a.obtenerOrigen(); // si falla, usá a.getOrigen()
			String destino = a.obtenerDestino(); // si falla, usá a.getDestino()

			Coordenada c1 = coordenadas.get(origen);
			Coordenada c2 = coordenadas.get(destino);

			if (c1 == null || c2 == null) {
				System.out.println("Sin coordenada para arista: " + origen + " - " + destino);
				continue;
			}

			Coordinate p1 = new Coordinate(c1.getLatitud(), c1.getLongitud());
			Coordinate p2 = new Coordinate(c2.getLatitud(), c2.getLongitud());

// Truco: agregar un punto intermedio muy cercano para que el polígono se vea como línea
			List<Coordinate> puntos = new ArrayList<>();
			puntos.add(p1);
			puntos.add(p2);
			puntos.add(p1); // cerrar el polígono (importante)

			MapPolygonImpl linea = new MapPolygonImpl(puntos);
			vistaMapa.addMapPolygon(linea);
		}

		vistaMapa.repaint();
	}

	/**
	 * Muestra solo el Árbol Generador Mínimo sobre el mapa.
	 */
	public void mostrarArbolMinimo(Grafo<String> grafo, Map<String, Coordenada> coordenadas, List<Arista<String>> agm) {
		mostrarGrafo(grafo, coordenadas, agm, true);
	}

	public JMapViewer getMapViewer() {
		return vistaMapa;
	}
}