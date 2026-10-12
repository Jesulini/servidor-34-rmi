package LibreriaVelocidad;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemotaCalculoVelocidad extends Remote {

    DatosVelocidad calcularPromedio(DatosVelocidad datos)
            throws RemoteException;
}