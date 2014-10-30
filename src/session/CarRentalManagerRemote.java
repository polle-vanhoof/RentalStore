/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import rental.CarRentalCompanyRemote;
import rental.CarType;


public interface CarRentalManagerRemote extends Remote{
    
	public Set<String> getCarTypes(String carRentalCompany) throws Exception;
    
    public int getNbOfReservationsByCarType(String carRentalCompany, String carType) throws Exception;
    
    public int getNbOfReservationsByClient (String client) throws RemoteException;

    public void setClientName(String name) throws RemoteException;

    public void setCompanyName(String carRentalName) throws RemoteException;
    
    public void registerCompany(CarRentalCompanyRemote company) throws RemoteException;
    
    public void unRegisterCompany(String companyName) throws RemoteException;
    
    public Set<String> getRegisteredCompanyNames() throws RemoteException;
    
	public CarType getMostPopularCarTypeIn(String company) throws Exception;
	
	public Set<String> getBestClients() throws RemoteException;
}
