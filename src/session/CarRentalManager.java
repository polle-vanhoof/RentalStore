package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rental.Car;
import rental.CarRentalCompany;
import rental.CarRentalCompanyRemote;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;


public class CarRentalManager implements CarRentalManagerRemote {

	public CarRentalManager() throws RemoteException{

	}

	String clientName = "";
	String companyName = "";
	@Override
	public Set<String> getCarTypes(String carRentalCompany) throws Exception {
		CarRentalCompanyRemote carRentalComp = RentalStore.getRentals().get(carRentalCompany);
		if(carRentalComp == null) {
			throw new IllegalArgumentException("No CarRentalCompany found with name " + carRentalCompany + ".");
		}
		Collection<CarType> carTypes = carRentalComp.getAllTypes();

		Set<String> carTypesStr = new HashSet<String>();
		for(CarType carType : carTypes) {
			carTypesStr.add(carType.getName());
		}

		return carTypesStr;
	}

	@Override
	public int getNbOfReservationsByCarType(String carRentalCompany, String carType) throws Exception {
		CarRentalCompanyRemote carRentalComp = RentalStore.getRentals().get(carRentalCompany);
		if(carRentalComp == null) {
			throw new IllegalArgumentException("No CarRentalCompany found with name " + carRentalCompany + ".");
		}
		Set<Car> carsByType = carRentalComp.getCarsByType(carType);
		List<Reservation> resByCarType = new ArrayList<Reservation>();
		for (Car car : carsByType) {
			resByCarType.addAll(car.getAllReservations());
		}
		return resByCarType.size();
	}

	@Override
	public int getNbOfReservationsByClient(String client) throws RemoteException {
		Set<CarRentalCompanyRemote> allRentalComp = new HashSet<CarRentalCompanyRemote>(RentalStore.getRentals().values());
		int totalNbOfRes = 0;
		for (CarRentalCompanyRemote carRentalComp : allRentalComp) {
			totalNbOfRes += carRentalComp.getNbOfReservationsByClient(client);
		}
		return totalNbOfRes;  
	}

	@Override
	public void setClientName(String name) {
		this.clientName = name;
	}

	@Override
	public void setCompanyName(String carRentalName) {
		this.companyName = carRentalName;
	}

	@Override
	public void registerCompany(CarRentalCompanyRemote company){
		RentalStore.addRentalCompany(this.companyName, company);
	}

	@Override
	public CarType getMostPopularCarTypeIn(String company) throws Exception{
		CarType popular = null;
		CarRentalCompanyRemote crc = RentalStore.getRentals().get(company);
		popular = crc.getMostPopularCarType();
		if(popular == null){
			throw new Exception("there was no cheapest car type in company " + company);
		}
		return popular;
	}
	
	@Override
	public Set<String> getBestClients() throws RemoteException{
		HashSet<String> bestClients = new HashSet<String>();
		HashMap<String, Integer> clientReservations = new HashMap<String, Integer>();
		for(CarRentalCompanyRemote crc : RentalStore.getRentals().values()){
			for(Reservation res : crc.getAllReservations()){
				String client = res.getCarRenter();
				if(clientReservations.get(client) == null){
					clientReservations.put(client, 1);
				}else{
					clientReservations.put(client, clientReservations.get(client)+1);
				}
			}
		}
		
		int maxReservations = -1;
		for(String client : clientReservations.keySet()){
			int clientNum = clientReservations.get(client);
			if(clientNum> maxReservations){
				maxReservations = clientNum;
			}
		}
		for(String client : clientReservations.keySet()){
			if(clientReservations.get(client) == maxReservations){
				bestClients.add(client);
			}
		}
		return bestClients;
	}

}
