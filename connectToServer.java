import java.rmi.Naming;
import java.util.Scanner;

public class connectToServer implements Runnable{

	private static final GameInterface NULL = null;


	@Override
	public void run() {
		connecting();
	}
	
	
	public void connecting(){
			String name = Client.getName();
        	GameInterface game = null;
	        
	        try{
	        	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
	        	game.connectToClient(name);
	        	System.out.println("Warte auf Spieler.");
	        	System.out.println("game.connectToClient(name);");
	        } catch (Exception ex3){
	        	System.out.println("connectToClient() failed " + ex3.getMessage() + " Trying again.");
	        	try{
	        		game.connectToClient(name);
	            	System.out.println("game.connectToClient(name);");

	        	} catch (Exception ex4){
	        		System.out.println("connectToClient() failed " + ex4.getMessage() + " Please restart.");
	        	}
	        }
	}

}
