// CarritoReserva.java
package modelo;

import java.util.ArrayList;
import java.util.List;

public class CarritoReserva {
    private List<Vuelo> vuelosSeleccionados;
    private double total;
    
    public CarritoReserva() {
        this.vuelosSeleccionados = new ArrayList<>();
        this.total = 0.0;
    }
    
    public void agregarVuelo(Vuelo vuelo) {
        if (!vuelosSeleccionados.contains(vuelo)) {
            vuelosSeleccionados.add(vuelo);
            total += vuelo.getPrecio();
        }
    }
    
    public void eliminarVuelo(Vuelo vuelo) {
        if (vuelosSeleccionados.remove(vuelo)) {
            total -= vuelo.getPrecio();
        }
    }
    
    public void vaciarCarrito() {
        vuelosSeleccionados.clear();
        total = 0.0;
    }
    
    public List<Vuelo> getVuelos() {
        return new ArrayList<>(vuelosSeleccionados);
    }
    
    public double getTotal() {
        return total;
    }
    
    public int getCantidad() {
        return vuelosSeleccionados.size();
    }
    
    public boolean contieneVuelo(Vuelo vuelo) {
        return vuelosSeleccionados.contains(vuelo);
    }
}