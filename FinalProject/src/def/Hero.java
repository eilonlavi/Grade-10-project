package def;

import projectiles.*;

public class Hero extends GameObject{
	int bulletSpeed;
	int life=5;
	//char firstPress=' ';
	//char secondPress=' ';
	//h values:2-5
	//v values:20-23
	
	public Hero(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		moveable=true;
		sprInd=2;
		speed=4;
		centreOrigin=true;
		bulletSpeed=7;
		canShoot=true;
		RAFCollision=true;
		solid=true;
		w=32;
		h=32;
		explode=true;
	}
	public void move(){
		x+=vx;
		y+=vy;
		if (firstPress=='a'){
			if (secondPress=='w'){
				sprInd=3;
			}
			else if (secondPress=='s'){
				sprInd=5;
			}
			else if (secondPress=='d'){
				sprInd=2;
			}
			else{
				sprInd=4;
			}
			vx=-speed;
			vy=0;
		}
		else if (firstPress=='s'){
			if (secondPress=='w'){
				sprInd=21;
			}
			else if (secondPress=='a'){
				sprInd=22;
			}
			else if (secondPress=='d'){
				sprInd=20;
			}
			else{
				sprInd=23;
			}
			vy=speed;
			vx=0;
		}
		else if (firstPress=='w'){
			if (secondPress=='s'){
				sprInd=23;
			}
			else if (secondPress=='a'){
				sprInd=22;
			}
			else if (secondPress=='d'){
				sprInd=20;
			}
			else{
				sprInd=21;
			}
			vy=-speed;
			vx=0;
		}
		else if (firstPress=='d'){
			if (secondPress=='w'){
				sprInd=3;
			}
			else if (secondPress=='s'){
				sprInd=5;
			}
			else if (secondPress=='a'){
				sprInd=4;
			}
			else{
				sprInd=2;
			}
			vx=speed;
			vy=0;
		}
		else {
			vx=0;
			vy=0;
		}
		
	}
	
	
	public Bullet shoot(){
		if (canShoot){
			Bullet bullet=new Bullet(x,y,4,4);
			int temp=sprInd;
			if (temp>10)temp-=18;
			switch(temp){
			case 2:
				bullet.vx=bullet.speed;
				break;
			case 3:
				bullet.vy=-bullet.speed;
				break;
			case 4:
				bullet.vx=-bullet.speed;
				break;
			case 5:
				bullet.vy=bullet.speed;
				break;
			}
			bullet.sign="Hero";
			bullet.parentInd=index;
			canShoot=false;
			return bullet;
		}
		else {
			return null;
		}
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
			vx=0;vy=0;
		}
		if (o.equals(Bullet.class) && g.sign.equals("Bad")){
			if (life>1) life--;
			else {
				RRL=true;
				RSD=true;
			}
			g.RSD=true;
			
		}
		
	}
	

}
