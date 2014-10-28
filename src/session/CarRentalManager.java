/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rental.Car;
import rental.CarRentalCompany;
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
        CarRentalCompany carRentalComp = RentalStore.getRentals().get(carRentalCompany);
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
        CarRentalCompany carRentalComp = RentalStore.getRentals().get(carRentalCompany);
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

    
    public int getNbOfReservationsByClient(String client) {
        Set<CarRentalCompany> allRentalComp = new HashSet<CarRentalCompany>(RentalStore.getRentals().values());
        List<Reservation> resByClient = new ArrayList<Reservation>();
        for (CarRentalCompany carRentalComp : allRentalComp) {
            List<Car> allCarsByComp = new ArrayList<Car>(carRentalComp.getAllCars());
            for(Car car : allCarsByComp) {
                List<Reservation> allResByCarByComp = new ArrayList<Reservation>(car.getAllReservations());
                for(Reservation res : allResByCarByComp) {
                    if(res.getCarRenter().equals(client)) {
                        resByClient.add(res);
                    }
                }
            }
        }
       return resByClient.size();  
    }

    @Override
    public void setClientName(String name) {
        this.clientName = name;
    }

    @Override
    public void setCompanyName(String carRentalName) {
        this.companyName = carRentalName;
    }
    
    public void registerCompany(CarRentalCompany company){
    	// TODO
    }

}
