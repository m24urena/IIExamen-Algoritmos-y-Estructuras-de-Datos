package cr.ac.tec.examen2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class GrafoDijkstraTest {

    @Test
    void busesRespetanSentidoYDevuelvenRutaMasRapida() throws IOException {
        Ciudad ciudad = cargarCiudad();

        GrafoDijkstra.ResultadoRuta ruta =
                GrafoDijkstra.dijkstraBuses(ciudad, "Parque", "Mercado");

        assertEquals(29, ruta.getMinutosTotales());
        assertEquals(4, ruta.getTramos().length);
        assertEquals("Faro", ruta.getParadas()[1].getNombre());
        assertFalse(ruta.getCaminatas()[0]);
        assertEquals("Bus 2", ruta.getTramos()[0].getLinea());
    }

    @Test
    void tramoDeBusNoPermiteRecorrerLaAristaEnSentidoInverso() {
        Ciudad ciudad = new Ciudad("Ciudad de prueba");
        Zona zona = new Zona("Zona de prueba");
        Parada salida = new Parada("A", "Salida", 0);
        Parada destino = new Parada("B", "Destino", 0);
        salida.agregarTramo(new Tramo("Bus prueba", "Salida", "Destino", 6));
        zona.agregarParada(salida);
        zona.agregarParada(destino);
        ciudad.agregarZona(zona);

        assertEquals(6, GrafoDijkstra.dijkstraBuses(ciudad, "Salida", "Destino")
                .getMinutosTotales());
        assertNull(GrafoDijkstra.dijkstraBuses(ciudad, "Destino", "Salida"));
    }

    @Test
    void caminataPermiteViajarEnAmbosSentidos() throws IOException {
        Ciudad ciudad = cargarCiudad();

        GrafoDijkstra.ResultadoRuta ida =
                GrafoDijkstra.dijkstraCaminata(ciudad, "Mercado", "Parque");
        GrafoDijkstra.ResultadoRuta vuelta =
                GrafoDijkstra.dijkstraCaminata(ciudad, "Parque", "Mercado");

        assertEquals(10, ida.getMinutosTotales());
        assertEquals(10, vuelta.getMinutosTotales());
        assertTrue(ida.getCaminatas()[0]);
        assertTrue(vuelta.getCaminatas()[0]);
        assertTrue(ida.getDescripcion().contains("Caminata"));
    }

    @Test
    void rutaCombinadaDevuelveTiempoMinimoYTramosCorrectos() throws IOException {
        Ciudad ciudad = cargarCiudad();

        GrafoDijkstra.ResultadoRuta ruta =
                GrafoDijkstra.dijkstra(ciudad, "Terminal", "Parque");

        assertEquals(9, ruta.getMinutosTotales());
        assertEquals(2, ruta.getTramos().length);
        assertEquals("Estadio", ruta.getParadas()[1].getNombre());
        assertEquals("Parque", ruta.getParadas()[2].getNombre());
        assertEquals("Bus 2", ruta.getTramos()[0].getLinea());
        assertEquals("Bus 2", ruta.getTramos()[1].getLinea());
        assertFalse(ruta.getCaminatas()[0]);
        assertFalse(ruta.getCaminatas()[1]);
        assertTrue(ruta.getDescripcion().contains("9 minutos"));
    }

    @Test
    void mismoOrigenYDestinoDevuelveRutaDeCeroMinutos() throws IOException {
        GrafoDijkstra.ResultadoRuta ruta =
                GrafoDijkstra.dijkstra(cargarCiudad(), "Terminal", "Terminal");

        assertEquals(0, ruta.getMinutosTotales());
        assertEquals(1, ruta.getParadas().length);
        assertEquals(0, ruta.getTramos().length);
        assertTrue(ruta.getDescripcion().contains("0 minutos"));
    }

    @Test
    void grafoYZonaVaciosNoTienenRuta() {
        Ciudad ciudad = new Ciudad("Ciudad vacia");

        assertNull(GrafoDijkstra.dijkstra(ciudad, "Terminal", "Mercado"));

        ciudad.agregarZona(new Zona("Zona vacia"));
        assertNull(GrafoDijkstra.dijkstra(ciudad, "Terminal", "Mercado"));
    }

    @Test
    void paradasDesconocidasYArgumentosNulosNoTienenRuta() throws IOException {
        Ciudad ciudad = cargarCiudad();

        assertNull(GrafoDijkstra.dijkstra(ciudad, "Inexistente", "Terminal"));
        assertNull(GrafoDijkstra.dijkstra(ciudad, "Terminal", "Inexistente"));
        assertNull(GrafoDijkstra.dijkstra(ciudad, null, "Terminal"));
        assertNull(GrafoDijkstra.dijkstra(ciudad, "Terminal", null));
        assertNull(GrafoDijkstra.dijkstra(null, "Terminal", "Mercado"));
    }

    private Ciudad cargarCiudad() throws IOException {
        return ArbolNArio.construirArbolNario(
                "../datos/version-a/abordajes.csv",
                "../datos/version-a/tramos.csv");
    }
}
