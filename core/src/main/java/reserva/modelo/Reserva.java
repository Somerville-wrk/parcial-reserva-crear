package reserva.modelo;

import reserva.exception.EstadoReservaInvalidoException;
import reserva.exception.ReservaInvalidaException;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {
    private UUID id;
    private String emailContacto;
    private LocalDate fecha;
    private int cantidadPersonas;
    private EstadoReserva estado;

    private Reserva(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = EstadoReserva.ACTIVA; // Regla: comienza ACTIVA
    }

    public static Reserva instance(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        if (id == null) throw new ReservaInvalidaException("El identificador es obligatorio");
        if (fecha == null) throw new ReservaInvalidaException("La fecha es obligatoria");
        if (emailContacto == null || emailContacto.isBlank()) throw new ReservaInvalidaException("El email no puede estar vacío");
        if (cantidadPersonas <= 0) throw new ReservaInvalidaException("La cantidad de personas debe ser mayor a cero");

        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public void cancelar() {
        if (this.estado == EstadoReserva.CANCELADA) {
            throw new EstadoReservaInvalidoException("Una reserva cancelada no puede cancelarse nuevamente");
        }
        this.estado = EstadoReserva.CANCELADA;
    }

    public UUID getId() { return id; }
    public EstadoReserva getEstado() { return estado; }
}