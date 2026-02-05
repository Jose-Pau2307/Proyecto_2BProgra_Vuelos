package vista;

import controlador.ControladorReserva;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SeleccionAsientosDialog extends javax.swing.JDialog {
    
    // Variables para control
    private int maxAsientosPermitidos;
    private double precioBase;
    private double precioPrimeraClase;
    private double precioClaseEjecutiva;
    private JLabel lblContador;
    private JLabel lblPrecioTotal;
    private boolean esIda;
    
    // Variables de lógica
    private List<String> asientosSeleccionados = new ArrayList<>();
    private Map<String, JButton> botonesAsientos = new HashMap<>();
    private double zoomLevel = 1.0;
    private SeleccionAsientosCallback callback;
    private List<String> asientosOcupados = new ArrayList<>();
    
    // Interface para callback
    public interface SeleccionAsientosCallback {
        void onAsientosSeleccionados(List<String> asientos);
    }
    
    public SeleccionAsientosDialog(java.awt.Frame parent, boolean modal, String titulo, 
                                int maxAsientos, double precioBase, 
                                double precioPrimeraClase, double precioClaseEjecutiva) {
        super(parent, modal);
        this.maxAsientosPermitidos = maxAsientos;
        this.precioBase = precioBase;
        this.precioPrimeraClase = precioPrimeraClase;
        this.precioClaseEjecutiva = precioClaseEjecutiva;
        
        initComponents();
        setTitle(titulo + " - Máximo: " + maxAsientos + " asientos");
        
        // Configurar el scroll pane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        configurarEventosZoom();
        configurarDisenoAvion();
        crearPanelContadores();
        setLocationRelativeTo(parent);
    }
    
    private void configurarEventosZoom() {
        btnZoomIn.addActionListener(e -> cambiarZoom(0.1));
        btnZoomOut.addActionListener(e -> cambiarZoom(-0.1));
        btnResetZoom.addActionListener(e -> resetZoom());
    }
    
    private void crearPanelContadores() {
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de Selección"));
        
        lblContador = new JLabel("Asientos: 0/" + maxAsientosPermitidos);
        lblContador.setFont(new Font("Arial", Font.BOLD, 14));
        lblContador.setForeground(Color.BLUE);
        
        lblPrecioTotal = new JLabel("Precio adicional: $0.00");
        lblPrecioTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecioTotal.setForeground(new Color(0, 150, 0));
        
        JLabel lblPrecios = new JLabel("<html>Precios adicionales: Primera $" + precioPrimeraClase + 
                                       " | Ejecutiva $" + precioClaseEjecutiva + "</html>");
        lblPrecios.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelInfo.add(lblContador);
        panelInfo.add(Box.createHorizontalStrut(20));
        panelInfo.add(lblPrecioTotal);
        panelInfo.add(Box.createHorizontalStrut(20));
        panelInfo.add(lblPrecios);
        
        // Insertar panel de información antes de los controles
        panelContenedor.add(panelInfo, BorderLayout.NORTH);
    }
    
    private void configurarDisenoAvion() {
        // Limpiar el panel si ya tiene contenido
        panelAvion.removeAll();
        
        // Cambiar el layout del panelAvion a BoxLayout vertical
        panelAvion.setLayout(new BoxLayout(panelAvion, BoxLayout.Y_AXIS));
        panelAvion.setBackground(Color.WHITE);
        
        // PARTE FRONTAL (Cockpit)
        JPanel frontal = crearParteFrontal();
        panelAvion.add(frontal);
        panelAvion.add(Box.createVerticalStrut(10));
        
        // PRIMERA CLASE (Filas 1-5)
        JPanel primeraClasePanel = crearSeccionClase("PRIMERA CLASE", 1, 5, "2-2", new Color(139, 0, 139));
        panelAvion.add(primeraClasePanel);
        panelAvion.add(Box.createVerticalStrut(15));
        
        // CLASE EJECUTIVA (Filas 6-15)
        JPanel ejecutivaPanel = crearSeccionClase("CLASE EJECUTIVA", 6, 15, "3-3", new Color(0, 0, 139));
        panelAvion.add(ejecutivaPanel);
        panelAvion.add(Box.createVerticalStrut(15));
        
        // CLASE ECONÓMICA (Filas 16-40) con scroll interno
        JPanel economicaPanel = crearSeccionClaseConScroll("CLASE ECONÓMICA", 16, 40, "3-4-3", new Color(0, 100, 0));
        panelAvion.add(economicaPanel);
        panelAvion.add(Box.createVerticalStrut(10));
        
        // PARTE TRASERA
        JPanel trasera = crearParteTrasera();
        panelAvion.add(trasera);
        panelAvion.add(Box.createVerticalStrut(10));
        
        // LEYENDA
        JPanel leyenda = crearLeyenda();
        panelAvion.add(leyenda);
        
        // Ajustar tamaño preferido para mejor scroll
        panelAvion.setPreferredSize(new Dimension(900, 2500));
        
        panelAvion.revalidate();
        panelAvion.repaint();
    }
    
    private JPanel crearSeccionClaseConScroll(String titulo, int filaInicio, int filaFin, String configuracion, Color colorBorde) {
        JPanel seccion = new JPanel(new BorderLayout());
        seccion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorBorde, 2),
            titulo + " (Filas " + filaInicio + "-" + filaFin + ")",
            0, 0,
            new Font("Arial", Font.BOLD, 12),
            colorBorde
        ));
        seccion.setBackground(Color.WHITE);
        
        // Panel para todas las filas
        JPanel filasPanel = new JPanel();
        filasPanel.setLayout(new GridLayout(filaFin - filaInicio + 1, 1, 0, 5));
        filasPanel.setBackground(Color.WHITE);
        
        for (int fila = filaInicio; fila <= filaFin; fila++) {
            filasPanel.add(crearFilaAsientos(fila, configuracion));
        }
        
        // Crear JScrollPane para esta sección
        JScrollPane scrollInterno = new JScrollPane(filasPanel);
        scrollInterno.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollInterno.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollInterno.setBorder(null);
        scrollInterno.setPreferredSize(new Dimension(850, 600));
        
        seccion.add(scrollInterno, BorderLayout.CENTER);
        
        return seccion;
    }
    
    private JPanel crearSeccionClase(String titulo, int filaInicio, int filaFin, String configuracion, Color colorBorde) {
        JPanel seccion = new JPanel(new BorderLayout());
        seccion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorBorde, 2),
            titulo + " (Filas " + filaInicio + "-" + filaFin + ")",
            0, 0,
            new Font("Arial", Font.BOLD, 12),
            colorBorde
        ));
        seccion.setBackground(Color.WHITE);
        
        // Panel para todas las filas
        JPanel filasPanel = new JPanel();
        filasPanel.setLayout(new GridLayout(filaFin - filaInicio + 1, 1, 0, 5));
        filasPanel.setBackground(Color.WHITE);
        
        for (int fila = filaInicio; fila <= filaFin; fila++) {
            filasPanel.add(crearFilaAsientos(fila, configuracion));
        }
        
        seccion.add(filasPanel, BorderLayout.CENTER);
        
        // Agregar etiqueta de precio si es clase especial
        if (titulo.contains("PRIMERA") || titulo.contains("EJECUTIVA")) {
            double precio = titulo.contains("PRIMERA") ? precioPrimeraClase : precioClaseEjecutiva;
            JLabel lblPrecio = new JLabel(" Precio adicional: +$" + precio + " por asiento ", SwingConstants.RIGHT);
            lblPrecio.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 11));
            lblPrecio.setForeground(new Color(0, 100, 0));
            lblPrecio.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
            seccion.add(lblPrecio, BorderLayout.SOUTH);
        }
        
        return seccion;
    }
    
    private JPanel crearFilaAsientos(int numFila, String configuracion) {
        JPanel filaPanel = new JPanel(new BorderLayout());
        filaPanel.setBackground(Color.WHITE);
        filaPanel.setMaximumSize(new Dimension(800, 50));
        
        // Número de fila a la izquierda
        JPanel panelNumero = new JPanel(new BorderLayout());
        panelNumero.setPreferredSize(new Dimension(40, 40));
        panelNumero.setBackground(new Color(240, 240, 240));
        panelNumero.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        JLabel lblNumFila = new JLabel(String.valueOf(numFila));
        lblNumFila.setHorizontalAlignment(SwingConstants.CENTER);
        lblNumFila.setFont(new Font("Arial", Font.BOLD, 12));
        panelNumero.add(lblNumFila, BorderLayout.CENTER);
        
        // Panel central para asientos
        JPanel asientosPanel = new JPanel();
        asientosPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 5));
        asientosPanel.setBackground(Color.WHITE);
        
        // Crear asientos según configuración
        String[] partes = configuracion.split("-");
        char letraActual = 'A';
        
        for (int i = 0; i < partes.length; i++) {
            int asientosEnGrupo = Integer.parseInt(partes[i]);
            
            // Crear asientos del grupo
            for (int j = 0; j < asientosEnGrupo; j++) {
                char letra = (char)(letraActual + j);
                JButton asiento = crearBotonAsiento(numFila, letra);
                asientosPanel.add(asiento);
                botonesAsientos.put(numFila + "" + letra, asiento);
            }
            
            letraActual += asientosEnGrupo;
            
            // Agregar separador de pasillo si no es el último grupo
            if (i < partes.length - 1) {
                asientosPanel.add(crearSeparadorPasillo());
            }
        }
        
        filaPanel.add(panelNumero, BorderLayout.WEST);
        filaPanel.add(asientosPanel, BorderLayout.CENTER);
        
        return filaPanel;
    }
    
    private JPanel crearParteFrontal() {
        JPanel frontal = new JPanel();
        frontal.setLayout(new BoxLayout(frontal, BoxLayout.Y_AXIS));
        frontal.setBackground(new Color(100, 100, 150));
        frontal.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        frontal.setMaximumSize(new Dimension(120, 200));
        frontal.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel cockpit = new JLabel("COCKPIT");
        cockpit.setAlignmentX(Component.CENTER_ALIGNMENT);
        cockpit.setFont(new Font("Arial", Font.BOLD, 14));
        cockpit.setForeground(Color.WHITE);
        
        JLabel puertas = new JLabel("PUERTAS");
        puertas.setAlignmentX(Component.CENTER_ALIGNMENT);
        puertas.setFont(new Font("Arial", Font.BOLD, 12));
        puertas.setOpaque(true);
        puertas.setBackground(Color.DARK_GRAY);
        puertas.setForeground(Color.WHITE);
        puertas.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        frontal.add(Box.createVerticalStrut(20));
        frontal.add(cockpit);
        frontal.add(Box.createVerticalStrut(20));
        frontal.add(puertas);
        frontal.add(Box.createVerticalStrut(20));
        
        return frontal;
    }
    
    private JLabel crearSeparadorPasillo() {
        JLabel separador = new JLabel("  |  ");
        separador.setFont(new Font("Arial", Font.BOLD, 16));
        separador.setForeground(Color.GRAY);
        separador.setPreferredSize(new Dimension(30, 35));
        return separador;
    }
    
    private JButton crearBotonAsiento(int fila, char letra) {
        String codigo = fila + "" + letra;
        JButton btn = new JButton(codigo);
        
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setFont(new Font("Arial", Font.BOLD, 10));
        
        // Verificar si está ocupado
        if (asientosOcupados.contains(codigo)) {
            btn.setBackground(Color.RED);
            btn.setEnabled(false);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.GREEN);
            btn.addActionListener(e -> toggleAsiento(btn, codigo));
            btn.setForeground(Color.BLACK);
        }
        
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    private void toggleAsiento(JButton btn, String codigo) {
        // Si ya está seleccionado, deseleccionar
        if (btn.getBackground().equals(Color.BLUE)) {
            btn.setBackground(Color.GREEN);
            btn.setForeground(Color.BLACK);
            asientosSeleccionados.remove(codigo);
            actualizarContadores();
        } 
        // Si está disponible, intentar seleccionar
        else if (btn.getBackground().equals(Color.GREEN)) {
            // Verificar límite de asientos
            if (asientosSeleccionados.size() >= maxAsientosPermitidos) {
                JOptionPane.showMessageDialog(this,
                    "¡Límite alcanzado!\nSolo puedes seleccionar " + maxAsientosPermitidos + " asientos.\n" +
                    "Deselecciona uno primero para seleccionar otro.",
                    "Límite de asientos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Seleccionar
            btn.setBackground(Color.BLUE);
            btn.setForeground(Color.WHITE);
            asientosSeleccionados.add(codigo);
            actualizarContadores();
        }
    }
    
    private void actualizarContadores() {
        // Actualizar contador
        lblContador.setText("Asientos: " + asientosSeleccionados.size() + "/" + maxAsientosPermitidos);
        
        // Calcular precio adicional
        double precioAdicional = 0;
        
        for (String asiento : asientosSeleccionados) {
            try {
                int fila = Integer.parseInt(asiento.replaceAll("[A-Z]", ""));
                
                if (fila >= 1 && fila <= 5) {
                    precioAdicional += precioPrimeraClase;
                } else if (fila >= 6 && fila <= 15) {
                    precioAdicional += precioClaseEjecutiva;
                }
                // Económica no tiene precio adicional
            } catch (NumberFormatException e) {
                // Ignorar si no se puede parsear
            }
        }
        
        // Actualizar label de precio
        lblPrecioTotal.setText(String.format("Precio adicional: $%.2f", precioAdicional));
        
        // Cambiar color si se acerca al límite
        if (asientosSeleccionados.size() >= maxAsientosPermitidos) {
            lblContador.setForeground(Color.RED);
        } else if (asientosSeleccionados.size() >= maxAsientosPermitidos - 1) {
            lblContador.setForeground(Color.ORANGE);
        } else {
            lblContador.setForeground(Color.BLUE);
        }
    }
    
    private JPanel crearParteTrasera() {
        JPanel trasera = new JPanel();
        trasera.setLayout(new BoxLayout(trasera, BoxLayout.Y_AXIS));
        trasera.setBackground(new Color(100, 100, 150));
        trasera.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        trasera.setMaximumSize(new Dimension(120, 200));
        trasera.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel puertas = new JLabel("PUERTAS");
        puertas.setAlignmentX(Component.CENTER_ALIGNMENT);
        puertas.setFont(new Font("Arial", Font.BOLD, 12));
        puertas.setOpaque(true);
        puertas.setBackground(Color.DARK_GRAY);
        puertas.setForeground(Color.WHITE);
        puertas.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        JLabel banos = new JLabel("BAÑOS");
        banos.setAlignmentX(Component.CENTER_ALIGNMENT);
        banos.setFont(new Font("Arial", Font.BOLD, 12));
        banos.setOpaque(true);
        banos.setBackground(new Color(139, 69, 19));
        banos.setForeground(Color.WHITE);
        banos.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        trasera.add(Box.createVerticalStrut(20));
        trasera.add(puertas);
        trasera.add(Box.createVerticalStrut(10));
        trasera.add(banos);
        trasera.add(Box.createVerticalStrut(20));
        
        return trasera;
    }
    
    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel();
        leyenda.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        leyenda.setBorder(BorderFactory.createTitledBorder("LEYENDA"));
        leyenda.setBackground(Color.WHITE);
        leyenda.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leyenda.add(crearItemLeyenda(Color.GREEN, "Disponible"));
        leyenda.add(crearItemLeyenda(Color.BLUE, "Seleccionado"));
        leyenda.add(crearItemLeyenda(Color.RED, "Ocupado"));
        leyenda.add(crearItemLeyenda(new Color(139, 0, 139), "Primera Clase"));
        leyenda.add(crearItemLeyenda(new Color(0, 0, 139), "Ejecutiva"));
        
        return leyenda;
    }
    
    private JPanel crearItemLeyenda(Color color, String texto) {
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        item.setBackground(Color.WHITE);
        
        JLabel colorLbl = new JLabel("  ");
        colorLbl.setOpaque(true);
        colorLbl.setBackground(color);
        colorLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        colorLbl.setPreferredSize(new Dimension(20, 20));
        
        JLabel textoLbl = new JLabel(texto);
        textoLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        
        item.add(colorLbl);
        item.add(textoLbl);
        
        return item;
    }
    
    private void cambiarZoom(double incremento) {
        zoomLevel += incremento;
        if (zoomLevel < 0.5) zoomLevel = 0.5;
        if (zoomLevel > 2.0) zoomLevel = 2.0;
        
        // Aplicar zoom
        int nuevoAncho = (int)(900 * zoomLevel);
        int nuevoAlto = (int)(2500 * zoomLevel);
        panelAvion.setPreferredSize(new Dimension(nuevoAncho, nuevoAlto));
        
        // Ajustar tamaño de botones
        int tamBoton = (int)(35 * zoomLevel);
        int tamFuente = (int)(10 * zoomLevel);
        
        for (JButton btn : botonesAsientos.values()) {
            btn.setPreferredSize(new Dimension(tamBoton, tamBoton));
            btn.setFont(new Font("Arial", Font.BOLD, tamFuente));
        }
        
        lblZoom.setText(String.format("Zoom: %d%%", (int)(zoomLevel * 100)));
        
        // Actualizar
        panelAvion.revalidate();
        panelAvion.repaint();
    }
    
    private void resetZoom() {
        zoomLevel = 1.0;
        cambiarZoom(0);
    }
    
    // Métodos públicos para configurar desde fuera
    public void setCallback(SeleccionAsientosCallback callback) {
        this.callback = callback;
    }
    
    public void setAsientosOcupados(List<String> asientosOcupados) {
        this.asientosOcupados = asientosOcupados != null ? asientosOcupados : new ArrayList<>();
        
        // Si ya hay botones creados, actualizarlos
        if (!botonesAsientos.isEmpty()) {
            for (Map.Entry<String, JButton> entry : botonesAsientos.entrySet()) {
                String codigo = entry.getKey();
                JButton btn = entry.getValue();
                
                if (this.asientosOcupados.contains(codigo)) {
                    btn.setBackground(Color.RED);
                    btn.setEnabled(false);
                    btn.setForeground(Color.WHITE);
                }
            }
        }
    }
    
    public List<String> getAsientosSeleccionados() {
        return new ArrayList<>(asientosSeleccionados);
    }

                                             

    
    
                                              
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelContenedor = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        panelAvion = new javax.swing.JPanel();
        panelControles = new javax.swing.JPanel();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        btnResetZoom = new javax.swing.JButton();
        lblZoom = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelContenedor.setLayout(new java.awt.BorderLayout());

        panelAvion.setBackground(new java.awt.Color(230, 247, 255));
        panelAvion.setPreferredSize(new java.awt.Dimension(600, 800));
        panelAvion.setLayout(new javax.swing.BoxLayout(panelAvion, javax.swing.BoxLayout.Y_AXIS));
        scrollPane.setViewportView(panelAvion);

        panelContenedor.add(scrollPane, java.awt.BorderLayout.CENTER);

        panelControles.setPreferredSize(new java.awt.Dimension(717, 30));

        btnZoomIn.setText("+");

        btnZoomOut.setText("-");

        btnResetZoom.setText("Reset");

        lblZoom.setText("Zoom: 100%");

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(btnZoomIn)
                .addGap(26, 26, 26)
                .addComponent(btnZoomOut)
                .addGap(43, 43, 43)
                .addComponent(btnResetZoom)
                .addGap(43, 43, 43)
                .addComponent(lblZoom)
                .addGap(103, 103, 103)
                .addComponent(btnAceptar)
                .addGap(51, 51, 51)
                .addComponent(btnCancelar)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnZoomIn)
                    .addComponent(btnZoomOut)
                    .addComponent(btnResetZoom)
                    .addComponent(lblZoom)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelContenedor.add(panelControles, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panelContenedor, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
       // Verificar que se seleccionaron asientos
        if (asientosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor selecciona al menos un asiento.",
                "Sin asientos seleccionados",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Calcular precio adicional
        double precioAdicional = 0;
        int primeraClase = 0;
        int ejecutiva = 0;
        int economica = 0;
        
        for (String asiento : asientosSeleccionados) {
            try {
                int fila = Integer.parseInt(asiento.replaceAll("[A-Z]", ""));
                
                if (fila >= 1 && fila <= 5) {
                    precioAdicional += precioPrimeraClase;
                    primeraClase++;
                } else if (fila >= 6 && fila <= 15) {
                    precioAdicional += precioClaseEjecutiva;
                    ejecutiva++;
                } else {
                    economica++;
                }
            } catch (NumberFormatException e) {
                // Continuar con el siguiente asiento
            }
        }
        
        // Crear mensaje de confirmación detallado
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("<html><body style='width: 300px'>");
        mensaje.append("<h3>RESUMEN DE SELECCIÓN</h3>");
        mensaje.append("<hr>");
        mensaje.append("<b>Asientos seleccionados:</b> ").append(asientosSeleccionados.size())
               .append(" de ").append(maxAsientosPermitidos).append("<br><br>");
        
        if (primeraClase > 0) {
            mensaje.append("• <font color='purple'>Primera Clase:</font> ").append(primeraClase)
                   .append(" asiento(s) - <b>+$").append(String.format("%.2f", primeraClase * precioPrimeraClase)).append("</b><br>");
        }
        if (ejecutiva > 0) {
            mensaje.append("• <font color='darkblue'>Clase Ejecutiva:</font> ").append(ejecutiva)
                   .append(" asiento(s) - <b>+$").append(String.format("%.2f", ejecutiva * precioClaseEjecutiva)).append("</b><br>");
        }
        if (economica > 0) {
            mensaje.append("• Clase Económica: ").append(economica).append(" asiento(s)<br>");
        }
        
        mensaje.append("<br>");
        mensaje.append("<b>Asientos:</b> ").append(asientosSeleccionados.toString()).append("<br><br>");
        
        mensaje.append("<hr>");
        mensaje.append("<b>PRECIO ADICIONAL:</b> <font color='green'>$").append(String.format("%.2f", precioAdicional)).append("</font><br>");
        mensaje.append("<b>PRECIO BASE:</b> $").append(String.format("%.2f", precioBase)).append("<br>");
        mensaje.append("<b>TOTAL ESTIMADO:</b> <font color='blue' size='+1'>$").append(String.format("%.2f", precioBase + precioAdicional)).append("</font><br><br>");
        
        mensaje.append("¿Confirmar selección?");
        mensaje.append("</body></html>");
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            mensaje.toString(),
            "Confirmar Selección de Asientos",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        /*if (confirmacion == JOptionPane.YES_OPTION) {
            if (callback != null) {
                callback.onAsientosSeleccionados(new ArrayList<>(asientosSeleccionados));
            }*/
        
        if (confirmacion == JOptionPane.YES_OPTION) {
    // 1. Convertir nombres a objetos Asiento
            List<modelo.Asiento> listaObjetos = new ArrayList<>();
        for (String s : asientosSeleccionados) {
            listaObjetos.add(new modelo.Asiento(s));
    }
    // 2. Obtener la reserva actual
            modelo.Reserva actual = controlador.ControladorReserva.getInstancia().getReservaActual();

    // 3. Decidir si guardar en Ida o Vuelta 
    // (Puedes usar una variable booleana 'esIda' que pases al abrir el diálogo)
        if (this.esIda) { 
            actual.setAsientosIda(listaObjetos);
        } else {
            actual.setAsientosVuelta(listaObjetos);
        }

    // Guardar el precio total acumulado
    actual.setTotalPagar(actual.getTotalPagar() + precioBase + precioAdicional);

        if (callback != null) {
            callback.onAsientosSeleccionados(new ArrayList<>(asientosSeleccionados));
    }
        
            this.dispose();
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        asientosSeleccionados.clear();
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SeleccionAsientosDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame parentFrame = new JFrame("Test");
                parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                parentFrame.setSize(300, 200);
                parentFrame.setVisible(true);
                
                SeleccionAsientosDialog dialog = new SeleccionAsientosDialog(
                    parentFrame, 
                    true, 
                    "Selección de Asientos - Vuelo de Ida",
                    3,      // Máximo 3 asientos
                    500.0,  // Precio base
                    200.0,  // Precio Primera Clase
                    100.0   // Precio Clase Ejecutiva
                );
                
                dialog.setCallback(new SeleccionAsientosCallback() {
                    @Override
                    public void onAsientosSeleccionados(List<String> asientos) {
                        System.out.println("Asientos confirmados: " + asientos);
                    }
                });
                
                List<String> ocupados = Arrays.asList("7A", "7B", "15C", "3D");
                dialog.setAsientosOcupados(ocupados);
                
                dialog.setSize(1200, 800);
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnResetZoom;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JLabel lblZoom;
    private javax.swing.JPanel panelAvion;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelControles;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
