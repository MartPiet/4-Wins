import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

public class Game implements Runnable{
	Playground p = new Playground();
    private Random start;
    private int nowPlaying = getRandomInt();
    private int coin;
    private boolean waitForPlayer1 = false, waitForPlayer2 = false;
    List<String> name;
    InterfaceUser client1;
    InterfaceUser client2;
    boolean endMessageFlag = true;


	
	public Game(List<String> inputName, InterfaceUser inputClient1, InterfaceUser inputClient2, char[][] playground, boolean first){
		if(playground != null){
			p.setPlayground(playground);
		}
		start = new Random();  
		name = inputName;
		try {
			client1 = (InterfaceUser) Naming.lookup("rmi://127.0.0.1:9090/" + name.get(0));
			client2 = (InterfaceUser) Naming.lookup("rmi://127.0.0.1:9090/" + name.get(1));

		} catch (MalformedURLException e) {
			System.out.println("Malformed URL.");
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("Remote Exception.");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("Not Bound.");
			e.printStackTrace();
		}
		if(first)
		printField();
	}
	
	public void run(){
		gameJudge(name, client1, client2);
	}

	public boolean hasWon(String nowPlaying){
		
	    for(int i = 0; i <= 4; i++){
	        for(int z = 0; z <= 9; z++){
	            if(((p.getCoin(i, z) != ' ')) && 
	            		((((z+3 <= 9) &&
	            				p.getCoin(i, z) == //vertikale
	            				p.getCoin(i, z + 1)) &&
	            			   (p.getCoin(i, z + 2) ==
	            				p.getCoin(i, z + 3)) &&
	            			   (p.getCoin(i,z) ==
	            				p.getCoin(i, z + 3))) ||
	            				
	            		(((i+3 <= 4) && (z+3 <= 9) && 
	            				p.getCoin(i, z) == //diagonale lr o
	                            p.getCoin(i + 1, z + 1)) &&
	                           (p.getCoin(i + 2, z + 2) ==
	                            p.getCoin(i + 3, z + 3)) &&
	                           (p.getCoin(i, z) ==
	                            p.getCoin(i + 3, z + 3))) ||
	                                                                  
	                    ((!(i-3 < 0) && (z+3 <= 9) && 
	                            p.getCoin(i, z) == //diagonale lr u
	                            p.getCoin(i - 1, z + 1)) &&
	                           (p.getCoin(i - 2, z + 2) ==
	                            p.getCoin(i - 3, z + 3)) &&
	                           (p.getCoin(i - 3, z + 3) ==
	                            p.getCoin(i, z))) ||
	                                                                  
	                    (((i+3 <= 4) &&
	                            p.getCoin(i, z) == //horizontale
	                            p.getCoin(i + 1, z)) &&
	                           (p.getCoin(i + 2, z) ==
	                            p.getCoin(i + 3, z)) &&
	                           (p.getCoin(i, z) ==
	                            p.getCoin(i + 3, z))))){
	            	System.out.println("Spieler " + nowPlaying + " hat gewonnen.");
	                return true;
	            }
	        }
	    }

	    return false;
	}

	public void gameJudge(List<String> name, InterfaceUser client1, InterfaceUser client2){ 	
	   
       if(!p.isFull() && !hasWon(name.get(nowPlaying))){
	        if(nowPlaying == 0){
				manageCoin(client1);
  	    		printField();
  	    		nowPlaying = 1;
  	    		Server.setPlaygroundBackup(p.getPlayground());
  	    		gameJudge(name, client1, client2);
	        } else if( nowPlaying == 1){
	        	manageCoin(client2);
  	    		printField();
  	    		nowPlaying = 0;
  	    		Server.setPlaygroundBackup(p.getPlayground());
	    		gameJudge(name, client1, client2);
	        }    
       }
      
       if(endMessageFlag == true){
    	   quitGame();
       }
	}

	public void manageCoin(InterfaceUser client){
		try {
			client.playerSetsCoin();
			System.out.println("client.playerSetsCoin() aufgerufen.");
		} catch (Exception e) {
			System.out.print("Remote Exception");
			e.printStackTrace();
		}
		
		if(client == client1){
			System.out.println("Spieler 1 ist dran.");
			System.out.print("Warte auf Spieler 1");
		} else {
			System.out.println("Spieler 2 ist dran.");
		    System.out.print("Warte auf Spieler 2");	
		}
		
		while(true){
			if((coin = Server.getCoin()) == 0){}
		    else { break; }
		    System.out.print(".");
		    try{
		    	Thread.sleep(1 * 1000);
		    } catch (Exception ex){
		      	System.out.println("Fehler bei Sleep()");
		    }
		}
		System.out.println("");
		if(client == client1)
			p.setCoin(coin, name.get(nowPlaying), 'X');  
		else
			p.setCoin(coin, name.get(nowPlaying), 'O');  
	}
	
	public void printField(){
		p.show();
  		try{
  		client1.printField(p.getPlayground());
  		client2.printField(p.getPlayground());
  		} catch (Exception ex){
  			System.out.println("Fehler bei Übermittlung der Spielfeldes.");
  		}
	}
	
	public void quitGame(){
	    try{
	    	if(nowPlaying == 1){
	    		client1.print("Du hast gewonnen!");
	    		client2.print(name.get(0) + " hat gewonnen!");
	    	} else {
	    		client2.print("Du hast gewonnen!");
	    		client1.print(name.get(1) + " hat gewonnen!");
	    	} 
	    } catch (Exception ex){
	    	System.out.println("Fehler bei Gewinnbenachrichtigung.");
	    }
	    System.out.println("Spiel vorbei!");
	    endMessageFlag = false;
	    
	}
	
	public int getRandomInt(){
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1);
		
		return randomInt;
    }

	public char[][] getPlayground(){
		return p.getPlayground();
	}

	public void setCoin(int inputCoin){
		coin = inputCoin;
	}
	
	public void stopWaiting(){
	    waitForPlayer1 = true; 
	    waitForPlayer2 = true;
	}

}