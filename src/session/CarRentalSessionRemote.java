package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Set;

import rental.CarType;
import rental.Quote;
import rental.ReservationConstraints;
import rental.ReservationException;

public interface CarRentalSessionRemote extends Remote{
    
    Set<String> getAllRentalCompanies() throws RemoteException;
    
    void createQuote(ReservationConstraints constraints, String company) throws ReservationException, RemoteException;
    
    Set<Quote> getCurrentQuotes() throws RemoteException;
    
    void confirmQuotes() throws ReservationException, RemoteException;
    
    Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
    
    void setClientName(String name) throws RemoteException;
    
}
