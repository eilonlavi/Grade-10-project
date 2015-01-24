package misc;
import def.GameObject;
public class Explosion extends GameObject{
	int timeLeft=25;
	public Explosion(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		moveable=true;
		sprInd=30;
		centreOrigin=true;
		w=32;
		h=32;
	}
	public void move(){
		timeLeft--;
		if (timeLeft<=0){
			RSD=true;
		}
	}
}
