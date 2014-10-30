package session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class SessionManager implements SessionManagerRemote {
	

	public SessionManager() throws RemoteException{
		// TODO Auto-generated constructor stub
	}

	@Override
	public CarRentalSessionRemote getReservationSession() throws RemoteException{
		CarRentalSession session = new CarRentalSession();
		CarRentalSessionRemote stub = (CarRentalSessionRemote) UnicastRemoteObject.exportObject(session, 0);
		return stub;
	}
	

	@Override
	public CarRentalManagerRemote getManagerSession() throws RemoteException{
		CarRentalManager session  = new CarRentalManager();
		CarRentalManagerRemote stub = (CarRentalManagerRemote) UnicastRemoteObject.exportObject(session, 0);
		return stub;
	}

}
