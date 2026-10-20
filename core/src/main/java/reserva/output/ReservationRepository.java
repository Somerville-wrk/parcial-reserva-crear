package reserva.output;

import reserva.modelo.Reserva;
import java.time.LocalDate;

public interface ReservationRepository {
    boolean existeActivaPorEmailYFecha(String email, LocalDate fecha);
    boolean guardar(Reserva reserva);
}