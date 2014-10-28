package session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionManagerRemote extends Remote{

	public abstract CarRentalSessionRemote getReservationSession() throws RemoteException;

	public abstract CarRentalManagerRemote getManagerSession() throws RemoteException;

}