package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CarRentalCompanyRemote extends Remote{

	/********
	 * NAME *
	 ********/

	public abstract String getName() throws RemoteException;

	/*************
	 * CAR TYPES *
	 *************/

	public abstract Collection<CarType> getAllTypes() throws RemoteException;

	public abstract CarType getType(String carTypeName) throws RemoteException;

	public abstract boolean isAvailable(String carTypeName, Date start, Date end) throws RemoteException;

	public abstract Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	public abstract CarType getCheapestType(Date start, Date end) throws Exception;

	/*********
	 * CARS *
	 *********/

	public abstract Set<Car> getCarsByType(String carType) throws RemoteException;

	/****************
	 * RESERVATIONS *
	 ****************/

	public abstract Quote createQuote(ReservationConstraints constraints, String guest) throws ReservationException, RemoteException;

	public abstract Reservation confirmQuote(Quote quote) throws ReservationException,RemoteException;

	public abstract void cancelReservation(Reservation res) throws RemoteException;

	public CarType getMostPopularCarType() throws RemoteException;

	public List<Reservation> getAllReservations() throws RemoteException;
	
	public int getNbOfReservationsByClient(String name) throws RemoteException;

}