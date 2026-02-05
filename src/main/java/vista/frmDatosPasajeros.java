/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author USUARIO
 */


import modelo.Pasajero;
import modelo.Vuelo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import java.util.List;

import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

public class frmDatosPasajeros extends javax.swing.JFrame {

    // --- Variables de Lógica ---
    private Vuelo vueloIda;
    private Vuelo vueloVuelta;
    private List<String> asientosIda;
    private List<String> asientosVuelta;
    private String claseIda, claseVuelta;
    private int adultos, ninos, bebes;
    private double totalPagar;

    private List<Pasajero> listaPasajeros = new ArrayList<>();
    private int indiceActual = 0;
    private int totalPasajerosConAsiento;

    // --- Componentes de la Interfaz (Estilo NetBeans) ---
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblContador;

    private javax.swing.JLabel lblNombre;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JTextField txtIdentificacion;
    private javax.swing.JLabel lblCelular;
    private javax.swing.JTextField txtCelular;

    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton btnSiguiente;

    // --- Constructor ---
    public frmDatosPasajeros(Vuelo vueloIda, Vuelo vueloVuelta,
            List<String> asientosIda, List<String> asientosVuelta,
            String claseIda, String claseVuelta,
            int adultos, int ninos, int bebes, double totalPagar) {

        this.vueloIda = vueloIda;
        this.vueloVuelta = vueloVuelta;
        this.asientosIda = asientosIda;
        this.asientosVuelta = asientosVuelta;
        this.claseIda = claseIda;
        this.claseVuelta = claseVuelta;
        this.adultos = adultos;
        this.ninos = ninos;
        this.bebes = bebes;
        this.totalPagar = totalPagar;
        this.totalPasajerosConAsiento = adultos + ninos;

        initComponents(); // Llamada al método constructor de UI
        actualizarPantalla();
        setLocationRelativeTo(null);
    }

    // --- Método initComponents (Imitando a NetBeans) ---
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registro de Pasajeros - VuelosFIS");
        setResizable(false);

        // Panel Principal Blanco
        jPanel1 = new javax.swing.JPanel();
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        // Header Azul (Igual a tu login)
        jPanelHeader = new javax.swing.JPanel();
        jPanelHeader.setBackground(new java.awt.Color(24, 84, 144));
        jPanelHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo = new javax.swing.JLabel();
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24));
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Datos del Pasajero");
        jPanelHeader.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblContador = new javax.swing.JLabel();
        lblContador.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblContador.setForeground(new java.awt.Color(204, 204, 204));
        lblContador.setText("Pasajero 1 de X");
        jPanelHeader.add(lblContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 55, -1, -1));

        jPanel1.add(jPanelHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 90));

        // Formulario
        lblSubtitulo = new javax.swing.JLabel();
        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblSubtitulo.setForeground(new java.awt.Color(16, 0, 79));
        lblSubtitulo.setText("Ingrese los datos del Adulto:");
        jPanel1.add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        // Campo Nombre
        lblNombre = new javax.swing.JLabel();
        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblNombre.setText("Nombres:");
        jPanel1.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        txtNombre = new javax.swing.JTextField();
        txtNombre.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 175, 400, 30));

        // Campo Apellido
        lblApellido = new javax.swing.JLabel();
        lblApellido.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblApellido.setText("Apellidos:");
        jPanel1.add(lblApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));

        txtApellido = new javax.swing.JTextField();
        txtApellido.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel1.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 245, 400, 30));

        // Campo Identificación
        lblCedula = new javax.swing.JLabel();
        lblCedula.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblCedula.setText("Identificación (C.I. / Pasaporte):");
        jPanel1.add(lblCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        txtIdentificacion = new javax.swing.JTextField();
        txtIdentificacion.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel1.add(txtIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 315, 190, 30));

        // Campo Celular
        lblCelular = new javax.swing.JLabel();
        lblCelular.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblCelular.setText("Celular de contacto:");
        jPanel1.add(lblCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, -1, -1));

        txtCelular = new javax.swing.JTextField();
        txtCelular.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel1.add(txtCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 315, 190, 30));

        // Separador
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 400, 10));

        // Botón Siguiente
        btnSiguiente = new javax.swing.JButton();
        btnSiguiente.setBackground(new java.awt.Color(24, 84, 144));
        btnSiguiente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setText("Siguiente Pasajero >");
        btnSiguiente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 390, 180, 40));

        // Configuración final del JFrame
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        pack();
    }

    // --- Lógica del Botón ---
    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellido.getText().trim().isEmpty()
                || txtIdentificacion.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Por favor complete los campos obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = (indiceActual < adultos) ? "Adulto" : "Niño";

        Pasajero p = new Pasajero(
                txtNombre.getText(),
                txtApellido.getText(),
                txtIdentificacion.getText(),
                txtCelular.getText(),
                tipo
        );

        listaPasajeros.add(p);
        indiceActual++;

        if (indiceActual < totalPasajerosConAsiento) {
            actualizarPantalla();
        } else {
            abrirPago();
        }
    }

    // --- Métodos Auxiliares ---
    private void actualizarPantalla() {
        // Limpiar campos
        txtNombre.setText("");
        txtApellido.setText("");
        txtIdentificacion.setText("");
        txtCelular.setText("");
        txtNombre.requestFocus();

        // Actualizar textos
        lblContador.setText("Pasajero " + (indiceActual + 1) + " de " + totalPasajerosConAsiento);

        String tipo = (indiceActual < adultos) ? "Adulto" : "Niño";
        lblSubtitulo.setText("Ingrese los datos del " + tipo + ":");

        if (indiceActual == totalPasajerosConAsiento - 1) {
            btnSiguiente.setText("Ir a Pagar");
            btnSiguiente.setBackground(new java.awt.Color(0, 153, 51)); // Verde éxito
        }
    }

    private void abrirPago() {
        frmPago pago = new frmPago(vueloIda, vueloVuelta, asientosIda, asientosVuelta,
                listaPasajeros, totalPagar);
        pago.setVisible(true);
        this.dispose();
    }
}
