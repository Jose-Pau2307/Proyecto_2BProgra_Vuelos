
package modelo;

import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private String dniPasajero;
    private Vuelo vueloIda;
    private Vuelo vueloVuelta; 
    private List<Asiento> asientosIda; // CAMBIADO A List<Asiento>
    private List<Asiento> asientosVuelta; 
    private double totalPagar;
    private String codigoReserva; // Si no lo tienes, añádelo para identificarla

    public String getCodigoReserva() { return codigoReserva; }
    
    public Reserva() {
        // Inicializar las listas para evitar NullPointerException
        this.asientosIda = new ArrayList<>();
        this.asientosVuelta = new ArrayList<>();
    }

    // Getters y Setters unificados
    
    public String getDniPasajero() { return dniPasajero; }
    public void setDniPasajero(String dniPasajero) { this.dniPasajero = dniPasajero; }

    public Vuelo getVueloIda() { return vueloIda; }
    public void setVueloIda(Vuelo vueloIda) { this.vueloIda = vueloIda; }

    public Vuelo getVueloVuelta() { return vueloVuelta; }
    public void setVueloVuelta(Vuelo vueloVuelta) { this.vueloVuelta = vueloVuelta; }

    public List<Asiento> getAsientosIda() { 
        return asientosIda; 
    }
    public void setAsientosIda(List<Asiento> asientosIda) { 
        this.asientosIda = asientosIda; }

    public List<Asiento> getAsientosVuelta() { return asientosVuelta; }
    public void setAsientosVuelta(List<Asiento> asientosVuelta) { this.asientosVuelta = asientosVuelta; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }
    
    
     // El método toString ayuda a ver los asientos como texto en la tabla
    public String asientosToString(List<Asiento> lista) {
        if (lista == null || lista.isEmpty()) return "Ninguno";
        return lista.toString(); // Asumiendo que Asiento tiene un toString (ej: "A1")
    }
    
    public String getResumenAsientos(List<Asiento> lista) {
    if (lista == null || lista.isEmpty()) return "Sin asientos";
    String resumen = "";
    for (Asiento a : lista) {
        // Asumiendo que tu clase Asiento tiene un método getNumero() o similar
        resumen += a.getNumero() + " "; 
    }
    return resumen.trim();
}
}
