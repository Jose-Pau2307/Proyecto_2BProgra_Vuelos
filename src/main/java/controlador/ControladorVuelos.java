package controlador;

import dao.VueloDAO;
import modelo.Vuelo;
import java.util.List;

public class ControladorVuelos {

    public List<Vuelo> obtenerTodosVuelosIda() {
        return VueloDAO.obtenerTodosVuelosIda();
    }

    public List<Vuelo> obtenerTodosVuelosVuelta() {
        return VueloDAO.obtenerTodosVuelosVuelta();
    }

    public List<Vuelo> buscarVuelosIda(String origen, String destino, String fecha) {
        return VueloDAO.buscarVuelosIda(origen, destino, fecha);
    }

    public List<Vuelo> buscarVuelosVuelta(String origen, String destino, String fecha) {
        return VueloDAO.buscarVuelosVuelta(origen, destino, fecha);
    }
}
