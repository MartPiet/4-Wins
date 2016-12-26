import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server2 extends UnicastRemoteObject implements GameInterface{
	static Thread newGame;
    static char[][] playgroundBackup;
    private int player = 0;
    private static int column;
    static List<String> playerName = new ArrayList<String>();
    private static InterfaceUser client1 = null;
	private static InterfaceUser client2 = null;
	static GameInterface masterServer = null;
    
    public Server2() throws RemoteException {
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
    
    public static void main(String[] args){
    	try {
			syncData();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public static void syncData() throws RemoteException, InterruptedException{
		
		connectToMasterServer();
		
		System.out.println("Starting Synchronization.");
		while(masterServer.getClient2() == null){
			Thread.sleep(1000);
		}
		try {
			setNames(masterServer.getNames());
			setClient1(masterServer.getClient1());
			setClient2(masterServer.getClient2());
			
			System.out.print("Syncing");
			while (true){
				try {
					setPlaygroundBackup(masterServer.getPlaygroundBackup());
					System.out.print(".");
			    	Thread.sleep(2 * 1000);
				} catch (Exception e){
					System.out.println("Synchronization failed.");
					try {
						setPlaygroundBackup(masterServer.getPlaygroundBackup());
						System.out.print(".");
						Thread.sleep(2 * 1000);
					} catch (Exception e2){
						System.out.print("Synchronization failed twice.");
						break;
					}
				}
			}
		} catch (Exception e){
			System.out.println("Synchronization failed.");
		}
		System.out.println(" Master Server is offline. Starting Backup-Server.");
			createServer();		
			start();
	}
	
	public static void connectToMasterServer(){

        try{
        	masterServer = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
        } catch (Exception ex) {
            System.out.println("connectToGameserver() failed: " + ex.getMessage() + " Trying again.");
            try{
            	masterServer = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
            } catch (Exception ex2) {
                System.out.println("connectToGameserver() failed again: " + ex2.getMessage() + " Qutting.");
            } 
        }
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
    	} else if(client2 == null){
    		try {
    			client2 = (InterfaceUser) Naming.lookup("rmi://127.0.0.1:9090/" + name);
        		System.out.println("Playing: " + player);
    		} catch (Exception ex) {
    			System.out.println("Exception(setName2): " + ex.getMessage());
    		}
    	}
    }

    public static void start() {        
    	clientReconnection();
        System.out.println("Spiel wird gestartet");
		newGame = new Thread( new Game(playerName, client1, client2, playgroundBackup, false) );
		newGame.start();
	    System.out.println("GameJudge ist aktiv");
    }
    
    public static void clientReconnection() {
    	try{
    		client1.reconnectToGameserver();
    		client2.reconnectToGameserver();
    	} catch (Exception e){
    		System.out.println("Reconnection failed.");
    	}
    }
    
    public static void createServer(){
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
		System.out.println("Test");
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
	
	public static void setPlayground(char[][] inputPlayground){
		playgroundBackup = inputPlayground;
	}
	
	public char[][] getPlayground(){
		return playgroundBackup;
	}
	
	public char[][] getPlaygroundBackup() throws RemoteException {
		return playgroundBackup;
	}
	
	public static void setNames(List<String> inputNames){
		playerName = inputNames;
	}

	public List<String> getNames() throws RemoteException {
		return playerName;
	}
	
	public static void setClient1(InterfaceUser inputClient){
		client1 = inputClient;
	}
	
	public static void setClient2(InterfaceUser inputClient){
		client2 = inputClient;
	}

	@Override
	public InterfaceUser getClient1() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterfaceUser getClient2() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNameOk(String name) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
}
