package cr.ac.tec.examen2;

public class Ciudad extends Ubicacion {
    private Zona[] zonas;

    public Ciudad(String nombre) {
        super(nombre);
        zonas = new Zona[0];
    }

    public void agregarZona(Zona zona) {
        Zona[] nuevas = new Zona[zonas.length + 1];
        System.arraycopy(zonas, 0, nuevas, 0, zonas.length);
        nuevas[zonas.length] = zona;
        zonas = nuevas;
        zona.establecerPadre(this);
    }

    public Zona[] getZonas() {
        return getHijos();
    }

    @Override
    public Zona[] getHijos() {
        Zona[] copia = new Zona[zonas.length];
        System.arraycopy(zonas, 0, copia, 0, zonas.length);
        return copia;
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

    public int getTotalAbordajesPorZona(String nombreZona) {
        Zona zona = buscarZona(nombreZona);
        return zona == null ? 0 : zona.getTotalAbordajes();
    }

    @Override
    public int getTotalAbordajes() {
        int total = 0;
        for (int i = 0; i < zonas.length; i++) {
            total += zonas[i].getTotalAbordajes();
        }
        return total;
    }
}
