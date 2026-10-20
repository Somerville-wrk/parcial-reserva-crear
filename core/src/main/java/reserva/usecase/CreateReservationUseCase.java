package reserva.usecase;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

public class CreateReservationUseCase {
    private final ReservationRepository repository;

    public CreateReservationUseCase(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reserva ejecutar(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {

        Reserva reserva = Reserva.instance(id, emailContacto, fecha, cantidadPersonas);


        if (repository.existeActivaPorEmailYFecha(emailContacto, fecha)) {
            throw new ReservationAlreadyExistsException("Ya existe una reserva activa para este correo y fecha.");
        }


        boolean guardadoExitoso = repository.guardar(reserva);
        if (!guardadoExitoso) {
            throw new PersistenceException("Fallo al intentar guardar la reserva.");
        }

        return reserva;
    }
}