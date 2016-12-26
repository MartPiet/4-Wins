import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server extends UnicastRemoteObject implements GameInterface{
	Thread newGame;

    private int player = 0;
    private static int column;
    List<String> playerName = new ArrayList<String>();
    private InterfaceUser client1 = null, client2 = null;
    static char[][] playgroundBackup = null;
    
    public Server() throws RemoteException {
    }
    
    public boolean setName(String name){
    	if(player < 2){
    			playerName.add(name);
        	player++;
    		System.out.println("Spieler: " + playerName);
    	} else {
    		return false;
    	}
    	return true;
    }
    
    public void connectToClient(String name){
    	setName(name);
    	if(client1 == null){
    		try {
    			client1 = (InterfaceUser) Naming.lookup("rmi://127.0.0.1:9090/" + name);
    			System.out.println("Playing: " + player);
    		} catch (Exception ex) {
    			System.out.println("Exception(setName1): " + ex.getMessage());
    		}
    	} else if(client2 == null && name != playerName.get(0)){
    		try {
    			client2 = (InterfaceUser) Naming.lookup("rmi://127.0.0.1:9090/" + name);
        		System.out.println("Playing: " + player);
    		} catch (Exception ex) {
    			System.out.println("Exception(setName2): " + ex.getMessage());
    		}
    		start();
    	} else {
    		
    	}
    }

    public void start() {        
        System.out.println("Spiel wird gestartet");
		newGame = new Thread( new Game(playerName, client1, client2, playgroundBackup, true));
		newGame.start();
	    System.out.println("GameJudge ist aktiv");
    }

    public static void main(String[] args) {
        System.setSecurityManager(new RMISecurityManager());
        Server server = null;
        try {
            server = new Server();
            Naming.bind("rmi://127.0.0.1:9090/server", server);
            System.out.println("Server started ...");
        } catch (Exception ex) {
            System.out.println("bind failed: " + ex.getMessage());
            System.out.println("try to rebind ...");
            try {
                Naming.rebind("rmi://127.0.0.1:9090/server", server);
                System.out.println("Server started ...");
            } catch (Exception exx) {
                System.out.println(exx.getMessage());
            }
        }
    }
	
	public synchronized void sleep(int secs){
		System.out.println("Beginn von Sleep(" + secs +")");
		try {
			Thread.sleep(secs * 1000);
		} 
		catch(InterruptedException e) {
			System.out.println(e);
		}
		System.out.println("Ende von Sleep(" + secs +")");
	}
	
	public void setCoin(int inputColumn){
		column = inputColumn;
	}
	
	public static int getCoin(){
		int tmpColumn = column;
		column = 0;
		return tmpColumn;
	}
	
	public static void setPlaygroundBackup(char[][] inputPlayground){
		playgroundBackup = inputPlayground;
	}
	
	public char[][] getPlaygroundBackup(){
		return playgroundBackup;
	}
	
	public List<String> getNames(){
		return playerName;
	}
	
	public InterfaceUser getClient1(){
		return client1;
	}
	
	public InterfaceUser getClient2(){
		return client2;
	}
	
	public boolean isNameOk(String name){
		if(player > 0 && name.equals(playerName.get(0)))
			return false;
		return true;
	}
	
}






