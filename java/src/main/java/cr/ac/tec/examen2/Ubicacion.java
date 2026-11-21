package cr.ac.tec.examen2;

public abstract class Ubicacion {
    private final String nombre;
    private Ubicacion padre;

    protected Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getPadre() {
        return padre;
    }

    public abstract Ubicacion[] getHijos();

    public int getCantidadHijos() {
        return getHijos().length;
    }

    protected void establecerPadre(Ubicacion padre) {
        this.padre = padre;
    }

    public final int getTotalAbordajesPorDia() {
        return getTotalAbordajes();
    }

    public abstract int getTotalAbordajes();
}
