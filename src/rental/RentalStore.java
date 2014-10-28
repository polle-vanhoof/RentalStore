package rental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import session.SessionManager;
import session.SessionManagerRemote;

public class RentalStore {

    private static Map<String, CarRentalCompany> rentals;
    
	public static void main(String[] args) throws ReservationException, NumberFormatException, IOException {
		System.setSecurityManager(null);
		SessionManager sessionManager = new SessionManager();

		//Bind port 1099 to the registry
		System.out.println("Initialize RMI");
		Registry reg = LocateRegistry.createRegistry(1099);
		System.out.println("Server is ready");
		@SuppressWarnings("unused")
		SessionManagerRemote stub = (SessionManagerRemote) UnicastRemoteObject.exportObject(sessionManager, 0);
		reg.rebind("sessionManager", sessionManager);
	}

	public static synchronized Map<String, CarRentalCompany> getRentals(){
        if(rentals == null){
            rentals = new HashMap<String, CarRentalCompany>();
            loadRental("Hertz","hertz.csv");
            loadRental("Dockx","dockx.csv");
        }
        return rentals;
    }

    
}