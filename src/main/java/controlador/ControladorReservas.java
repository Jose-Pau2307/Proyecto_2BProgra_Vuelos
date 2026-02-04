
package controlador;

import modelo.Vuelo;
import modelo.Pasajero;
import java.util.ArrayList;
import java.util.List;

public class ControladorReservas {
    private static ControladorReservas instancia;
    
    // Datos temporales de la sesión actual
    private Vuelo vueloIda;
    private Vuelo vueloVuelta;
    private List<String> asientosSeleccionados;
    private double precioTotal;
    private String nombrePasajero;
    private String dniPasajero;

    // Constructor privado para Singleton
    private ControladorReservas() {
        asientosSeleccionados = new ArrayList<>();
    }

    public static ControladorReservas getInstancia() {
        if (instancia == null) {
            instancia = new ControladorReservas();
        }
        return instancia;
    }

    // Métodos para gestionar los datos
    public void reiniciarReserva() {
        vueloIda = null;
        vueloVuelta = null;
        asientosSeleccionados.clear();
        precioTotal = 0;
    }

    public Vuelo getVueloIda() {
        return vueloIda;
    }

    public Vuelo getVueloVuelta() {
        return vueloVuelta;
    }

    public List<String> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public String getDniPasajero() {
        return dniPasajero;
    }
    
    
}
