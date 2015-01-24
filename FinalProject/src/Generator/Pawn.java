package Generator;
import def.GameObject;
public class Pawn {
	
	public int x,y,h,w;
	public Class<?> c;
	public boolean centreOrigin;
	public int sprInd;
	public Pawn(GameObject g){
		x=g.x;
		y=g.y;
		w=g.w;
		h=g.h;
		c=g.getClass();
		centreOrigin=g.centreOrigin;
		sprInd=g.sprInd;
	}
	public void copyObject(GameObject g){
		x=g.x;
		y=g.y;
		c=g.getClass();
		centreOrigin=g.centreOrigin;
		sprInd=g.sprInd;
	}
}
