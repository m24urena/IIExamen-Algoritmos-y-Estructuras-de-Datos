package cr.ac.tec.examen2;

public class Zona extends Ubicacion {
    private Parada[] paradas;

    public Zona(String nombre) {
        super(nombre);
        paradas = new Parada[0];
    }

    public void agregarParada(Parada parada) {
        Parada[] nuevas = new Parada[paradas.length + 1];
        System.arraycopy(paradas, 0, nuevas, 0, paradas.length);
        nuevas[paradas.length] = parada;
        paradas = nuevas;
        parada.establecerPadre(this);
    }

    public Parada[] getParadas() {
        return getHijos();
    }

    @Override
    public Parada[] getHijos() {
        Parada[] copia = new Parada[paradas.length];
        System.arraycopy(paradas, 0, copia, 0, paradas.length);
        return copia;
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

    @Override
    public int getTotalAbordajes() {
        int total = 0;
        for (int i = 0; i < paradas.length; i++) {
            total += paradas[i].getTotalAbordajes();
        }
        return total;
    }

    public int getTotalAbordajesPorParada(String nombreParada) {
        Parada parada = buscarParada(nombreParada);
        return parada == null ? 0 : parada.getTotalAbordajes();
    }
}
