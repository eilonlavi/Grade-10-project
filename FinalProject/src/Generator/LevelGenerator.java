package Generator;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import def.*;
import enemies.*;
import misc.*;


public class LevelGenerator extends Applet implements MouseListener{
	private static final long serialVersionUID = 2L;
	Image[] sprite;
	GameObject[] AllObjects={
			new Hero(0,0,0,0),new Hero2(0,0,0,0),new MediumEnemy(0,0,0,0),new MediumEnemy2(0,0,0,0),new WeakEnemy(0,0,0,0),new WeakEnemyFlip(0,0,0,0),
			new Block(0,0,0,0),new Pit(0,0,0,0)
	};
	Pawn[] gameObjects;
	int cObject=0;
	int objLen=0;
	Image bg;
	
	public void destroy(){
		String out="";
		for (int i=1;i<gameObjects.length && gameObjects[i]!=null;i++){
			out+="addObject(new ";
			out+=getClassName(gameObjects[i].c);
			out+="(";
			if (gameObjects[i].centreOrigin){
				out+=gameObjects[i].x+gameObjects[i].w/2-32;
			}
			else out+=gameObjects[i].x-32;
			out+=",";
			if (gameObjects[i].centreOrigin){
				out+=gameObjects[i].y+gameObjects[i].h/2;
			}
			else out+=gameObjects[i].y;
			out+=",0,0));";
			out+="\n";
			
		}
		System.out.println(out);
	}
	
	public void init(){
		addMouseListener(this);
		for (objLen=0;objLen<AllObjects.length && AllObjects[objLen]!=null;objLen++){}
		gameObjects=new Pawn[800];
		sprite=new Image[50];
		bg=getImage(getCodeBase(),"grass.gif");
		sprite[0]=getImage(getCodeBase(),"startbutton.gif");
		sprite[1]=getImage(getCodeBase(),"helpbutton.gif");
		sprite[2]=getImage(getCodeBase(),"GTank1_h0.gif");
		sprite[3]=getImage(getCodeBase(),"GTank1_h90.gif");
		sprite[4]=getImage(getCodeBase(),"GTank1_h180.gif");
		sprite[5]=getImage(getCodeBase(),"GTank1_h270.gif");
		sprite[6]=getImage(getCodeBase(),"bullet.gif");
		sprite[7]=getImage(getCodeBase(),"block1.gif");
		sprite[8]=getImage(getCodeBase(),"ETank1_270.gif");
		sprite[9]=getImage(getCodeBase(),"ETank1_90.gif");
		sprite[10]=getImage(getCodeBase(),"GTank2_h0.gif");
		sprite[11]=getImage(getCodeBase(),"GTank2_h90.gif");
		sprite[12]=getImage(getCodeBase(),"GTank2_h180.gif");
		sprite[13]=getImage(getCodeBase(),"GTank2_h270.gif");
		sprite[14]=getImage(getCodeBase(),"Etank3_0.gif");
		sprite[15]=getImage(getCodeBase(),"Etank3_90.gif");
		sprite[16]=getImage(getCodeBase(),"Etank3_180.gif");
		sprite[17]=getImage(getCodeBase(),"Etank3_270.gif");
		sprite[18]=getImage(getCodeBase(),"yesplayer1.gif");
		sprite[19]=getImage(getCodeBase(),"yesplayer2.gif");
		sprite[20]=getImage(getCodeBase(),"GTank1_v0.gif");
		sprite[21]=getImage(getCodeBase(),"GTank1_v90.gif");
		sprite[22]=getImage(getCodeBase(),"GTank1_v180.gif");
		sprite[23]=getImage(getCodeBase(),"GTank1_v270.gif");
		sprite[24]=getImage(getCodeBase(),"GTank2_v0.gif");
		sprite[25]=getImage(getCodeBase(),"GTank2_v90.gif");
		sprite[26]=getImage(getCodeBase(),"GTank2_v180.gif");
		sprite[27]=getImage(getCodeBase(),"GTank2_v270.gif");
		sprite[28]=getImage(getCodeBase(),"pit.gif");
		
		
		gameObjects[0]=new Pawn(AllObjects[cObject]);
		resize(new Dimension(640,480));
		
		for (int x=32;x+32<=getWidth();x+=32){
			addObject(new Block(x,0,32,32));
			addObject(new Block(x,getHeight()-32,32,32));
		}
		for (int y=32;y+32<=getHeight();y+=32){
			addObject(new Block(32,y,32,32));
			addObject(new Block(getWidth()-32,y,32,32));
		}
		
	}
	public void paint(Graphics g){
		g.drawImage(bg,32,0,this);
		for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
			g.drawImage(sprite[gameObjects[i].sprInd], gameObjects[i].x,gameObjects[i].y,this);
		}
	}
	public String getClassName(Class<?> c){
		String temp1=c.toString();
		String temp2="";
		String temp3="";
		for (int i=temp1.length()-1;temp1.charAt(i)!='.' && temp1.charAt(i)!=' ';i--)temp2+=temp1.charAt(i);
		for (int i=temp2.length()-1;i>=0;i--)temp3+=temp2.charAt(i);
		return temp3;
	}
	public void increaseIndex(){
		if (cObject+1<objLen){
			cObject++;
		}
		else{
			cObject=0;
		}
	}
	public void decreaseIndex(){
		if (cObject>0){
			cObject--;
		}
		else{
			cObject=objLen-1;
		}
	}
	public void addObject(GameObject g){
		for (int i=0;i<gameObjects.length;i++){
			if (gameObjects[i]==null){
				gameObjects[i]=new Pawn(g);
				repaint();
				break;
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		if (e.getButton()==1){
			if (e.getX()<32 && e.getY()<=getHeight()/2){
				increaseIndex();
				gameObjects[0].copyObject(AllObjects[cObject]);
				repaint();
			}
			else if (e.getX()<32 && e.getY()>getHeight()/2){
				decreaseIndex();
				gameObjects[0].copyObject(AllObjects[cObject]);
				repaint();
			}
			else {
				for (int i=0;i<gameObjects.length;i++){
					if (gameObjects[i]==null){
						gameObjects[i]=new Pawn(AllObjects[cObject]);
						gameObjects[i].x=e.getX();
						gameObjects[i].y=e.getY();
						gameObjects[i].x-=gameObjects[i].x%32;
						gameObjects[i].y-=gameObjects[i].y%32;
						repaint();
						break;
					}
				}
			}
		}
		else {
			int x=e.getX();
			x-=x%32;
			int y=e.getY();
			y-=y%32;
			for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].x==x && gameObjects[i].y==y){
					for (int n=0;n<gameObjects.length;n++){
						if (gameObjects[n]==null){
							gameObjects[i]=gameObjects[n-1];
							gameObjects[n-1]=null;
							repaint();
							break;
						}
					}
				}
			}
		}
		
	}


	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
