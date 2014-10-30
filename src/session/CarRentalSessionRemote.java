package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public interface CarRentalSessionRemote extends Remote{
    
    public Set<String> getAllRentalCompanies() throws RemoteException;
    
    public void createQuote(ReservationConstraints constraints, String company) throws ReservationException, RemoteException;
    
    public Set<Quote> getCurrentQuotes() throws RemoteException;
    
    public List<Reservation> confirmQuotes() throws ReservationException, RemoteException;
    
    public Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
    
    public void setClientName(String name) throws RemoteException;
    
    public String getCheapestCarType(Date start, Date end) throws RemoteException, Exception;
    
}
