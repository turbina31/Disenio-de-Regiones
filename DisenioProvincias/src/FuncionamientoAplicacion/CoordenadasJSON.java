package FuncionamientoAplicacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoordenadasJSON {
    private ArrayList<Coordenada> coordenadas;

    public CoordenadasJSON() {
        coordenadas = new ArrayList<>();
    }

    public void agregarCoordenada(Coordenada c) {
        coordenadas.add(c);
    }

    public Coordenada dame(int indice) {
        return coordenadas.get(indice);
    }

    public int tamanio() {
        return coordenadas == null ? 0 : coordenadas.size();
    }

    public List<Coordenada> obtenerCoordenadas() {
        return coordenadas;
    }

    public String generarJSONBasico() {
        return new GsonBuilder().create().toJson(this);
    }

    public String generarJSONPretty() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public void guardarJSON(String jsonParaGuardar, String archivoDestino) {
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(archivoDestino), StandardCharsets.UTF_8)) {
            w.write(jsonParaGuardar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarEnArchivo(String archivoDestino) {
        guardarJSON(generarJSONPretty(), archivoDestino);
    }

    public static CoordenadasJSON leerJSON(String archivo) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(archivo), StandardCharsets.UTF_8)) {
            CoordenadasJSON ret = new Gson().fromJson(br, CoordenadasJSON.class);
            if (ret == null || ret.coordenadas == null) {
                System.err.println("JSON vacío o sin la clave \"coordenadas\".");
                return null;
            }
            ret.coordenadas.removeIf(c -> c == null || c.getNombre() == null || c.getNombre().trim().isEmpty());
            return ret;
        } catch (Exception e) {
            System.err.println("Error leyendo JSON: " + e.getMessage());
            return null;
        }
    }
}
