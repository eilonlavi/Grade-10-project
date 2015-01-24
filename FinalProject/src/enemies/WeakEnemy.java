package enemies;
import misc.Block;
import def.*;
import projectiles.*;
public class WeakEnemy extends GameObject{

	int life=3;
	int speed=3;
	boolean canMoveUpDown=true;
	boolean moveLeft=true;
	int shootCount=(int) (Math.random()*70+10);


	public WeakEnemy(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		moveable=true;
		RAFCollision=true;
		sprInd=8;
		centreOrigin=true;
		w=24; h=42;
		solid=true;
		enemy=true;
		explode=true;
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
		if (g.solid){
			if (moveLeft){
				if (!o.equals(Hero.class) && !o.equals(Hero2.class)) moveLeft=false;
				else moveLeft=(ox>x); 

				if (canMoveUpDown) ;
				else if (!o.equals(WeakEnemy.class) && !o.equals(WeakEnemyFlip.class)) x++;
			}
			else {
				if (!o.equals(Hero.class) && !o.equals(Hero2.class)) moveLeft=true;
				else moveLeft=!(ox<x); 

				if (canMoveUpDown) ;
				else if (!o.equals(WeakEnemy.class) && !o.equals(WeakEnemyFlip.class)) x--;
			}

			if (!o.equals(Block.class) && canMoveUpDown){
				if (oy<y && sprInd==8) sprInd=9;
				else if (oy>y && sprInd==9) sprInd=8;
				vx=0;vy=0;
			}
			

			
		}
		if (o.equals(Bullet.class) && (g.sign.equals("Hero") || g.sign.equals("Hero2"))){
			g.RSD=true;
			if (life>1) life--;
			else RSD=true;
			if (g.sign.equals("Hero")) message="s1";
			else if (g.sign.equals("Hero2")) message="s2";
		}
		if (o.equals(Block.class)){
			vx=0;
			vy=0;
			canMoveUpDown=false;//DEBUGGED!//canMoveUpDown=(!((sprInd==8 && oy<y)||(sprInd==9 && oy>y)));
		}
	}

	public void move(){
		x+=vx;
		y+=vy;

		if (shootCount>0) shootCount--;
		else {RShoot=true; shootCount=(int) (Math.random()*10+10);}
		
		if (canMoveUpDown){
			vx=0;
			if (sprInd==8)
				vy=-speed;
			else vy=speed;
		}
		else{
			if (moveLeft){
				vx=-speed;
				vy=0;
			}
			else {
				vx=speed;
				vy=0;
			}
		}
	}

	public Bullet shoot(){
		RShoot=false;
		Bullet bullet=new Bullet(x,y,0,0);
		bullet.vx=0;
		if (sprInd==8){
			bullet.vy=bullet.speed;
		}
		else bullet.vy=-bullet.speed;
		bullet.sign="Bad";
		bullet.parentInd=index;
		return bullet;
	}
}
