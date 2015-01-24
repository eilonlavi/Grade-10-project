package projectiles;
import misc.*;
import def.*;
public class Bullet extends GameObject{
	public int speed=10;
	boolean secondHit=false;
	public int parentInd=-1;
	public Bullet(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		sprInd=6;
		moveable=true;
		centreOrigin=true;
		RAFCollision=true;
		w=4;
		h=4;
	}

	public void handleCollision(GameObject g){
		Class <?> o=g.getClass();
		int ox=g.x+g.vx;
		int oy=g.y+g.vy;
		int oh=g.h;
		int ow=g.w;
		if (ox-ow/2>x+vx+w/2) return;
		if (ox+ow/2<x+vx-w/2) return;
		if (oy-oh/2>y+vy+h/2) return;
		if (oy+oh/2<y+vy-h/2) return;
		if (o.equals(Block.class)){
			RSD=true;
		}
		else if (o.equals(Bullet.class)){
			RSD=true;
			g.RSD=true;
		}
		//used for fixing a glitch
		if (g.solid && g.index!=parentInd && !o.equals(Pit.class)){
			if (secondHit) RSD=true;
			else secondHit=true;
		}
	}
}