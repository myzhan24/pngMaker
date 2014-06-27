package base;

public class Coordinate {
	private int x;
	private int y;
	public Coordinate(int xa, int ya)
	{
		x=xa;
		y=ya;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setXY(int xa, int ya)
	{
		x=xa;
		y=ya;
	}
	public void setX(int xa)
	{
		x=xa;
	}
	public void setY(int ya)
	{
		y=ya;
	}
	public String toString()
	{
		return "("+x+","+y+")";
	}
}
