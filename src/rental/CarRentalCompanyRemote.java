package rental;

import java.rmi.Remote;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CarRentalCompanyRemote extends Remote{

	/********
	 * NAME *
	 ********/

	public abstract String getName();

	/*************
	 * CAR TYPES *
	 *************/

	public abstract Collection<CarType> getAllTypes();

	public abstract CarType getType(String carTypeName);

	public abstract boolean isAvailable(String carTypeName, Date start, Date end);

	public abstract Set<CarType> getAvailableCarTypes(Date start, Date end);

	/*********
	 * CARS *
	 *********/
	public abstract List<Car> getAllCars();

	public abstract Set<Car> getCarsByType(String carType);

	/****************
	 * RESERVATIONS *
	 ****************/

	public abstract Quote createQuote(ReservationConstraints constraints, String guest) throws ReservationException;

	public abstract Reservation confirmQuote(Quote quote) throws ReservationException;

	public abstract void cancelReservation(Reservation res);

}