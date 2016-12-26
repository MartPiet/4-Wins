import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ServerSynchronization implements Runnable{
	GameInterface masterServer = null;

	
	public void run() {
		syncData();
	}
	
	public void syncData(){
		connectToMasterServer();
		
		System.out.println("Starting Synchronization.");
		
		try {
			Server2.setNames(masterServer.getNames());
			Server2.setClient1(masterServer.getClient1());
			Server2.setClient2(masterServer.getClient2());
			
			System.out.print("Syncing");
			while (true){
				try {
					Server2.setPlaygroundBackup(masterServer.getPlaygroundBackup());
					System.out.print(".");
			    	Thread.sleep(2 * 1000);
				} catch (Exception e){
					System.out.println("Synchronization failed.");
					try {
						Server2.setPlaygroundBackup(masterServer.getPlaygroundBackup());
						System.out.print(".");
						Thread.sleep(2 * 1000);
					} catch (Exception e2){
						System.out.println("Synchronization failed twice. Starting Backupserver.");
						break;
					}
				}
			}
		} catch (Exception e){
			System.out.println("Synchronization failed.");
		}
		System.out.println("Master Server is offline. Starting Backup Server.");
			Server2.createServer();
			Server2.start();

		
		/*System.out.print("Syncing");
		while (true){
			try {
				Server2.setPlaygroundBackup(masterServer.getPlaygroundBackup());
				System.out.print(".");
		    	Thread.sleep(2 * 1000);
			} catch (Exception e){
				System.out.println("Synchronization failed.");
			} try {
				Server2.setPlaygroundBackup(masterServer.getPlaygroundBackup());
			} catch (Exception e){
				System.out.println("Synchronization failed twice. Starting Backupserver.");
				break;
			}
		}*/
		Server2.createServer();
		Server2.start();
	}
	
	public void connectToMasterServer(){

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
}
