package modelo;

public class Vuelo {
    private String codigo;
    private String origen;
    private String destino;
    private String fecha;
    private double precio;
    private String avion;
    
    // Constructor vacío
    public Vuelo() {}
    
    // Constructor con parámetros
    public Vuelo(String codigo, String origen, String destino, double precio, String avion) {
        this.codigo = codigo;
        this.origen = origen;
        this.destino = destino;
        this.precio = precio;
        this.avion = avion;
    }
    
    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public String getAvion() { return avion; }
    public void setAvion(String avion) { this.avion = avion; }
    
    @Override
    public String toString() {
        return codigo + " - " + origen + " → " + destino + " - $" + precio;
    }
}