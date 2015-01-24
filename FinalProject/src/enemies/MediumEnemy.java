package enemies;

import projectiles.*;
import def.*;

public class MediumEnemy extends WeakEnemy
{
	int speed = 3;
	int[] hx=new int[]{ 0,0,0 };
	int[] hy=new int[]{ 0,0,0 };
	int HTCA=1;
	int shoot=0;

	public MediumEnemy(int X, int Y, int W, int H)
	{	super(X, Y, W, H);
		moveable=true;
		RAFCollision=true;
		sprInd=14; //sprInd - 14 to 17
		centreOrigin=true;
		w=32; h=32;
		solid=true;
		RHeroLocation=true;
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
			vx=0;
			vy=0;
		}
		
		if (o.equals(Bullet.class) && (g.sign.equals("Hero")||g.sign.equals("Hero2"))){
			RSD=true;
			g.RSD=true;
			if (g.sign.equals("Hero")) message="s1";
			else if (g.sign.equals("Hero2")) message="s2";
		}
	}


	public void move(){
		x+=vx; y+=vy;
		int horiD = Math.abs(x+vx-hx[HTCA]);
		int vertD = Math.abs(y+vy-hy[HTCA]);
			//if the hero is closer vertically (than horizontally)
			if (vertD<horiD) {
				//if the hero is directly above or below
				if (vertD<=8){
					if (x<hx[HTCA]) {
						vx=speed;
						vy=0;
						sprInd=14;
					}
					else {
						vx=-speed;
						vy=0;
						sprInd=16;
					}
					waitToShoot();
				}
				//if the hero is to the left or right somewhere
				else {
					if (y<hy[HTCA]) {
						sprInd=17;
						vx=0;
						vy=speed;
					}
					else {
						sprInd=15;
						vx=0;
						vy=-speed;
					}
				}
			}

			//if the hero is closer horizontally (than vertically)
			else if (horiD<=vertD) {
				//if the hero is directly to the left or right
				if (horiD<=8){
					if (y<hy[HTCA]) {
						vy=speed;
						vx=0;
						sprInd=17;
					}
					else {
						vy=-speed;
						vx=0;
						sprInd=15;
					}
					waitToShoot();
				}
				//if the hero is above or below somewhere
				else {
					if (x<hx[HTCA]) {
						sprInd=14;
						vy=0;
						vx=speed;
					}
					else {
						sprInd=16;
						vy=0;
						vx=-speed;
					}
				}
			}
		}

	public void waitToShoot(){
		if (shoot<5) shoot++;
		else {
			RShoot=true;
			shoot=0;
		}
	}

	@Override
	public void handleHeroLocation( int xValueOfHero1, int yValueOfHero1, int xValueOfHero2, int yValueOfHero2 ){
		hx[1]=xValueOfHero1; hy[1]=yValueOfHero1; hx[2]=xValueOfHero2; hy[2]=yValueOfHero2;
		if (hx[1]==-1) HTCA=2;
		else if (hx[2]==-1) HTCA=1;
	}

	@Override
	public Bullet shoot(){
		RShoot=false;
		Bullet bullet = new Bullet(x,y,0,0);
		if (sprInd==14) bullet.vx=bullet.speed;
		else if (sprInd==15) bullet.vy=-bullet.speed;
		else if (sprInd==16) bullet.vx=-bullet.speed;
		else bullet.vy=bullet.speed;
		bullet.sign="Bad";
		bullet.parentInd=index;
		return bullet;
	}
}