package ServidorVelocidad;

import LibreriaVelocidad.DatosVelocidad;
import LibreriaVelocidad.IRemotaCalculoVelocidad;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculoRmiVelocidadImplem
        extends UnicastRemoteObject
        implements IRemotaCalculoVelocidad {

    public CalculoRmiVelocidadImplem() throws RemoteException {
        super();
    }

    @Override
    public DatosVelocidad calcularPromedio(DatosVelocidad datos)
            throws RemoteException {

        if (datos == null
                || datos.getVelocidades() == null
                || datos.getVelocidades().length == 0) {
            throw new RemoteException(
                    "Debe ingresar al menos una velocidad.");
        }

        double[] velocidades = datos.getVelocidades();
        double suma = 0;

        for (double velocidad : velocidades) {
            if (!Double.isFinite(velocidad) || velocidad < 0) {
                throw new RemoteException(
                        "Las velocidades deben ser números válidos "
                        + "y no negativos.");
            }

            suma += velocidad;
        }

        double promedio = suma / velocidades.length;
        datos.setPromedio(promedio);

        return datos;
    }
}