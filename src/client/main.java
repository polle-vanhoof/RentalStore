package client;

import java.util.Date;
import javax.naming.InitialContext;
import rental.CarType;

public class Main extends AbstractScriptedSimpleTripTest<CarRentalSessionRemote, CarRentalManagerRemote>{
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
        Main main = new Main("simpleTrips");
        main.run();
    }

    public Main(String scriptFile) {
        super(scriptFile);
    }

    @Override
    protected CarRentalSessionRemote getNewReservationSession(String name) throws Exception {
        InitialContext context = new InitialContext();
        CarRentalSessionRemote rentalSession = (CarRentalSessionRemote) context.lookup(CarRentalSessionRemote.class.getName());
        rentalSession.setClientName(name);
        return rentalSession;
    }

    @Override
    protected CarRentalManagerRemote getNewManagerSession(String name, String carRentalName) throws Exception {
        InitialContext context = new InitialContext();
        CarRentalManagerRemote managerSession = (CarRentalManagerRemote) context.lookup(CarRentalManagerRemote.class.getName());
        managerSession.setClientName(name);
        managerSession.setCompanyName(carRentalName);
        return managerSession;
    }

    @Override
    protected void checkForAvailableCarTypes(CarRentalSessionRemote session, Date start, Date end) throws Exception {
        for(CarType carType : session.getAvailableCarTypes(start, end)){
            System.out.println(carType.toString());
        }
    }

    @Override
    protected void addQuoteToSession(CarRentalSessionRemote session, String name, Date start, Date end, String carType, String carRentalName) throws Exception {
        ReservationConstraints constraints = new ReservationConstraints(start, end, carType);
        session.createQuote(constraints, carRentalName);
    }

    @Override
    protected void confirmQuotes(CarRentalSessionRemote session, String name) throws Exception {
        session.confirmQuotes();
    }

    @Override
    protected int getNumberOfReservationsBy(CarRentalManagerRemote ms, String clientName) throws Exception {
        return ms.getNbOfReservationsByClient(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(CarRentalManagerRemote ms, String carRentalName, String carType) throws Exception {
        return ms.getNbOfReservationsByCarType(carRentalName, carType);
    }
}
