package cr.ac.tec.examen2;

public class Zona extends Ubicacion {
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
