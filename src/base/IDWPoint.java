package base;

public class IDWPoint {
	float distance;
	int x;
	int y;
	public IDWPoint(int xc, int yc, float d)
	{
		x=xc;
		y=yc;
		distance=d;
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public float getDistance()
	{
		return this.distance;
	}
}
