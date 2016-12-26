import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceUser extends Remote {
	public void print(String name) throws RemoteException;
	public void playerSetsCoin() throws RemoteException;
	public void errorInputMessage(int errCode) throws RemoteException;
	public void printField(char[][] field) throws RemoteException;
	public void reconnectToGameserver() throws RemoteException;
}
