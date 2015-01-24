package def;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.Scanner;





import menu.*;
import misc.*;
import enemies.*;
import projectiles.*;


public class MAIN  extends Applet implements Runnable , MouseListener, KeyListener, MouseMotionListener {
	private static final long serialVersionUID = 1L; //<----I really don't know. Eclipse recommends it. I like to keep my programs error-free.
	int r=0;
	int p1Score=0;
	int p2Score=0;
	int lastLevel=6;
	Image sprite[];
	Image bg;
	Thread th;
	int clevel=0;
	boolean run=true;
	//this is the array with all objects in the game
	GameObject gameObjects[];
	private Image dbImage; 
	private Graphics dbg; 
	int amountOfObjects=0;
	boolean pressSpace=false;
	boolean pressEnter=false;
	int GTanks=2;
	int BTanks=0;
	boolean multip=false;
	Scanner in;
	PrintWriter out;
	
	public void init(){
		
		//here are things such as pictures that need to be loaded
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
		gameObjects=new GameObject[500];
		bg=getImage(getCodeBase(), "grass.gif");
		sprite=new Image[50];
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
		sprite[29]=getImage(getCodeBase(),"helptxt.gif");
		sprite[30]=getImage(getCodeBase(),"explosion.gif");
	}
	
	public void start(){
		resize(new Dimension(608,480));
		clevel=0;
		gotoLevel(clevel);
		th=new Thread(this);
		th.start();
	}

	public void stop(){
	}

	public void destroy(){
	}
	
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); 
		while (run){
			repaint();
			try {Thread.sleep(20);} catch (InterruptedException omganexeption){}
			//handle all object requests
			for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
				//handle a collision request
				if (gameObjects[i].RAFCollision){
					for (int m=0;m<gameObjects.length && gameObjects[m]!=null;m++){
						if (m!=i){
							gameObjects[i].handleCollision(gameObjects[m]);
						}
					}
				}
				//handle requests to increase scores
				if (gameObjects[i].message.equals("s1")){
					p1Score++;
					gameObjects[i].message="";
				}
				if (gameObjects[i].message.equals("s2")){
					p2Score++;
					gameObjects[i].message="";
				}
				//handle requests to restart level
				if (gameObjects[i].RRL){
						if (GTanks==2) {
							GTanks--;
						}
						else {
							p1Score=0;
							p2Score=0;
							gotoLevel(clevel);
						}
				}
				//handle requests of self destruction
				if (gameObjects[i].RSD){
					boolean temp=(gameObjects[i].enemy);
					if (gameObjects[i].explode){
						addObject(new Explosion(gameObjects[i].x,gameObjects[i].y,0,0));
						sprite[30]=getImage(getCodeBase(), "explosion.gif");
					}
					removeObject(i);
					i--;
					if (temp && BTanks==0) {
						clevel++; gotoLevel(clevel);
					}
				}
				else{
					if (gameObjects[i].RShoot){
						addObject(gameObjects[i].shoot());
					}
					
					if (gameObjects[i].moveable){
						gameObjects[i].move();
					}
					//handle requests for the heros' locations
					if (gameObjects[i].RHeroLocation){
						int[] hx = new int[3], hy = new int[3];
						hx[1]=-1; hy[1]=-1; hx[2]=-1; hy[2]=-1;
						for (int n=0; n<gameObjects.length && gameObjects[n]!=null; n++){
							if (gameObjects[n].getClass().equals(Hero.class)){
								hx[1]=gameObjects[n].x;
								hy[1]=gameObjects[n].y;
							}
							else if (gameObjects[n].getClass().equals(Hero2.class)){
								hx[2]=gameObjects[n].x;
								hy[2]=gameObjects[n].y;
								break;
							}
						}
						gameObjects[i].handleHeroLocation(hx[1], hy[1], hx[2], hy[2]);
					}
				}
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY); 
		}
	}
	public void paint(Graphics g){
		
		/*g.setColor(Color.GREEN);
		for ( int i = 0; i < 10; ++i ) {
	         g.drawLine( 600, 400, i * 600 / 10, 0 );
	      }*/
		
		//draw the background
	    g.drawImage(bg,0,0,this);
		//draw the help image if in the help "level"
		if (clevel==-1) g.drawImage(sprite[29],getWidth()/2-504/2,getHeight()/2-360/2,this);
		//if in the game, draw the scores of the players next to the players
		if (clevel>0){ 
			for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					g.drawString(p1Score+"",gameObjects[i].x+17,gameObjects[i].y+4);
					break;
				}
			}
			if (multip){
				for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
					if (gameObjects[i].getClass().equals(Hero2.class)){
						g.drawString(p2Score+"",gameObjects[i].x+17,gameObjects[i].y+4);
						break;
					}
				}
			}
		}
		//draw all objects in gameobjects on the screen
		for (int i=amountOfObjects-1;i>=0;i--){
			if (gameObjects[i]!=null) {
				//make the image centred in (x,y) if centreorigin is true
				if (gameObjects[i].centreOrigin)
					g.drawImage(sprite[gameObjects[i].sprInd],gameObjects[i].x-gameObjects[i].w/2,gameObjects[i].y-gameObjects[i].h/2,this);
				//otherwise make the top left of the image in x,y
				else
					g.drawImage(sprite[gameObjects[i].sprInd],gameObjects[i].x,gameObjects[i].y,this);
			}
			else break;
		}
	}

	public void addObject(GameObject go){
		//add an object at the first free space
		if (go!=null)
		if (multip || !go.getClass().equals(Hero2.class))
		for (int i=0;i<gameObjects.length;i++){
			if (gameObjects[i]==null){
				gameObjects[i]=go;
				amountOfObjects++;
				gameObjects[i].index=i;
				if (gameObjects[i].enemy) BTanks++;
				return;
			}
		}
	}

	public void removeObject(int i){
		//  put the last non-null object in the array in the place of the deleted object
		if (gameObjects[i].enemy) BTanks--;
		for (int n=0;n<gameObjects.length;n++){
			if (gameObjects[n]==null){
				gameObjects[n-1].index=i;
				gameObjects[i]=gameObjects[n-1];
				gameObjects[n-1]=null;
				for (int x=0;x<gameObjects.length && gameObjects[x]!=null;x++){
					if (gameObjects[x].getClass().equals(Bullet.class)){
						Bullet b=(Bullet)gameObjects[x];
						if (b.parentInd==n-1) b.parentInd=i;
						gameObjects[x]=b;
					}
				}
				amountOfObjects--;
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		//if in help level, go to menu
		if (clevel==-1){
			clevel=0;
			gotoLevel(clevel);
		}
		//handle clicks on buttons
		for (int i=0;i<gameObjects.length;i++){
			if (gameObjects[i]!=null){
				Class<?> c=gameObjects[i].getClass();
				if (gameObjects[i].isPointerInside(e)){
					if (c.equals(StartButton.class)){
						clevel++;
						gotoLevel(clevel);
					}
					if (c.equals(HelpButton.class)){
						clevel=-1;
						gotoLevel(clevel);
					}
					if (c.equals(OnePlayerButton.class)){
						multip=false;
						clevel++;
						gotoLevel(clevel);
					}
					if (c.equals(TwoPlayerButton.class)){
						multip=true;
						clevel++;
						gotoLevel(clevel);
					}
				}
			}
		}
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	public void update (Graphics g) 
	{ // initialize buffer
      if (dbImage == null) 
	  {	dbImage = createImage (this.getSize().width, this.getSize().height); 
        dbg = dbImage.getGraphics (); 
      } 
      // clear screen in background 
      dbg.setColor (getBackground ()); 
      dbg.fillRect (0, 0, this.getSize().width, this.getSize().height); 

      // draw elements in background 
      dbg.setColor (getForeground()); 
      paint (dbg); 

      // draw image on the screen 
      g.drawImage (dbImage, 0, 0, this); 
    }


	public void keyPressed(KeyEvent e) {
		
		//cheat
		if(e.getKeyCode()==KeyEvent.VK_0){
			clevel++; gotoLevel(clevel);
		}
		
		//tells heros which keys are pressed (using variables)
		if(e.getKeyCode()==KeyEvent.VK_A){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					if (gameObjects[i].firstPress!='a'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='a';
						else
							gameObjects[i].secondPress='a';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_S){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					if (gameObjects[i].firstPress!='s'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='s';
						else
							gameObjects[i].secondPress='s';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_D){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					if (gameObjects[i].firstPress!='d'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='d';
						else
							gameObjects[i].secondPress='d';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_W){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					if (gameObjects[i].firstPress!='w'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='w';
						else
							gameObjects[i].secondPress='w';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_SPACE){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero.class)){
					pressSpace=true;
					addObject(gameObjects[i].shoot());
					return;
				}
			}
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero2.class)){
					if (gameObjects[i].firstPress!='a'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='a';
						else
							gameObjects[i].secondPress='a';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_DOWN){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero2.class)){
					if (gameObjects[i].firstPress!='s'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='s';
						else
							gameObjects[i].secondPress='s';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero2.class)){
					if (gameObjects[i].firstPress!='d'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='d';
						else
							gameObjects[i].secondPress='d';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_UP){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero2.class)){
					if (gameObjects[i].firstPress!='w'){
						if (gameObjects[i].firstPress==' ')
							gameObjects[i].firstPress='w';
						else
							gameObjects[i].secondPress='w';
					}
					return;
				}
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
				if (gameObjects[i].getClass().equals(Hero2.class)){
					pressEnter=true;
					addObject(gameObjects[i].shoot());
					return;
				}
			}
		}
	}


	public void keyReleased(KeyEvent arg0) {
		for (int i=0;i<=gameObjects.length && gameObjects[i]!=null;i++){
			if (gameObjects[i].getClass().equals(Hero.class)){
				//tells heros which keys are released (using variables)
				if (arg0.getKeyCode()==KeyEvent.VK_A){
					if (gameObjects[i].firstPress=='a'){
						if (gameObjects[i].secondPress=='a'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='a'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_D){
					if (gameObjects[i].firstPress=='d'){
						if (gameObjects[i].secondPress=='d'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='d'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_W){
					if (gameObjects[i].firstPress=='w'){
						if (gameObjects[i].secondPress=='w'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='w'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if(arg0.getKeyCode()==KeyEvent.VK_S){
					if (gameObjects[i].firstPress=='s'){
						if (gameObjects[i].secondPress=='s'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='s'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_SPACE &&pressSpace){
					pressSpace=false;
					gameObjects[i].canShoot=true;
				}
			}
			else if (gameObjects[i].getClass().equals(Hero2.class)){
				if (arg0.getKeyCode()==KeyEvent.VK_LEFT){
					if (gameObjects[i].firstPress=='a'){
						if (gameObjects[i].secondPress=='a'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='a'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_RIGHT){
					if (gameObjects[i].firstPress=='d'){
						if (gameObjects[i].secondPress=='d'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='d'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_UP){
					if (gameObjects[i].firstPress=='w'){
						if (gameObjects[i].secondPress=='w'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='w'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
					if (gameObjects[i].firstPress=='s'){
						if (gameObjects[i].secondPress=='s'){
							gameObjects[i].firstPress=' ';
							gameObjects[i].secondPress=' ';
						}
						else{
							gameObjects[i].firstPress=gameObjects[i].secondPress;
							gameObjects[i].secondPress=' ';
						}
					}
					else if (gameObjects[i].secondPress=='s'){
						gameObjects[i].secondPress=' ';
					}
				}
				else if (arg0.getKeyCode()==KeyEvent.VK_ENTER &&pressEnter){
					pressEnter=false;
					gameObjects[i].canShoot=true;
				}
				return;
			}
		}
	}


	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}


	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	public void mouseMoved(MouseEvent e) {
	}

	
	public void gotoLevel(int lev){
		//erase all objects
		for (int i=0;i<gameObjects.length && gameObjects[i]!=null;i++){
			gameObjects[i]=null;
		}
		BTanks=0;
		if (multip)
			GTanks=2;
		else GTanks=1;
		amountOfObjects=0;
		if (lev==0){
			addObject(new OnePlayerButton(getWidth()/2,100,0,0));
			addObject(new TwoPlayerButton(getWidth()/2,200,0,0));
			addObject(new HelpButton((getWidth()-122)/2,300,122,42));
		}
		else if (lev==1){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new WeakEnemy(268,85,0,0));
			addObject(new WeakEnemy(108,85,0,0));
			addObject(new WeakEnemy(460,85,0,0));
			addObject(new Block(48,144,0,0));
			addObject(new Block(80,144,0,0));
			addObject(new Block(112,144,0,0));
			addObject(new Block(144,144,0,0));
			addObject(new Block(560,144,0,0));
			addObject(new Block(528,144,0,0));
			addObject(new Block(496,144,0,0));
			addObject(new Block(464,144,0,0));
			addObject(new Block(368,144,0,0));
			addObject(new Block(240,144,0,0));
			addObject(new Pit(272,144,0,0));
			addObject(new Pit(304,144,0,0));
			addObject(new Pit(336,144,0,0));
			addObject(new Pit(208,144,0,0));
			addObject(new Pit(176,144,0,0));
			addObject(new Pit(400,144,0,0));
			addObject(new Pit(432,144,0,0));
			addObject(new Hero(80,368,0,0));
			addObject(new Hero2(528,368,0,0));
		}
		else if (lev==2){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new WeakEnemy(140,85,0,0));
			addObject(new WeakEnemy(460,85,0,0));
			addObject(new WeakEnemyFlip(140,400,0,0));
			addObject(new WeakEnemyFlip(460,400,0,0));
			addObject(new Block(400,176,0,0));
			addObject(new Block(400,304,0,0));
			addObject(new Block(176,176,0,0));
			addObject(new Block(208,176,0,0));
			addObject(new Block(176,304,0,0));
			addObject(new Block(144,304,0,0));
			addObject(new Block(208,304,0,0));
			addObject(new Block(144,176,0,0));
			addObject(new Block(336,304,0,0));
			addObject(new Block(368,304,0,0));
			addObject(new Block(336,176,0,0));
			addObject(new Block(368,176,0,0));
			addObject(new Hero(176,240,0,0));
			addObject(new Hero2(368,240,0,0));
		}
		else if (lev==3){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Hero(80,400,0,0));
			addObject(new Hero2(528,400,0,0));
			addObject(new WeakEnemy(460,85,0,0));
			addObject(new MediumEnemy(272,272,0,0));
			addObject(new MediumEnemy2(336,272,0,0));
			addObject(new WeakEnemy(524,85,0,0));
			addObject(new Block(112,336,0,0));
			addObject(new Block(48,336,0,0));
			addObject(new Block(80,336,0,0));
			addObject(new Block(560,336,0,0));
			addObject(new Block(528,336,0,0));
			addObject(new Block(496,336,0,0));
			addObject(new WeakEnemy(396,85,0,0));
			addObject(new WeakEnemy(76,85,0,0));
			addObject(new WeakEnemy(140,85,0,0));
			addObject(new WeakEnemy(204,85,0,0));
			addObject(new Pit(48,176,0,0));
			addObject(new Pit(80,176,0,0));
			addObject(new Pit(112,176,0,0));
			addObject(new Pit(144,176,0,0));
			addObject(new Pit(176,176,0,0));
			addObject(new Pit(208,176,0,0));
			addObject(new Pit(240,176,0,0));
			addObject(new Pit(272,176,0,0));
			addObject(new Pit(304,176,0,0));
			addObject(new Pit(336,176,0,0));
			addObject(new Pit(368,176,0,0));
			addObject(new Pit(400,176,0,0));
			addObject(new Pit(432,176,0,0));
			addObject(new Pit(464,176,0,0));
			addObject(new Pit(496,176,0,0));
			addObject(new Pit(528,176,0,0));
			addObject(new Pit(560,176,0,0));
		}
		else if (lev==4){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new MediumEnemy(496,112,0,0));
			addObject(new MediumEnemy2(112,112,0,0));
			addObject(new MediumEnemy2(176,112,0,0));
			addObject(new MediumEnemy2(208,176,0,0));
			addObject(new MediumEnemy2(400,304,0,0));
			addObject(new MediumEnemy2(208,336,0,0));
			addObject(new MediumEnemy2(336,176,0,0));
			addObject(new Hero(336,80,0,0));
			addObject(new Hero2(80,336,0,0));
		}
		else if (lev==5){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new MediumEnemy(240,80,0,0));
			addObject(new MediumEnemy(240,144,0,0));
			addObject(new MediumEnemy(240,208,0,0));
			addObject(new MediumEnemy(240,272,0,0));
			addObject(new MediumEnemy2(368,80,0,0));
			addObject(new MediumEnemy2(368,144,0,0));
			addObject(new MediumEnemy2(368,208,0,0));
			addObject(new MediumEnemy2(368,272,0,0));
			addObject(new Block(144,112,0,0));
			addObject(new Block(144,144,0,0));
			addObject(new Block(144,176,0,0));
			addObject(new Block(144,208,0,0));
			addObject(new Block(144,240,0,0));
			addObject(new Block(144,272,0,0));
			addObject(new Block(144,304,0,0));
			addObject(new Block(144,336,0,0));
			addObject(new Block(144,368,0,0));
			addObject(new Block(464,112,0,0));
			addObject(new Block(464,144,0,0));
			addObject(new Block(464,176,0,0));
			addObject(new Block(464,208,0,0));
			addObject(new Block(464,240,0,0));
			addObject(new Block(464,272,0,0));
			addObject(new Block(464,304,0,0));
			addObject(new Block(464,336,0,0));
			addObject(new Block(464,368,0,0));
			addObject(new MediumEnemy(240,336,0,0));
			addObject(new MediumEnemy(240,400,0,0));
			addObject(new MediumEnemy2(368,336,0,0));
			addObject(new MediumEnemy2(368,400,0,0));
			addObject(new Hero(80,240,0,0));
			addObject(new Hero2(528,240,0,0));
		}
		else if (lev==6){
			addObject(new Block(16,16,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(48,16,0,0));
			addObject(new Block(48,464,0,0));
			addObject(new Block(80,16,0,0));
			addObject(new Block(80,464,0,0));
			addObject(new Block(112,16,0,0));
			addObject(new Block(112,464,0,0));
			addObject(new Block(144,16,0,0));
			addObject(new Block(144,464,0,0));
			addObject(new Block(176,16,0,0));
			addObject(new Block(176,464,0,0));
			addObject(new Block(208,16,0,0));
			addObject(new Block(208,464,0,0));
			addObject(new Block(240,16,0,0));
			addObject(new Block(240,464,0,0));
			addObject(new Block(272,16,0,0));
			addObject(new Block(272,464,0,0));
			addObject(new Block(304,16,0,0));
			addObject(new Block(304,464,0,0));
			addObject(new Block(336,16,0,0));
			addObject(new Block(336,464,0,0));
			addObject(new Block(368,16,0,0));
			addObject(new Block(368,464,0,0));
			addObject(new Block(400,16,0,0));
			addObject(new Block(400,464,0,0));
			addObject(new Block(432,16,0,0));
			addObject(new Block(432,464,0,0));
			addObject(new Block(464,16,0,0));
			addObject(new Block(464,464,0,0));
			addObject(new Block(496,16,0,0));
			addObject(new Block(496,464,0,0));
			addObject(new Block(528,16,0,0));
			addObject(new Block(528,464,0,0));
			addObject(new Block(560,16,0,0));
			addObject(new Block(560,464,0,0));
			addObject(new Block(592,16,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Block(16,48,0,0));
			addObject(new Block(592,48,0,0));
			addObject(new Block(16,80,0,0));
			addObject(new Block(592,80,0,0));
			addObject(new Block(16,112,0,0));
			addObject(new Block(592,112,0,0));
			addObject(new Block(16,144,0,0));
			addObject(new Block(592,144,0,0));
			addObject(new Block(16,176,0,0));
			addObject(new Block(592,176,0,0));
			addObject(new Block(16,208,0,0));
			addObject(new Block(592,208,0,0));
			addObject(new Block(16,240,0,0));
			addObject(new Block(592,240,0,0));
			addObject(new Block(16,272,0,0));
			addObject(new Block(592,272,0,0));
			addObject(new Block(16,304,0,0));
			addObject(new Block(592,304,0,0));
			addObject(new Block(16,336,0,0));
			addObject(new Block(592,336,0,0));
			addObject(new Block(16,368,0,0));
			addObject(new Block(592,368,0,0));
			addObject(new Block(16,400,0,0));
			addObject(new Block(592,400,0,0));
			addObject(new Block(16,432,0,0));
			addObject(new Block(592,432,0,0));
			addObject(new Block(16,464,0,0));
			addObject(new Block(592,464,0,0));
			addObject(new Pit(272,144,0,0));
			addObject(new Hero(144,240,0,0));
			addObject(new WeakEnemy(76,85,0,0));
			addObject(new WeakEnemy(204,85,0,0));
			addObject(new WeakEnemy(140,85,0,0));
			addObject(new WeakEnemy(268,85,0,0));
			addObject(new WeakEnemy(332,85,0,0));
			addObject(new WeakEnemy(396,85,0,0));
			addObject(new WeakEnemy(460,85,0,0));
			addObject(new WeakEnemy(524,85,0,0));
			addObject(new WeakEnemyFlip(524,405,0,0));
			addObject(new WeakEnemyFlip(460,405,0,0));
			addObject(new WeakEnemyFlip(396,405,0,0));
			addObject(new WeakEnemyFlip(332,405,0,0));
			addObject(new WeakEnemyFlip(268,405,0,0));
			addObject(new WeakEnemyFlip(204,405,0,0));
			addObject(new WeakEnemyFlip(140,405,0,0));
			addObject(new WeakEnemyFlip(76,405,0,0));
			addObject(new Pit(48,336,0,0));
			addObject(new Pit(80,336,0,0));
			addObject(new Pit(112,336,0,0));
			addObject(new Pit(144,336,0,0));
			addObject(new Pit(176,336,0,0));
			addObject(new Pit(208,336,0,0));
			addObject(new Pit(240,336,0,0));
			addObject(new Pit(272,336,0,0));
			addObject(new Pit(304,336,0,0));
			addObject(new Pit(336,336,0,0));
			addObject(new Pit(368,336,0,0));
			addObject(new Pit(400,336,0,0));
			addObject(new Pit(432,336,0,0));
			addObject(new Pit(464,336,0,0));
			addObject(new Pit(496,336,0,0));
			addObject(new Pit(528,336,0,0));
			addObject(new Pit(560,336,0,0));
			addObject(new Pit(560,144,0,0));
			addObject(new Pit(528,144,0,0));
			addObject(new Pit(496,144,0,0));
			addObject(new Pit(464,144,0,0));
			addObject(new Pit(432,144,0,0));
			addObject(new Pit(400,144,0,0));
			addObject(new Pit(368,144,0,0));
			addObject(new Pit(336,144,0,0));
			addObject(new Pit(304,144,0,0));
			addObject(new Pit(240,144,0,0));
			addObject(new Pit(208,144,0,0));
			addObject(new Pit(176,144,0,0));
			addObject(new Pit(112,144,0,0));
			addObject(new Pit(144,144,0,0));
			addObject(new Pit(80,144,0,0));
			addObject(new Pit(48,144,0,0));
			addObject(new Block(464,176,0,0));
			addObject(new Block(496,176,0,0));
			addObject(new Block(112,176,0,0));
			addObject(new Block(112,304,0,0));
			addObject(new Block(464,304,0,0));
			addObject(new Block(496,304,0,0));
			addObject(new Block(432,304,0,0));
			addObject(new Block(432,176,0,0));
			addObject(new Block(144,304,0,0));
			addObject(new Block(176,304,0,0));
			addObject(new Block(144,176,0,0));
			addObject(new Block(176,176,0,0));
			addObject(new Hero2(464,240,0,0));
		}
		if (lev==lastLevel+1){
			try{
				in=new Scanner(new File("high.txt"));
			}
			catch(Exception FileNotFoundException){System.out.println("There was an error.");}

			String[] names=new String[5];
			int[] Scores=new int[5];
			for (int i=0;i<5;i++){
				names[i]=in.next();
				Scores[i]=in.nextInt();
			}
			in.close();
			String name="<unknown>";
			System.out.println("Current High Scores:");
			for (int i=0;i<5;i++){
				System.out.println(names[i]+" "+Scores[i]);
			}
			int score=0;
			if (p1Score>Scores[4] || p2Score>Scores[4]){
				if (p2Score>p1Score){
					Scanner scan=new Scanner(System.in);
					System.out.println("\nPlayer 2, enter your name!");
					name=scan.next();
					score=p2Score;
					scan.close();
				}
				else {
					Scanner scan=new Scanner(System.in);
					System.out.println("\nPlayer 1, enter your name!");
					name=scan.next();
					score=p1Score;
					scan.close();
				}
				System.out.println("You got a high score of: "+score);
				
				if(score>Scores[0]){
					Scores[4]=Scores[3];
					Scores[3]=Scores[2];
					Scores[2]=Scores[1];
					Scores[1]=Scores[0];
					Scores[0]=score;

					names[4]=names[3];
					names[3]=names[2];
					names[2]=names[1];
					names[1]=names[0];
					names[0]=name;
				}
				else if (score>Scores[1]){
					Scores[4]=Scores[3];
					Scores[3]=Scores[2];
					Scores[2]=Scores[1];
					Scores[1]=score;

					names[4]=names[3];
					names[3]=names[2];
					names[2]=names[1];
					names[1]=name;
				}
				else if (score>Scores[2]){
					Scores[4]=Scores[3];
					Scores[3]=Scores[2];
					Scores[2]=score;

					names[4]=names[3];
					names[3]=names[2];
					names[2]=name;
				}
				else if (score>Scores[3]){
					Scores[4]=Scores[3];
					Scores[3]=score;

					names[4]=names[3];
					names[3]=name;
				}
				else if (score>Scores[4]){
					Scores[4]=score;
					names[4]=name;
				}

				System.out.println("\n New High Score Table:");
				for (int i=0;i<5;i++){
					System.out.println(names[i]+" "+Scores[i]);
				}
				
				try{
					out=new PrintWriter(new FileWriter(new File("high.txt")));
				}
				catch(Exception FileNotFoundException){System.out.println("There was an error.");}
				for (int i=0;i<5;i++){
					out.println(names[i]+" "+Scores[i]);
				}
				out.close();
				System.out.println("Your score has been saved.");
			}
		}
	}
}
