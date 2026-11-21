package cr.ac.tec.examen2;

public class Tramo {
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
