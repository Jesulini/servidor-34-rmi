package ClienteVelocidad;

import LibreriaVelocidad.DatosVelocidad;
import LibreriaVelocidad.IRemotaCalculoVelocidad;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private JTextField txtVelocidades;
    private JTextArea txtResultado;
    private JButton btnCalcular;
    private JButton btnLimpiar;

    private IRemotaCalculoVelocidad servicio;

    public VentanaPrincipal() {

        setTitle("Promedio de Velocidad - RMI");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        conectarServidor();
        crearInterfaz();
    }

    private void conectarServidor() {

        try {
            Registry registro =
                    LocateRegistry.getRegistry("localhost", 1099);

            servicio = (IRemotaCalculoVelocidad)
                    registro.lookup("ServicioVelocidad");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con el servidor RMI.\n"
                    + "Inicie primero el servidor.\n\n"
                    + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearInterfaz() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel(
                "Cálculo del promedio de velocidad",
                SwingConstants.CENTER);

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 10));

        JLabel lblInstruccion = new JLabel(
                "Ingrese velocidades en km/h separadas por comas:");

        txtVelocidades = new JTextField();
        txtVelocidades.setToolTipText("Ejemplo: 40, 60, 80, 100");

        panelSuperior.add(lblInstruccion, BorderLayout.NORTH);
        panelSuperior.add(txtVelocidades, BorderLayout.CENTER);

        btnCalcular = new JButton("Calcular promedio");
        btnLimpiar = new JButton("Limpiar");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnCalcular);
        panelBotones.add(btnLimpiar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBorder(
                BorderFactory.createTitledBorder("Resultado"));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelSuperior, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.add(panelBotones, BorderLayout.NORTH);
        panelInferior.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnCalcular.addActionListener(e -> calcular());
        btnLimpiar.addActionListener(e -> limpiar());
        txtVelocidades.addActionListener(e -> calcular());
    }

    private void calcular() {

        if (servicio == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay conexión con el servidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String entrada = txtVelocidades.getText().trim();

        if (entrada.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese al menos una velocidad.");
            return;
        }

        try {
            String[] valores = entrada.split(",");
            double[] velocidades = new double[valores.length];

            for (int i = 0; i < valores.length; i++) {
                velocidades[i] = Double.parseDouble(
                        valores[i].trim());

                if (!Double.isFinite(velocidades[i])
                        || velocidades[i] < 0) {
                    throw new NumberFormatException();
                }
            }

            DatosVelocidad datos =
                    new DatosVelocidad(velocidades);

            // La solicitud se envía al servidor RMI.
            DatosVelocidad respuesta =
                    servicio.calcularPromedio(datos);

            StringBuilder resultado = new StringBuilder();

            resultado.append("VELOCIDADES REGISTRADAS\n");
            resultado.append("------------------------------\n");

            double[] recibidas = respuesta.getVelocidades();

            for (int i = 0; i < recibidas.length; i++) {
                resultado.append("Velocidad ")
                         .append(i + 1)
                         .append(": ")
                         .append(String.format("%.2f", recibidas[i]))
                         .append(" km/h\n");
            }

            resultado.append("\nCantidad de velocidades: ")
                     .append(recibidas.length);

            resultado.append("\nSuma total: ")
                     .append(String.format(
                             "%.2f", sumar(recibidas)))
                     .append(" km/h");

            resultado.append("\n\nPROMEDIO: ")
                     .append(String.format(
                             "%.2f", respuesta.getPromedio()))
                     .append(" km/h");

            txtResultado.setText(resultado.toString());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese únicamente números válidos "
                    + "separados por comas.\n"
                    + "Ejemplo: 40, 60, 80",
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al calcular el promedio:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private double sumar(double[] velocidades) {
        double suma = 0;

        for (double velocidad : velocidades) {
            suma += velocidad;
        }

        return suma;
    }

    private void limpiar() {
        txtVelocidades.setText("");
        txtResultado.setText("");
        txtVelocidades.requestFocus();
    }
}