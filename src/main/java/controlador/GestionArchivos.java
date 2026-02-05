
package controlador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class GestionArchivos {
    // Rutas de los archivos dentro de la carpeta src
    private final String RUTA_VUELOS = "src/archivos/vuelos.txt";
    private final String RUTA_RESERVAS = "src/archivos/reservas.txt";

    public GestionArchivos() {
        verificarArchivos();
    }

    // Crea los archivos si no existen para evitar errores
    private void verificarArchivos() {
        try {
            File directorio = new File("src/archivos");
            if (!directorio.exists()) directorio.mkdirs();
            
            File fReservas = new File(RUTA_RESERVAS);
            if (!fReservas.exists()) fReservas.createNewFile();
        } catch (IOException e) {
            System.err.println("Error al inicializar archivos: " + e.getMessage());
        }
    }

    // GUARDAR RESERVA: Escribe una línea nueva con formato CSV (separado por comas o pipes)
    public boolean guardarNuevaReserva(String lineaReserva) {
        try (FileWriter fw = new FileWriter(RUTA_RESERVAS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            
            pw.println(lineaReserva);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
            return false;
        }
    }

    // LEER RESERVAS: Devuelve una lista de líneas para llenar la tabla de "Mis Reservas"
    public List<String> obtenerTodasLasReservas() {
        List<String> datos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_RESERVAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    datos.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer reservas: " + e.getMessage());
        }
        return datos;
    }
    
    // BUSCAR VUELOS: Para cargar la tabla inicial desde el archivo de texto
    public List<String> cargarVuelosDisponibles() {
        List<String> vuelos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_VUELOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                vuelos.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Crea el archivo vuelos.txt en src/archivos/");
        }
        return vuelos;
    }
}
