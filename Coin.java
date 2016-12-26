
public class Coin implements Runnable{
	private InterfaceUser client;
	
	public Coin(InterfaceUser inputClient){
		try{
			inputClient.playerSetsCoin();
		} catch (Exception ex){
			System.out.print("Fehler beim thread Coin");
		}
	}
	
	public void run(){
		
	}
}
