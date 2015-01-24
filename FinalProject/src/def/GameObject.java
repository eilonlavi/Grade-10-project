package def;
import java.awt.event.MouseEvent;
import projectiles.*;

/*
 * Used as a base for all objects in the game
 * objects can manipulate variables to interact with the main program.
 * Some variables are used to minimize running time, similar to java's import system.
 */
public class GameObject {
	public int x, y;
	public int speed;
	public int vx,vy;
	public int w, h;
	public int sprInd;
	public int index=0;
	public char firstPress=' ';
	public char secondPress=' ';
	public boolean moveable=false;
	public boolean centreOrigin=false;
	public boolean canChangeSprite=false;
	public boolean canShoot=false;
	public boolean RAFCollision=false;
	public boolean RSD=false;
	public boolean solid=false;
	public boolean RShoot=false;
	public boolean RRL=false;
	public boolean enemy=false;
	public boolean explode=false;
	public String message="";
	public boolean RHeroLocation=false;
	public String sign="";
	
	public GameObject(int X,int Y,int W,int H){
		x=X;
		y=Y;
		w=W;
		h=H;
		vx=0;
		vy=0;
	}
	public boolean isPointerInside(MouseEvent e){
		if (!centreOrigin){
			if (e.getX()<x) return false;
			if (e.getX()>x+w)return false;
			if (e.getY()<y) return false;
			if (e.getY()>y+h) return false;
		}
		else {
			if (e.getX()<x-w/2) return false;
			if (e.getX()>x+w/2) return false;
			if (e.getY()<y-h/2) return false;
			if (e.getY()>y+h/2) return false;
		}
		return true;
	}
	public void pointTowardsMouse(MouseEvent e){
		
	}
	public void move(){
		x+=vx;
		y+=vy;
	}
	
	public Bullet shoot(){
		return new Bullet(0,0,0,0);
	}
	
	public void handleCollision(GameObject g){
		
	}

	public void handleHeroLocation( int xValueOfHero1, int yValueOfHero1, int xValueOfHero2, int yValueOfHero2 )
	{}

}
