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

public class frmSeleccionAsientosVuelta extends JFrame {
    
    private Vuelo vueloVuelta;
    private String claseVuelta;
    private int adultos;
    private int ninos;
    private int bebes;
    private double precioVuelta;
    private double precioIda;
    private String claseIda;
    private String fechaIda;
    
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
    
    public frmSeleccionAsientosVuelta(Vuelo vueloVuelta, String claseVuelta,
                                     int adultos, int ninos, int bebes,
                                     double precioVuelta, double precioIda,
                                     String claseIda, String fechaIda) {
        this.vueloVuelta = vueloVuelta;
        this.claseVuelta = claseVuelta;
        this.adultos = adultos;
        this.ninos = ninos;
        this.bebes = bebes;
        this.precioVuelta = precioVuelta;
        this.precioIda = precioIda;
        this.claseIda = claseIda;
        this.fechaIda = fechaIda;
        
        initComponents();
        configurarDespuesInit();
    }
    
    private void configurarDespuesInit() {
        jblTitulo.setText("Selección de Asientos - Vuelta");
        jLabel2.setText("Vuelo: " + vueloVuelta.getDestino() + " → " + vueloVuelta.getOrigen() + 
                       " | Clase: " + claseVuelta);
        
        // Panel con diseño de avión
        panelAsientos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo de avión
                g.setColor(new Color(240, 240, 240));
                g.fillRect(30, 30, getWidth() - 60, getHeight() - 60);
                
                // Cabina
                g.setColor(new Color(200, 200, 200));
                int[] xPoints = {getWidth()/2 - 100, getWidth()/2 + 100, getWidth()/2 + 50, getWidth()/2 - 50};
                int[] yPoints = {30, 30, 80, 80};
                g.fillPolygon(xPoints, yPoints, 4);
                
                // Pasillo
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(getWidth()/2 - 20, 80, 40, getHeight() - 110);
                
                // Indicadores de ventanas
                g.setColor(new Color(135, 206, 235));
                for (int i = 0; i < 8; i++) {
                    g.fillOval(40 + i*40, 40, 15, 15);
                    g.fillOval(getWidth() - 55 - i*40, 40, 15, 15);
                }
            }
        };
        panelAsientos.setLayout(new GridLayout(FILAS, COLUMNAS, 3, 3));
        panelAsientos.setBackground(Color.WHITE);
        panelAsientos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLUE, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
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
        
        actualizarResumen();
    }
    
    private void crearAsientos() {
        todosLosAsientos = AsientoDAO.generarAsientosParaVuelo(claseVuelta, true);
        
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
                
                // Colores según posición
                Color colorBase;
                String tooltip = "";
                
                if (col == 0 || col == 5) { // Ventana
                    colorBase = new Color(173, 216, 230);
                    tooltip = "Ventana - $15.00 extra";
                } else if (col == 2 || col == 3) { // Pasillo
                    colorBase = new Color(144, 238, 144);
                    tooltip = "Pasillo - $10.00 extra";
                } else { // Centro
                    colorBase = new Color(255, 228, 181);
                    tooltip = "Centro - Sin costo extra";
                }
                
                configurarBotonAsiento(btnAsiento, idAsiento, disponible, colorBase, tooltip);
                panelAsientos.add(btnAsiento);
            }
        }
    }
    
    private void configurarBotonAsiento(JButton btn, String id, boolean disponible, Color colorBase, String tooltip) {
        btn.setFont(new Font("Arial", Font.BOLD, 10));
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(1, 1, 1, 1));
        btn.setToolTipText(tooltip);
        
        if (!disponible) {
            btn.setEnabled(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setText(id + " (X)");
            btn.setForeground(Color.RED);
            btn.setToolTipText("ASIENTO OCUPADO");
        } else {
            btn.setBackground(colorBase);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(colorBase.darker(), 1));
            
            btn.addActionListener(e -> {
                seleccionarAsiento(btn, id);
            });
        }
    }
    
    private void seleccionarAsiento(JButton btn, String id) {
        if (asientosSeleccionados.contains(btn)) {
            asientosSeleccionados.remove(btn);
            Asiento asiento = mapaAsientos.get(id);
            if (asiento != null) {
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
            int asientosNecesarios = adultos + ninos;
            if (asientosSeleccionados.size() >= asientosNecesarios) {
                JOptionPane.showMessageDialog(this,
                    "Límite alcanzado. Deselecciona un asiento primero.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            asientosSeleccionados.add(btn);
            btn.setBackground(Color.CYAN);
            btn.setForeground(Color.BLACK);
            
            Asiento asientoSeleccionado = mapaAsientos.get(id);
            if (asientoSeleccionado != null) {
                lblTipoSeleccionado.setText("Tipo: " + asientoSeleccionado.getTipo());
                lblPrecioExtra.setText("Extra: $" + asientoSeleccionado.getPrecioExtra());
            }
        }
        
        actualizarContadores();
    }
    
    private void actualizarContadores() {
        int seleccionados = asientosSeleccionados.size();
        int necesarios = adultos + ninos;
        
        lblAsientosSeleccionados.setText("Seleccionados: " + seleccionados);
        
        if (seleccionados == necesarios) {
            lblAsientosSeleccionados.setForeground(new Color(0, 153, 76));
        } else {
            lblAsientosSeleccionados.setForeground(Color.RED);
        }
    }
    
    private void actualizarResumen() {
        double totalGeneral = precioIda + precioVuelta;
        
        String resumen = "<html><b>RESUMEN COMPLETO</b><br>" +
                        "<font color='blue'>✈ VUELO IDA:</font><br>" +
                        "• " + vueloVuelta.getDestino() + " → " + vueloVuelta.getOrigen() + "<br>" +
                        "• Clase: " + claseIda + "<br>" +
                        "• Precio: <b>$" + String.format("%.2f", precioIda) + "</b><br><br>" +
                        
                        "<font color='green'>✈ VUELO VUELTA:</font><br>" +
                        "• " + vueloVuelta.getOrigen() + " → " + vueloVuelta.getDestino() + "<br>" +
                        "• Clase: " + claseVuelta + "<br>" +
                        "• Precio base: <b>$" + String.format("%.2f", precioVuelta) + "</b><br><br>" +
                        
                        "👥 Pasajeros: " + adultos + " adultos, " + ninos + " niños<br>" +
                        "💺 Asientos a seleccionar: " + (adultos + ninos) + "<br><br>" +
                        
                        "💰 <b>TOTAL ESTIMADO: $" + String.format("%.2f", totalGeneral) + "</b></html>";
        
        jLabel3.setText(resumen);
    }
    
    // === MÉTODO QUE FALTA AGREGAR ===
    private void mostrarDialogClasesAsientos() {
        JDialog dialog = new JDialog(this, "Tipos de Asientos - Vuelta", true);
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
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                
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
            }
        };
        panelDiagrama.setPreferredSize(new Dimension(600, 150));
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
    
                                               

                                                 

                                             

    // === AGREGAR EL MÉTODO validarSeleccion() ===
    private void validarSeleccion() {
        int asientosNecesarios = adultos + ninos;
        int asientosSeleccionadosCount = asientosSeleccionados.size();
        
        if (asientosSeleccionadosCount < asientosNecesarios) {
            JOptionPane.showMessageDialog(this,
                "Faltan " + (asientosNecesarios - asientosSeleccionadosCount) + " asientos por seleccionar.",
                "Selección incompleta",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Calcular extras
        double extrasVuelta = 0;
        StringBuilder detalleAsientos = new StringBuilder();
        
        for (JButton btn : asientosSeleccionados) {
            String idAsiento = btn.getText().replace(" (X)", "");
            Asiento asiento = mapaAsientos.get(idAsiento);
            if (asiento != null) {
                extrasVuelta += asiento.getPrecioExtra();
                detalleAsientos.append("• ").append(idAsiento)
                              .append(" (").append(asiento.getTipo()).append(")")
                              .append(": +$").append(String.format("%.2f", asiento.getPrecioExtra())).append("\n");
            }
        }
        
        double totalVuelta = precioVuelta + extrasVuelta;
        double totalGeneral = precioIda + totalVuelta;
        
        String resumen = "✓ RESERVA COMPLETA\n\n" +
                        "VUELO IDA:\n" +
                        "• " + vueloVuelta.getDestino() + " → " + vueloVuelta.getOrigen() + "\n" +
                        "• Clase: " + claseIda + "\n" +
                        "• Precio: $" + String.format("%.2f", precioIda) + "\n\n" +
                        
                        "VUELO VUELTA:\n" +
                        "• " + vueloVuelta.getOrigen() + " → " + vueloVuelta.getDestino() + "\n" +
                        "• Clase: " + claseVuelta + "\n" +
                        "• Precio base: $" + String.format("%.2f", precioVuelta) + "\n" +
                        "• Extras asientos: $" + String.format("%.2f", extrasVuelta) + "\n" +
                        detalleAsientos.toString() + "\n" +
                        "• Total vuelta: $" + String.format("%.2f", totalVuelta) + "\n\n" +
                        
                        "💰 TOTAL FINAL: $" + String.format("%.2f", totalGeneral);
        
        int confirm = JOptionPane.showConfirmDialog(this, resumen, "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "¡Reserva completa confirmada!\nTotal: $" + String.format("%.2f", totalGeneral),
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
        jLabel3 = new javax.swing.JLabel();
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

        jLabel3.setForeground(new java.awt.Color(16, 0, 79));
        jLabel3.setText("Leyenda:  Verde = Disponible | Cyan = Seleccionado | Gris = Ocupado");

        btnInfoClases.setBackground(new java.awt.Color(255, 204, 51));
        btnInfoClases.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInfoClases.setForeground(new java.awt.Color(16, 0, 79));
        btnInfoClases.setText("Ver Tipos de Asientos");
        btnInfoClases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoClasesActionPerformed(evt);
            }
        });

        btnVolver.setBackground(new java.awt.Color(153, 153, 153));
        btnVolver.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

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
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnInfoClases)
                        .addGap(68, 68, 68)
                        .addComponent(btnVolver)
                        .addGap(61, 61, 61)
                        .addComponent(btnConfirmar)))
                .addContainerGap(80, Short.MAX_VALUE))
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
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfoClases)
                    .addComponent(btnVolver)
                    .addComponent(btnConfirmar))
                .addContainerGap(39, Short.MAX_VALUE))
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
        validarSeleccion();
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnInfoClasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoClasesActionPerformed
        mostrarDialogClasesAsientos();
    }//GEN-LAST:event_btnInfoClasesActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

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
            java.util.logging.Logger.getLogger(frmSeleccionAsientosVuelta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosVuelta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosVuelta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmSeleccionAsientosVuelta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Ejemplo de uso (para pruebas)
                Vuelo vueloVuelta = new Vuelo();
                vueloVuelta.setCodigo("AV456");
                vueloVuelta.setOrigen("Guayaquil");
                vueloVuelta.setDestino("Quito");
                
                new frmSeleccionAsientosVuelta(vueloVuelta, "Económica", 2, 1, 0, 
                    350.0, 350.0, "Económica", "2024-02-15").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnInfoClases;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jblTitulo;
    // End of variables declaration//GEN-END:variables
}
