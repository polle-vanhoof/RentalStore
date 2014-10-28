package session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class SessionManager implements SessionManagerRemote {
	
	ArrayList<CarRentalSession> rentalSessions = new ArrayList<CarRentalSession>();
	ArrayList<CarRentalManager> managerSessions = new ArrayList<CarRentalManager>();

	public SessionManager() throws RemoteException{
		// TODO Auto-generated constructor stub
	}

	@Override
	public CarRentalSessionRemote getReservationSession() throws RemoteException{
		CarRentalSession session = new CarRentalSession();
		CarRentalSessionRemote stub = (CarRentalSessionRemote) UnicastRemoteObject.exportObject(session, 0);
		this.rentalSessions.add(session);
		return stub;
	}
	

	@Override
	public CarRentalManagerRemote getManagerSession() throws RemoteException{
		CarRentalManager session  = new CarRentalManager();
		CarRentalManagerRemote stub = (CarRentalManagerRemote) UnicastRemoteObject.exportObject(session, 0);
		this.managerSessions.add(session);
		return stub;
	}

}
