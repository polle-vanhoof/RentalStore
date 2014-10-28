package rental;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import session.SessionManager;
import session.SessionManagerRemote;

public class RentalStore {

    private static Map<String, CarRentalCompanyRemote> rentalCompanies;
    
	public static void main(String[] args) throws ReservationException, NumberFormatException, IOException {
		System.setSecurityManager(null);
		SessionManager sessionManager = new SessionManager();
		rentalCompanies = new HashMap<String, CarRentalCompanyRemote>();

		//Bind port 1099 to the registry
		System.out.println("Initialize RMI");
		Registry reg = LocateRegistry.createRegistry(1099);
		System.out.println("Server is ready");
		@SuppressWarnings("unused")
		SessionManagerRemote stub = (SessionManagerRemote) UnicastRemoteObject.exportObject(sessionManager, 0);
		reg.rebind("sessionManager", sessionManager);
	}

    public static synchronized Map<String, CarRentalCompanyRemote> getRentals(){
        return rentalCompanies;
    }
    
    public static synchronized void addRentalCompany(String name, CarRentalCompanyRemote crcr) {
    	if(rentalCompanies.get(name) == null) {
    		rentalCompanies.put(name, crcr);
    	}
    }
    
}