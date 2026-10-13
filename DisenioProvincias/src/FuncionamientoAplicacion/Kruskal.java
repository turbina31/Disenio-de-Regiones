package FuncionamientoAplicacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {

    public static <T> List<Arista<T>> ejecutar(Grafo<T> grafo) {
        List<Arista<T>> arbol = new ArrayList<>();

        List<T> vertices = new ArrayList<>(grafo.obtenerVertices());
        int n = vertices.size();
        if (n < 2) {
            return arbol;
        }

        Map<T, Integer> indice = new HashMap<>();
        for (int k = 0; k < n; k++) {
            indice.put(vertices.get(k), k);
        }

        List<Arista<T>> aristas = new ArrayList<>(UtilidadesGrafo.obtenerAristasUnicas(grafo));
        Collections.sort(aristas);

        UnionFind uf = new UnionFind(n);

        for (Arista<T> e : aristas) {
            if (arbol.size() == n - 1) {
                break; // árbol completo
            }
            int u = indice.get(e.obtenerOrigen());
            int v = indice.get(e.obtenerDestino());

            if (uf.union(u, v)) { // true = no formaba ciclo
                arbol.add(e);
            }
        }
        return arbol;
    }

    public static <T> boolean esConexo(Grafo<T> grafo) {
        int n = grafo.tamanio();
        return n <= 1 || ejecutar(grafo).size() == n - 1;
    }

    public static <T> double pesoTotal(Grafo<T> grafo) {
        double total = 0;
        for (Arista<T> a : ejecutar(grafo)) {
            total += a.obtenerPeso();
        }
        return total;
    }
}
