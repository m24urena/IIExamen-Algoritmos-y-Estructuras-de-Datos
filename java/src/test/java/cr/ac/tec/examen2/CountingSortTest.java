package cr.ac.tec.examen2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class CountingSortTest {

    @Test
    void ordenaAbordajesDescendentesYEmpatesPorCodigoDesdeCsv() throws IOException {
        Ciudad ciudad = ArbolNArio.construirArbolNario(
                "../datos/version-a/abordajes.csv",
                "../datos/version-a/tramos.csv");
        Parada[] paradas = obtenerParadas(ciudad);
        String[] codigosEsperados = {"S01", "S03", "S08", "S02", "S05", "S04", "S07", "S06"};

        Parada[] resultado = CountingSort.ordenarParadas(paradas);

        assertSame(paradas, resultado);
        for (int i = 0; i < codigosEsperados.length; i++) {
            assertEquals(codigosEsperados[i], resultado[i].getCodigo());
        }
    }

    @Test
    void ordenaAbordajesCeroYCodigosDeLongitudDistinta() {
        Parada[] paradas = {
            new Parada("S10", "Parada diez", 0),
            new Parada("S2", "Parada dos", 20),
            new Parada("S1", "Parada uno", 0),
            new Parada("S01", "Parada cero uno", 0)
        };

        CountingSort.countingSortParadas(paradas);

        assertEquals("S2", paradas[0].getCodigo());
        assertEquals("S01", paradas[1].getCodigo());
        assertEquals("S1", paradas[2].getCodigo());
        assertEquals("S10", paradas[3].getCodigo());
        assertEquals(20, paradas[0].getAbordajes());
        assertEquals(0, paradas[1].getAbordajes());
    }

    @Test
    void arregloVacioSeDevuelveSinCambios() {
        Parada[] paradas = new Parada[0];

        assertSame(paradas, CountingSort.ordenarParadas(paradas));
    }

    @Test
    void unElementoConCeroAbordajesSeMantiene() {
        Parada parada = new Parada("S00", "Parada vacia", 0);
        Parada[] paradas = {parada};

        CountingSort.ordenarParadas(paradas);

        assertSame(parada, paradas[0]);
    }

    @Test
    void arregloConParadaNulaLanzaNullPointerException() {
        Parada[] paradas = {null};

        assertThrows(NullPointerException.class, () -> CountingSort.ordenarParadas(paradas));
    }

    @Test
    void rangoDeAbordajesQueNoCabeEnArregloLanzaIllegalArgumentException() {
        Parada[] paradas = {
            new Parada("MIN", "Minimo", Integer.MIN_VALUE),
            new Parada("MAX", "Maximo", Integer.MAX_VALUE)
        };

        assertThrows(IllegalArgumentException.class, () -> CountingSort.ordenarParadas(paradas));
    }

    private Parada[] obtenerParadas(Ciudad ciudad) {
        Zona[] zonas = ciudad.getZonas();
        int cantidad = 0;
        for (int i = 0; i < zonas.length; i++) {
            cantidad += zonas[i].getParadas().length;
        }

        Parada[] paradas = new Parada[cantidad];
        int indice = 0;
        for (int i = 0; i < zonas.length; i++) {
            Parada[] paradasZona = zonas[i].getParadas();
            for (int j = 0; j < paradasZona.length; j++) {
                paradas[indice++] = paradasZona[j];
            }
        }
        return paradas;
    }
}
