package reserva.usecase;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateReservationUseCaseTest {

    @Test
    void deberiaRegistrarReservaExitosamente() {
        // 1. Preparación (Arrange)
        ReservationRepository repositoryMock = mock(ReservationRepository.class);
        CreateReservationUseCase useCase = new CreateReservationUseCase(repositoryMock);

        UUID id = UUID.randomUUID();
        String email = "gaston@test.com";
        LocalDate fecha = LocalDate.now();

        // Simulamos que NO existe una reserva previa y que el guardado es exitoso
        when(repositoryMock.existeActivaPorEmailYFecha(email, fecha)).thenReturn(false);
        when(repositoryMock.guardar(any(Reserva.class))).thenReturn(true);

        // 2. Ejecución (Act)
        Reserva resultado = useCase.ejecutar(id, email, fecha, 4);

        // 3. Verificación (Assert)
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(repositoryMock, times(1)).guardar(any(Reserva.class));
    }

    @Test
    void deberiaLanzarExcepcionSiReservaYaExiste() {
        ReservationRepository repositoryMock = mock(ReservationRepository.class);
        CreateReservationUseCase useCase = new CreateReservationUseCase(repositoryMock);

        String email = "gaston@test.com";
        LocalDate fecha = LocalDate.now();

        // Simulamos que YA existe una reserva
        when(repositoryMock.existeActivaPorEmailYFecha(email, fecha)).thenReturn(true);

        assertThrows(ReservationAlreadyExistsException.class, () ->
                useCase.ejecutar(UUID.randomUUID(), email, fecha, 4)
        );

        // Verificamos que NUNCA se intentó guardar
        verify(repositoryMock, never()).guardar(any(Reserva.class));
    }

    @Test
    void deberiaLanzarExcepcionSiFallaLaPersistencia() {
        ReservationRepository repositoryMock = mock(ReservationRepository.class);
        CreateReservationUseCase useCase = new CreateReservationUseCase(repositoryMock);

        String email = "gaston@test.com";
        LocalDate fecha = LocalDate.now();

        // Simulamos que no existe previa, pero el guardado falla
        when(repositoryMock.existeActivaPorEmailYFecha(email, fecha)).thenReturn(false);
        when(repositoryMock.guardar(any(Reserva.class))).thenReturn(false);

        assertThrows(PersistenceException.class, () ->
                useCase.ejecutar(UUID.randomUUID(), email, fecha, 4)
        );
    }
}
