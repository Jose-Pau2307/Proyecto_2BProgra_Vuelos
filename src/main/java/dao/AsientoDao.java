
package dao;

import java.util.ArrayList;
import java.util.List;
import modelo.Asiento;


public class AsientoDao {
    // Lista de asientos predefinidos para simular asientos ocupados consistentes
    private static final String[] ASIENTOS_OCUPADOS = {
        "1A", "3C", "4F", "5B", "6D",  // Ocupados en ida
        "2A", "3D", "4E", "6F", "1B"   // Ocupados en vuelta
    };
    
    public static List<Asiento> generarAsientosParaVuelo(String clase, boolean esVuelta) {
        List<Asiento> asientos = new ArrayList<>();
        
        // Definir configuraciones según clase
        int filas = 0;
        switch(clase.toLowerCase()) {
            case "económica":
            case "economica":
                filas = 6;  // 6 filas para todas las clases (para simplificar)
                break;
            case "media":
                filas = 6;
                break;
            case "vip":
                filas = 6;
                break;
            default:
                filas = 6;
        }
        
        // Generar asientos A-F (6 asientos por fila)
        for (int fila = 1; fila <= filas; fila++) {
            char[] letras = {'A', 'B', 'C', 'D', 'E', 'F'};
            
            for (char letra : letras) {
                String numero = fila + "" + letra;  // Ejemplo: "1A", "1B", etc.
                String tipo = determinarTipoAsiento(letra);
                
                // Precio extra según tipo
                double precioExtra = calcularPrecioExtra(tipo);
                
                // Determinar si está disponible
                boolean disponible = estaDisponible(numero, esVuelta);
                
                Asiento asiento = new Asiento();
                asiento.setNumero(numero);
                asiento.setTipo(tipo);
                asiento.setPrecioExtra(precioExtra);
                asiento.setSeccion(clase);
                asiento.setDisponible(disponible);
                asiento.setFila(fila);
                asiento.setLetra(String.valueOf(letra));
                
                asientos.add(asiento);
            }
        }
        
        return asientos;
    }
    
    private static String determinarTipoAsiento(char letra) {
        switch(letra) {
            case 'A':
            case 'F':
                return "ventana";
            case 'C':
            case 'D':
                return "pasillo";
            case 'B':
            case 'E':
            default:
                return "centro";
        }
    }
    
    private static double calcularPrecioExtra(String tipo) {
        switch(tipo) {
            case "ventana":
                return 15.00;
            case "pasillo":
                return 10.00;
            case "centro":
            default:
                return 0.00;
        }
    }
    
    private static boolean estaDisponible(String numeroAsiento, boolean esVuelta) {
        // Si es vuelta, usar los primeros 5 asientos ocupados
        // Si es ida, usar los segundos 5 asientos ocupados
        int inicio = esVuelta ? 0 : 5;
        int fin = esVuelta ? 4 : 9;
        
        for (int i = inicio; i <= fin; i++) {
            if (i < ASIENTOS_OCUPADOS.length && ASIENTOS_OCUPADOS[i].equals(numeroAsiento)) {
                return false; // Está ocupado
            }
        }
        return true; // Está disponible
    }
    
    // Método para obtener solo los asientos disponibles
    public static List<Asiento> obtenerAsientosDisponibles(String clase, boolean esVuelta) {
        List<Asiento> todos = generarAsientosParaVuelo(clase, esVuelta);
        List<Asiento> disponibles = new ArrayList<>();
        
        for (Asiento a : todos) {
            if (a.isDisponible()) {
                disponibles.add(a);
            }
        }
        
        return disponibles;
    }
    
    // Método para marcar un asiento como ocupado
    public static boolean reservarAsiento(String numeroAsiento, String clase, boolean esVuelta) {
        // En una implementación real, esto actualizaría la base de datos
        // Por ahora, solo verificamos si está disponible
        List<Asiento> asientos = generarAsientosParaVuelo(clase, esVuelta);
        
        for (Asiento a : asientos) {
            if (a.getNumero().equals(numeroAsiento)) {
                return a.isDisponible(); // Retorna true si se pudo reservar (estaba disponible)
            }
        }
        
        return false; // No se encontró el asiento
    }
    
    // Filtrar asientos por tipo (ventana, pasillo, centro)
    public static List<Asiento> filtrarPorTipo(List<Asiento> asientos, String tipo) {
        List<Asiento> filtrados = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.getTipo().equalsIgnoreCase(tipo) && a.isDisponible()) {
                filtrados.add(a);
            }
        }
        return filtrados;
    }
    
    // Método para obtener el precio de un asiento específico
    public static double obtenerPrecioAsiento(String numeroAsiento, String clase, boolean esVuelta) {
        List<Asiento> asientos = generarAsientosParaVuelo(clase, esVuelta);
        
        for (Asiento a : asientos) {
            if (a.getNumero().equals(numeroAsiento)) {
                return a.getPrecioExtra();
            }
        }
        
        return 0.0; // Precio base si no se encuentra
    }
}
