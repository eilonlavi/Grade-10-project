package misc;
import def.GameObject;
public class Pit extends GameObject{

	public Pit(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		sprInd=28;
		solid=true;
		centreOrigin=true;
		w=32;
		h=32;
	}

}
