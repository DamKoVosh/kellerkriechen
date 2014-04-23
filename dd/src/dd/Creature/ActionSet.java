package dd.Creature;

public class ActionSet {
	public int standard;
	public int move;
	public int minor;
	
	public ActionSet() {
		this(0,0,0);
	}
	
	public ActionSet(int standard, int move, int minor) {
		this.standard = standard;
		this.move = move;
		this.minor = minor;
	}
	
	
}
