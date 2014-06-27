package base;

import java.util.ArrayList;

public class CoordinateSystem {
	ArrayList<Coordinate> myCS;
	int numRow;
	int numCol;
	
	public CoordinateSystem(int r, int c)
	{
		myCS = new ArrayList<Coordinate>();
		numRow=r;
		numCol=c;
	}
	public void addNeighbors(int r, int c)
	{
		addPoint(r-1,c-1);
		addPoint(r-1,c);
		addPoint(r-1,c+1);
		addPoint(r,c-1);
		addPoint(r,c+1);
		addPoint(r+1,c-1);
		addPoint(r+1,c);
		addPoint(r+1,c+1);
	}
	public void expand(int scale)
	{
		for(int i=0;i<myCS.size();i++)
		{
			myCS.set(i, myCS.get(i));
		}
	}
	public boolean addPoint(int r, int c)
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
		myCS.add(new Coordinate(r,c));
		return true;
	}
	public void remove(int i)
	{
		myCS.remove(i);
	}
	public Coordinate get(int i)
	{
		return myCS.get(i);
	}
	public int size()
	{
		return myCS.size();
	}
}
