/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USUARIO
 */


public class Pasajero {
    private String nombre;
    private String apellido;
    private String identificacion;
    private String celular; // "celular y no sé qué más" según tu imagen
    private String tipo;    // Adulto o Niño

    public Pasajero(String nombre, String apellido, String identificacion, String celular, String tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.identificacion = identificacion;
        this.celular = celular;
        this.tipo = tipo;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getIdentificacion() { return identificacion; }
    public String getTipo() { return tipo; }
    
    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + identificacion + ")";
    }
}
