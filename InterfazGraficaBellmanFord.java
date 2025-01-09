import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InterfazGraficaBellmanFord {
    private JFrame ventana;
    private JPanel panelPrincipal;
    private JPanel panelControl;
    private JPanel panelGrafo;
    private JPanel panelEntrada;
    private JPanel panelSur;

    private JTable tablaDistancias;
    private DefaultTableModel modeloTabla;

    private JTextField campoOrigen;
    private JTextField campoDestino;
    private JTextField campoPeso;

    private JButton botonAgregarArista;
    private JButton botonEjecutar;
    private JButton botonLimpiar;

    private JTextArea areaResultados;
    private JScrollPane scrollResultados;
    private JScrollPane scrollTabla;

    private JLabel labelTitulo;
    private JLabel labelOrigen;
    private JLabel labelDestino;
    private JLabel labelPeso;

    private Grafo grafo;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton agregarAristaButton;
    private JButton ejecutarBellmanFordButton;
    private JButton limpiarButton;
    private JTextArea textArea1;
    private JTable table1;

    public InterfazGraficaBellmanFord() {
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
        agregarVerticesIniciales();
    }

    private void inicializarComponentes() {
        // Inicializar grafo
        grafo = new Grafo(10); // Máximo 10 vértices

        // Crear componentes principales
        ventana = new JFrame("Algoritmo de Bellman-Ford");
        panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelControl = new JPanel(new BorderLayout(5, 5));
        PanelGrafo panelGrafoPersonalizado = new PanelGrafo(grafo);
        panelGrafo = panelGrafoPersonalizado;
        panelEntrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelSur = new JPanel(new GridLayout(2, 1, 5, 5));

        // Configurar panel del grafo
        panelGrafo.setPreferredSize(new Dimension(500, 400));
        panelGrafo.setBorder(BorderFactory.createTitledBorder("Visualización del Grafo"));
        panelGrafo.setBackground(Color.WHITE);

        // Crear campos de texto
        campoOrigen = new JTextField(5);
        campoDestino = new JTextField(5);
        campoPeso = new JTextField(5);

        // Crear etiquetas
        labelTitulo = new JLabel("Ingresar Nueva Arista", SwingConstants.CENTER);
        labelOrigen = new JLabel("Origen:");
        labelDestino = new JLabel("Destino:");
        labelPeso = new JLabel("Peso:");

        // Crear botones
        botonAgregarArista = new JButton("Agregar Arista");
        botonEjecutar = new JButton("Ejecutar Bellman-Ford");
        botonLimpiar = new JButton("Limpiar");

        // Configurar tabla
        modeloTabla = new DefaultTableModel();
        tablaDistancias = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaDistancias);
        scrollTabla.setPreferredSize(new Dimension(400, 150));

        // Configurar área de resultados
        areaResultados = new JTextArea(5, 40);
        areaResultados.setEditable(false);
        scrollResultados = new JScrollPane(areaResultados);

        // Agregar componentes al panel de entrada
        panelEntrada.add(labelOrigen);
        panelEntrada.add(campoOrigen);
        panelEntrada.add(labelDestino);
        panelEntrada.add(campoDestino);
        panelEntrada.add(labelPeso);
        panelEntrada.add(campoPeso);
        panelEntrada.add(botonAgregarArista);
        panelEntrada.add(botonEjecutar);
        panelEntrada.add(botonLimpiar);

        // Configurar panel sur
        panelSur.add(scrollTabla);
        panelSur.add(scrollResultados);

        // Configurar panel de control
        panelControl.add(labelTitulo, BorderLayout.NORTH);
        panelControl.add(panelEntrada, BorderLayout.CENTER);
        panelControl.add(panelSur, BorderLayout.SOUTH);

        // Agregar todo al panel principal
        panelPrincipal.add(panelGrafo, BorderLayout.CENTER);
        panelPrincipal.add(panelControl, BorderLayout.SOUTH);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void configurarVentana() {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setContentPane(panelPrincipal);
        ventana.setSize(800, 700);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
    }

    private void configurarEventos() {
        botonAgregarArista.addActionListener(e -> agregarArista());
        botonEjecutar.addActionListener(e -> ejecutarBellmanFord());
        botonLimpiar.addActionListener(e -> limpiarGrafo());
    }

    private void agregarArista() {
        try {
            int origen = Integer.parseInt(campoOrigen.getText().trim());
            int destino = Integer.parseInt(campoDestino.getText().trim());
            int peso = Integer.parseInt(campoPeso.getText().trim());

            if (origen >= 0 && origen < grafo.getCantidadVertices() &&
                    destino >= 0 && destino < grafo.getCantidadVertices()) {

                grafo.agregarArista(origen, destino, peso);
                actualizarTabla();
                actualizarVisualizacionGrafo();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(ventana,
                        "Los vértices deben estar entre 0 y " + (grafo.getCantidadVertices() - 1),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana,
                    "Por favor, ingrese números válidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarBellmanFord() {
        try {
            String verticeInicio = JOptionPane.showInputDialog(ventana,
                    "Ingrese el vértice de inicio (0-" + (grafo.getCantidadVertices()-1) + "):");

            if (verticeInicio != null) {
                int inicio = Integer.parseInt(verticeInicio);
                ResultadoBellmanFord resultado = grafo.bellmanFord(inicio);

                if (resultado.distancias != null) {
                    actualizarResultados(inicio, resultado);
                } else {
                    areaResultados.setText(resultado.mensaje);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ventana,
                    "Por favor, ingrese un número de vértice válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(grafo.getCantidadVertices());

        // Establecer encabezados
        String[] encabezados = new String[grafo.getCantidadVertices()];
        for (int i = 0; i < grafo.getCantidadVertices(); i++) {
            encabezados[i] = "V" + i;
        }
        modeloTabla.setColumnIdentifiers(encabezados);

        // Crear y llenar matriz de adyacencia
        Object[][] matrizAdyacencia = new Object[grafo.getCantidadVertices()][grafo.getCantidadVertices()];
        for (int i = 0; i < grafo.getCantidadVertices(); i++) {
            for (int j = 0; j < grafo.getCantidadVertices(); j++) {
                matrizAdyacencia[i][j] = "";
            }
        }

        // Agregar pesos de las aristas
        for (Arista arista : grafo.getAristas()) {
            matrizAdyacencia[arista.origen][arista.destino] = arista.peso;
        }

        // Agregar filas a la tabla
        for (int i = 0; i < grafo.getCantidadVertices(); i++) {
            modeloTabla.addRow(matrizAdyacencia[i]);
        }
    }

    private void actualizarVisualizacionGrafo() {

        if (panelGrafo instanceof PanelGrafo) {
            ((PanelGrafo) panelGrafo).setDistancias(null);
            panelGrafo.repaint();
        }
    }

    private void actualizarResultados(int inicio, ResultadoBellmanFord resultado) {
        StringBuilder sb = new StringBuilder();
        sb.append("Caminos más cortos desde el vértice ").append(inicio).append(":\n\n");

        for (int i = 0; i < resultado.distancias.length; i++) {
            sb.append("Al vértice ").append(i).append(": ");
            if (resultado.distancias[i] == Integer.MAX_VALUE) {
                sb.append("INFINITO");
            } else {
                sb.append(resultado.distancias[i]);
            }
            sb.append("\n");
        }

        areaResultados.setText(sb.toString());

        // Actualizar visualización de distancias en el grafo
        if (panelGrafo instanceof PanelGrafo) {
            ((PanelGrafo) panelGrafo).setDistancias(resultado.distancias);
            panelGrafo.repaint();
        }
    }

    private void limpiarCampos() {
        campoOrigen.setText("");
        campoDestino.setText("");
        campoPeso.setText("");
        campoOrigen.requestFocus();
    }

    private void limpiarGrafo() {
        grafo = new Grafo(10);
        agregarVerticesIniciales();
        actualizarTabla();
        actualizarVisualizacionGrafo();
        areaResultados.setText("");
    }

    private void agregarVerticesIniciales() {
        int centroX = 250;
        int centroY = 200;
        int radio = 150;
        int vertices = 5;

        for (int i = 0; i < vertices; i++) {
            double angulo = 2 * Math.PI * i / vertices;
            int x = centroX + (int)(radio * Math.cos(angulo));
            int y = centroY + (int)(radio * Math.sin(angulo));
            grafo.agregarVertice(x, y);
        }

        actualizarTabla();
    }

    public void mostrar() {
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfazGraficaBellmanFord interfaz = new InterfazGraficaBellmanFord();
            interfaz.mostrar();
        });
    }
}
