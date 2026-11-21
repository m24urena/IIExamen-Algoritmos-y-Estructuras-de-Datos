package cr.ac.tec.examen2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Algoritmos {
    public static void main(String[] args) {
        System.out.println("Algoritmos base listos.");
    }
    public abstract static class Ubicacion {
        private final String nombre;
        private Ubicacion padre;
        private Ubicacion[] hijos;

        protected Ubicacion(String nombre) {
            this.nombre = nombre;
            this.hijos = new Ubicacion[0];
        }

        public String getNombre() {
            return nombre;
        }

        public Ubicacion getPadre() {
            return padre;
        }

        public Ubicacion[] getHijos() {
            Ubicacion[] copia = new Ubicacion[hijos.length];
            System.arraycopy(hijos, 0, copia, 0, hijos.length);
            return copia;
        }

        public int getCantidadHijos() {
            return hijos.length;
        }

        protected void agregarHijo(Ubicacion hijo) {
            Ubicacion[] nuevos = new Ubicacion[hijos.length + 1];
            System.arraycopy(hijos, 0, nuevos, 0, hijos.length);
            nuevos[hijos.length] = hijo;
            hijos = nuevos;
            hijo.padre = this;
        }
    }

    public static class Ciudad extends Ubicacion {
        public Ciudad(String nombre) {
            super(nombre);
        }

        public void agregarZona(Zona zona) {
            agregarHijo(zona);
        }

        public Zona[] getZonas() {
            Ubicacion[] hijos = getHijos();
            Zona[] zonas = new Zona[hijos.length];
            for (int i = 0; i < hijos.length; i++) {
                zonas[i] = (Zona) hijos[i];
            }
            return zonas;
        }

        public Zona buscarZona(String nombre) {
            Zona[] zonas = getZonas();
            for (int i = 0; i < zonas.length; i++) {
                if (zonas[i].getNombre().equals(nombre)) {
                    return zonas[i];
                }
            }
            return null;
        }

        public int getTotalAbordajes() {
            Zona[] zonas = getZonas();
            int total = 0;
            for (int i = 0; i < zonas.length; i++) {
                total += zonas[i].getTotalAbordajes();
            }
            return total;
        }
    }

    public static class Zona extends Ubicacion {
        public Zona(String nombre) {
            super(nombre);
        }

        public void agregarParada(Parada parada) {
            agregarHijo(parada);
        }

        public Parada[] getParadas() {
            Ubicacion[] hijos = getHijos();
            Parada[] paradas = new Parada[hijos.length];
            for (int i = 0; i < hijos.length; i++) {
                paradas[i] = (Parada) hijos[i];
            }
            return paradas;
        }

        public Parada buscarParada(String nombre) {
            Parada[] paradas = getParadas();
            for (int i = 0; i < paradas.length; i++) {
                if (paradas[i].getNombre().equals(nombre)) {
                    return paradas[i];
                }
            }
            return null;
        }

        public int getTotalAbordajes() {
            Parada[] paradas = getParadas();
            int total = 0;
            for (int i = 0; i < paradas.length; i++) {
                total += paradas[i].getAbordajes();
            }
            return total;
        }
    }

    public static class Tramo {
        private final String linea;
        private final String desde;
        private final String hacia;
        private final int minutos;

        public Tramo(String linea, String desde, String hacia, int minutos) {
            this.linea = linea;
            this.desde = desde;
            this.hacia = hacia;
            this.minutos = minutos;
        }

        public String getLinea() {
            return linea;
        }

        public String getDesde() {
            return desde;
        }

        public String getHacia() {
            return hacia;
        }

        public int getMinutos() {
            return minutos;
        }
    }

    public static class Parada extends Ubicacion {
        private final String codigo;
        private final int abordajes;
        private Tramo[] tramos;

        public Parada(String codigo, String nombre, int abordajes) {
            super(nombre);
            this.codigo = codigo;
            this.abordajes = abordajes;
            this.tramos = new Tramo[0];
        }

        public String getCodigo() {
            return codigo;
        }

        public int getAbordajes() {
            return abordajes;
        }

        public void agregarTramo(Tramo tramo) {
            Tramo[] nuevos = new Tramo[tramos.length + 1];
            System.arraycopy(tramos, 0, nuevos, 0, tramos.length);
            nuevos[tramos.length] = tramo;
            tramos = nuevos;
        }

        public Tramo[] getTramos() {
            Tramo[] copia = new Tramo[tramos.length];
            System.arraycopy(tramos, 0, copia, 0, tramos.length);
            return copia;
        }
    }

    public static Ciudad construirArbolNario(String archivoAbordajes, String archivoTramos)
            throws IOException {
        Ciudad ciudad = new Ciudad("Puerto Celeste");
        BufferedReader lector = new BufferedReader(new FileReader(archivoAbordajes));
        String linea = lector.readLine();

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
                throw new IllegalArgumentException("Abordajes invalidos para " + nombreParada, error);
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
        lector.close();

        BufferedReader lectorTramos = new BufferedReader(new FileReader(archivoTramos));
        lectorTramos.readLine();

        while ((linea = lectorTramos.readLine()) != null) {
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
        lectorTramos.close();
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
