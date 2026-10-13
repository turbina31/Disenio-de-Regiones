package FuncionamientoAplicacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilidadesGrafo {
	public static <T> List<Arista<T>> obtenerAristasUnicas(Grafo<T> grafo) {
        List<Arista<T>> lista = new ArrayList<>();
        Set<String> vistas = new HashSet<>();

        for (T v : grafo.obtenerVertices()) {
            for (Arista<T> a : grafo.obtenerAdyacentes(v)) {
                String clave = generarClave(a.obtenerOrigen(), a.obtenerDestino());
                if (vistas.add(clave)) {
                    lista.add(a);
                }
            }
        }
        return lista;
    }

    public static <T> int contarAristasUnicas(Grafo<T> grafo) {
        return obtenerAristasUnicas(grafo).size();
    }

    private static <T> String generarClave(T origen, T destino) {
        String o = origen.toString();
        String d = destino.toString();
        return o.compareTo(d) < 0 ? o + "|" + d : d + "|" + o;
    }
}
