package modelo;

public class Asiento {
    private String numero;
    private String tipo; // "ventana", "pasillo", "centro"
    private double precioExtra;
    private String seccion; // "Económica", "Media", "VIP"
    private boolean disponible;
    private int fila;
    private String letra;
    
    // Constructor vacío
    public Asiento() {
    }
    
    // Constructor con parámetros
    public Asiento(String numero, String tipo, double precioExtra, String seccion, 
                   boolean disponible, int fila, String letra) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioExtra = precioExtra;
        this.seccion = seccion;
        this.disponible = disponible;
        this.fila = fila;
        this.letra = letra;
    }
    
    // Getters y setters
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public double getPrecioExtra() {
        return precioExtra;
    }
    
    public void setPrecioExtra(double precioExtra) {
        this.precioExtra = precioExtra;
    }
    
    public String getSeccion() {
        return seccion;
    }
    
    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public int getFila() {
        return fila;
    }
    
    public void setFila(int fila) {
        this.fila = fila;
    }
    
    public String getLetra() {
        return letra;
    }
    
    public void setLetra(String letra) {
        this.letra = letra;
    }
    
    @Override
    public String toString() {
        return "Asiento{" +
                "numero='" + numero + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precioExtra=" + precioExtra +
                ", seccion='" + seccion + '\'' +
                ", disponible=" + disponible +
                ", fila=" + fila +
                ", letra='" + letra + '\'' +
                '}';
    }
}