/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.Set;


public interface CarRentalManagerRemote {
    Set<String> getCarTypes(String carRentalCompany) throws Exception;
    int getNbOfReservationsByCarType(String carRentalCompany, String carType) throws Exception;
    int getNbOfReservationsByClient (String client) throws Exception;

    public void setClientName(String name);

    public void setCompanyName(String carRentalName);
}
