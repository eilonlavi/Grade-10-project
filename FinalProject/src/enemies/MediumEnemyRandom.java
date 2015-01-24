package enemies;

public class MediumEnemyRandom extends MediumEnemy{

	public MediumEnemyRandom(int X, int Y, int W, int H) {
		super(X, Y, W, H);
		HTCA=(int)(Math.random()*2+1);
	}
}