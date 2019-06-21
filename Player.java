package goose;

public class Player {
	private String Name=null;
	private int Position=0;
	private int PrevPosition=0;
	
	int GetPosition(){
		return(Position);
	}
	int GetPrevPosition(){
		return(PrevPosition);
	}
	String GetName() {
		return(Name);
	}
	void SetName(String NewNAme) {
		Name=NewNAme;
	}
	void GoOn(int NPos){
		PrevPosition=Position;
		Position=Position+NPos;
	}
	void GoBack(int NPos){
		
		Position=Position-NPos;
	}
	
}
