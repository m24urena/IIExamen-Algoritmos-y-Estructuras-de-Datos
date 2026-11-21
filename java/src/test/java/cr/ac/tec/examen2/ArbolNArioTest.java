package cr.ac.tec.examen2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ArbolNArioTest {

    @Test
    void ciudadVaciaTieneTotalCeroYNoContieneZonas() {
        Ciudad ciudad = new Ciudad("Ciudad vacia");

        assertEquals(0, ciudad.getZonas().length);
        assertEquals(0, ciudad.getCantidadHijos());
        assertEquals(0, ciudad.getTotalAbordajes());
        assertEquals(0, ciudad.getTotalAbordajesPorDia());
        assertEquals(0, ciudad.getTotalAbordajesPorZona("Inexistente"));
        assertNull(ciudad.buscarZona("Inexistente"));
    }

    @Test
    void zonaVaciaTieneTotalCeroYNoContieneParadas() {
        Zona zona = new Zona("Zona vacia");

        assertEquals(0, zona.getParadas().length);
        assertEquals(0, zona.getCantidadHijos());
        assertEquals(0, zona.getTotalAbordajes());
        assertEquals(0, zona.getTotalAbordajesPorDia());
        assertEquals(0, zona.getTotalAbordajesPorParada("Inexistente"));
        assertNull(zona.buscarParada("Inexistente"));
    }

    @Test
    void agregaZonasYParadasYCalculaAbordajesPorDia() {
        Ciudad ciudad = new Ciudad("Ciudad");
        Zona zona = new Zona("Centro");
        Parada parada = new Parada("S01", "Terminal", 1520);

        ciudad.agregarZona(zona);
        zona.agregarParada(parada);

        assertSame(ciudad, zona.getPadre());
        assertSame(zona, parada.getPadre());
        assertSame(zona, ciudad.getZonas()[0]);
        assertSame(parada, zona.getParadas()[0]);
        assertEquals(1520, parada.getTotalAbordajes());
        assertEquals(1520, parada.getTotalAbordajesPorDia());
        assertEquals(1520, zona.getTotalAbordajesPorParada("Terminal"));
        assertEquals(1520, zona.getTotalAbordajes());
        assertEquals(1520, zona.getTotalAbordajesPorDia());
        assertEquals(1520, ciudad.getTotalAbordajesPorZona("Centro"));
        assertEquals(1520, ciudad.getTotalAbordajes());
    }

    @Test
    void agregarZonaNulaLanzaNullPointerException() {
        Ciudad ciudad = new Ciudad("Ciudad");

        assertThrows(NullPointerException.class, () -> ciudad.agregarZona(null));
    }

    @Test
    void agregarParadaNulaLanzaNullPointerException() {
        Zona zona = new Zona("Zona");

        assertThrows(NullPointerException.class, () -> zona.agregarParada(null));
    }

    @Test
    void arbolConstruidoDesdeCsvTieneTotalesYJerarquiaEsperados() throws IOException {
        Ciudad ciudad = ArbolNArio.construirArbolNario(
                "../datos/version-a/abordajes.csv",
                "../datos/version-a/tramos.csv");

        assertEquals("Puerto Celeste", ciudad.getNombre());
        assertEquals(3, ciudad.getZonas().length);
        assertEquals(8, contarParadas(ciudad));
        assertEquals(7835, ciudad.getTotalAbordajesPorDia());
        assertEquals(3810, ciudad.getTotalAbordajesPorZona("Centro"));
        assertEquals(2745, ciudad.getTotalAbordajesPorZona("Norte"));
        assertEquals(1280, ciudad.getTotalAbordajesPorZona("Costa"));
        assertEquals(0, ciudad.getTotalAbordajesPorZona("Zona inexistente"));

        Zona centro = ciudad.buscarZona("Centro");
        assertNotNull(centro);
        assertEquals(3, centro.getParadas().length);
        assertEquals(1520, centro.getTotalAbordajesPorParada("Terminal"));
        assertEquals(0, centro.getTotalAbordajesPorParada("Parada inexistente"));
        assertEquals(1520, centro.buscarParada("Terminal").getTotalAbordajes());
        assertNull(ArbolNArio.buscarParada(ciudad, "Parada inexistente"));
    }

    private int contarParadas(Ciudad ciudad) {
        int cantidad = 0;
        Zona[] zonas = ciudad.getZonas();
        for (int i = 0; i < zonas.length; i++) {
            cantidad += zonas[i].getParadas().length;
        }
        return cantidad;
    }
}
