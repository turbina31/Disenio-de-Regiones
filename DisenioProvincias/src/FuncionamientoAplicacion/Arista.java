package FuncionamientoAplicacion;

import java.util.Objects;

public class Arista<T> implements Comparable<Arista<T>>
{
    private final T origen;
    private final T destino;
    private final double peso;

    public Arista(T origen, T destino, double peso)
    {
    	if (origen == null) {
			throw new IllegalArgumentException("el dato de origen es nulo " + origen);
		}
    	if (destino == null) {
			throw new IllegalArgumentException("el dato de destino es nulo " + destino);
		}
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public T obtenerOrigen()
    {
        return origen;
    }

    public T obtenerDestino()
    {
        return destino;
    }

    public double obtenerPeso()
    {
        return peso;
    }

    @Override
    public int compareTo(Arista<T> otra) {
        return Double.compare(this.peso, otra.peso);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arista<?> arista)) return false;
        return Double.compare(arista.peso, peso) == 0 &&
               Objects.equals(origen, arista.origen) &&
               Objects.equals(destino, arista.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origen, destino, peso);
    }
    
    @Override
    public String toString()
    {
        return "(" + origen + " - " +
               destino + ", peso=" + peso + ")";
    }
}