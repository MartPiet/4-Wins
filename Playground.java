import java.io.*;

public class Playground {
	private char field[][] = {{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        	  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        	  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        	  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        	  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}};
	public Playground(){
        
	}
	
	public char[][] getPlayground(){
		return field;
	}
	
	public void setPlayground(char[][] inputPlayground){
		field = inputPlayground;
	}
	
	public void show(){
	    for(int z = 0; z <= 4; z++){
	    	System.out.print("|");
	        for(int i = 0; i <= 9; i++){
	        	System.out.print(field[z][i]); System.out.print("|");
	        }
	        System.out.println("");
	    }
	}

	public  boolean isFull(){ //Indizes tauschen ###################
	    return ((field[0][0] != ' ') && (field[0][1] != ' ') && (field[0][2] != ' ') && 
	    		(field[0][3] != ' ') && (field[0][4] != ' ') && (field[0][5] != ' ') && 
	    		(field[0][6] != ' ') && (field[0][7] != ' ') && (field[0][8] != ' ') && 
	    		(field[0][9] != ' '));
	}

/*	public Coin* getCoin(){
	    return &coins;
	}*/

	public int getCoin(int x, int y){
	    return field[x][y];
	}
	
	public boolean setCoin(int column, String name, char Symbol){
		if(controllEntry(column)){
			for(int y = 4; y >= 0; y--){
				if(field[y][column-1] == ' '){
					field[y][column-1] = Symbol;
    				return true;
				}
			} 
		} else {
			return false;
		}
		return false;
    }

/*	public void playerOneSetsCoin(int column, String name){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int x = 0;
	    
	    System.out.println(name + ": Geben Sie die Spalte an.");
	    try {
			x = Integer.parseInt(br.readLine());
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	    
	    if(controllEntry(x)){
	    	for(int y = 4; y >= 0; y--){
	    		if(field[y][x-1] == ' '){
	    			field[y][x-1] = 'O';
	    			break;
	    		}
	    		else if(y <= 0) {
	    			System.out.println("Spalte nicht verfügbar!");
	    			playerOneSetsCoin(column, name);
	    		}
	    	}
	    } else {
	    	System.out.println("Spalte nicht verfügbar!");
            playerOneSetsCoin(column, name);
	    }
	}

	public void playerTwoSetsCoin(int column, String name){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    int x = 0;
	    
	    System.out.println(name + ": Geben Sie die Spalte an.");
	    try {
			x = Integer.parseInt(br.readLine());
		} catch (NumberFormatException | IOException e) {
			System.out.print("test");
			e.printStackTrace();		
		}
	    
	    if(controllEntry(x)){
	    	for(int y = 4; y >= 0; y--){
	    		if(field[y][x-1] == ' '){
	    			field[y][x-1] = 'X';
	    			break;
	    		}
	    		else if(y <= 0){
	    			System.out.println("Spalte nicht verfügbar!");
	    			playerTwoSetsCoin(column, name);
	    		}
	    	} 
	    } else {
	    	System.out.println("Spalte nicht verfügbar!");
            playerTwoSetsCoin(column, name);
	    }
	}*/
	
	public boolean controllEntry(int entry){
		return 0 <= (entry - 1) && (entry -1) <= 9;
	}

}

//Evtl. playerOneSetsCoin und playerTwoSetsCoin in Game.java schreiben

