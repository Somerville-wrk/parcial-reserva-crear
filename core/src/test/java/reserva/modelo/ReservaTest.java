package reserva.modelo;

import org.junit.jupiter.api.Test;
import reserva.exception.EstadoReservaInvalidoException;
import reserva.exception.ReservaInvalidaException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void deberiaCrearReservaActivaConDatosValidos() {
        UUID id = UUID.randomUUID();
        LocalDate fecha = LocalDate.now();
        Reserva reserva = Reserva.instance(id, "gaston@universidad.edu.ar", fecha, 2);

        assertNotNull(reserva);
        assertEquals(id, reserva.getId());
        assertEquals("ACTIVA", reserva.getEstado().name());
    }

    @Test
    void deberiaLanzarExcepcionSiIdEsNulo() {
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(null, "test@test.com", LocalDate.now(), 2)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiFechaEsNula() {
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(UUID.randomUUID(), "test@test.com", null, 2)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiEmailEsNuloOVacio() {
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(UUID.randomUUID(), null, LocalDate.now(), 2)
        );
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(UUID.randomUUID(), "", LocalDate.now(), 2)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiCantidadPersonasEsCeroOMenor() {
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(UUID.randomUUID(), "test@test.com", LocalDate.now(), 0)
        );
        assertThrows(ReservaInvalidaException.class, () ->
                Reserva.instance(UUID.randomUUID(), "test@test.com", LocalDate.now(), -1)
        );
    }

    @Test
    void deberiaLanzarExcepcionAlCancelarReservaYaCancelada() {
        Reserva reserva = Reserva.instance(UUID.randomUUID(), "test@test.com", LocalDate.now(), 2);
        reserva.cancelar(); // Primera cancelación exitosa

        assertThrows(EstadoReservaInvalidoException.class, () ->
                reserva.cancelar() // Segunda cancelación debe fallar
        );
    }
}