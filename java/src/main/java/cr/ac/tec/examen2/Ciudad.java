package cr.ac.tec.examen2;

public class Ciudad extends Ubicacion {
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
