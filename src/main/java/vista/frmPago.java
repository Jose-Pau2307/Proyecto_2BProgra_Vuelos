/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import modelo.Pasajero;
import modelo.Vuelo;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dilan
 */
public class frmPago extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmPago.class.getName());

    private modelo.Vuelo vueloIda;
    private modelo.Vuelo vueloVuelta;
    private java.util.List<String> asientosIda;
    private java.util.List<String> asientosVuelta;
    private java.util.List<modelo.Pasajero> pasajeros;
    private double montoTotal;

    // Constructor que recibe TODOS los datos
    public frmPago(modelo.Vuelo vueloIda, modelo.Vuelo vueloVuelta,
            java.util.List<String> asientosIda, java.util.List<String> asientosVuelta,
            java.util.List<modelo.Pasajero> pasajeros, double montoTotal) {

        // 1. Iniciar componentes visuales
        initComponents();
        setLocationRelativeTo(null); // Centrar en pantalla

        // 2. Guardar los datos en las variables
        this.vueloIda = vueloIda;
        this.vueloVuelta = vueloVuelta;
        this.asientosIda = asientosIda;
        this.asientosVuelta = asientosVuelta;
        this.pasajeros = pasajeros;
        this.montoTotal = montoTotal;

        // 3. Mostrar el total en la etiqueta (Asegúrate que tu label se llame así)
        lblTotalPagar.setText("Total a Pagar: $" + String.format("%.2f", montoTotal));
    }

    /**
     * Creates new form frmPago
     */
    public frmPago() {
        initComponents();
    }

    private void generarFacturaTXT() {
        String nombreArchivo = "Ticket_" + System.currentTimeMillis() + ".txt";
        
        try (java.io.FileWriter fw = new java.io.FileWriter(nombreArchivo);
             java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {
            
            // Escribir el encabezado
            pw.println("==========================================");
            pw.println("          VUELOS FIS - TICKET             ");
            pw.println("==========================================");
            pw.println("Fecha: " + new java.util.Date());
            pw.println("Titular: " + txtNombreTitular.getText());
            pw.println("Tarjeta: ****-****-****-" + txtTarjeta.getText().substring(Math.max(0, txtTarjeta.getText().length() - 4)));
            pw.println("------------------------------------------");
            
            // Datos del Vuelo de Ida
            pw.println("VUELO IDA: " + vueloIda.getCodigo()); 
            pw.println("Ruta: " + vueloIda.getOrigen() + " -> " + vueloIda.getDestino());
            pw.println("Asientos: " + asientosIda);
            
            // Datos del Vuelo de Vuelta (si existe)
            if (vueloVuelta != null) {
                pw.println("------------------------------------------");
                pw.println("VUELO VUELTA: " + vueloVuelta.getCodigo());
                pw.println("Ruta: " + vueloVuelta.getOrigen() + " -> " + vueloVuelta.getDestino());
                pw.println("Asientos: " + asientosVuelta);
            }
            
            // Lista de Pasajeros
            pw.println("------------------------------------------");
            pw.println("PASAJEROS:");
            for (modelo.Pasajero p : pasajeros) {
                pw.println("- " + p.getNombre() + " " + p.getApellido() + " (ID: " + p.getIdentificacion() + ")");
            }
            
            // Total
            pw.println("------------------------------------------");
            pw.println("TOTAL PAGADO: $" + String.format("%.2f", montoTotal));
            pw.println("==========================================");
            
            // Mensaje de éxito
            javax.swing.JOptionPane.showMessageDialog(this, 
                "¡Pago Aprobado Exitosamente!\n\n" +
                "Se ha generado su ticket: " + nombreArchivo + "\n" +
                "(Revise la carpeta del proyecto)", 
                "Transacción Finalizada", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            vista.frmPrincipal principal = new vista.frmPrincipal(); // Asegúrate que el nombre de la clase sea exacto
            principal.setVisible(true);
            this.dispose(); // Cierra la aplicación
            
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar el ticket: " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTotalPagar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTarjeta = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombreTitular = new javax.swing.JTextField();
        txtVencimiento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCvv = new javax.swing.JTextField();
        btnPagar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(194, 215, 236));

        jLabel1.setText("Pasarela de Pago");

        jLabel2.setText("Total a pargar");

        lblTotalPagar.setText("0.00");

        jLabel3.setText("Número de Tarjeta");

        txtTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarjetaActionPerformed(evt);
            }
        });
        txtTarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTarjetaKeyTyped(evt);
            }
        });

        jLabel4.setText("Nombre del Titular:");

        jLabel5.setText("Vencimiento (MM/YY)");

        txtNombreTitular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreTitularActionPerformed(evt);
            }
        });
        txtNombreTitular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreTitularKeyTyped(evt);
            }
        });

        txtVencimiento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtVencimientoKeyTyped(evt);
            }
        });

        jLabel6.setText("CVC");

        txtCvv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCvvKeyTyped(evt);
            }
        });

        btnPagar.setText("Pagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(168, 168, 168)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTarjeta, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                    .addComponent(txtCvv)
                                    .addComponent(txtVencimiento)
                                    .addComponent(txtNombreTitular))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                .addComponent(btnPagar))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblTotalPagar)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(51, 51, 51))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblTotalPagar))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagar))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNombreTitular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCvv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(40, 40, 40))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 310));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreTitularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreTitularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreTitularActionPerformed

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        if (txtTarjeta.getText().trim().isEmpty()
                || txtNombreTitular.getText().trim().isEmpty()
                || txtCvv.getText().trim().isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor complete los datos del pago.",
                    "Datos incompletos",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Intentar generar el ticket
        try {
            generarFacturaTXT(); // Llamamos al método que crea el archivo

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ocurrió un error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnPagarActionPerformed

    private void txtTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarjetaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTarjetaActionPerformed

    private void txtTarjetaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTarjetaKeyTyped
        char c = evt.getKeyChar();
    // 1. Verificar si es un número
        if (!Character.isDigit(c)) {
            evt.consume(); // Ignora la tecla si no es número
        }
    // 2. Limitar a 16 caracteres
        if (txtTarjeta.getText().length() >= 16) {
            evt.consume(); // Ignora si ya llegó al límite
        }
    }//GEN-LAST:event_txtTarjetaKeyTyped

    private void txtVencimientoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVencimientoKeyTyped
        char c = evt.getKeyChar();
        String textoActual = txtVencimiento.getText();
    // 1. Solo permitir números
        if (!Character.isDigit(c)) {
            evt.consume();
            return; // Salimos para no ejecutar lo siguiente
        }
    // 2. Limitar a un máximo de 5 caracteres (MM/YY)
        if (textoActual.length() >= 5) {
            evt.consume();
            return;
        }
    // 3. Auto-formato: Poner la "/" automáticamente
        if (textoActual.length() == 2) {
            txtVencimiento.setText(textoActual + "/");
        }
    }//GEN-LAST:event_txtVencimientoKeyTyped

    private void txtCvvKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCvvKeyTyped
        char c = evt.getKeyChar();
    // 1. Verificar si es un número
        if (!Character.isDigit(c)) {
            evt.consume(); // Ignora la tecla si no es número
        }
    // 2. Limitar a 3 caracteres
        if (txtTarjeta.getText().length() >= 3) {
            evt.consume(); // Ignora si ya llegó al límite
        }
    }//GEN-LAST:event_txtCvvKeyTyped

    private void txtNombreTitularKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreTitularKeyTyped
        char c = evt.getKeyChar();
    // 1. Permitir letras, espacios y teclas de control (como borrar) y usamos ! para decir: "Si NO es letra Y NO es espacio Y NO es borrar..."
        if (!(Character.isLetter(c) || c == java.awt.event.KeyEvent.VK_SPACE || c == java.awt.event.KeyEvent.VK_BACK_SPACE)) {
            evt.consume(); // Bloquea la tecla (números o símbolos)
        }
    // 2. Opcional: Convertir automáticamente a MAYÚSCULAS mientras escribe
        if (Character.isLowerCase(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }
    }//GEN-LAST:event_txtNombreTitularKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmPago().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPagar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTotalPagar;
    private javax.swing.JTextField txtCvv;
    private javax.swing.JTextField txtNombreTitular;
    private javax.swing.JTextField txtTarjeta;
    private javax.swing.JTextField txtVencimiento;
    // End of variables declaration//GEN-END:variables
}
