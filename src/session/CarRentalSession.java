package session;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.ejb.Stateful;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.RentalStore;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Stateful
public class CarRentalSession implements CarRentalSessionRemote {
    private final HashSet<Quote> quotes = new HashSet<Quote>();
    private String clientName = "";

    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }

    @Override
    public void createQuote(ReservationConstraints constraints, String company) throws ReservationException{
        Quote quote = RentalStore.getRentals().get(company).createQuote(constraints, this.clientName);
        quotes.add(quote);
    }

    @Override
    public Set<Quote> getCurrentQuotes() {
        return this.quotes;
    }

    @Override
    public void confirmQuotes() throws ReservationException{
        HashMap<Quote,Reservation> reservations = new HashMap<Quote, Reservation>();
        try {
            for(Quote quote : this.quotes){
                CarRentalCompany company = RentalStore.getRentals().get(quote.getRentalCompany());
                Reservation r = company.confirmQuote(quote);
                reservations.put(quote, r);
            }
        } catch (ReservationException ex) {
            for(Quote q : reservations.keySet()){
                 CarRentalCompany company = RentalStore.getRentals().get(q.getRentalCompany());
                 company.cancelReservation(reservations.get(q));
            }
            throw ex;
        }
    }

    @Override
    public Set<CarType> getAvailableCarTypes(Date start, Date end) {
        HashSet<CarType> carTypes = new HashSet<CarType>();
        for(CarRentalCompany company : RentalStore.getRentals().values()){
            carTypes.addAll(company.getAvailableCarTypes(start, end));
        }
        return carTypes;
    }

    @Override
    public void setClientName(String name) {
        this.clientName = name;
    }
    
}