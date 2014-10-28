/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Set;

import rental.CarRentalCompanyRemote;
import rental.CarType;


public interface CarRentalManagerRemote extends Remote{
    Set<String> getCarTypes(String carRentalCompany) throws Exception;
    int getNbOfReservationsByCarType(String carRentalCompany, String carType) throws Exception;
    int getNbOfReservationsByClient (String client) throws RemoteException;

    public void setClientName(String name) throws RemoteException;

    public void setCompanyName(String carRentalName) throws RemoteException;
    
    public void registerCompany(CarRentalCompanyRemote company) throws RemoteException;
    
	public CarType getMostPopularCarTypeIn(String company) throws Exception;
}
