
package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.Reserva;

public class ControladorReserva {
    private List<Reserva> listaReservas;
    private Reserva reservaActual;
    
    // Instancia única (Singleton) para que todos los formularios vean la misma lista
    private static ControladorReserva instancia;

    private ControladorReserva() {
        this.listaReservas = new ArrayList<>();
        this.reservaActual = new Reserva(); // Inicializamos una vacía
    }

    public static ControladorReserva getInstancia() {
        if (instancia == null) {
            instancia = new ControladorReserva();
        }
        return instancia;
    }

    public void reiniciarReserva() {
    this.reservaActual = new Reserva(); // Simplemente crea una nueva instancia limpia
}
    public List<Reserva> getListaReservas() {
        return listaReservas;
    }

    // 3. Método para obtener la reserva que se está editando ahora
    public Reserva getReservaActual() {
        return reservaActual;
    }

    // 4. Método para guardar la reserva actual en la lista histórica
    public void finalizarYGuardarReserva() {
        if (reservaActual != null) {
            listaReservas.add(reservaActual);
            // Creamos una nueva reserva limpia para el siguiente pasajero
            reservaActual = new Reserva(); 
        }
    }
}
