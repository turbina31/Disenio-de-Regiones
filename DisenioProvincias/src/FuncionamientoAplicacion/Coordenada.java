package FuncionamientoAplicacion;

public class Coordenada {
	private String nombre;
	private double lat;//latitud;
	private double lon;//longitud;

	public Coordenada(String nombre, double latitud, double longitud) {
		this.nombre = nombre;
		this.lat = latitud;
		this.lon = longitud;
	}

	public Coordenada() {
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
