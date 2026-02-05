package dao;

import modelo.Vuelo;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VueloDAO {

    private static final String ARCHIVO_DESTINOS = "/csv/destinos.csv";
    private static final String ARCHIVO_VUELOS_IDA = "/csv/vuelos_ida.csv";
    private static final String ARCHIVO_VUELOS_VUELTA = "/csv/vuelos_vuelta.csv";

    // ================= DESTINOS =================
    public static List<String> obtenerDestinosUnicos() {
        List<String> destinos = new ArrayList<>();

        try (
            InputStream is = VueloDAO.class.getResourceAsStream(ARCHIVO_DESTINOS);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                destinos.add(linea.trim());
            }

        } catch (Exception e) {
            return List.of(
                "Quito", "Guayaquil", "Cuenca", "Manta", "Portoviejo",
                "Loja", "Ambato", "Riobamba", "Ibarra", "Tulcán"
            );
        }

        return destinos;
    }

    // ================= OBTENER TODOS =================
    public static List<Vuelo> obtenerTodosVuelosIda() {
        return buscarVuelos("", "", "", ARCHIVO_VUELOS_IDA);
    }

    public static List<Vuelo> obtenerTodosVuelosVuelta() {
        return buscarVuelos("", "", "", ARCHIVO_VUELOS_VUELTA);
    }

    // ================= BUSCAR =================
    public static List<Vuelo> buscarVuelosIda(String origen, String destino, String fecha) {
        return buscarVuelos(origen, destino, fecha, ARCHIVO_VUELOS_IDA);
    }

    public static List<Vuelo> buscarVuelosVuelta(String origen, String destino, String fecha) {
        return buscarVuelos(origen, destino, fecha, ARCHIVO_VUELOS_VUELTA);
    }

    // ================= MÉTODO GENÉRICO =================
    private static List<Vuelo> buscarVuelos(String origen, String destino,
                                            String fecha, String archivo) {

        List<Vuelo> vuelos = new ArrayList<>();

        try (
            InputStream is = VueloDAO.class.getResourceAsStream(archivo);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split(",");

                if (datos.length >= 5) {
                    String codigo = datos[0].trim();
                    String orig = datos[1].trim();
                    String dest = datos[2].trim();
                    double precio = Double.parseDouble(datos[3].trim());
                    String avion = datos[4].trim();

                    // 🔑 FILTRO FLEXIBLE
                    boolean coincideOrigen = origen.isEmpty() || orig.equalsIgnoreCase(origen);
                    boolean coincideDestino = destino.isEmpty() || dest.equalsIgnoreCase(destino);

                    if (coincideOrigen && coincideDestino) {
                        Vuelo v = new Vuelo();
                        v.setCodigo(codigo);
                        v.setOrigen(orig);
                        v.setDestino(dest);
                        v.setPrecio(precio);
                        v.setAvion(avion);
                        v.setFecha(fecha);

                        vuelos.add(v);
                    }
                }
            }

        } catch (Exception e) {
            vuelos = crearVuelosEjemplo(origen, destino, fecha, archivo);
        }

        return vuelos;
    }

    // ================= VUELOS DE EJEMPLO =================
    private static List<Vuelo> crearVuelosEjemplo(String origen, String destino,
                                                  String fecha, String archivo) {

        List<Vuelo> vuelos = new ArrayList<>();
        Random r = new Random();

        String prefijo = archivo.contains("vuelta") ? "VR" : "ID";
        int cantidad = 3 + r.nextInt(3);

        if (origen.isEmpty()) origen = "Quito";
        if (destino.isEmpty()) destino = "Guayaquil";

        for (int i = 1; i <= cantidad; i++) {
            Vuelo v = new Vuelo();
            v.setCodigo(prefijo + String.format("%03d", i));
            v.setOrigen(origen);
            v.setDestino(destino);
            v.setPrecio(80 + r.nextInt(120));
            v.setAvion("A320");
            v.setFecha(fecha.isEmpty() ? "2026-01-01" : fecha);
            vuelos.add(v);
        }

        return vuelos;
    }

    // ================= VALIDAR RUTA =================
    public static boolean existeRuta(String origen, String destino) {
        List<String> destinos = obtenerDestinosUnicos();
        return destinos.contains(origen) && destinos.contains(destino);
    }
}
