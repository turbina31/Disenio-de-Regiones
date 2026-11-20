package FuncionamientoApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Grafo simple, no dirigido y ponderado (sin loops ni aristas paralelas).
 *
 * Las relaciones entre provincias (límites, similaridades) son simétricas, por lo
 * que cada arista {a, b} se guarda en las listas de adyacencia de ambos extremos
 * y siempre se agrega, actualiza y elimina en los dos a la vez.
 */
public class Grafo<T> {

    private final Map<T, List<Arista<T>>> adyacencia = new HashMap<>();

    // ==================== Vértices ====================

    public void agregarVertice(T vertice) {
        verificarNoNulo(vertice);
        adyacencia.putIfAbsent(vertice, new ArrayList<>());
    }

    public void eliminarVertice(T vertice) {
        verificarVertice(vertice);
        adyacencia.remove(vertice);
        // Quitar las aristas que apuntaban al vértice eliminado
        for (List<Arista<T>> lista : adyacencia.values()) {
            lista.removeIf(a -> a.obtenerDestino().equals(vertice));
        }
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

    public void limpiar() {
        adyacencia.clear();
    }

    // ==================== Aristas ====================

    /**
     * Agrega la arista {origen, destino} con el peso indicado. La similaridad siempre se
     * define de forma explícita: no hay un peso por defecto.
     *
     * Ambos vértices deben existir; este método no los crea. Así, un error de tipeo falla
     * en lugar de generar una provincia fantasma. Si la arista ya existía, reemplaza su peso.
     *
     * @throws IllegalArgumentException si algún extremo es null o no pertenece al grafo,
     *                                  o si origen y destino son el mismo vértice
     */
    public void agregarArista(T origen, T destino, double peso) {
        verificarVertice(origen);
        verificarVertice(destino);
        verificarDistintos(origen, destino);

        quitarAdyacencia(origen, destino);
        quitarAdyacencia(destino, origen);
        adyacencia.get(origen).add(new Arista<>(origen, destino, peso));
        adyacencia.get(destino).add(new Arista<>(destino, origen, peso));
    }

    public void eliminarArista(T origen, T destino) {
        verificarVertice(origen);
        verificarVertice(destino);
        verificarDistintos(origen, destino);

        quitarAdyacencia(origen, destino);
        quitarAdyacencia(destino, origen);
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
        for (Arista<T> a : adyacencia.get(vertice)) {
            resultado.add(a.obtenerDestino());
        }
        return Collections.unmodifiableSet(resultado);
    }

    // ==================== Auxiliares ====================

    /** Quita de la lista de {@code desde} la entrada que apunta a {@code hacia}. */
    private void quitarAdyacencia(T desde, T hacia) {
        adyacencia.get(desde).removeIf(a -> a.obtenerDestino().equals(hacia));
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