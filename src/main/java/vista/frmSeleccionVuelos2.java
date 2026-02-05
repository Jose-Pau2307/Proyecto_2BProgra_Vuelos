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

public class frmSeleccionVuelos2 extends JFrame {
    private Vuelo vueloIdaSeleccionado; 
    private String origen;
    private String destino;
    private String fechaIda;
    private String fechaVuelta;
    private String claseSeleccionadaIda;
    private double precioFinalIda;
    private Vuelo vueloSeleccionado;
    private String claseSeleccionada;
    private double precioFinal;
    private int filaSeleccionada = -1;
    private int adultos;
    private int ninos;
    private int bebes;

    private double precioIdaSeleccionado;
    private static final double DESCUENTO_NINO = 0.30;
    private static final double DESCUENTO_BEBE = 0.90;

    private List<String> asientosIdaSeleccionados;
    private List<String> asientosVueltaSeleccionados;

    // En frmSeleccionVuelos2 - MODIFICA EL CONSTRUCTOR
    public frmSeleccionVuelos2(String origen, String fechaIda, String destino, int adultos, int ninos, int bebes, Vuelo vueloIdaSeleccionado, String claseSeleccionadaIda, double precioFinalIda) {

        initComponents();

        this.origen = origen;
        this.destino = destino;
        this.fechaVuelta = fechaVuelta;
        this.adultos = adultos;
        this.ninos = ninos;
        this.bebes = bebes;
        this.fechaIda = fechaIda; // ✅ NECESARIA


        // Guardar información del vuelo de IDA
        this.vueloIdaSeleccionado = vueloIdaSeleccionado;
        this.claseSeleccionadaIda = claseSeleccionadaIda;
        this.precioFinalIda = precioFinalIda;

        this.vueloSeleccionado = null; // Vuelo de VUELTA
        this.claseSeleccionada = null;
        this.precioFinal = 0.0;

        setLocationRelativeTo(null);
        configurarInterfazDespuesDeInit();
        configurarTabla();
        cargarVuelosVuelta();
    }

    // Agrega estas variables
    

    private void configurarInterfazDespuesDeInit() {
        jblTitulo.setText("Vuelos de Vuelta: " + origen + " → " + destino);
        jLabel5.setText(origen + " → " + destino + " | Fecha: " + fechaVuelta);

        tblVuelos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVuelosMouseClicked(evt);
            }
        });
    }

    private void configurarTabla() {
        String[] columnas = {"Código", "Origen", "Destino", "Hora Salida", "Duración", "Precio", "Seleccionar"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
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

    private void cargarVuelosVuelta() {
        List<Vuelo> vuelos = VueloDAO.buscarVuelosVuelta(origen, destino, fechaVuelta);

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
                    "No hay vuelos de vuelta disponibles para esta ruta",
                    "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void tblVuelosMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tblVuelos.rowAtPoint(evt.getPoint());
        int columna = tblVuelos.columnAtPoint(evt.getPoint());

        if (fila != -1 && columna == 6) {
            String textoBoton = tblVuelos.getValueAt(fila, 6).toString();

            if (textoBoton.startsWith("✓")) {
                deseleccionarVuelo();
            } else {
                if (filaSeleccionada != -1 && filaSeleccionada != fila) {
                    ((DefaultTableModel) tblVuelos.getModel())
                            .setValueAt("Seleccionar", filaSeleccionada, 6);
                }
                mostrarDialogClases(fila);
            }
        }
    }

    private void deseleccionarVuelo() {
        if (filaSeleccionada != -1) {
            ((DefaultTableModel) tblVuelos.getModel())
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
        txtInfo.setText("Código: " + codigo + "\n"
                + "Ruta: " + origen + " → " + destino + "\n"
                + "Fecha: " + fechaVuelta + "\n"
                + "Hora: " + hora + "\n"
                + "Precio base: " + precioBase);
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

        double precioBaseConExtra = precioBase + extra;
        precioFinal = calcularPrecioTotal(precioBaseConExtra);

        JTextArea txtBeneficios = new JTextArea(beneficios);
        txtBeneficios.setEditable(false);
        txtBeneficios.setOpaque(false);
        txtBeneficios.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelPrecio = new JPanel(new BorderLayout());
        JLabel lblPrecio = new JLabel("Precio: $" + precioBaseConExtra);
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecio.setForeground(new Color(0, 102, 0));
        panelPrecio.add(lblPrecio, BorderLayout.CENTER);

        JButton btnElegir = new JButton("Elegir " + nombre);
        btnElegir.setBackground(new Color(24, 84, 144));
        btnElegir.setForeground(Color.WHITE);
        btnElegir.setFont(new Font("Arial", Font.BOLD, 12));

        btnElegir.addActionListener(e -> {
            claseSeleccionada = nombre;
            precioFinal = calcularPrecioTotal(precioBase);

            vueloSeleccionado = new Vuelo();
            vueloSeleccionado.setCodigo(tblVuelos.getValueAt(fila, 0).toString());
            vueloSeleccionado.setOrigen(origen);
            vueloSeleccionado.setDestino(destino);
            vueloSeleccionado.setPrecio(precioBaseConExtra);
            vueloSeleccionado.setFecha(fechaVuelta);

            tblVuelos.setRowSelectionInterval(fila, fila);

            ((DefaultTableModel) tblVuelos.getModel()).setValueAt("✓ " + nombre, fila, 6);

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

    private void abrirSeleccionAsientosCompleta() {
        // Primero abrir selección de asientos para el vuelo de IDA
        abrirSeleccionAsientosIda();
    }

    private void abrirSeleccionAsientosIda() {
        // Calcular pasajeros que necesitan asiento
        int pasajerosConAsiento = adultos + ninos;

        // Calcular precio base del vuelo de ida
        double precioBaseIda = vueloIdaSeleccionado.getPrecio();
        double precioTotalBaseIda = precioBaseIda * pasajerosConAsiento;

        // Precios adicionales por clase
        double precioPrimeraClase = 200.00;
        double precioClaseEjecutiva = 100.00;

        SeleccionAsientosDialog dialogIda = new SeleccionAsientosDialog(
                this,
                true,
                "Seleccione asientos - Vuelo de Ida: "
                + vueloIdaSeleccionado.getOrigen() + " a " + vueloIdaSeleccionado.getDestino(),
                pasajerosConAsiento,
                precioTotalBaseIda,
                precioPrimeraClase,
                precioClaseEjecutiva
        );

        // Configurar asientos ocupados para ida (deberías obtener de BD)
        List<String> asientosOcupadosIda = obtenerAsientosOcupados(
                vueloIdaSeleccionado.getOrigen(),
                vueloIdaSeleccionado.getDestino(),
                fechaIda
        );
        dialogIda.setAsientosOcupados(asientosOcupadosIda);

        dialogIda.setCallback(new SeleccionAsientosDialog.SeleccionAsientosCallback() {
            @Override
            public void onAsientosSeleccionados(List<String> asientosIda) {
                // 1. GUARDAMOS LA LISTA EN LA VARIABLE DE CLASE
                asientosIdaSeleccionados = asientosIda;

                // 2. Mensaje opcional (puedes quitarlo si quieres ir más rápido)
                JOptionPane.showMessageDialog(frmSeleccionVuelos2.this,
                        "Asientos de ida guardados. Ahora seleccione la vuelta.",
                        "Progreso",
                        JOptionPane.INFORMATION_MESSAGE);

                // 3. Pasamos a la vuelta
                abrirSeleccionAsientosVuelta();
            }
        });

        dialogIda.setSize(1200, 800);
        dialogIda.setLocationRelativeTo(this);
        dialogIda.setVisible(true);
    }

    private void abrirSeleccionAsientosVuelta() {
        // Calcular pasajeros que necesitan asiento
        int pasajerosConAsiento = adultos + ninos;

        // Calcular precio base del vuelo de vuelta
        double precioBaseVuelta = vueloSeleccionado.getPrecio();
        double precioTotalBaseVuelta = precioBaseVuelta * pasajerosConAsiento;

        // Precios adicionales por clase
        double precioPrimeraClase = 200.00;
        double precioClaseEjecutiva = 100.00;

        SeleccionAsientosDialog dialogVuelta = new SeleccionAsientosDialog(
                this,
                true,
                "Seleccione asientos - Vuelo de Vuelta: "
                + vueloSeleccionado.getOrigen() + " a " + vueloSeleccionado.getDestino(),
                pasajerosConAsiento,
                precioTotalBaseVuelta,
                precioPrimeraClase,
                precioClaseEjecutiva
        );

        // Configurar asientos ocupados para vuelta
        List<String> asientosOcupadosVuelta = obtenerAsientosOcupados(
                vueloSeleccionado.getOrigen(),
                vueloSeleccionado.getDestino(),
                fechaVuelta
        );
        dialogVuelta.setAsientosOcupados(asientosOcupadosVuelta);

        dialogVuelta.setCallback(new SeleccionAsientosDialog.SeleccionAsientosCallback() {
            @Override
            public void onAsientosSeleccionados(List<String> asientosVuelta) {
                // 1. GUARDAMOS LA LISTA EN LA VARIABLE DE CLASE
                asientosVueltaSeleccionados = asientosVuelta;

                // 2. Vamos al resumen final
                mostrarResumenFinal();
            }
        });

        dialogVuelta.setSize(1200, 800);
        dialogVuelta.setLocationRelativeTo(this);
        dialogVuelta.setVisible(true);
    }

    private void mostrarResumenFinal() {
        double totalGeneral = precioFinalIda + precioFinal;

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESERVA COMPLETADA ===\n\n");
        resumen.append("VUELO DE IDA:\n");
        resumen.append("• Código: ").append(vueloIdaSeleccionado.getCodigo()).append("\n");
        resumen.append("• Ruta: ").append(vueloIdaSeleccionado.getOrigen())
                .append(" → ").append(vueloIdaSeleccionado.getDestino()).append("\n");
        resumen.append("• Fecha: ").append(fechaIda).append("\n");
        resumen.append("• Clase: ").append(claseSeleccionadaIda).append("\n");
        resumen.append("• Precio: $").append(String.format("%.2f", precioFinalIda)).append("\n\n");

        resumen.append("VUELO DE VUELTA:\n");
        resumen.append("• Código: ").append(vueloSeleccionado.getCodigo()).append("\n");
        resumen.append("• Ruta: ").append(vueloSeleccionado.getOrigen())
                .append(" → ").append(vueloSeleccionado.getDestino()).append("\n");
        resumen.append("• Fecha: ").append(fechaVuelta).append("\n");
        resumen.append("• Clase: ").append(claseSeleccionada).append("\n");
        resumen.append("• Precio: $").append(String.format("%.2f", precioFinal)).append("\n\n");

        resumen.append("PASAJEROS:\n");
        resumen.append("• Adultos: ").append(adultos).append("\n");
        resumen.append("• Niños: ").append(ninos).append("\n");
        if (bebes > 0) {
            resumen.append("• Bebés: ").append(bebes).append(" (sin asiento)\n");
        }

        resumen.append("\nTOTAL A PAGAR: $").append(String.format("%.2f", totalGeneral));

        JOptionPane.showMessageDialog(this,
                resumen.toString(),
                "Reserva Completada",
                JOptionPane.INFORMATION_MESSAGE);

        int opcion = JOptionPane.showConfirmDialog(this,
                resumen.toString(),
                "Reserva Completada - Confirmar",
                JOptionPane.YES_NO_OPTION, // Botones Sí/No
                JOptionPane.INFORMATION_MESSAGE);

        // 2. Si el usuario dice "SÍ" (YES), abrimos la siguiente ventana
        if (opcion == JOptionPane.YES_OPTION) {

            // Creamos la ventana de pasajeros pasándole TODOS los datos que recolectamos
            frmDatosPasajeros registro = new frmDatosPasajeros(
                    vueloIdaSeleccionado, // Objeto Vuelo Ida
                    vueloSeleccionado, // Objeto Vuelo Vuelta (en esta clase es este)
                    asientosIdaSeleccionados, // Lista asientos Ida (ya la guardamos arriba)
                    asientosVueltaSeleccionados, // Lista asientos Vuelta (ya la guardamos arriba)
                    claseSeleccionadaIda, // Nombre clase Ida
                    claseSeleccionada, // Nombre clase Vuelta
                    adultos, ninos, bebes, // Cantidad pasajeros
                    totalGeneral // Precio final acumulado
            );

            registro.setVisible(true); // Mostrar nueva ventana
            this.dispose();            // Cerrar la actual
        }
    }

    private List<String> obtenerAsientosOcupados(String origen, String destino, String fecha) {
        // Consultar a la base de datos
        List<String> ocupados = new ArrayList<>();
        // ... código para obtener asientos ocupados de la BD ...
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
        jblTitulo.setText("Elija un vuelo de vuelta");
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
        jPanel1.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, -1, -1));

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
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 410, 130, -1));

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
        frmSeleccionVuelos ventanaVuelos = new frmSeleccionVuelos(
        origen,
        destino,
        fechaIda,
        fechaVuelta,
        false,      // 👈 NO es solo ida (ya estás en ida + vuelta)
        adultos,
        ninos,
        bebes
    );

    ventanaVuelos.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed

        if (vueloSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un vuelo de vuelta primero",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Calcular precio total de ambos vuelos
        double totalGeneral = precioFinalIda + precioFinal;

        // Mostrar resumen
        String mensaje = "✓ RESUMEN DE RESERVA:\n\n"
                + "VUELO DE IDA:\n"
                + "Código: " + vueloIdaSeleccionado.getCodigo() + "\n"
                + "Ruta: " + vueloIdaSeleccionado.getOrigen() + " → " + vueloIdaSeleccionado.getDestino() + "\n"
                + "Fecha: " + fechaIda + "\n"
                + "Clase: " + claseSeleccionadaIda + "\n"
                + "Precio: $" + precioFinalIda + "\n\n"
                + "VUELO DE VUELTA:\n"
                + "Código: " + vueloSeleccionado.getCodigo() + "\n"
                + "Ruta: " + vueloSeleccionado.getOrigen() + " → " + vueloSeleccionado.getDestino() + "\n"
                + "Fecha: " + fechaVuelta + "\n"
                + "Clase: " + claseSeleccionada + "\n"
                + "Precio: $" + precioFinal + "\n\n"
                + "TOTAL: $" + totalGeneral;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                mensaje,
                "Confirmar reserva completa",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            // Abrir selección de asientos para AMBOS vuelos
            abrirSeleccionAsientosCompleta();
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
        } catch (Exception ex) {
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
