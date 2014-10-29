package rental;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRentalCompany implements CarRentalCompanyRemote {

	private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());
	
	private String name;
	private List<Car> cars;
	private Map<String,CarType> carTypes = new HashMap<String, CarType>();

	/***************
	 * CONSTRUCTOR *
	 ***************/

	public CarRentalCompany(String name, List<Car> cars) {
		logger.log(Level.INFO, "<{0}> Car Rental Company {0} starting up...", name);
		setName(name);
		this.cars = cars;
		for(Car car:cars)
			carTypes.put(car.getType().getName(), car.getType());
	}



	@Override
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}



	@Override
	public Collection<CarType> getAllTypes(){
		return carTypes.values();
	}
	

	@Override
	public CarType getType(String carTypeName){
		if(carTypes.containsKey(carTypeName))
			return carTypes.get(carTypeName);
		throw new IllegalArgumentException("<" + carTypeName + "> No cartype of name " + carTypeName);
	}
	

	@Override
	public boolean isAvailable(String carTypeName, Date start, Date end) {
		logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[]{name, carTypeName});
		return getAvailableCarTypes(start, end).contains(carTypes.get(carTypeName));
	}
	

	@Override
	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		Set<CarType> availableCarTypes = new HashSet<CarType>();
		for (Car car : cars) {
			if (car.isAvailable(start, end)) {
				availableCarTypes.add(car.getType());
			}
		}
		return availableCarTypes;
	}


	@Override
	public List<Car> getAllCars(){
		return cars;
	}


	@Override
	public Set<Car> getCarsByType(String carType) {
		Set<Car> carsByType = new HashSet<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType)) {
				carsByType.add(car);
			}
		}
		return carsByType;
	}

	private Car getCar(int uid) {
		for (Car car : cars) {
			if (car.getId() == uid)
				return car;
		}
		throw new IllegalArgumentException("<" + name + "> No car with uid " + uid);
	}
	
	private List<Car> getAvailableCars(String carType, Date start, Date end) {
		List<Car> availableCars = new LinkedList<Car>();
		for (Car car : cars) {
			if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
				availableCars.add(car);
			}
		}
		return availableCars;
	}

        

	@Override
	public Quote createQuote(ReservationConstraints constraints, String guest)
			throws ReservationException {
		logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}", 
                        new Object[]{name, guest, constraints.toString()});
		
		CarType type = getType(constraints.getCarType());
		
		if(!isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate()))
			throw new ReservationException("<" + name
				+ "> No cars available to satisfy the given constraints.");
		
		double price = calculateRentalPrice(type.getRentalPricePerDay(),constraints.getStartDate(), constraints.getEndDate());
		
		return new Quote(guest, constraints.getStartDate(), constraints.getEndDate(), getName(), constraints.getCarType(), price);
	}

	// Implementation can be subject to different pricing strategies
	private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
		return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime())
						/ (1000 * 60 * 60 * 24D));
	}


	@Override
	public Reservation confirmQuote(Quote quote) throws ReservationException {
		logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[]{name, quote.toString()});
		List<Car> availableCars = getAvailableCars(quote.getCarType(), quote.getStartDate(), quote.getEndDate());
		if(availableCars.isEmpty())
			throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
	                + " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
		Car car = availableCars.get((int)(Math.random()*availableCars.size()));
		
		Reservation res = new Reservation(quote, car.getId());
		car.addReservation(res);
		return res;
	}


	@Override
	public void cancelReservation(Reservation res) {
		logger.log(Level.INFO, "<{0}> Cancelling reservation {1}", new Object[]{name, res.toString()});
		getCar(res.getCarId()).removeReservation(res);
	}
	
	public CarType getCheapestType(Date start, Date end) {
		CarType cheapest = new CarType("dummy", 0, 0, Double.MAX_VALUE, false);
		for(CarType carType : getAvailableCarTypes(start, end)) {
			if(carType.getRentalPricePerDay() < cheapest.getRentalPricePerDay()) {
				cheapest = carType;
			}
		}
		return cheapest;
	}
	
	/** Get all cars of specific type, count the amount of reservations
	 * 
	 * @return most popular carType
	 */
	@Override
	public CarType getMostPopularCarType(){
		CarType popular = null;
		int maxReservations = -1;
		for(CarType type : getAllTypes()){
			int numReservations = 0;
			for(Car car : getCarsByType(type.getName())){
				numReservations =+ car.getAllReservations().size();
			}
			if(numReservations > maxReservations){
				maxReservations = numReservations;
				popular = type;
			}
		}
		return popular;
	}
	
	@Override
	public String getBestClient(){
		HashMap<String, Integer> clientReservations = new HashMap<String, Integer>();
		for(Reservation res : getAllReservations()){
			String client = res.getCarRenter();
			if(clientReservations.get(client) == null){
				clientReservations.put(client, 1);
			}else{
				clientReservations.put(client, clientReservations.get(client)+1);
			}
		}
		int maxReservations = -1;
		String bestClient = null;
		for(String client : clientReservations.keySet()){
			int clientNum = clientReservations.get(client);
			if(clientNum> maxReservations){
				bestClient = client;
				maxReservations = clientNum;
			}
		}
		return bestClient;
	}
	
	private List<Reservation> getAllReservations(){
		LinkedList<Reservation> reservations = new LinkedList<Reservation>();
		for(Car car : getAllCars()){
			reservations.addAll(car.getAllReservations());
		}
		return reservations;
	}
}