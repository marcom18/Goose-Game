package goose;

import java.awt.Window.Type;
import java.io.*;
import java.math.*;

public class Main {		
	
	static int PlayerIndex=1;
	static Box box[]= new Box[64];
	static Player player[]= new Player[100];
	
	
	private static int DiceRoll() {									//generate 2 casual number between 1 and 6
		return((int)((Math.random()*5)+1));	
	}
	private static Boolean  NumberCheck(String BufferString) {		//take 2 numbers from main and check in they are between 1 and 6
		if(((BufferString.charAt(BufferString.length()-4)=='1')||
		   (BufferString.charAt(BufferString.length()-4)=='2')||
		   (BufferString.charAt(BufferString.length()-4)=='3')||
		   (BufferString.charAt(BufferString.length()-4)=='4')||
		   (BufferString.charAt(BufferString.length()-4)=='5')||
		   (BufferString.charAt(BufferString.length()-4)=='6'))&&	
		   ((BufferString.charAt(BufferString.length()-1)=='1')||
		   (BufferString.charAt(BufferString.length()-1)=='2')||
		   (BufferString.charAt(BufferString.length()-1)=='3')||
		   (BufferString.charAt(BufferString.length()-1)=='4')||
		   (BufferString.charAt(BufferString.length()-1)=='5')||
		   (BufferString.charAt(BufferString.length()-1)=='6'))){
			return(true);
			
		}else {
			return(false);
		}
		
	}
	
	
	private static void BoxCheck(int Dice1, int Dice2, Player player) {
		int position=player.GetPosition();
		if(box[position].IsGoose==true) {							//check if the player position is a goose 
			box[position].SetPlayer(null);							//and if this is true it move the playrer forward again by the sum of the two dice
			player.GoOn(Dice1+Dice2);
			System.out.print(", The Goose "+player.GetName()+" moves again and goes to "+player.GetPosition());
			BoxCheck(Dice1, Dice2, player);							//finaly start again Boxcheck to check where player landed
		}
		if(box[position].IsBridge==true) {							//check if the player position is a bridge 
			box[position].SetPlayer(null);							//and if this is true it move the player forward to the box 12
			player.GoOn(6);

			System.out.print(". "+player.GetName()+" jumps to 12");
			BoxCheck(Dice1, Dice2, player);							//finaly start again Boxcheck to check where player landed
		}
		if(box[position].WhoIsHere()!=null) {						//check if the box where player landed is occupied by an othe player
			Player BufferPlayer=box[position].WhoIsHere();
			System.out.println(". On "+position+" there is "+BufferPlayer.GetName()+" who returns to "+player.GetPrevPosition());
			if(player.GetPosition()>=player.GetPrevPosition()) {	//if is true and the player previus position was > than actual position send  forward the other player to the player previus position
				BufferPlayer.GoBack((player.GetPosition()-player.GetPrevPosition()));
				box[player.GetPrevPosition()].SetPlayer(BufferPlayer);
			}else {													//if is true and the player previus position was < than actual position send  back the other player to the player previus position
				BufferPlayer.GoOn((player.GetPrevPosition()-player.GetPosition()));
				box[player.GetPrevPosition()].SetPlayer(BufferPlayer);	
			}
			
		}
		box[position].SetPlayer(player);							//finaly set the player to the to his new position
		
	}
	
	
	private static Boolean MovePlayer(String BufferString, int Dice1, int Dice2) {
		Boolean Victory=false;
		String PlayerName= BufferString.substring(5,BufferString.length()-5);
		if(Dice1==0) {
			 Dice1=DiceRoll();
			 Dice2=DiceRoll();
		}

		for(int k=1;k<PlayerIndex;k++) {						//search the player from the name iserted
			if(PlayerName.equals(player[k].GetName())) {
				
				int BeginPosition=player[k].GetPosition();
				box[BeginPosition].SetPlayer(null);
				if(BeginPosition+Dice1+Dice2>63) {				//if the sum of dice1+dice2+ position >63 player bounces
					int StepBack=(Dice1+Dice2)-(63-BeginPosition);
					player[k].GoOn(Dice1+Dice2);
					player[k].GoBack(player[k].GetPosition()-63+StepBack);
					System.out.print(player[k].GetName()+" rolls "+Dice1+","+Dice2+". "+player[k].GetName()+" moves from "+BeginPosition+" to 63. "+player[k].GetName()+" bounces! "+player[k].GetName()+" returns to "+(63-StepBack));;
					BoxCheck(Dice1,Dice2, player[k]);			//and finaly he start the BoxCheck
				}else {
					if(BeginPosition==0) {						//if the begin position=0  print player move from start to...
						if((Dice1+Dice2)==6) {
							System.out.print(player[k].GetName()+" rolls "+Dice1+","+Dice2+". "+player[k].GetName()+" moves from start to The Bridge");	 
						}else {									//if the player land to the bridge he print player move from... to the the bridge
							System.out.print(player[k].GetName()+" rolls "+Dice1+","+Dice2+". "+player[k].GetName()+" moves from start to "+(player[k].GetPosition()+Dice1+Dice2));
;
						}
						player[k].GoOn(Dice1+Dice2);
						BoxCheck(Dice1,Dice2, player[k]);
					}else {
						if((Dice1+Dice2+player[k].GetPosition())==6) {	//if the player land to the bridge he print player move from... to  the bridge
							System.out.print(player[k].GetName()+" rolls "+Dice1+","+Dice2+". "+player[k].GetName()+" moves from "+player[k].GetPosition()+" to The Bridge ");
						}else {
							
																		//if the begin position is not 0 and the player dont land to the bridge print player move from...  to...
							System.out.print(player[k].GetName()+" rolls "+Dice1+","+Dice2+". "+player[k].GetName()+" moves from "+player[k].GetPosition()+" to "+(player[k].GetPosition()+Dice1+Dice2));
						}
						player[k].GoOn(Dice1+Dice2);					//move the player
						BoxCheck(Dice1,Dice2, player[k]);				//start BoxCheck
					}
				}
				if(player[k].GetPosition()==63) {						//if new player position is 63 return true else return false
					System.out.print(" "+player[k].GetName()+" wins!!");
					System.out.println("");
					Victory=true;
					
				}else {
					System.out.println("");
					Victory=false;
				}
			
			}
		}
		
		return(Victory);
	}

	
	private static void AddPlayer(String BufferString) {
		Boolean NameCheck=true;
		String PlayerName=BufferString.substring(11);
		for(int k=1;k<PlayerIndex;k++) {							//check if the player name already exist
			if(PlayerName.equals(player[k].GetName())) {
				System.out.println(PlayerName+": already existing player");
				NameCheck=false;
			
			
			}
		}
			if(NameCheck==true) {									//is the name dont exist already create new player and print the player list
				
				player[PlayerIndex]= new Player();
				player[PlayerIndex].SetName(PlayerName);
				PlayerIndex++;
				System.out.print("players: ");
				for(int j=1;j<PlayerIndex;j++) {
			
					System.out.print(player[j].GetName()+", ");
			
				}
			}
			System.out.println("");
		
	}
	
	
	public static void main(String[] args) {
		InputStreamReader input= new InputStreamReader(System. in);
		BufferedReader inputbox= new BufferedReader(input) ;
		
		
		System.out.println("-----WELCOME TO THE GOOSE GAME BY MARCO MILANI----- \n \n");
		System.out.println("--Rules--\n\n");
		System.out.println("Goose game is a game where two or more players move pieces around a track by rolling a die. \n");
		System.out.println("the winner must get  the exact dice shooting, if he get a dice shooting who send him in a box > than 63 he bounces. \n");
		System.out.println("along the way there are aids to get to the finish line faster.");
		System.out.println(" -The Bridge:  when one player get to the space 6, The Bridge , he jump to the space 12.");
		System.out.println(" -The Goose: when one get to a space with a picture The Goose , he move forward again by the sum of the two dice rolled before.");
		System.out.println(" --The spaces 5, 9, 14, 18, 23, 27 have a picture of The Goose. \n");
		System.out.println("Attention: when one player es. pippo land on a space occupied by another player es. pluto, he  send pluto to the previous position of pippo. \n\n");
		System.out.println("--how to play--\n\n");
		System.out.println("before starting you need to create your player.");
		System.out.println("digit: (add player  your_nickname)");
		System.out.println("Attention: player nickname cant contain numbers or special charaters.");
		System.out.println("Attention: player nickname cant be duplicated, you cant take a nickname who already exist.\n");
		System.out.println("you can move your player in 2 ways:");
		System.out.println("-rolling dice and digiting: (move nickname first_number, second_number) es. (move pippo 6, 3)");
	
		System.out.println("-letting dice rolling to the program and digiting: (move nickname) es. (move pippo) \n\n");
		System.out.println("now you are ready to play good luck\n\n");
		
		String BufferString;
		boolean Victory=false;
		String Action=null;

		
		for(int i=0;i<=63;i++) {										//create the objects box and he set Gosses and the Bridge 
			box[i]= new Box();
		}
		
		box[5].SetIsGoose(true);
		box[9].SetIsGoose(true);
		box[14].SetIsGoose(true);
		box[18].SetIsGoose(true);
		box[23].SetIsGoose(true);
		box[27].SetIsGoose(true);
		
		box[6].SetIsBridge(true);
		try {
			
			while(Victory==false) {									  //continue to get data from keyboard until one player win
			int Dice1=0, Dice2=0;																	
			BufferString=inputbox.readLine();
			Action=BufferString.substring(0,10);
			
			if(Action.contentEquals("add player")) {				 //check if the user inseerted the string add player
				AddPlayer(BufferString);						   	//and if positive he start the subroutine AddPlayer
				
			
		}
			
																					
			
			Action=BufferString.substring(0,4);
			
			
			if(Action.equals("move")) {							 //check if the user added the string move
				if(NumberCheck(BufferString)==true) {			//if positive he start the subroutin NumberCheck who check if user inserted a correct dice rolling
				
					Dice1=Character.getNumericValue(BufferString.charAt(BufferString.length()-4));	//if dice rolling is correct he start the subroutine MovePlayer giving 2 correct  dice 
					Dice2=Character.getNumericValue(BufferString.charAt(BufferString.length()-1));
				}else {
			
					BufferString=BufferString+"     ";												//else it start the subroutine giving 0 and 0 as dice 
				}
				Victory=MovePlayer(BufferString,Dice1,Dice2);

			}
			
			
			
			
																			
			}
		

			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
	
