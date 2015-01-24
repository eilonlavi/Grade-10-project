package misc;
import def.GameObject;

public class Block extends GameObject{

	public Block(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		sprInd=7;
		centreOrigin=true;
		solid=true;
		w=32;
		h=32;
	}

}
