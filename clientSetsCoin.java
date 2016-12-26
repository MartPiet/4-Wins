import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.Scanner;

public class clientSetsCoin implements Runnable{
	public clientSetsCoin(){
		
	}
	public void run(){
	    int x = 0;

        String name = Client.getName();
        GameInterface game = null;
        
        try{
        	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
        } catch (Exception ex){
        	System.out.println("Thread kann sich nicht zum Server verbinden");
        }
        
	    System.out.println(name + ": Geben Sie die Spalte an.");
	    
        Scanner input = new Scanner(System.in);


        x = input.nextInt();
	    
        if((x < 10) && (x > 0)){
	    	try{
	    		game.setCoin(x);
				Client.coinWasSet();
	    	} catch (Exception ex){
	            try{
	            	game = (GameInterface) Naming.lookup("rmi://127.0.0.1:9090/server");
		    		game.setCoin(x);
					Client.coinWasSet();
	            } catch (Exception ex2){
	            	System.out.println("Thread \"clientSetsCoin()\" kann sich nicht zum Server verbinden");
	            }
	    	}
		   
	    } else {
	    	 System.out.println(name + ": Bitte wähle eine Spalte zwischen 1 bis 10.");
			    run();
	    }
	}
}
