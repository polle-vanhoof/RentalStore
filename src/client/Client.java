package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import rental.Car;
import rental.CarRentalCompany;
import rental.CarRentalCompanyRemote;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;
import session.CarRentalManagerRemote;
import session.CarRentalSessionRemote;
import session.SessionManagerRemote;

public class Client extends AbstractScriptedTripTest<CarRentalSessionRemote, CarRentalManagerRemote>{
    
	
	static SessionManagerRemote sm = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
		System.setSecurityManager(null);
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		
		sm = (SessionManagerRemote) registry.lookup("sessionManager");
		
		registerCompanies();
		
        Client client = new Client("trips");
        client.run();
    }

    private static void registerCompanies() throws Exception {
    	// register dockx
    	CarRentalCompanyRemote dockx = loadRental("Dockx","dockx.csv");
		CarRentalManagerRemote dockxSession = sm.getManagerSession();
		System.out.println("test");
		dockxSession.setCompanyName("Dockx");
		CarRentalCompanyRemote stubDockx = (CarRentalCompanyRemote) UnicastRemoteObject.exportObject(dockx, 0);
		dockxSession.registerCompany(stubDockx);
		
		//register hertz
		CarRentalCompanyRemote hertz = loadRental("Hertz","hertz.csv");
		CarRentalManagerRemote hertzSession = sm.getManagerSession();
		hertzSession.setCompanyName("Hertz");
		CarRentalCompanyRemote stubHertz = (CarRentalCompanyRemote) UnicastRemoteObject.exportObject(hertz, 0);
		hertzSession.registerCompany(stubHertz);
		
		
	}

	public Client(String scriptFile) {
        super(scriptFile);
    }

    @Override
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
    	return sm.getReservationSession();
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
		return sm.getManagerSession();
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
	
	private static CarRentalCompanyRemote loadRental(String name, String datafile) {
        Logger.getLogger(RentalStore.class.getName()).log(Level.INFO, "loading {0} from file {1}", new Object[]{name, datafile});
        try {
            List<Car> cars = loadData(datafile);
            CarRentalCompanyRemote company = new CarRentalCompany(name, cars);
            return company;
        } catch (NumberFormatException ex) {
            Logger.getLogger(RentalStore.class.getName()).log(Level.SEVERE, "bad file", ex);
        } catch (IOException ex) {
            Logger.getLogger(RentalStore.class.getName()).log(Level.SEVERE, null, ex);
        }
		return null;
    }

    private static List<Car> loadData(String datafile)
            throws NumberFormatException, IOException {

        List<Car> cars = new LinkedList<Car>();

        int nextuid = 0;
       
        //open file from jar
        BufferedReader in = new BufferedReader(new InputStreamReader(RentalStore.class.getClassLoader().getResourceAsStream(datafile)));
        //while next line exists
        while (in.ready()) {
            //read line
            String line = in.readLine();
            //if comment: skip
            if (line.startsWith("#")) {
                continue;
            }
            //tokenize on ,
            StringTokenizer csvReader = new StringTokenizer(line, ",");
            //create new car type from first 5 fields
            CarType type = new CarType(csvReader.nextToken(),
                    Integer.parseInt(csvReader.nextToken()),
                    Float.parseFloat(csvReader.nextToken()),
                    Double.parseDouble(csvReader.nextToken()),
                    Boolean.parseBoolean(csvReader.nextToken()));
            //create N new cars with given type, where N is the 5th field
            for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
                cars.add(new Car(nextuid++, type));
            }
        }

        return cars;
    }
}
