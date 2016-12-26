import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GameInterface extends Remote {
	public boolean setName(String name) throws RemoteException;
	public void connectToClient(String name) throws RemoteException;
	public void setCoin(int inputCoin) throws RemoteException;
	public char[][] getPlaygroundBackup() throws RemoteException;
	public List<String> getNames() throws RemoteException;
	public InterfaceUser getClient1() throws RemoteException;
	public InterfaceUser getClient2() throws RemoteException;
	public boolean isNameOk(String name) throws RemoteException;
	}