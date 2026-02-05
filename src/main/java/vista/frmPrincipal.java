package vista;

import com.toedter.calendar.JTextFieldDateEditor;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import dao.VueloDAO;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class frmPrincipal extends javax.swing.JFrame {

    // Variables para almacenar la información de la reserva
    private String origen;
    private String destino;
    private String fechaIda;
    private String fechaVuelta;
    private boolean soloIda;
    private int adultos;
    private int ninos;
    private int bebes;
    
    // Variables para almacenar asientos seleccionados
    private List<String> asientosIdaSeleccionados = new ArrayList<>();
    private List<String> asientosVueltaSeleccionados = new ArrayList<>();
    
    // Variables de precios (ejemplo)
    private double precioPrimeraClase = 200.00;
    private double precioClaseEjecutiva = 100.00;

    public frmPrincipal() {
        initComponents();
        configurarRadioButtons(); 
        configurarDateChoosers();
        configurarEventos();
        cargarDestinosComboBox();
    }

    // ================= RADIO BUTTONS =================
    private void configurarRadioButtons() {
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbIdaVuelta);
        grupo.add(rbSoloIda);
        rbIdaVuelta.setSelected(true);
    }

    // ================= DATECHOOSERS =================
    private void configurarDateChoosers() {
        Date hoy = new Date();

        fechaIdaChooser.setDateFormatString("dd/MM/yyyy");
        fechaIdaChooser.setMinSelectableDate(hoy);
        ((JTextFieldDateEditor) fechaIdaChooser.getDateEditor()).setEditable(false);

        fechaVueltaChooser.setDateFormatString("dd/MM/yyyy");
        ((JTextFieldDateEditor) fechaVueltaChooser.getDateEditor()).setEditable(false);
        fechaVueltaChooser.setEnabled(false);
    }

    // ================= EVENTOS =================
    private void configurarEventos() {

        rbSoloIda.addActionListener(e -> {
            fechaVueltaChooser.setEnabled(false);
            fechaVueltaChooser.setDate(null);
        });

        rbIdaVuelta.addActionListener(e -> {
            if (fechaIdaChooser.getDate() != null) {
                habilitarFechaVuelta();
            } else {
                fechaVueltaChooser.setEnabled(false);
                fechaVueltaChooser.setDate(null);
            }
        });

        fechaIdaChooser.addPropertyChangeListener("date", e -> {
            if (fechaIdaChooser.getDate() != null && rbIdaVuelta.isSelected()) {
                habilitarFechaVuelta();
            } else {
                fechaVueltaChooser.setEnabled(false);
                fechaVueltaChooser.setDate(null);
            }
        });
    }

    private void habilitarFechaVuelta() {
        Date fechaIda = fechaIdaChooser.getDate();
        if (fechaIda == null) return;

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaIda);
        cal.add(Calendar.DAY_OF_MONTH, 1);

        fechaVueltaChooser.setMinSelectableDate(cal.getTime());
        fechaVueltaChooser.setEnabled(true);
        fechaVueltaChooser.setDate(null);
    }

    // ================= DESTINOS =================
    private void cargarDestinosComboBox() {
        try {
            cmbOrigen.removeAllItems();
            cmbDestino.removeAllItems();

            cmbOrigen.addItem("Seleccione origen");
            cmbDestino.addItem("Seleccione destino");

            List<String> destinos = VueloDAO.obtenerDestinosUnicos();

            if (destinos.isEmpty()) {
                destinos = List.of(
                    "Quito", "Guayaquil", "Cuenca", "Manta", "Portoviejo",
                    "Loja", "Ambato", "Riobamba", "Ibarra", "Tulcán"
                );
            }

            for (String d : destinos) {
                cmbOrigen.addItem(d);
                cmbDestino.addItem(d);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar destinos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================= BÚSQUEDA =================
    // ================= BÚSQUEDA =================
private void buscarVuelos() {
    // ========= VALIDACIONES BÁSICAS =========
    if (cmbOrigen.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Seleccione el origen");
        return;
    }

    if (cmbDestino.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Seleccione el destino");
        return;
    }

    String origen = cmbOrigen.getSelectedItem().toString();
    String destino = cmbDestino.getSelectedItem().toString();

    if (origen.equals(destino)) {
        JOptionPane.showMessageDialog(this,
                "El origen y destino no pueden ser iguales");
        return;
    }

    if (fechaIdaChooser.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione la fecha de ida");
        return;
    }

    boolean soloIda = rbSoloIda.isSelected();

    if (!soloIda && fechaVueltaChooser.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione la fecha de vuelta");
        return;
    }

    if (!VueloDAO.existeRuta(origen, destino)) {
        JOptionPane.showMessageDialog(this,
                "No existen vuelos para esa ruta",
                "Ruta no disponible",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    // ========= VALIDACIÓN DE PASAJEROS =========
    if (cmbAdultos.getSelectedIndex() == 0 ||
        cmbNinos.getSelectedIndex() == 0 ||
        cmbBebes.getSelectedIndex() == 0) {

        JOptionPane.showMessageDialog(this,
                "Seleccione correctamente los pasajeros");
        return;
    }

    int adultos = Integer.parseInt(
            cmbAdultos.getSelectedItem().toString());

    int ninos = Integer.parseInt(
            cmbNinos.getSelectedItem().toString());

    int bebes = Integer.parseInt(
            cmbBebes.getSelectedItem().toString());

    if (bebes > adultos) {
        JOptionPane.showMessageDialog(this,
                "El número de bebés no puede ser mayor al de adultos (1 adulto por bebé)");
        return;
    }

    if (adultos == 0) {
        JOptionPane.showMessageDialog(this,
                "Debe haber al menos un adulto en la reserva");
        return;
    }

    // ========= FECHAS =========
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String fechaIda = sdf.format(fechaIdaChooser.getDate());
    String fechaVuelta = soloIda
            ? null
            : sdf.format(fechaVueltaChooser.getDate());

    // ========= GUARDAR DATOS PARA USAR DESPUÉS =========
    this.origen = origen;
    this.destino = destino;
    this.fechaIda = fechaIda;
    this.fechaVuelta = fechaVuelta;
    this.soloIda = soloIda;
    this.adultos = adultos;
    this.ninos = ninos;
    this.bebes = bebes;
    
    // ========= PASAR A SELECCIÓN DE VUELOS =========
    abrirSeleccionVuelos();
}

// ================= MÉTODO PARA ABRIR SELECCIÓN DE VUELOS =================
private void abrirSeleccionVuelos() {
    // Abrir frmSeleccionVuelos para seleccionar el vuelo de IDA
    frmSeleccionVuelos seleccionVuelos = new frmSeleccionVuelos(
        origen, 
        destino, 
        fechaIda, 
        fechaVuelta, 
        soloIda, 
        adultos, 
        ninos, 
        bebes
    );
    
    seleccionVuelos.setVisible(true);
    this.dispose(); // Cerrar el formulario principal
}

// ================= ELIMINA ESTOS MÉTODOS QUE YA NO NECESITAS =================
// Quita o comenta estos métodos:
// private void iniciarSeleccionAsientos() { ... }
// private void abrirSeleccionAsientosIda() { ... }
// private void abrirSeleccionAsientosVuelta() { ... }
// private void mostrarResumenSeleccionIda() { ... }
// private void mostrarResumenSeleccionCompleta() { ... }
// private double obtenerPrecioBaseVuelo() { ... }
// private List<String> obtenerAsientosOcupados() { ... }

    /* ================= MÉTODOS PARA SELECCIÓN DE ASIENTOS =================
    
    private void iniciarSeleccionAsientos() {
        // Limpiar selecciones previas
        asientosIdaSeleccionados.clear();
        asientosVueltaSeleccionados.clear();
        
        // NOTA IMPORTANTE: Los bebés NO ocupan asiento
        // Solo adultos y niños mayores de 2 años necesitan asiento
        int pasajerosConAsiento = adultos + ninos;
        
        if (pasajerosConAsiento == 0) {
            JOptionPane.showMessageDialog(this,
                "Debe haber al menos un pasajero que requiera asiento (adulto o niño mayor de 2 años)",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Mostrar información sobre bebés
        if (bebes > 0) {
            JOptionPane.showMessageDialog(this,
                "INFORMACIÓN IMPORTANTE:\n\n" +
                "• Los bebés (menores de 2 años) NO ocupan asiento propio.\n" +
                "• Viajan en el regazo de un adulto.\n" +
                "• Solo necesita seleccionar asientos para " + pasajerosConAsiento + " pasajero(s).",
                "Información sobre Bebés",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Obtener precio base desde la base de datos (simulado)
        double precioBaseVuelo = obtenerPrecioBaseVuelo(origen, destino);
        
        if (soloIda) {
            // Solo ida: abrir solo selección de ida
            abrirSeleccionAsientosIda(pasajerosConAsiento, precioBaseVuelo);
        } else {
            // Ida y vuelta: abrir primero ida, luego vuelta
            abrirSeleccionAsientosIda(pasajerosConAsiento, precioBaseVuelo);
        }
    }
    
    private double obtenerPrecioBaseVuelo(String origen, String destino) {
        // Aquí deberías consultar la base de datos para el precio real
        // Por ahora usamos valores fijos como ejemplo
        if (origen.equals("Quito") && destino.equals("Guayaquil")) {
            return 150.00;
        } else if (origen.equals("Guayaquil") && destino.equals("Quito")) {
            return 150.00;
        } else {
            return 200.00; // Precio base para otras rutas
        }
    }
    
    private void abrirSeleccionAsientosIda(int pasajerosConAsiento, double precioBaseVuelo) {
        // Calcular precio total base para los pasajeros que necesitan asiento
        double precioTotalBase = precioBaseVuelo * pasajerosConAsiento;
        
        SeleccionAsientosDialog dialog = new SeleccionAsientosDialog(
            this, 
            true, 
            "Seleccione asientos - Vuelo de Ida: " + origen + " a " + destino,
            pasajerosConAsiento,       // Máximo asientos permitidos = pasajeros con asiento
            precioTotalBase,           // Precio base total
            precioPrimeraClase,        // Precio adicional primera clase
            precioClaseEjecutiva       // Precio adicional clase ejecutiva
        );
        
        // Configurar asientos ocupados
        List<String> asientosOcupados = obtenerAsientosOcupados(origen, destino, fechaIda);
        dialog.setAsientosOcupados(asientosOcupados);
        
        dialog.setCallback(new SeleccionAsientosDialog.SeleccionAsientosCallback() {
            @Override
            public void onAsientosSeleccionados(List<String> asientos) {
                asientosIdaSeleccionados = asientos;
                
                if (soloIda) {
                    // Si es solo ida, mostrar mensaje de éxito
                    mostrarResumenSeleccionIda();
                } else {
                    // Si es ida y vuelta, continuar con vuelta
                    abrirSeleccionAsientosVuelta(pasajerosConAsiento, precioBaseVuelo);
                }
            }
        });
        
        dialog.setSize(1200, 800); // Ventana más grande para ver mejor
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void abrirSeleccionAsientosVuelta(int pasajerosConAsiento, double precioBaseVuelo) {
        // Calcular precio total base para los pasajeros que necesitan asiento
        double precioTotalBase = precioBaseVuelo * pasajerosConAsiento;
        
        SeleccionAsientosDialog dialog = new SeleccionAsientosDialog(
            this, 
            true, 
            "Seleccione asientos - Vuelo de Vuelta: " + destino + " a " + origen,
            pasajerosConAsiento,       // Máximo asientos permitidos = pasajeros con asiento
            precioTotalBase,           // Precio base total
            precioPrimeraClase,        // Precio adicional primera clase
            precioClaseEjecutiva       // Precio adicional clase ejecutiva
        );
        
        // Configurar asientos ocupados para el vuelo de vuelta
        List<String> asientosOcupados = obtenerAsientosOcupados(destino, origen, fechaVuelta);
        dialog.setAsientosOcupados(asientosOcupados);
        
        dialog.setCallback(new SeleccionAsientosDialog.SeleccionAsientosCallback() {
            @Override
            public void onAsientosSeleccionados(List<String> asientos) {
                asientosVueltaSeleccionados = asientos;
                mostrarResumenSeleccionCompleta();
            }
        });
        
        dialog.setSize(1200, 800); // Ventana más grande para ver mejor
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void mostrarResumenSeleccionIda() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE SELECCIÓN ===\n\n");
        resumen.append("VUELO DE IDA: ").append(origen).append(" → ").append(destino).append("\n");
        resumen.append("Fecha: ").append(fechaIda).append("\n\n");
        
        resumen.append("PASAJEROS:\n");
        resumen.append("• Adultos: ").append(adultos).append("\n");
        resumen.append("• Niños (2-12 años): ").append(ninos).append("\n");
        if (bebes > 0) {
            resumen.append("• Bebés (<2 años): ").append(bebes).append(" (sin asiento)\n");
        }
        
        resumen.append("\nASIENTOS SELECCIONADOS (").append(asientosIdaSeleccionados.size()).append("):\n");
        for (String asiento : asientosIdaSeleccionados) {
            resumen.append("• ").append(asiento).append("\n");
        }
        
        JOptionPane.showMessageDialog(this,
            resumen.toString(),
            "Selección Completa - Solo Ida",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarResumenSeleccionCompleta() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE SELECCIÓN ===\n\n");
        
        resumen.append("VUELO DE IDA: ").append(origen).append(" → ").append(destino).append("\n");
        resumen.append("Fecha: ").append(fechaIda).append("\n");
        resumen.append("Asientos: ").append(asientosIdaSeleccionados.toString()).append("\n\n");
        
        resumen.append("VUELO DE VUELTA: ").append(destino).append(" → ").append(origen).append("\n");
        resumen.append("Fecha: ").append(fechaVuelta).append("\n");
        resumen.append("Asientos: ").append(asientosVueltaSeleccionados.toString()).append("\n\n");
        
        resumen.append("PASAJEROS:\n");
        resumen.append("• Adultos: ").append(adultos).append("\n");
        resumen.append("• Niños (2-12 años): ").append(ninos).append("\n");
        if (bebes > 0) {
            resumen.append("• Bebés (<2 años): ").append(bebes).append(" (sin asiento)\n");
        }
        
        resumen.append("\nTotal asientos seleccionados: ");
        resumen.append(asientosIdaSeleccionados.size() + asientosVueltaSeleccionados.size());
        
        JOptionPane.showMessageDialog(this,
            resumen.toString(),
            "Selección Completa - Ida y Vuelta",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ================= MÉTODOS AUXILIARES =================
    
    private List<String> obtenerAsientosOcupados(String origen, String destino, String fecha) {
        // Aquí deberías consultar a la base de datos
        // Por ahora, retornamos algunos asientos aleatorios como ocupados
        List<String> ocupados = new ArrayList<>();
        
        // Simular algunos asientos ocupados
        String[] posiblesOcupados = {"1A", "3C", "5F", "10D", "12E", "15B", "20G", "25A", "30C", "35F"};
        int numOcupados = (int)(Math.random() * 4) + 2; // 2-5 asientos ocupados
        
        for (int i = 0; i < numOcupados; i++) {
            ocupados.add(posiblesOcupados[(int)(Math.random() * posiblesOcupados.length)]);
        }
        
        return ocupados;
    }
    
    // ================= MÉTODOS PARA ACCEDER DESDE OTRAS CLASES =================
    
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getFechaIda() { return fechaIda; }
    public String getFechaVuelta() { return fechaVuelta; }
    public boolean isSoloIda() { return soloIda; }
    public int getAdultos() { return adultos; }
    public int getNinos() { return ninos; }
    public int getBebes() { return bebes; }
    public List<String> getAsientosIda() { return asientosIdaSeleccionados; }
    public List<String> getAsientosVuelta() { return asientosVueltaSeleccionados; }
    
    public double getPrecioPrimeraClase() { return precioPrimeraClase; }
    public double getPrecioClaseEjecutiva() { return precioClaseEjecutiva; }

    */
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rbIdaVuelta = new javax.swing.JRadioButton();
        rbSoloIda = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        fechaIdaChooser = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        fechaVueltaChooser = new com.toedter.calendar.JDateChooser();
        jSeparator4 = new javax.swing.JSeparator();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbDestino = new javax.swing.JComboBox<>();
        cmbAdultos = new javax.swing.JComboBox<>();
        cmbNinos = new javax.swing.JComboBox<>();
        cmbBebes = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnBuscarVuelos = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelPrincipal.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        rbIdaVuelta.setBackground(new java.awt.Color(255, 255, 255));
        rbIdaVuelta.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        rbIdaVuelta.setForeground(new java.awt.Color(70, 88, 223));
        rbIdaVuelta.setText("Ida y vuelta");
        rbIdaVuelta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbIdaVueltaActionPerformed(evt);
            }
        });

        rbSoloIda.setBackground(new java.awt.Color(255, 255, 255));
        rbSoloIda.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        rbSoloIda.setForeground(new java.awt.Color(70, 88, 223));
        rbSoloIda.setText("Solo ida");

        jLabel4.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(16, 0, 79));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Desde:");

        jLabel5.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(16, 0, 79));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Hacia:");

        jLabel1.setFont(new java.awt.Font("Calibri Light", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(16, 0, 79));
        jLabel1.setText("¿A dónde quieres ir?");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel6.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(16, 0, 79));
        jLabel6.setText("Fecha Ida:");

        fechaIdaChooser.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(16, 0, 79));
        jLabel7.setText("Fecha Vuelta:");

        fechaVueltaChooser.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        cmbOrigen.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbOrigen.setToolTipText("");
        cmbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigenActionPerformed(evt);
            }
        });

        cmbDestino.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        cmbDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDestino.setToolTipText("");

        cmbAdultos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adultos", "1", "2", "3", "4", "5", "6", "7", "8", "9", " ", " " }));

        cmbNinos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Niños", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " ", " " }));

        cmbBebes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bebés", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " ", " " }));

        jLabel10.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(16, 0, 79));
        jLabel10.setText("Pasajeros:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator4)
            .addComponent(jSeparator3)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addGap(43, 43, 43)
                                .addComponent(rbIdaVuelta)
                                .addGap(36, 36, 36)
                                .addComponent(rbSoloIda, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jLabel6)
                                .addGap(12, 12, 12)
                                .addComponent(fechaIdaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73)
                                .addComponent(jLabel7)
                                .addGap(34, 34, 34)
                                .addComponent(fechaVueltaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel10)
                .addGap(49, 49, 49)
                .addComponent(cmbAdultos, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbNinos, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbBebes, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbIdaVuelta)
                    .addComponent(rbSoloIda))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAdultos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNinos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBebes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(26, 26, 26)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(fechaIdaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(fechaVueltaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        panelPrincipal.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 540, 370));

        jPanel3.setBackground(new java.awt.Color(24, 84, 144));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image.png"))); // NOI18N
        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(635, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPrincipal.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 170));

        btnBuscarVuelos.setBackground(new java.awt.Color(24, 84, 144));
        btnBuscarVuelos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnBuscarVuelos.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarVuelos.setText("Buscar");
        btnBuscarVuelos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarVuelosActionPerformed(evt);
            }
        });
        panelPrincipal.add(btnBuscarVuelos, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 420, 120, 40));

        btnCerrarSesion.setBackground(new java.awt.Color(100, 100, 100));
        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        panelPrincipal.add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar la sesión?",
            "Confirmar Cierre de Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // Volver al login
            frmLogin login = new frmLogin();
            login.setVisible(true);
            this.dispose(); // Cerrar menú principal
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void rbIdaVueltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIdaVueltaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbIdaVueltaActionPerformed

    private void btnBuscarVuelosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarVuelosActionPerformed
         buscarVuelos();
    }//GEN-LAST:event_btnBuscarVuelosActionPerformed

    private void cmbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbOrigenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new frmPrincipal().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarVuelos;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JComboBox<String> cmbAdultos;
    private javax.swing.JComboBox<String> cmbBebes;
    private javax.swing.JComboBox<String> cmbDestino;
    private javax.swing.JComboBox<String> cmbNinos;
    private javax.swing.JComboBox<String> cmbOrigen;
    private com.toedter.calendar.JDateChooser fechaIdaChooser;
    private com.toedter.calendar.JDateChooser fechaVueltaChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JRadioButton rbIdaVuelta;
    private javax.swing.JRadioButton rbSoloIda;
    // End of variables declaration//GEN-END:variables
}
