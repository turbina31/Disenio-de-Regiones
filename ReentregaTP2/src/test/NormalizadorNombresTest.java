package test;

import static org.junit.Assert.*;

import org.junit.Test;

import FuncionamientoApp.NormalizadorNombres;

public class NormalizadorNombresTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void nuloLanzaExcepcion() {
		NormalizadorNombres.normalizar(null);
	}

	@Test
	public void pasaAMinusculas() {
		assertEquals("salta", NormalizadorNombres.normalizar("SALTA"));
	}

	@Test
	public void quitaTildes() {
		assertEquals("cordoba", NormalizadorNombres.normalizar("Córdoba"));
		assertEquals("tucuman", NormalizadorNombres.normalizar("Tucumán"));
		assertEquals("entre rios", NormalizadorNombres.normalizar("Entre Ríos"));
	}

	@Test
	public void quitaEspaciosDeLosExtremos() {
		assertEquals("chaco", NormalizadorNombres.normalizar("  Chaco \t"));
	}

	@Test
	public void colapsaEspaciosInternos() {
		assertEquals("santa cruz", NormalizadorNombres.normalizar("Santa     Cruz"));
	}

	@Test
	public void nombresEquivalentesDanLoMismo() {
		assertEquals(NormalizadorNombres.normalizar("Entre Ríos "), NormalizadorNombres.normalizar("entre   rios"));
	}

	@Test
	public void nombresDistintosSiguenSiendoDistintos() {
		assertEquals(false, NormalizadorNombres.normalizar("San Juan").equals(NormalizadorNombres.normalizar("San Luis")));
	}

}
