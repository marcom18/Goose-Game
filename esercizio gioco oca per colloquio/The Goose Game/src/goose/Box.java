package goose;

public class Box {
protected Boolean IsBridge=false;
protected Boolean IsGoose=false;
protected Player player=null;

public void SetIsBridge(Boolean Set) {
	IsBridge=Set;
}
public void SetIsGoose(Boolean Set) {
	IsGoose=Set;
}
public void SetPlayer(Player p) {
	player=p;
}
public Boolean GetIsBridge(){
	return(IsBridge);
}
public Boolean GetIsGoose(){
	return(IsGoose);
}
public Player WhoIsHere() {
	return player;
}
}