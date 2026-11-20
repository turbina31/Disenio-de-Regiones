package FuncionamientoApp;

import java.text.Normalizer;

public class NormalizadorNombres {
	public NormalizadorNombres() {
    }

    /** Minúsculas, sin tildes y con los espacios sobrantes colapsados. */
    public static String normalizar(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser null");
        }
        String sinTildes = Normalizer.normalize(nombre.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.replaceAll("\\s+", " ").toLowerCase();
    }
}
