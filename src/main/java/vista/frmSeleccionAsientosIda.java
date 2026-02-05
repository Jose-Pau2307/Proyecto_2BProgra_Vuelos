package vista;

import modelo.Vuelo;
import dao.AsientoDAO;
import modelo.Asiento;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class frmSeleccionAsientosIda extends JFrame {
    
    private Vuelo vuelo;
    private String clase;
    private int adultos;
    private int ninos;
    private int bebes;
    private double precioFinal;
    
    private List<JButton> asientosSeleccionados = new ArrayList<>();
    private List<Asiento> todosLosAsientos = new ArrayList<>();
    private Map<String, Asiento> mapaAsientos = new HashMap<>();
    
    private JPanel panelAsientos;
    private JLabel lblAsientosSeleccionados;
    private JLabel lblTotalSeleccionados;
    private JLabel lblTipoSeleccionado;
    private JLabel lblPrecioExtra;
    
    private static final int FILAS = 6;
    private static final int COLUMNAS = 6;
    
    public frmSeleccionAsientosIda(Vuelo vuelo, String clase, 
                                  int adultos, int ninos, int bebes,
                                  double precioFinal) {
        this.vuelo = vuelo;
        this.clase = clase;
        this.adultos = adultos;
        this.ninos = ninos;
        this.bebes = bebes;
        this.precioFinal = precioFinal;
        
        initComponents();
        configurarDespuesInit();
    }
    
    private void configurarDespuesInit() {
        jblTitulo.setText("Selección de Asientos - " + vuelo.getCodigo());
        jLabel2.setText("Vuelo: " + vuelo.getOrigen() + " → " + vuelo.getDestino() + 
                       " | Clase: " + clase);
        
        // Panel principal con imagen de fondo de avión
        panelAsientos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar silueta de avión
                g.setColor(new Color(220, 220, 220));
                g.fillRect(50, 50, getWidth() - 100, getHeight() - 100);
                
                // Dibujar pasillo central
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(getWidth()/2 - 30, 50, 60, getHeight() - 100);
                
                // Etiquetas de secciones
                g.setColor(Color.DARK_GRAY);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString("VENTANA", 70, 40);
                g.drawString("PASILLO", getWidth()/2 - 15, 40);
                g.drawString("VENTANA", getWidth() - 100, 40);
            }
        };
        panelAsientos.setLayout(new GridLayout(FILAS, COLUMNAS, 3, 3));
        panelAsientos.setBackground(Color.WHITE);
        panelAsientos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        jScrollPane1.setViewportView(panelAsientos);
        crearAsientos();
        
        // Etiquetas de información
        lblAsientosSeleccionados = new JLabel("Asientos seleccionados: 0");
        lblTotalSeleccionados = new JLabel("Necesarios: " + (adultos + ninos));
        lblTipoSeleccionado = new JLabel("Tipo seleccionado: -");
        lblPrecioExtra = new JLabel("Precio extra: $0.00");
        
        jPanel1.add(lblAsientosSeleccionados, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 200, 20));
        jPanel1.add(lblTotalSeleccionados, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 200, 20));
        jPanel1.add(lblTipoSeleccionado, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 470, 200, 20));
        jPanel1.add(lblPrecioExtra, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 490, 200, 20));
        
        configurarEventos();
    }
    
    private void crearAsientos() {
        todosLosAsientos = AsientoDAO.generarAsientosParaVuelo(clase, false);
        
        // Crear mapa para acceso rápido
        for (Asiento a : todosLosAsientos) {
            mapaAsientos.put(a.getNumero(), a);
        }
        
        char[] letras = {'A', 'B', 'C', 'D', 'E', 'F'};
        
        for (int fila = 1; fila <= FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                String idAsiento = fila + "" + letras[col];
                JButton btnAsiento = new JButton(idAsiento);
                
                Asiento asiento = mapaAsientos.get(idAsiento);
                boolean disponible = (asiento != null) ? asiento.isDisponible() : true;
                
                // Color según tipo de asiento
                Color colorFondo;
                if (col == 0 || col == 5) { // Ventana (A y F)
                    colorFondo = new Color(173, 216, 230); // Azul claro
                } else if (col == 2 || col == 3) { // Pasillo (C y D)
                    colorFondo = new Color(144, 238, 144); // Verde claro
                } else { // Centro (B y E)
                    colorFondo = new Color(255, 228, 181); // Amarillo claro
                }
                
                configurarBotonAsiento(btnAsiento, idAsiento, disponible, colorFondo);
                panelAsientos.add(btnAsiento);
            }
        }
    }
    
    private void configurarBotonAsiento(JButton btn, String id, boolean disponible, Color colorBase) {
        btn.setFont(new Font("Arial", Font.BOLD, 10));
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(1, 1, 1, 1));
        
        if (!disponible) {
            btn.setEnabled(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setText(id + " (X)");
            btn.setForeground(Color.RED);
        } else {
            btn.setBackground(colorBase);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            
            btn.addActionListener(e -> {
                seleccionarAsiento(btn, id);
            });
        }
    }
    
    private void seleccionarAsiento(JButton btn, String id) {
        if (asientosSeleccionados.contains(btn)) {
            // Deseleccionar
            asientosSeleccionados.remove(btn);
            Asiento asiento = mapaAsientos.get(id);
            if (asiento != null) {
                // Restaurar color según tipo
                char letra = id.charAt(id.length() - 1);
                if (letra == 'A' || letra == 'F') {
                    btn.setBackground(new Color(173, 216, 230));
                } else if (letra == 'C' || letra == 'D') {
                    btn.setBackground(new Color(144, 238, 144));
                } else {
                    btn.setBackground(new Color(255, 228, 181));
                }
            }
            btn.setForeground(Color.BLACK);
        } else {
            // Verificar límite
            int asientosNecesarios = adultos + ninos;
            if (asientosSeleccionados.size() >= asientosNecesarios) {
                JOptionPane.showMessageDialog(this,
                    "Ya has seleccionado todos los asientos necesarios.\n" +
                    "Deselecciona uno para seleccionar otro.",
                    "Límite alcanzado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Seleccionar
            asientosSeleccionados.add(btn);
            btn.setBackground(Color.CYAN);
            btn.setForeground(Color.BLACK);
            
            // Mostrar información del asiento seleccionado
            Asiento asientoSeleccionado = mapaAsientos.get(id);
            if (asientoSeleccionado != null) {
                lblTipoSeleccionado.setText("Tipo: " + asientoSeleccionado.getTipo());
                lblPrecioExtra.setText("Precio extra: $" + String.format("%.2f", asientoSeleccionado.getPrecioExtra()));
            }
        }
        
        actualizarContadores();
    }
    
    private void actualizarContadores() {
        int seleccionados = asientosSeleccionados.size();
        int necesarios = adultos + ninos;
        
        lblAsientosSeleccionados.setText("Asientos seleccionados: " + seleccionados);
        
        if (seleccionados == necesarios) {
            lblAsientosSeleccionados.setForeground(new Color(0, 153, 76));
        } else {
            lblAsientosSeleccionados.setForeground(Color.RED);
        }
    }
    
    private void mostrarDialogClasesAsientos() {
        JDialog dialog = new JDialog(this, "Información de Tipos de Asientos", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 240, 240));
        dialog.setLayout(new BorderLayout());
        
        // Panel superior con diagrama
        JPanel panelDiagrama = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar diagrama de sección de avión
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Asiento ventana (azul)
                g.setColor(new Color(173, 216, 230));
                g.fillRect(50, 50, 60, 40);
                g.setColor(Color.BLACK);
                g.drawRect(50, 50, 60, 40);
                g.drawString("VENTANA", 55, 45);
                
                // Asiento centro (amarillo)
                g.setColor(new Color(255, 228, 181));
                g.fillRect(130, 50, 60, 40);
                g.setColor(Color.BLACK);
                g.drawRect(130, 50, 60, 40);
                g.drawString("CENTRO", 140, 45);
                
                // Asiento pasillo (verde)
                g.setColor(new Color(144, 238, 144));
                g.fillRect(210, 50, 60, 40);
                g.setColor(Color.BLACK);
                g.drawRect(210, 50, 60, 40);
                g.drawString("PASILLO", 220, 45);
                
                // Flechas indicando ubicación
                g.setColor(Color.RED);
                g.drawLine(80, 100, 80, 150); // Ventana ← ventana del avión
                g.drawString("← Ventana del avión", 30, 180);
                
                g.drawLine(220, 100, getWidth()/2, 150); // Pasillo ← centro
                g.drawString("← Pasillo central", getWidth()/2 - 50, 180);
            }
        };
        panelDiagrama.setPreferredSize(new Dimension(600, 200));
        panelDiagrama.setBackground(Color.WHITE);
        panelDiagrama.setBorder(BorderFactory.createTitledBorder("Diagrama de Sección"));
        
        // Panel de información de tipos
        JPanel panelInfo = new JPanel(new GridLayout(1, 3, 10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInfo.setBackground(new Color(240, 240, 240));
        
        panelInfo.add(crearPanelTipoAsiento("VENTANA", 
            "• Junto a la ventana del avión\n• Vista exterior\n• Más privacidad\n• Precio extra: $15.00",
            new Color(173, 216, 230)));
        
        panelInfo.add(crearPanelTipoAsiento("PASILLO", 
            "• Junto al pasillo central\n• Fácil acceso para salir\n• Más espacio para estirar piernas\n• Precio extra: $10.00",
            new Color(144, 238, 144)));
        
        panelInfo.add(crearPanelTipoAsiento("CENTRO", 
            "• Entre ventana y pasillo\n• Equilibrado\n• Precio base\n• Sin costo adicional",
            new Color(255, 228, 181)));
        
        // Botón de cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(0, 102, 204));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.addActionListener(e -> dialog.dispose());
        
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(240, 240, 240));
        panelBoton.add(btnCerrar);
        
        dialog.add(panelDiagrama, BorderLayout.NORTH);
        dialog.add(panelInfo, BorderLayout.CENTER);
        dialog.add(panelBoton, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel crearPanelTipoAsiento(String titulo, String descripcion, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(color.darker());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JTextArea txtDesc = new JTextArea(descripcion);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtDesc), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarEventos() {
        btnVolver.addActionListener(e -> this.dispose());
        btnInfoClases.addActionListener(e -> mostrarDialogClasesAsientos());
        btnConfirmar.addActionListener(e -> validarSeleccion());
    }
    
    private void validarSeleccion() {
        int asientosNecesarios = adultos + ninos;
        int asientosSeleccionadosCount = asientosSeleccionados.size();
        
        if (asientosSeleccionadosCount < asientosNecesarios) {
            JOptionPane.showMessageDialog(this,
                "Debes seleccionar " + asientosNecesarios + " asientos.\n" +
                "Te faltan " + (asientosNecesarios - asientosSeleccionadosCount) + " asientos.",
                "Selección incompleta",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Calcular precio total con extras
        double totalExtras = 0;
        StringBuilder resumen = new StringBuilder();
        resumen.append("✓ RESERVACIÓN CONFIRMADA\n\n");
        resumen.append("Vuelo: ").append(vuelo.getCodigo()).append("\n");
        resumen.append("Ruta: ").append(vuelo.getOrigen()).append(" → ").append(vuelo.getDestino()).append("\n");
        resumen.append("Clase: ").append(clase).append("\n");
        resumen.append("Pasajeros: ").append(adultos).append(" adultos, ")
                .append(ninos).append(" niños, ").append(bebes).append(" bebés\n");
        resumen.append("Precio base: $").append(String.format("%.2f", precioFinal)).append("\n\n");
        resumen.append("Asientos seleccionados:\n");
        
        for (JButton btn : asientosSeleccionados) {
            String idAsiento = btn.getText().replace(" (X)", "");
            Asiento asiento = mapaAsientos.get(idAsiento);
            if (asiento != null) {
                resumen.append("- ").append(idAsiento)
                       .append(" (").append(asiento.getTipo()).append(")")
                       .append(": +$").append(String.format("%.2f", asiento.getPrecioExtra())).append("\n");
                totalExtras += asiento.getPrecioExtra();
            }
        }
        
        double totalFinal = precioFinal + totalExtras;
        resumen.append("\nExtras asientos: $").append(String.format("%.2f", totalExtras));
        resumen.append("\nTOTAL FINAL: $").append(String.format("%.2f", totalFinal));
        
        int confirm = JOptionPane.showConfirmDialog(this,
            resumen.toString(),
            "Confirmar reservación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "¡Reserva guardada exitosamente!\n" +
                "Total pagado: $" + String.format("%.2f", totalFinal) + "\n" +
                "Gracias por su compra.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose();
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
        jblTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        btnInfoClases = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        btnConfirmar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jblTitulo.setForeground(new java.awt.Color(16, 0, 79));
        jblTitulo.setText("Sección de Asientos");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(16, 0, 79));
        jLabel2.setText("Vuelo: QUITO → GUAYAQUIL | Clase: Económica");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mapa Asientos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(16, 0, 79))); // NOI18N

        jLabel1.setForeground(new java.awt.Color(16, 0, 79));
        jLabel1.setText("Leyenda:  Verde = Disponible | Cyan = Seleccionado | Gris = Ocupado");

        btnInfoClases.setBackground(new java.awt.Color(255, 204, 51));
        btnInfoClases.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInfoClases.setForeground(new java.awt.Color(16, 0, 79));
        btnInfoClases.setText("Ver Tipos de Asientos");

        btnVolver.setBackground(new java.awt.Color(153, 153, 153));
        btnVolver.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("Volver");

        btnConfirmar.setBackground(new java.awt.Color(51, 51, 255));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("Confirmar Selección");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnInfoClases)
                        .addGap(68, 68, 68)
                        .addComponent(btnVolver)
                        .addGap(61, 61, 61)
                        .addComponent(btnConfirmar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfoClases)
                    .addComponent(btnVolver)
                    .addComponent(btnConfirmar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnConfirmarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosIda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosIda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosIda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosIda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Ejemplo de uso (para pruebas)
                Vuelo vueloEjemplo = new Vuelo();
                vueloEjemplo.setCodigo("AV123");
                vueloEjemplo.setOrigen("Quito");
                vueloEjemplo.setDestino("Guayaquil");
                
                new frmSeleccionAsientosIda(vueloEjemplo, "Económica", 2, 1, 0, 350.0).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnInfoClases;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jblTitulo;
    // End of variables declaration//GEN-END:variables
}
