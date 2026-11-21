package cr.ac.tec.examen2;

public abstract class Ubicacion {
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
