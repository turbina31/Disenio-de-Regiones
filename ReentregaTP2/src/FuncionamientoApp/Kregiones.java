package FuncionamientoApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kregiones<T> {
	
	public static <T> Map<T, Integer> separarEnKRegiones(Grafo<T> grafo, int k) {
        if (grafo == null || grafo.tamanio() == 0) {
            throw new IllegalArgumentException("El grafo está vacío");
        }
        if (k < 1 || k > grafo.tamanio()) {
            throw new IllegalArgumentException(
                    "k debe estar entre 1 y " + grafo.tamanio());
        }

        List<Arista<T>> mst = Kruskal.ejecutar(grafo);
        List<Arista<T>> restantes = eliminarAristasMasPesadas(mst, k);
        return asignarRegiones(grafo, restantes);
    }

    /**
     * Ordena las aristas del MST de mayor a menor peso
     * y elimina las k-1 más pesadas.
     * Devuelve las aristas que quedan (bosque de regiones).
     */
    public static <T> List<Arista<T>> eliminarAristasMasPesadas(List<Arista<T>> agm, int k) {

        List<Arista<T>> copia = new ArrayList<>(agm);
        copia.sort((a, b) -> Double.compare(b.obtenerPeso(), a.obtenerPeso()));

        List<Arista<T>> restantes = new ArrayList<>();
        for (int i = k - 1; i < copia.size(); i++) {
            restantes.add(copia.get(i));
        }
        return restantes;
    }

    /**
     * Aplica Union-Find sobre las aristas restantes
     * y asigna un id de región a cada vértice.
     */
    public static <T> Map<T, Integer> asignarRegiones(Grafo<T> grafo, List<Arista<T>> aristasRestantes) {

        List<T> vertices = new ArrayList<>(grafo.obtenerVertices());
        Map<T, Integer> indice = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            indice.put(vertices.get(i), i);
        }

        UnionFind uf = new UnionFind(vertices.size());
        for (Arista<T> a : aristasRestantes) {
            Integer u = indice.get(a.obtenerOrigen());
            Integer v = indice.get(a.obtenerDestino());
            if (u != null && v != null) {
                uf.union(u, v);
            }
        }

        Map<T, Integer> regionDe = new HashMap<>();
        Map<Integer, Integer> raizARegion = new HashMap<>();
        int idRegion = 0;

        for (T v : vertices) {
            int raiz = uf.find(indice.get(v));
            if (!raizARegion.containsKey(raiz)) {
                raizARegion.put(raiz, idRegion++);
            }
            regionDe.put(v, raizARegion.get(raiz));
        }
        return regionDe;
    }

    /** Cantidad de regiones distintas en el mapa. */
    public static int cantidadRegiones(Map<?, Integer> regionDe) {
        return (int) regionDe.values().stream().distinct().count();
    }
    
}
