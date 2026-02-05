package vista;

import dao.VueloDAO;
import modelo.Vuelo;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class frmSeleccionVuelos extends JFrame {

    private String origen;
    private String destino;
    private String fechaIda;
    private String fechaVuelta;
    private boolean soloIda;
    private Vuelo vueloSeleccionado;
    private String claseSeleccionada;
    private double precioFinal;
    private int filaSeleccionada = -1;
    private int adultos;
    private int ninos;
    private int bebes;
    private static final double DESCUENTO_NINO = 0.30; // 30%
    private static final double DESCUENTO_BEBE = 0.90; // 90%


    public frmSeleccionVuelos(String origen, String destino, String fechaIda,
                          String fechaVuelta, boolean soloIda,
                          int adultos, int ninos, int bebes) {

    // 1️⃣ Construir la interfaz
    initComponents();

    // 2️⃣ Asignar datos
    this.origen = origen;
    this.destino = destino;
    this.fechaIda = fechaIda;
    this.fechaVuelta = fechaVuelta;
    this.soloIda = soloIda;
    this.adultos = adultos;
    this.ninos = ninos;
    this.bebes = bebes;

    // 3️⃣ Configuraciones visuales
    setLocationRelativeTo(null);

    // 4️⃣ Configurar tabla e interfaz
    configurarTabla();
    configurarInterfazDespuesDeInit();
    cargarVuelosIda();
}

    
    private void configurarInterfazDespuesDeInit() {
        tblVuelos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVuelosMouseClicked(evt);
            }
        });
        
        if (soloIda) {
            jblTitulo.setText("Vuelos de Ida: " + origen + " → " + destino);
        } else {
            jblTitulo.setText("Vuelos de Ida: " + origen + " → " + destino);
        }
        jLabel5.setText(origen + " → " + destino + " | Fecha: " + fechaIda);
        
        jPanel1.setBackground(new Color(240, 248, 255));
    }
    
    private void configurarTabla() {
        String[] columnas = {"Código", "Origen", "Destino", "Hora Salida", "Duración", "Precio", "Seleccionar"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        tblVuelos.setModel(modelo);
        
        tblVuelos.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblVuelos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblVuelos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblVuelos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblVuelos.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblVuelos.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblVuelos.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblVuelos.getColumnCount(); i++) {
            tblVuelos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void cargarVuelosIda() {
        List<Vuelo> vuelos = VueloDAO.buscarVuelosIda(origen, destino, fechaIda);
        
        DefaultTableModel modelo = (DefaultTableModel) tblVuelos.getModel();
        modelo.setRowCount(0);
        
        Random r = new Random();
        
        for (Vuelo v : vuelos) {
            int hora = 6 + r.nextInt(13);
            int minuto = r.nextInt(60);
            String horaSalida = String.format("%02d:%02d", hora, minuto);
            
            int duracionMinutos = 40 + r.nextInt(81);
            String duracion = duracionMinutos + " min";
            
            modelo.addRow(new Object[]{
                v.getCodigo(),
                v.getOrigen(),
                v.getDestino(),
                horaSalida,
                duracion,
                "$" + v.getPrecio(),
                "Seleccionar"
            });
        }
        
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay vuelos disponibles para esta ruta",
                "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void tblVuelosMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tblVuelos.rowAtPoint(evt.getPoint());
        int columna = tblVuelos.columnAtPoint(evt.getPoint());
        
        if (fila != -1 && columna == 6) {
            String textoBoton = tblVuelos.getValueAt(fila, 6).toString();
            
            // Si ya está seleccionado, deseleccionar
            if (textoBoton.startsWith("✓")) {
                deseleccionarVuelo();
            } else {
                // Si hay otra fila seleccionada, primero deseleccionarla
                if (filaSeleccionada != -1 && filaSeleccionada != fila) {
                    ((DefaultTableModel)tblVuelos.getModel())
                        .setValueAt("Seleccionar", filaSeleccionada, 6);
                }
                mostrarDialogClases(fila);
            }
        }
    }
    
    private void deseleccionarVuelo() {
        if (filaSeleccionada != -1) {
            ((DefaultTableModel)tblVuelos.getModel())
                .setValueAt("Seleccionar", filaSeleccionada, 6);
            
            vueloSeleccionado = null;
            claseSeleccionada = null;
            precioFinal = 0.0;
            filaSeleccionada = -1;
        }
    }
    
    private void mostrarDialogClases(int filaSeleccionada) {
        String codigo = tblVuelos.getValueAt(filaSeleccionada, 0).toString();
        String hora = tblVuelos.getValueAt(filaSeleccionada, 3).toString();
        String precioBase = tblVuelos.getValueAt(filaSeleccionada, 5).toString();
        double precio = Double.parseDouble(precioBase.replace("$", ""));
        
        JDialog dialog = new JDialog(this, "Seleccione la clase del vuelo", true);
        dialog.setSize(600, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(1, 3, 15, 15));
        dialog.getContentPane().setBackground(new Color(240, 240, 240));
        
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("Detalles del vuelo"));
        panelInfo.setBackground(Color.WHITE);
        
        JTextArea txtInfo = new JTextArea();
        txtInfo.setText("Código: " + codigo + "\n" +
                       "Ruta: " + origen + " → " + destino + "\n" +
                       "Fecha: " + fechaIda + "\n" +
                       "Hora: " + hora + "\n" +
                       "Precio base: " + precioBase);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        txtInfo.setOpaque(false);
        
        panelInfo.add(txtInfo, BorderLayout.CENTER);
        dialog.add(panelInfo);
        
        dialog.add(crearPanelClase("Económica",
            "• Equipaje de mano (10kg)\n• Asiento estándar\n• Selección básica",
            precio, 0, dialog, filaSeleccionada));
        
        dialog.add(crearPanelClase("Media",
            "• Maleta 23kg\n• Snack a bordo\n• Asiento reclinable\n• Prioridad en embarque",
            precio, 50, dialog, filaSeleccionada));
        
        dialog.add(crearPanelClase("VIP",
            "• 2 maletas 23kg\n• Comida completa\n• Sala VIP\n• Asiento premium\n• Wi-Fi gratis",
            precio, 150, dialog, filaSeleccionada));
        
        dialog.setVisible(true);
    }
    
    private JPanel crearPanelClase(String nombre, String beneficios, 
                                  double precioBase, int extra, JDialog dialog, int fila) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(nombre),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        double precioTotal = precioBase + extra;
        
        JTextArea txtBeneficios = new JTextArea(beneficios);
        txtBeneficios.setEditable(false);
        txtBeneficios.setOpaque(false);
        txtBeneficios.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JPanel panelPrecio = new JPanel(new BorderLayout());
        JLabel lblPrecio = new JLabel("Precio: $" + precioTotal);
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecio.setForeground(new Color(0, 102, 0));
        panelPrecio.add(lblPrecio, BorderLayout.CENTER);
        
        JButton btnElegir = new JButton("Elegir " + nombre);
        btnElegir.setBackground(new Color(24, 84, 144));
        btnElegir.setForeground(Color.WHITE);
        btnElegir.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnElegir.addActionListener(e -> {
            claseSeleccionada = nombre;
            precioFinal = calcularPrecioTotal(precioTotal);

            
            vueloSeleccionado = new Vuelo();
            vueloSeleccionado.setCodigo(tblVuelos.getValueAt(fila, 0).toString());
            vueloSeleccionado.setOrigen(origen);
            vueloSeleccionado.setDestino(destino);
            vueloSeleccionado.setPrecio(precioTotal);
            vueloSeleccionado.setFecha(fechaIda);
            
            tblVuelos.setRowSelectionInterval(fila, fila);
            
            // Solo mostrar la clase en el botón, sin "Seleccionado"
            ((DefaultTableModel)tblVuelos.getModel()).setValueAt("✓ " + nombre, fila, 6);
            
            filaSeleccionada = fila;
            dialog.dispose();
            // SIN mensaje de confirmación
        });
        
        panel.add(txtBeneficios, BorderLayout.CENTER);
        panel.add(panelPrecio, BorderLayout.NORTH);
        panel.add(btnElegir, BorderLayout.SOUTH);
        
        return panel;
    }

    private double calcularPrecioTotal(double precioBase) {
        double total = 0;

        total += adultos * precioBase;
        total += ninos * (precioBase * (1 - DESCUENTO_NINO));
        total += bebes * (precioBase * (1 - DESCUENTO_BEBE));

        return total;
    }
    
   private void abrirSeleccionAsientosIda() {
    // Calcular pasajeros que necesitan asiento (bebés no ocupan)
    int pasajerosConAsiento = adultos + ninos;
    
    // Calcular precio base del vuelo seleccionado
    double precioBaseVuelo = vueloSeleccionado.getPrecio();
    double precioTotalBase = precioBaseVuelo * pasajerosConAsiento;
    
    // Precios adicionales por clase (puedes ajustar según la clase seleccionada)
    double precioPrimeraClase = 200.00;
    double precioClaseEjecutiva = 100.00;
    
    // IMPORTANTE: Si la clase seleccionada es "VIP" o "Media", ajustar el precio adicional
    double precioAdicionalClase = 0.0;
    if ("VIP".equals(claseSeleccionada)) {
        precioAdicionalClase = 150.00;
    } else if ("Media".equals(claseSeleccionada)) {
        precioAdicionalClase = 50.00;
    }
    
    // Sumar el precio adicional de la clase al precio base
    double precioBaseConClase = precioTotalBase + (precioAdicionalClase * pasajerosConAsiento);
    
    SeleccionAsientosDialog dialog = new SeleccionAsientosDialog(
        this, 
        true, 
        "Seleccione asientos - Vuelo de Ida: " + origen + " a " + destino,
        pasajerosConAsiento,
        precioBaseConClase,  // Precio base incluyendo clase seleccionada
        precioPrimeraClase,  // Precio adicional por primera clase
        precioClaseEjecutiva // Precio adicional por clase ejecutiva
    );
    
    // Configurar asientos ocupados
    List<String> asientosOcupados = obtenerAsientosOcupados(origen, destino, fechaIda);
    dialog.setAsientosOcupados(asientosOcupados);
    
    dialog.setCallback(new SeleccionAsientosDialog.SeleccionAsientosCallback() {
        @Override
        public void onAsientosSeleccionados(List<String> asientos) {
            // Mostrar resumen final del vuelo
            mostrarResumenFinal(asientos);
        }
    });
    
    dialog.setSize(1200, 800);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void mostrarResumenFinal(List<String> asientosSeleccionados) {
    double totalPagar = precioFinal; // precioFinal ya incluye descuentos de niños/bebés
    
    StringBuilder resumen = new StringBuilder();
    resumen.append("=== RESERVA COMPLETADA ===\n\n");
    resumen.append("VUELO DE IDA (SOLO IDA):\n");
    resumen.append("• Código: ").append(vueloSeleccionado.getCodigo()).append("\n");
    resumen.append("• Ruta: ").append(origen).append(" → ").append(destino).append("\n");
    resumen.append("• Fecha: ").append(fechaIda).append("\n");
    resumen.append("• Hora: ").append(tblVuelos.getValueAt(filaSeleccionada, 3)).append("\n");
    resumen.append("• Clase: ").append(claseSeleccionada).append("\n");
    resumen.append("• Duración: ").append(tblVuelos.getValueAt(filaSeleccionada, 4)).append("\n\n");
    
    resumen.append("PASAJEROS:\n");
    resumen.append("• Adultos: ").append(adultos).append(" x $").append(String.format("%.2f", vueloSeleccionado.getPrecio())).append("\n");
    resumen.append("• Niños: ").append(ninos).append(" x $").append(String.format("%.2f", vueloSeleccionado.getPrecio() * (1 - DESCUENTO_NINO))).append("\n");
    if (bebes > 0) {
        resumen.append("• Bebés: ").append(bebes).append(" x $").append(String.format("%.2f", vueloSeleccionado.getPrecio() * (1 - DESCUENTO_BEBE))).append(" (sin asiento)\n");
    }
    
    if ("VIP".equals(claseSeleccionada) || "Media".equals(claseSeleccionada)) {
        double adicionalClase = "VIP".equals(claseSeleccionada) ? 150.00 : 50.00;
        resumen.append("• Adicional clase ").append(claseSeleccionada).append(": $").append(String.format("%.2f", adicionalClase)).append(" por pasajero\n");
    }
    
    resumen.append("\nASIENTOS SELECCIONADOS:\n");
    for (String asiento : asientosSeleccionados) {
        resumen.append("• ").append(asiento).append("\n");
    }
    
    resumen.append("\nTOTAL A PAGAR: $").append(String.format("%.2f", totalPagar));
    
    int opcion = JOptionPane.showConfirmDialog(this,
        resumen.toString(),
        "Reserva Completada - Solo Ida",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.INFORMATION_MESSAGE);
    
    if (opcion == JOptionPane.OK_OPTION) {
        // Aquí podrías abrir el formulario de pago
        JOptionPane.showMessageDialog(this,
            "Reserva confirmada exitosamente!\n\n" +
            "Proceda con el pago de $" + String.format("%.2f", totalPagar),
            "Pago",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Si tienes un formulario de pago:
        // frmPago pago = new frmPago(totalPagar, origen, destino, fechaIda, 
        //                           vueloSeleccionado, asientosSeleccionados, ...);
        // pago.setVisible(true);
        // this.dispose();
        
        // O simplemente cerrar todo
        this.dispose();
    }
}

private List<String> obtenerAsientosOcupados(String origen, String destino, String fecha) {
    // Consultar a la base de datos para obtener asientos ocupados
    List<String> ocupados = new ArrayList<>();
    
    // Simular algunos asientos ocupados para prueba
    // En producción, esto debería venir de la BD
    String[] posiblesOcupados = {"1A", "3C", "5F", "10D", "12E", "15B", "20G"};
    int numOcupados = (int)(Math.random() * 4) + 2;
    
    for (int i = 0; i < numOcupados; i++) {
        ocupados.add(posiblesOcupados[(int)(Math.random() * posiblesOcupados.length)]);
    }
    
    return ocupados;
}


   
    


   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVuelos = new javax.swing.JTable();
        jblTitulo = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnSiguiente = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        tblVuelos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblVuelos.setForeground(new java.awt.Color(16, 0, 79));
        tblVuelos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblVuelos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 600, 180));

        jblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jblTitulo.setForeground(new java.awt.Color(16, 0, 79));
        jblTitulo.setText("Elija un vuelo de Ida");
        jPanel1.add(jblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 24, -1, -1));

        btnVolver.setBackground(new java.awt.Color(153, 153, 153));
        btnVolver.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel1.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 410, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Vuelos Disponibles");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, -1, -1));

        btnSiguiente.setBackground(new java.awt.Color(51, 51, 255));
        btnSiguiente.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 410, 130, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
       new frmPrincipal().setVisible(true);
        this.dispose();
 
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
                                                 
    if (vueloSeleccionado == null) {
        JOptionPane.showMessageDialog(this,
            "Debe seleccionar un vuelo primero",
            "Advertencia",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Verificar que se seleccionó una clase
    if (claseSeleccionada == null) {
        JOptionPane.showMessageDialog(this,
            "Debe seleccionar una clase para el vuelo",
            "Advertencia",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (soloIda) {
        // Si es solo ida, mostrar confirmación y luego ir a asientos
        String mensajeConfirmacion = "✓ VUELO SELECCIONADO:\n" +
                                    "Código: " + vueloSeleccionado.getCodigo() + "\n" +
                                    "Ruta: " + origen + " → " + destino + "\n" +
                                    "Fecha: " + fechaIda + "\n" +
                                    "Clase: " + claseSeleccionada + "\n" +
                                    "Precio total: $" + String.format("%.2f", precioFinal);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            mensajeConfirmacion + "\n\n¿Continuar con la selección de asientos?",
            "Confirmar vuelo",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Ir directamente a selección de asientos
            abrirSeleccionAsientosIda();
        }
    } else {
        // Si es ida y vuelta, ir a frmSeleccionVuelos2
        frmSeleccionVuelos2 frm = new frmSeleccionVuelos2(
            destino, // Origen para el vuelo de vuelta
            origen,  // Destino para el vuelo de vuelta
            fechaVuelta,
            adultos,
            ninos,
            bebes,
            vueloSeleccionado,  // Vuelo de ida seleccionado
            claseSeleccionada,
            precioFinal
        );

        frm.setVisible(true);
        this.dispose();
    }


    }//GEN-LAST:event_btnSiguienteActionPerformed

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
        }  catch (Exception ex) {
            ex.printStackTrace();
        }

        //</editor-fold>

        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnVolver;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jblTitulo;
    private javax.swing.JTable tblVuelos;
    // End of variables declaration//GEN-END:variables
}
