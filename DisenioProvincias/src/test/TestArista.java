package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import FuncionamientoAplicacion.Arista;

public class TestArista {
	private Arista<String> arista1;
	private Arista<String> arista2;
	private Arista<String> arista3;
	
	@Before
	public void inicializar() {
		arista1 = new Arista<>("Jujuy", "Salta", 8);
		arista2 = new Arista<>("Tucuman", "Catamarca", 22);
		arista3 = new Arista<>("Formosa", "Chaco", 18);
	}
	
	@Test
	public void testObtenerOrigen() {
		assertEquals("Jujuy", arista1.obtenerOrigen());
		assertEquals("Tucuman", arista2.obtenerOrigen());
		assertEquals("Formosa", arista3.obtenerOrigen());
		assertFalse("Catamarca".equals((arista1).obtenerOrigen()));;
	}
	
	@Test
	public void testObtenerDestino() {
		assertEquals("Salta", arista1.obtenerDestino());
		assertEquals("Catamarca", arista2.obtenerDestino());
		assertFalse("Jujuy".equals(arista3.obtenerDestino()));
	}
	
	@Test
	public void testVacio() {
		assertFalse("".equals(arista1.obtenerOrigen()));
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void testOrigenNull() {
        new Arista<>(null, "Buenos Aires", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDestinoNull() {
        new Arista<>("Buenos Aires", null, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAmbosNull() {
        new Arista<>(null, null, 50);
    }
}
