package def;

import projectiles.Bullet;

public class Hero2 extends Hero{

	public Hero2(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		sprInd=10;
	}
	//sprite 10-13, 24-27.

	public void move(){
		x+=vx;
		y+=vy;
		if (firstPress=='a'){
			if (secondPress=='w'){
				sprInd=11;
			}
			else if (secondPress=='s'){
				sprInd=13;
			}
			else if (secondPress=='d'){
				sprInd=10;
			}
			else{
				sprInd=12;
			}
			vx=-speed;
			vy=0;
		}
		else if (firstPress=='s'){
			if (secondPress=='w'){
				sprInd=25;
			}
			else if (secondPress=='a'){
				sprInd=26;
			}
			else if (secondPress=='d'){
				sprInd=24;
			}
			else{
				sprInd=27;
			}
			vy=speed;
			vx=0;
		}
		else if (firstPress=='w'){
			if (secondPress=='s'){
				sprInd=27;
			}
			else if (secondPress=='a'){
				sprInd=26;
			}
			else if (secondPress=='d'){
				sprInd=24;
			}
			else{
				sprInd=25;
			}
			vy=-speed;
			vx=0;
		}
		else if (firstPress=='d'){
			if (secondPress=='w'){
				sprInd=11;
			}
			else if (secondPress=='s'){
				sprInd=13;
			}
			else if (secondPress=='a'){
				sprInd=12;
			}
			else{
				sprInd=10;
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
			if (temp>20)temp-=14;
			switch(temp){
			case 10:
				bullet.vx=bullet.speed;
				break;
			case 11:
				bullet.vy=-bullet.speed;
				break;
			case 12:
				bullet.vx=-bullet.speed;
				break;
			case 13:
				bullet.vy=bullet.speed;
				break;
			}
			bullet.sign="Hero2";
			bullet.parentInd=index;
			canShoot=false;
			return bullet;
		}
		else {
			return null;
		}
	}
}
