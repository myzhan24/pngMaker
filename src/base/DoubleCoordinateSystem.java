package base;

import java.util.ArrayList;

import ucar.ma2.ArrayDouble;

public class DoubleCoordinateSystem {
	ArrayList<DoubleCoordinate> myCS;
	double numRow;
	double numCol;
	
	public DoubleCoordinateSystem(double r, double c)
	{
		myCS = new ArrayList<DoubleCoordinate>();
		numRow=r;
		numCol=c;
	}
	public void addCoor(double a, double b, double val)
	{
		myCS.add(new DoubleCoordinate(a,b,val));
	}
	public void addIntCoordinate(Coordinate c)
	{
		myCS.add(new DoubleCoordinate(c.getX(),c.getY()));
	}
	
	public boolean contains(DoubleCoordinate fc)
	{
		if(myCS.contains(fc))
			return true;
		
		else
			return false;
	}
	
	public void addNeighbors(double r, double c, ArrayDouble.D2 ar)
	{
		addPoint(r-1,c-1,ar);
		addPoint(r-1,c,ar);
		addPoint(r-1,c+1,ar);
		addPoint(r,c-1,ar);
		addPoint(r,c+1,ar);
		addPoint(r+1,c-1,ar);
		addPoint(r+1,c,ar);
		addPoint(r+1,c+1,ar);
	}
	public void addGrid(double r, double c, ArrayDouble.D2 ar)
	{
		addPoint(r-1,c-1,ar);
		addPoint(r-1,c,ar);
		addPoint(r-1,c+1,ar);
		addPoint(r,c-1,ar);
		addPoint(r,c+1,ar);
		addPoint(r,c,ar);
		addPoint(r+1,c-1,ar);
		addPoint(r+1,c,ar);
		addPoint(r+1,c+1,ar);
	}
	public void expand(double scale)
	{
		for(int i=0;i<myCS.size();i++)
		{
			myCS.get(i).scaleXY(scale);
			myCS.get(i).translateXY(scale/2.0);
		}
	}
	public void scale(double scale)
	{
		for(int i=0;i<myCS.size();i++)
		{
			myCS.get(i).scaleXY(scale);
		}
	}
	public void expandRandom(double scale)
	{
		double randRow;
		double randCol;
		double size=scale/3.0;
		for(int i=0;i<myCS.size();i++)
		{
			randRow=(Math.random()*size + size);
			randCol=(Math.random()*size + size);
			myCS.get(i).scaleXY(scale);
			myCS.get(i).translateXY(randRow,randCol);
		}
	}
	public boolean addPoint(double r, double c, ArrayDouble.D2 ar)
	{
		if(r < 0)
		{
			return false;
		}
		else if(r >= numRow)
		{
			return false;
		}
		else if(c < 0)
		{
			return false;
		}
		else if(c >= numCol)
		{
			return false;
		}
		myCS.add(new DoubleCoordinate(r,c,ar.get((int)r, (int)c)));
		return true;
	}
	public void remove(int i)
	{
		myCS.remove(i);
	}
	public DoubleCoordinate get(int i)
	{
		return myCS.get(i);
	}
	public int size()
	{
		return myCS.size();
	}
}
