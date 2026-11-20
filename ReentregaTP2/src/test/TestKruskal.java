package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import FuncionamientoApp.Arista;
import FuncionamientoApp.Grafo;
import FuncionamientoApp.Kruskal;

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

        List<Arista<String>> agm = Kruskal.ejecutar(grafo);
        assertTrue(agm.isEmpty());
        assertEquals(0.0, Kruskal.pesoTotal(grafo), 0.001);
    }

    @Test
    public void testGrafoSinAristas() {
        grafo.agregarVertice("Q");
        grafo.agregarVertice("W");
        grafo.agregarVertice("E");

        List<Arista<String>> agm = Kruskal.ejecutar(grafo);
        // No se pueden conectar → bosque con 0 aristas
        assertTrue(agm.isEmpty());
    }
    
    @Test
	public void dosVerticesUnaArista() {
    	grafo.agregarVertice("Q");
        grafo.agregarVertice("W");
		grafo.agregarArista("Q", "W", 5);

		List<Arista<String>> agm = Kruskal.ejecutar(grafo);

		assertEquals(1, agm.size());
		assertEquals(5, Kruskal.pesoTotal(grafo), 0.0001);
	}
    
    @Test
	public void verticeAisladoNoConexo() {
    	grafo.agregarVertice("Q");
        grafo.agregarVertice("W");
        
		grafo.agregarArista("Q", "W", 1);
		
		grafo.agregarVertice("E");

		assertEquals(1, Kruskal.ejecutar(grafo).size());
	}
    
    @Test
	public void grafoSinAristasNoEsConexo() {
		grafo.agregarVertice("Q");
		grafo.agregarVertice("W");

		assertTrue(Kruskal.ejecutar(grafo).isEmpty());
	}
    
    /*public void grafoEsConexo() {
    	grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
       
		grafo.agregarArista("A", "B", 1);
		grafo.agregarArista("B", "C", 1);

		assertTrue(Kruskal.esConexo(grafo));
	}*/

	@Test
	public void testDosComponentesConexa() {
		grafo.agregarVertice("Q");
        grafo.agregarVertice("W");
        grafo.agregarVertice("H");
        grafo.agregarVertice("R");
        grafo.agregarVertice("T");
        
		grafo.agregarArista("Q", "W", 1);
		grafo.agregarArista("Q", "H", 1);
		grafo.agregarArista("R", "T", 2);

		List<Arista<String>> agm = Kruskal.ejecutar(grafo);
		System.out.print(agm.size());
		assertEquals(3, agm.size()); // 4 vértices, 2 componentes → n - 2 aristas
	}
	
	@Test
	public void borrarElVerticeQueUniaDosPartesDejaElGrafoNoConexo() {
		grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
       
		grafo.agregarArista("A", "B", 1);
		grafo.agregarArista("B", "C", 1);

		grafo.eliminarVertice("B");

		assertTrue(Kruskal.ejecutar(grafo).isEmpty());
	}
}
