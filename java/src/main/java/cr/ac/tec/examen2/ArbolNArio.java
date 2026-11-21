package cr.ac.tec.examen2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArbolNArio {
    private ArbolNArio() {
    }

    public static Ciudad construirArbolNario(String archivoAbordajes, String archivoTramos)
            throws IOException {
        Ciudad ciudad = new Ciudad("Puerto Celeste");
        String linea;
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoAbordajes))) {
            lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length < 4) {
                    throw new IllegalArgumentException("Dato mal formado en abordajes.csv");
                }

                String codigo = datos[0].trim();
                String nombreParada = datos[1].trim();
                String nombreZona = datos[2].trim();
                int abordajes;
                try {
                    abordajes = Integer.parseInt(datos[3].trim());
                } catch (NumberFormatException error) {
                    throw new IllegalArgumentException(
                            "Abordajes invalidos para " + nombreParada, error);
                }

                Zona zona = ciudad.buscarZona(nombreZona);
                if (zona == null) {
                    zona = new Zona(nombreZona);
                    ciudad.agregarZona(zona);
                }
                if (zona.buscarParada(nombreParada) == null) {
                    zona.agregarParada(new Parada(codigo, nombreParada, abordajes));
                }
            }
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoTramos))) {
            lector.readLine();
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",", -1);
                if (datos.length < 4) {
                    throw new IllegalArgumentException("Dato mal formado en tramos.csv");
                }

                String nombreLinea = datos[0].trim();
                String desde = datos[1].trim();
                String hacia = datos[2].trim();
                int minutos;
                try {
                    minutos = Integer.parseInt(datos[3].trim());
                } catch (NumberFormatException error) {
                    throw new IllegalArgumentException(
                            "Minutos invalidos para el tramo " + desde + "-" + hacia, error);
                }

                Tramo tramo = new Tramo(nombreLinea, desde, hacia, minutos);
                Parada paradaDesde = buscarParada(ciudad, desde);
                if (paradaDesde != null) {
                    paradaDesde.agregarTramo(tramo);
                }
                if (nombreLinea.startsWith("Caminata")) {
                    Parada paradaHacia = buscarParada(ciudad, hacia);
                    if (paradaHacia != null) {
                        paradaHacia.agregarTramo(tramo);
                    }
                }
            }
        }
        return ciudad;
    }

    public static Ciudad crearArbolNario(String archivoAbordajes, String archivoTramos)
            throws IOException {
        return construirArbolNario(archivoAbordajes, archivoTramos);
    }

    public static Ciudad generarArbolNario(String archivoAbordajes, String archivoTramos)
            throws IOException {
        return construirArbolNario(archivoAbordajes, archivoTramos);
    }

    public static Parada buscarParada(Ciudad ciudad, String nombre) {
        if (ciudad == null || nombre == null) {
            return null;
        }
        Zona[] zonas = ciudad.getZonas();
        for (int i = 0; i < zonas.length; i++) {
            Parada parada = zonas[i].buscarParada(nombre);
            if (parada != null) {
                return parada;
            }
        }
        return null;
    }
}
