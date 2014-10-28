package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;

import rental.CarType;
import rental.Reservation;
import session.CarRentalSessionRemote;
import session.CarRentalManagerRemote;

public class Client extends AbstractScriptedTripTest<CarRentalSessionRemote, CarRentalManagerRemote>{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		
		SessionManagerRemote sm = (SessionManagerRemote) registry.lookup("sessionManager");
		
        Client client = new Client("trips");
        client.run();
    }

    public Client(String scriptFile) {
        super(scriptFile);
    }

    @Override
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
    	// TODO Auto-generated method stub
    	return null;
    }

    @Override
    protected void checkForAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception {
        for(CarType carType : session.getAvailableCarTypes(start, end)){
            System.out.println(carType.toString());
        }
    }

    @Override
    protected int getNumberOfReservationsBy(CarRentalManagerRemote ms, String clientName) throws Exception {
        return ms.getNbOfReservationsByClient(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(CarRentalManagerRemote ms, String carRentalName, String carType) throws Exception {
        return ms.getNbOfReservationsByCarType(carRentalName, carType);
    }

	@Override
	protected CarRentalManagerRemote getNewManagerSession(String name) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCheapestCarType(CarRentalSessionRemote session, Date start, Date end) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addQuoteToSession(CarRentalSessionRemote session, Date start, Date end, String carType, String carRentalName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<Reservation> confirmQuotes(CarRentalSessionRemote session) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<String> getBestClients(CarRentalManagerRemote ms) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CarType getMostPopularCarTypeIn(CarRentalManagerRemote ms, String carRentalCompanyName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}