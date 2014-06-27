package base;

public class DoubleCoordinate {
	private double x;
	private double y;
	private double value;
	public DoubleCoordinate(double xa, double ya)
	{
		x=xa;
		y=ya;
		value=0;
	}
	public DoubleCoordinate(double xa, double ya, double val)
	{
		x=xa;
		y=ya;
		value=val;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public double getValue()
	{
		return value;
	}
	public void setXY(double xa, double ya)
	{
		x=xa;
		y=ya;
	}
	public void setX(double xa)
	{
		x=xa;
	}
	public void setY(double ya)
	{
		y=ya;
	}
	public void translateXY(double t)
	{
		x+=t;
		y+=t;
	}
	public void translateXY(double t,double g)
	{
		x+=t;
		y+=g;
	}
	public void scaleXY(double t)
	{
		x=x*t;
		y=y*t;
	}
	public String toString()
	{
		return "("+x+","+y+","+value+")";
	}
}
