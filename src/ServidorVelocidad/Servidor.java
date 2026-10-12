package ServidorVelocidad;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main(String[] args) {

        try {
            Registry registro =
                    LocateRegistry.createRegistry(1099);

            CalculoRmiVelocidadImplem calculadora =
                    new CalculoRmiVelocidadImplem();

            registro.rebind("ServicioVelocidad", calculadora);

            System.out.println("==================================");
            System.out.println(" SERVIDOR RMI DE VELOCIDAD");
            System.out.println("==================================");
            System.out.println("Servidor iniciado correctamente.");
            System.out.println("Puerto: 1099");
            System.out.println("Servicio: ServicioVelocidad");
            System.out.println("Esperando solicitudes...");

        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor:");
            e.printStackTrace();
        }
    }
}