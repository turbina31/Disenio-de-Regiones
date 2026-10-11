package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import FuncionamientoAplicacion.Arista;
import FuncionamientoAplicacion.Grafo;
import FuncionamientoAplicacion.Kruskal;

public class TestKruskal {

	private Grafo<String> grafo;

    @Before
    public void inicializar() {
        grafo = new Grafo<>();

        // Grafo pequeño y conocido
        //      2
        //  A ----- B
        //  |       |
        // 3|       |1
        //  |       |
        //  C ----- D
        //      4
    }

    @Test
    public void testCantidadAristas() {
    	grafo.agregarArista("A", "B", 2);
        grafo.agregarArista("A", "C", 3);
        grafo.agregarArista("B", "D", 1);
        grafo.agregarArista("C", "D", 4);
        
        List<Arista<String>> agm = Kruskal.ejecutar(grafo);
        // Un MST de 4 vértices tiene exactamente 3 aristas
        assertEquals(3, agm.size());
    }

    @Test
    public void testPesoTotal() {
    	grafo.agregarArista("A", "B", 2);
        grafo.agregarArista("A", "C", 3);
        grafo.agregarArista("B", "D", 1);
        grafo.agregarArista("C", "D", 4);
        
        // Las aristas del MST deben ser: B-D (1), A-B (2), A-C (3) → peso total 6
        double peso = Kruskal.pesoTotal(grafo);
        assertEquals(6, peso, 0.001);
    }

    @Test
    public void testGrafoConUnSoloVertice() {
        grafo.agregarVertice("Q");

        List<Arista<String>> mst = Kruskal.ejecutar(grafo);
        assertTrue(mst.isEmpty());
        assertEquals(0.0, Kruskal.pesoTotal(grafo), 0.001);
    }

    @Test
    public void testGrafoSinAristas() {
        grafo.agregarVertice("Q");
        grafo.agregarVertice("W");
        grafo.agregarVertice("E");

        List<Arista<String>> mst = Kruskal.ejecutar(grafo);
        // No se pueden conectar → bosque con 0 aristas
        assertTrue(mst.isEmpty());
    }
    
    @Test
	public void dosVerticesUnaArista() {
		grafo.agregarArista("Q", "W", 5);

		List<Arista<String>> mst = Kruskal.ejecutar(grafo);

		assertEquals(1, mst.size());
		assertEquals(5, Kruskal.pesoTotal(grafo), 0.0001);
	}
    
    @Test
	public void verticeAisladoNoConexo() {
		grafo.agregarArista("Q", "W", 1);
		grafo.agregarVertice("E");

		assertFalse(Kruskal.esConexo(grafo));
		assertEquals(1, Kruskal.ejecutar(grafo).size());
	}
    
    @Test
	public void grafoSinAristasNoEsConexo() {
		grafo.agregarVertice("Q");
		grafo.agregarVertice("W");

		assertTrue(Kruskal.ejecutar(grafo).isEmpty());
		assertFalse(Kruskal.esConexo(grafo));
	}
    
    public void grafoEsConexo() {
		grafo.agregarArista("A", "B", 1);
		grafo.agregarArista("B", "C", 1);

		assertTrue(Kruskal.esConexo(grafo));
	}

	@Test
	public void testDosComponentesConexa() {
		grafo.agregarArista("Q", "W", 1);
		grafo.agregarArista("Q", "H", 1);
		grafo.agregarArista("R", "T", 2);

		List<Arista<String>> mst = Kruskal.ejecutar(grafo);
		System.out.print(mst.size());
		assertEquals(3, mst.size()); // 4 vértices, 2 componentes → n - 2 aristas
	}
	
	@Test
	public void borrarElVerticeQueUniaDosPartesDejaElGrafoNoConexo() {
		grafo.agregarArista("A", "B", 1);
		grafo.agregarArista("B", "C", 1);

		assertTrue(Kruskal.esConexo(grafo));
		grafo.eliminarVertice("B");

		assertFalse(Kruskal.esConexo(grafo));
		assertTrue(Kruskal.ejecutar(grafo).isEmpty());
	}
}
