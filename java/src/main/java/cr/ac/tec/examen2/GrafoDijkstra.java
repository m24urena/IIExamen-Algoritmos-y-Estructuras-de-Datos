package cr.ac.tec.examen2;

public class GrafoDijkstra {
    private static final int SOLO_BUSES = 1;
    private static final int SOLO_CAMINATA = 2;
    private static final int BUS_Y_CAMINATA = 3;
    private static final long DISTANCIA_INFINITA = Long.MAX_VALUE;

    private GrafoDijkstra() {
    }

    public static class ResultadoRuta {
        private final Parada[] paradas;
        private final Tramo[] tramos;
        private final boolean[] caminatas;
        private final long minutosTotales;

        private ResultadoRuta(Parada[] paradas, Tramo[] tramos,
                boolean[] caminatas, long minutosTotales) {
            this.paradas = paradas;
            this.tramos = tramos;
            this.caminatas = caminatas;
            this.minutosTotales = minutosTotales;
        }

        public Parada[] getParadas() {
            Parada[] copia = new Parada[paradas.length];
            System.arraycopy(paradas, 0, copia, 0, paradas.length);
            return copia;
        }

        public Tramo[] getTramos() {
            Tramo[] copia = new Tramo[tramos.length];
            System.arraycopy(tramos, 0, copia, 0, tramos.length);
            return copia;
        }

        public boolean[] getCaminatas() {
            boolean[] copia = new boolean[caminatas.length];
            System.arraycopy(caminatas, 0, copia, 0, caminatas.length);
            return copia;
        }

        public long getMinutosTotales() {
            return minutosTotales;
        }

        public String getDescripcion() {
            if (tramos.length == 0) {
                return "Ya se encuentra en " + paradas[0].getNombre()
                        + ". Tiempo total: 0 minutos.";
            }

            StringBuilder descripcion = new StringBuilder();
            descripcion.append("Ruta: ").append(paradas[0].getNombre());
            for (int i = 0; i < tramos.length; i++) {
                descripcion.append(" --[");
                if (caminatas[i]) {
                    descripcion.append("Caminata");
                } else {
                    descripcion.append(tramos[i].getLinea());
                }
                descripcion.append(", ").append(tramos[i].getMinutos()).append(" min]--> ");
                descripcion.append(paradas[i + 1].getNombre());
            }
            descripcion.append(". Tiempo total: ").append(minutosTotales).append(" minutos.");
            return descripcion.toString();
        }
    }

    private static class Grafo {
        private final Parada[] paradas;
        private final int[] primero;
        private final int[] destino;
        private final int[] siguiente;
        private final Tramo[] tramos;
        private final boolean[] caminata;
        private int cantidadAristas;

        private Grafo(Parada[] paradas, int capacidadAristas) {
            this.paradas = paradas;
            primero = new int[paradas.length];
            destino = new int[capacidadAristas];
            siguiente = new int[capacidadAristas];
            tramos = new Tramo[capacidadAristas];
            caminata = new boolean[capacidadAristas];
            for (int i = 0; i < primero.length; i++) {
                primero[i] = -1;
            }
        }

        private void agregarArista(int origen, int fin, Tramo tramo, boolean esCaminata) {
            destino[cantidadAristas] = fin;
            tramos[cantidadAristas] = tramo;
            caminata[cantidadAristas] = esCaminata;
            siguiente[cantidadAristas] = primero[origen];
            primero[origen] = cantidadAristas;
            cantidadAristas++;
        }
    }

    private static class MinHeap {
        private final int[] heap;
        private final int[] posicion;
        private final long[] distancias;
        private int tamano;

        private MinHeap(long[] distancias) {
            this.distancias = distancias;
            heap = new int[distancias.length];
            posicion = new int[distancias.length];
            tamano = distancias.length;
            for (int i = 0; i < heap.length; i++) {
                heap[i] = i;
                posicion[i] = i;
            }
            for (int i = tamano / 2 - 1; i >= 0; i--) {
                bajar(i);
            }
        }

        private boolean estaVacio() {
            return tamano == 0;
        }

        private int extraerMinimo() {
            int minimo = heap[0];
            intercambiar(0, tamano - 1);
            tamano--;
            posicion[minimo] = -1;
            if (tamano > 0) {
                bajar(0);
            }
            return minimo;
        }

        private void disminuirClave(int vertice) {
            int indice = posicion[vertice];
            while (indice > 0) {
                int padre = (indice - 1) / 2;
                if (distancias[heap[padre]] <= distancias[heap[indice]]) {
                    break;
                }
                intercambiar(padre, indice);
                indice = padre;
            }
        }

        private void bajar(int indice) {
            while (true) {
                int menor = indice;
                int izquierdo = indice * 2 + 1;
                int derecho = izquierdo + 1;
                if (izquierdo < tamano
                        && distancias[heap[izquierdo]] < distancias[heap[menor]]) {
                    menor = izquierdo;
                }
                if (derecho < tamano
                        && distancias[heap[derecho]] < distancias[heap[menor]]) {
                    menor = derecho;
                }
                if (menor == indice) {
                    return;
                }
                intercambiar(indice, menor);
                indice = menor;
            }
        }

        private void intercambiar(int primero, int segundo) {
            int temporal = heap[primero];
            heap[primero] = heap[segundo];
            heap[segundo] = temporal;
            posicion[heap[primero]] = primero;
            posicion[heap[segundo]] = segundo;
        }
    }

    public static ResultadoRuta dijkstraBuses(Ciudad ciudad, String salida, String destino) {
        return buscarRuta(ciudad, salida, destino, SOLO_BUSES);
    }

    public static ResultadoRuta dijkstraCaminata(Ciudad ciudad, String salida, String destino) {
        return buscarRuta(ciudad, salida, destino, SOLO_CAMINATA);
    }

    public static ResultadoRuta dijkstra(Ciudad ciudad, String salida, String destino) {
        return buscarRuta(ciudad, salida, destino, BUS_Y_CAMINATA);
    }

    private static ResultadoRuta buscarRuta(Ciudad ciudad, String salida, String destino,
            int tipoGrafo) {
        if (ciudad == null || salida == null || destino == null) {
            return null;
        }

        Grafo grafo = construirGrafo(ciudad, tipoGrafo);
        int origen = buscarIndiceParada(grafo.paradas, salida);
        int fin = buscarIndiceParada(grafo.paradas, destino);
        if (origen < 0 || fin < 0) {
            return null;
        }

        long[] distancias = new long[grafo.paradas.length];
        int[] anterior = new int[grafo.paradas.length];
        int[] aristaAnterior = new int[grafo.paradas.length];
        for (int i = 0; i < distancias.length; i++) {
            distancias[i] = DISTANCIA_INFINITA;
            anterior[i] = -1;
            aristaAnterior[i] = -1;
        }
        distancias[origen] = 0;
        MinHeap pendientes = new MinHeap(distancias);

        while (!pendientes.estaVacio()) {
            int actual = pendientes.extraerMinimo();
            if (distancias[actual] == DISTANCIA_INFINITA || actual == fin) {
                break;
            }

            for (int arista = grafo.primero[actual]; arista != -1;
                    arista = grafo.siguiente[arista]) {
                int vecino = grafo.destino[arista];
                if (pendientes.posicion[vecino] == -1) {
                    continue;
                }
                long nuevaDistancia = distancias[actual] + grafo.tramos[arista].getMinutos();
                if (nuevaDistancia < distancias[vecino]) {
                    distancias[vecino] = nuevaDistancia;
                    anterior[vecino] = actual;
                    aristaAnterior[vecino] = arista;
                    pendientes.disminuirClave(vecino);
                }
            }
        }

        if (distancias[fin] == DISTANCIA_INFINITA) {
            return null;
        }

        int cantidadTramos = 0;
        int actual = fin;
        while (actual != origen) {
            if (anterior[actual] < 0) {
                return null;
            }
            cantidadTramos++;
            actual = anterior[actual];
        }

        Parada[] rutaParadas = new Parada[cantidadTramos + 1];
        Tramo[] rutaTramos = new Tramo[cantidadTramos];
        boolean[] rutaCaminatas = new boolean[cantidadTramos];
        actual = fin;
        rutaParadas[cantidadTramos] = grafo.paradas[actual];
        for (int i = cantidadTramos - 1; i >= 0; i--) {
            int arista = aristaAnterior[actual];
            rutaTramos[i] = grafo.tramos[arista];
            rutaCaminatas[i] = grafo.caminata[arista];
            actual = anterior[actual];
            rutaParadas[i] = grafo.paradas[actual];
        }
        return new ResultadoRuta(rutaParadas, rutaTramos, rutaCaminatas, distancias[fin]);
    }

    private static Grafo construirGrafo(Ciudad ciudad, int tipoGrafo) {
        Parada[] paradas = obtenerParadas(ciudad);
        ordenarParadasPorNombre(paradas);
        int cantidadAristas = 0;
        for (int i = 0; i < paradas.length; i++) {
            Tramo[] tramos = paradas[i].getTramos();
            for (int j = 0; j < tramos.length; j++) {
                boolean esCaminata = tramos[j].getLinea().startsWith("Caminata");
                if (tramos[j].getDesde().equals(paradas[i].getNombre())
                        && ((esCaminata && (tipoGrafo & SOLO_CAMINATA) != 0)
                                || (!esCaminata && (tipoGrafo & SOLO_BUSES) != 0))) {
                    cantidadAristas += esCaminata ? 2 : 1;
                }
            }
        }

        Grafo grafo = new Grafo(paradas, cantidadAristas);
        for (int i = 0; i < paradas.length; i++) {
            Tramo[] tramos = paradas[i].getTramos();
            for (int j = 0; j < tramos.length; j++) {
                Tramo tramo = tramos[j];
                boolean esCaminata = tramo.getLinea().startsWith("Caminata");
                if (tramo.getDesde().equals(paradas[i].getNombre())
                        && ((esCaminata && (tipoGrafo & SOLO_CAMINATA) != 0)
                                || (!esCaminata && (tipoGrafo & SOLO_BUSES) != 0))) {
                    int indiceDestino = buscarIndiceParada(paradas, tramo.getHacia());
                    if (indiceDestino >= 0) {
                        grafo.agregarArista(i, indiceDestino, tramo, esCaminata);
                        if (esCaminata) {
                            grafo.agregarArista(indiceDestino, i, tramo, true);
                        }
                    }
                }
            }
        }
        return grafo;
    }

    private static Parada[] obtenerParadas(Ciudad ciudad) {
        Zona[] zonas = ciudad.getZonas();
        int cantidad = 0;
        for (int i = 0; i < zonas.length; i++) {
            cantidad += zonas[i].getParadas().length;
        }
        Parada[] paradas = new Parada[cantidad];
        int indice = 0;
        for (int i = 0; i < zonas.length; i++) {
            Parada[] paradasZona = zonas[i].getParadas();
            for (int j = 0; j < paradasZona.length; j++) {
                paradas[indice++] = paradasZona[j];
            }
        }
        return paradas;
    }

    private static int buscarIndiceParada(Parada[] paradas, String nombre) {
        int inicio = 0;
        int fin = paradas.length - 1;
        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int comparacion = paradas[medio].getNombre().compareTo(nombre);
            if (comparacion == 0) {
                return medio;
            }
            if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    private static void ordenarParadasPorNombre(Parada[] paradas) {
        Parada[] auxiliar = new Parada[paradas.length];
        ordenarParadasPorNombre(paradas, auxiliar, 0, paradas.length);
    }

    private static void ordenarParadasPorNombre(Parada[] paradas, Parada[] auxiliar,
            int inicio, int fin) {
        if (fin - inicio < 2) {
            return;
        }
        int medio = inicio + (fin - inicio) / 2;
        ordenarParadasPorNombre(paradas, auxiliar, inicio, medio);
        ordenarParadasPorNombre(paradas, auxiliar, medio, fin);

        int izquierda = inicio;
        int derecha = medio;
        int indice = inicio;
        while (izquierda < medio && derecha < fin) {
            if (paradas[izquierda].getNombre().compareTo(paradas[derecha].getNombre()) <= 0) {
                auxiliar[indice++] = paradas[izquierda++];
            } else {
                auxiliar[indice++] = paradas[derecha++];
            }
        }
        while (izquierda < medio) {
            auxiliar[indice++] = paradas[izquierda++];
        }
        while (derecha < fin) {
            auxiliar[indice++] = paradas[derecha++];
        }
        System.arraycopy(auxiliar, inicio, paradas, inicio, fin - inicio);
    }
}
