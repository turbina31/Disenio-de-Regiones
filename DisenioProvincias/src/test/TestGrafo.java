package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import FuncionamientoAplicacion.Grafo;

public class TestGrafo {

	private Grafo<String> grafo;
	
	@Before
	public void inicializar() {
		grafo = new Grafo<>();
		grafo.agregarArista("Jujuy", "Salta", 8);
		grafo.agregarArista("Salta", "Formosa", 22);
		grafo.agregarArista("Salta", "Chaco", 18);
		grafo.agregarArista("Salta", "Santiago del Estero", 14);
		grafo.agregarArista("Salta", "Tucuman", 10);
		grafo.agregarArista("Formosa", "Chaco", 9);
		
	}
	
	// ---------- Tests de validación ----------

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarAristaConNull() {
        grafo.agregarArista(null, "Salta", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarAristaAmbosNull() {
        grafo.agregarArista(null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarLoop() {
        // No se permiten aristas de un vértice a sí mismo
        grafo.agregarArista("Salta", "Salta", 10);
    }
    
 // ---------- Tests de funcionalidad ----------

    @Test
    public void testCantidadVertices() {
        // Jujuy, Salta, Formosa, Chaco, Santiago del Estero, Tucuman,
        // Buenos Aires, Cordoba  →  8 vértices
        assertEquals(6, grafo.tamanio());
    }

    @Test
    public void testExisteVertice() {
        assertTrue(grafo.existeVertice("Salta"));
        assertFalse(grafo.existeVertice("Buenos Aires"));
        assertFalse(grafo.existeVertice("Misiones"));
    }

    @Test
    public void testExisteArista() {
        assertTrue(grafo.existeArista("Jujuy", "Salta"));
        assertTrue(grafo.existeArista("Salta", "Jujuy")); // no dirigido
        assertFalse(grafo.existeArista("Buenos Aires", "Cordoba"));
    }

    @Test
    public void testAgregarVerticeDuplicado() {
        int antes = grafo.tamanio();
        grafo.agregarVertice("Salta"); // ya existe, no debe aumentar
        System.out.println(grafo.tamanio());
        assertEquals(antes, grafo.tamanio());
    }

    @Test
    public void testAgregarVertice() {
        int antes = grafo.tamanio();
        grafo.agregarVertice("Córdoba"); // ya existe, no debe aumentar
        System.out.println("testAgregarVertice(): tamanio actual: " + grafo.tamanio());
        assertEquals(7, grafo.tamanio());
    }
    
    @Test
    public void testEliminarVertice() {
        grafo.eliminarVertice("Formosa");
        assertFalse(grafo.existeVertice("Formosa"));
        assertFalse(grafo.existeArista("Salta", "Formosa"));
        assertEquals(5, grafo.tamanio());
    }
    
    @Test
    public void testVaciarGrafo() {
    	grafo.limpiar();
    	assertEquals(0, grafo.tamanio());
    	assertFalse(grafo.existeVertice("Salta"));
    }

    @Test
    public void testVecinos() {
    	// Salta está conectada con Jujuy, Formosa y Chaco
        assertTrue(grafo.vecinos("Salta").contains("Jujuy"));
        assertTrue(grafo.vecinos("Salta").contains("Formosa"));
        assertTrue(grafo.vecinos("Salta").contains("Chaco"));

        // No está conectada con Buenos Aires
        assertFalse(grafo.vecinos("Salta").contains("Buenos Aires"));
    }

}
