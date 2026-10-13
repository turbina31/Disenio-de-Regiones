package FuncionamientoAplicacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo<T> {

    private Map<T, List<Arista<T>>> adyacencia;
    private final boolean dirigido;

    public Grafo() {
        this(false); // por defecto no dirigido
    }

    public Grafo(boolean dirigido) {
        this.adyacencia = new HashMap<>();
        this.dirigido = dirigido;
    }

    // ==================== Vértices ====================

    public void agregarVertice(T vertice) {
        verificarNoNulo(vertice);
        adyacencia.putIfAbsent(vertice, new ArrayList<>());
    }

    public void eliminarVertice(T vertice) {
        verificarVertice(vertice);
        adyacencia.remove(vertice);
        for (List<Arista<T>> elemento : adyacencia.values()) {
			elemento.removeIf(a -> a.obtenerDestino().equals(vertice));
		}
        adyacencia.remove(vertice);
    }

    public boolean existeVertice(T vertice) {
        return adyacencia.containsKey(vertice);
    }

    public int tamanio() {
        return adyacencia.size();
    }

    public Set<T> obtenerVertices() {
        return Collections.unmodifiableSet(adyacencia.keySet());
    }

    // ==================== Aristas ====================

    public void agregarArista(T origen, T destino, double peso) {
        verificarNoNulo(origen);
        verificarNoNulo(destino);
        verificarDistintos(origen, destino);

        // Primero nos aseguramos de que existan los vértices
        agregarVertice(origen);
        agregarVertice(destino);

        // Evitar aristas duplicadas
        if (existeArista(origen, destino)) {
            return;
        }

        adyacencia.get(origen).add(new Arista<>(origen, destino, peso));

        if (!dirigido) {
            adyacencia.get(destino).add(new Arista<>(destino, origen, peso));
        }
    }

    public void agregarArista(T origen, T destino) {
        agregarArista(origen, destino, 1.0);
    }

    public void eliminarArista(T origen, T destino) {
        verificarVertice(origen);
        verificarVertice(destino);
        verificarDistintos(origen, destino);

        quitarDirigida(origen, destino);
        if (!dirigido) {
            quitarDirigida(destino, origen);
        }
        
        adyacencia.get(origen).remove(destino);
        adyacencia.get(destino).remove(origen);
    }

    public boolean existeArista(T origen, T destino) {
        if (!existeVertice(origen) || !existeVertice(destino)) {
            return false;
        }
        for (Arista<T> a : adyacencia.get(origen)) {
            if (a.obtenerDestino().equals(destino)) {
                return true;
            }
        }
        return false;
    }

    public List<Arista<T>> obtenerAdyacentes(T vertice) {
    	List<Arista<T>> lista = adyacencia.get(vertice);
        if (lista == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(lista);
    }

    public Set<T> vecinos(T vertice) {
        verificarVertice(vertice);
        Set<T> resultado = new HashSet<>();
        for (Arista<T> a : adyacencia.get(vertice)) if (!vertice.equals(a.obtenerDestino()))
		{
        	if (this.existeArista(a.obtenerOrigen(), a.obtenerDestino())) {
				resultado.add(a.obtenerDestino());
			}
        }
        return Collections.unmodifiableSet(resultado);
    }

    public void limpiar() {
    	adyacencia.clear();
    }
    
    // ==================== Validaciones ====================

    private void quitarDirigida(T origen, T destino) {
        adyacencia.get(origen).removeIf(a -> a.obtenerDestino().equals(destino));
    }
    
    private void verificarNoNulo(T v) {
        if (v == null) {
            throw new IllegalArgumentException("El vértice no puede ser null");
        }
    }

    private void verificarVertice(T vertice) {
        verificarNoNulo(vertice);
        if (!existeVertice(vertice)) {
            throw new IllegalArgumentException("El vértice no pertenece al grafo: " + vertice);
        }
    }

    private void verificarDistintos(T v1, T v2) {
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("No se permiten loops: (" + v1 + ", " + v2 + ")");
        }
    }
}
