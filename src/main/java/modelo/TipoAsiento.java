package modelo;

public enum TipoAsiento {
    ESTANDAR("Estándar", 11),
    EMERGENCIA("Salida de emergencia", 14),
    ADELANTE("Más adelante", 16);

    private final String nombre;
    private final double precio;

    TipoAsiento(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }
}
