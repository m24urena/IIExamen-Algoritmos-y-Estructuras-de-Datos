package cr.ac.tec.examen2;

public class Parada extends Ubicacion {
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
