package session;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rental.CarRentalCompanyRemote;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;


public class CarRentalSession implements CarRentalSessionRemote {
	private final HashSet<Quote> quotes = new HashSet<Quote>();
	private String clientName = "";

	public CarRentalSession() throws RemoteException{
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<String> getAllRentalCompanies() {
		return new HashSet<String>(RentalStore.getRentals().keySet());
	}

	@Override
	public void createQuote(ReservationConstraints constraints, String company) throws ReservationException, RemoteException{
		Quote quote = RentalStore.getRentals().get(company).createQuote(constraints, this.clientName);
		quotes.add(quote);
	}

	@Override
	public Set<Quote> getCurrentQuotes() {
		return this.quotes;
	}

	@Override
	public synchronized List<Reservation> confirmQuotes() throws ReservationException, RemoteException{
		HashMap<Quote,Reservation> reservations = new HashMap<Quote, Reservation>();
		LinkedList<Reservation> confirmedReservations = new LinkedList<Reservation>();
		try {
			for(Quote quote : this.quotes){
				CarRentalCompanyRemote company = RentalStore.getRentals().get(quote.getRentalCompany());
				Reservation r = company.confirmQuote(quote);
				reservations.put(quote, r);
			}
			confirmedReservations.addAll(reservations.values());

		} catch (ReservationException ex) {
			for(Quote q : reservations.keySet()){
				CarRentalCompanyRemote company = RentalStore.getRentals().get(q.getRentalCompany());
				company.cancelReservation(reservations.get(q));
			}
			throw ex;
		}
		return confirmedReservations;
	}

	@Override
	public Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		HashSet<CarType> carTypes = new HashSet<CarType>();
		for(CarRentalCompanyRemote company : RentalStore.getRentals().values()){
			carTypes.addAll(company.getAvailableCarTypes(start, end));
		}
		return carTypes;
	}

	@Override
	public void setClientName(String name) {
		this.clientName = name;
	}

	@Override
	public String getCheapestCarType(Date start, Date end) throws Exception {
		CarType cheapest = null;
		double cheapestPrice = new Double(Double.MAX_VALUE);;
		for(CarRentalCompanyRemote company : RentalStore.getRentals().values()) {
			if(company.getCheapestType(start, end).getRentalPricePerDay() < cheapestPrice) {
				cheapest = company.getCheapestType(start, end);
				cheapestPrice = company.getCheapestType(start, end).getRentalPricePerDay();
			}	
		}
		if(cheapest == null) {
			throw new Exception("No cheapest car type found!");
		}
		return cheapest.getName();
	}

}
