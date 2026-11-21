package cr.ac.tec.examen2;

public class CountingSort {
    private CountingSort() {
    }

    public static Parada[] ordenarParadas(Parada[] paradas) {
        if (paradas.length == 0) {
            return paradas;
        }

        int longitudMaximaCodigo = 0;
        int abordajesMinimos = paradas[0].getAbordajes();
        int abordajesMaximos = abordajesMinimos;
        for (int i = 0; i < paradas.length; i++) {
            int longitudCodigo = paradas[i].getCodigo().length();
            if (longitudCodigo > longitudMaximaCodigo) {
                longitudMaximaCodigo = longitudCodigo;
            }
            if (paradas[i].getAbordajes() < abordajesMinimos) {
                abordajesMinimos = paradas[i].getAbordajes();
            }
            if (paradas[i].getAbordajes() > abordajesMaximos) {
                abordajesMaximos = paradas[i].getAbordajes();
            }
        }

        Parada[] porCodigo = new Parada[paradas.length];
        System.arraycopy(paradas, 0, porCodigo, 0, paradas.length);
        Parada[] auxiliar = new Parada[paradas.length];
        int[] conteosCodigo = new int[Character.MAX_VALUE + 2];

        for (int posicion = longitudMaximaCodigo - 1; posicion >= 0; posicion--) {
            for (int i = 0; i < conteosCodigo.length; i++) {
                conteosCodigo[i] = 0;
            }
            for (int i = 0; i < porCodigo.length; i++) {
                int clave = posicion < porCodigo[i].getCodigo().length()
                        ? porCodigo[i].getCodigo().charAt(posicion) + 1
                        : 0;
                conteosCodigo[clave]++;
            }
            for (int i = 1; i < conteosCodigo.length; i++) {
                conteosCodigo[i] += conteosCodigo[i - 1];
            }
            for (int i = porCodigo.length - 1; i >= 0; i--) {
                int clave = posicion < porCodigo[i].getCodigo().length()
                        ? porCodigo[i].getCodigo().charAt(posicion) + 1
                        : 0;
                auxiliar[--conteosCodigo[clave]] = porCodigo[i];
            }
            Parada[] temporal = porCodigo;
            porCodigo = auxiliar;
            auxiliar = temporal;
        }

        long rangoAbordajes = (long) abordajesMaximos - abordajesMinimos + 1;
        if (rangoAbordajes > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("El rango de abordajes es demasiado grande");
        }
        int[] conteosAbordajes = new int[(int) rangoAbordajes];
        for (int i = 0; i < porCodigo.length; i++) {
            conteosAbordajes[porCodigo[i].getAbordajes() - abordajesMinimos]++;
        }

        int siguientePosicion = 0;
        for (int i = conteosAbordajes.length - 1; i >= 0; i--) {
            int cantidad = conteosAbordajes[i];
            conteosAbordajes[i] = siguientePosicion;
            siguientePosicion += cantidad;
        }

        for (int i = 0; i < porCodigo.length; i++) {
            int clave = porCodigo[i].getAbordajes() - abordajesMinimos;
            paradas[conteosAbordajes[clave]++] = porCodigo[i];
        }
        return paradas;
    }

    public static Parada[] countingSortParadas(Parada[] paradas) {
        return ordenarParadas(paradas);
    }
}
