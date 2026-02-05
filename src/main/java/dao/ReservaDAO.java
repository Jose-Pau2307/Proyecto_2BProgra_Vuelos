
package dao;

import controlador.GestionArchivos;
import java.util.stream.Collectors;
import modelo.Asiento;
import modelo.Reserva; 

public class ReservaDAO {
    private GestionArchivos gestion;

    public ReservaDAO() {
        this.gestion = new GestionArchivos();
    }

    public boolean insertarReserva(Reserva r) {
        // Extraemos solo los números de los asientos para guardarlos como String
        String numAsientosIda = r.getAsientosIda().stream()
                                 .map(Asiento::getNumero)
                                 .collect(Collectors.joining(","));
        
        String linea = r.getDniPasajero() + "|" +
                       (r.getVueloIda() != null ? r.getVueloIda().getCodigo() : "N/A") + "|" +
                       "[" + numAsientosIda + "]" + "|" +
                       r.getTotalPagar();
        
        return gestion.guardarNuevaReserva(linea);
    }
}
