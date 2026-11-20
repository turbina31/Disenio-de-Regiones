package FuncionamientoApp;

public class Coordenada {
	private String nombre;
	private double lat;
	private double lon;
	
	public Coordenada() {}
	
	public Coordenada(String nombre, double latitud, double longitud) {
		this.nombre = nombre;
		this.lat = latitud;
		this.lon = longitud;
	}

	public String getNombre() {
		return nombre;
	}

	public double getLatitud() {
		return lat;
	}

	public double getLongitud() {
		return lon;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setLatitud(double latitud) {
		this.lat = latitud;
	}

	public void setLongitud(double longitud) {
		this.lon = longitud;
	}

	@Override
	public String toString() {
		return nombre + " (" + lat + ", " + lon + ")";
	}
}
