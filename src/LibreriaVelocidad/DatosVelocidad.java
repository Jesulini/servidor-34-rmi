package LibreriaVelocidad;

import java.io.Serializable;

public class DatosVelocidad implements Serializable {

    private static final long serialVersionUID = 1L;

    private double[] velocidades;
    private double promedio;

    public DatosVelocidad(double[] velocidades) {
        this.velocidades = velocidades;
    }

    public double[] getVelocidades() {
        return velocidades;
    }

    public void setVelocidades(double[] velocidades) {
        this.velocidades = velocidades;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
}