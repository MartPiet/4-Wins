import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Scanner;

import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements InterfaceUser {

	private static final long serialVersionUID = 1L;
	int column = 1;
	private static String name;
	private static GameInterface game = null;
	private static boolean coinWasSet = true;
	
    public Client() throws RemoteException {
    }
	
	public static void main(String[] args) throws RemoteException {
        System.setSecurityManager(new RMISecurityManager());
        
        connectToGameserver(); System.out.println("connectToGameserver() OK");
        setName();
        createServer(); System.out.println("createServer() OK");
    	game.connectToClient(name);
    	System.out.println("Warte auf Spieler.");
    	System.out.println("game.connectToClient(name);");
    }
	
	public static void main() throws RemoteException {
		System.out.println("test");
	}
	
	public static void setName(){
        Scanner in = new Scanner(System.in);

	    System.out.println("Gebe deinen Nutzernamen ein.");
	    name = in.nextLine();
	    name.replaceAll(" ", "");
    	try{
    		if(!game.isNameOk(name)){
	    		while(true){
	    			System.out.println("Name existiert schon. Gebe deinen Nutzernamen ein.");
	    			name = in.nextLine();
	    			name.replaceAll(" ", "");
	    		    if(game.isNameOk(name))
	    		    	break;
	    		}
    		}
	    } catch (Exception ex){
	    	System.out.println("Fehler bei Namenskontrolle seitens des Servers");
	       	try{
	    		if(!game.isNameOk(name)){
		    		while(true){
		    			System.out.println("Name existiert schon. Gebe deinen Nutzernamen ein.");
		    			name = in.nextLine();
		    			name.replaceAll(" ", "");
		    		    if(game.isNameOk(name))
		    		    	break;
		    		}
	    		}
		    } catch (Exception ex2){
		    	System.out.println("Fehler bei Namenskontrolle seitens des Servers");
		    }
	    }
	}

	public static void createServer(){
        //System.setSecurityManager(new RMISecurityManager());
	    System.out.println("2" +name);
	   
        Client client = null;
               
        try {
        	client = new Client();
            Naming.bind("rmi://127.0.0.1:9090/" + name, client);
        } catch (Exception ex) {
            System.out.println("bind failed: " + ex.getMessage());
            System.out.println("try to rebind ...");
            try {
                Naming.rebind("rmi://127.0.0.1:9090/" + name, client);
                System.out.println("Server " + name + " started ... (rmi://127.0.0.1:9090/" + name + ")");
            } catch (Exception exx) {
                System.out.println(exx.getMessage());
            }
        }  
	}
	
	public static void connectToGameserver(){
        try{
        	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
        } catch (Exception ex) {
            System.out.println("connectToGameserver() failed: " + ex.getMessage() + " Trying again.");
            try{
            	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
            } catch (Exception ex2) {
                System.out.println("connectToGameserver() failed again: " + ex2.getMessage() + " Qutting.");
            } 
        }
	}
	
	public void reconnectToGameserver(){
        try{
        	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
        } catch (Exception ex) {
            System.out.println("connectToGameserver() failed: " + ex.getMessage() + " Trying again.");
            try{
            	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
            } catch (Exception ex2) {
                System.out.println("connectToGameserver() failed again: " + ex2.getMessage() + " Qutting.");
            } 
        }
	}

    
	public void playerSetsCoin() throws RemoteException{
		if(coinWasSet){
			coinWasSet = false;
			Thread coin = new Thread( new clientSetsCoin() );
			coin.start();
		}
	}
	
	public static void coinWasSet(){
		coinWasSet = true;
	}
	
	public void errorInputMessage(int errCode){
		switch (errCode) {
        case 1:  System.out.println("Wähle eine Spalte zwischen 1 bis 10!");
                 break;
        case 2:  System.out.println("Wiederhole bitte noch einmal deinen Spielzug.");
        		 break;
        case 3:  System.out.println("Aktuelle Serverkapazitäten sind ausgeschöpft.");
		 		 break;	
        case 4:  System.out.println("Spiel beginnt.");
		 		 break;
        case 5:  System.out.println("Spiel beginnt.");
		 		 break;	
        default: System.out.println("Falsche Eingabe!");
                 break;
		}
	}
	
	public void printField(char[][] field) throws RemoteException{
	    for(int z = 0; z <= 4; z++){
	    	System.out.print("|");
	        for(int i = 0; i <= 9; i++){
	        	System.out.print(field[z][i]); System.out.print("|");
	        }
	        System.out.println("");
	    }
	    System.out.println("");
	}

	public void print(String name) throws RemoteException {
		System.out.println(name);
	}
	
	public static String getName(){
		return name;
	}
	
	public static GameInterface getGameInterfaceData(){
		return game;
	}
}
