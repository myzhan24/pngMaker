package base;

import java.util.ArrayList;
import java.util.Iterator;

import ucar.ma2.ArrayFloat;

public class FloatCoordinateSystem {
	ArrayList<FloatCoordinate> myCS;
	int numRow;
	int numCol;
	int lastFound=0;
	
	public FloatCoordinateSystem(int numR, int numC)
	{
		myCS = new ArrayList<FloatCoordinate>();
		numRow=numR;
		numCol=numC;
	}
	public FloatCoordinateSystem(FloatCoordinateSystem fcs, boolean clone)
	{
		if(clone)
			myCS = (ArrayList<FloatCoordinate>) fcs.myCS.clone();
		
		else
			myCS = new ArrayList<FloatCoordinate>();
		
		numRow=fcs.numRow;
		numCol=fcs.numCol;
	}
	public void addCoor(float a, float b, float val)
	{
		myCS.add(new FloatCoordinate(a,b,val));
	}
	public void addFloatCoordinate(FloatCoordinate c)
	{
		myCS.add(new FloatCoordinate(c.getX(),c.getY(),c.getValue()));
	}
	public boolean containsPoint(float r, float c)
	{
		for(FloatCoordinate fc: myCS)
		{
			if(fc.getX()==r && fc.getY()==c)
			{
				return true;
			}
		}
		return false;
	}
	public boolean containsPointAndStore(float r, float c)
	{
		for(int i=0;i<myCS.size();i++)
		{
			if(myCS.get(i).getX()==r && myCS.get(i).getY()==c)
			{
				lastFound=i;
				return true;
			}
		}
		return false;
	}
	public FloatCoordinate getLastFound()
	{
		return myCS.get(lastFound);
	}
	public boolean contains(FloatCoordinate fc)
	{
		if(myCS.contains(fc))
			return true;
		
		else
			return false;
	}
	
	public void addNeighbors(float r, float c, ArrayFloat.D2 ar)
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
	public void addGrid(float r, float c, ArrayFloat.D2 ar)
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
	public void scaleCenter(float scale)
	{
		for(int i=0;i<myCS.size();i++)
		{
			myCS.get(i).scaleXY(scale);
			//myCS.get(i).translateXY(scale/2.0f);
			myCS.get(i).translateXY((scale/2.0f));
		}
		numRow*=scale;
		numCol*=scale;
	}
	public void scale(float scale)
	{
		for(int i=0;i<myCS.size();i++)
		{
			myCS.get(i).scaleXY(scale);
		}
		numRow*=scale;
		numCol*=scale;
	}
	public void scaleRandom(float scale)
	{
		float randRow;
		float randCol;
		for(int i=0;i<myCS.size();i++)
		{
			randRow=(float)Math.random()*scale;
			randCol=(float)Math.random()*scale;
			myCS.get(i).scaleXY(scale);
			myCS.get(i).translateXY(randRow,randCol);
		}
		numRow*=scale;
		numCol*=scale;
	}
	public void scaleRandomCenter(float scale)
	{
		float randRow;
		float randCol;
		float size= scale/3f;
		for(int i=0;i<myCS.size();i++)
		{
			randRow=(float)(Math.random()*size + size);
			randCol=(float)(Math.random()*size + size);
			myCS.get(i).scaleXY(scale);
			myCS.get(i).translateXY(randRow,randCol);
		}
		numRow*=scale;
		numCol*=scale;
	}
	public boolean addPoint(float r, float c, ArrayFloat.D2 ar)
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
		myCS.add(new FloatCoordinate(r,c,ar.get((int)r, (int)c)));
		return true;
	}
	public boolean isValidPoint(float r, float c)
	{
		if(r<0)
			return false;
		
		else if( r>= numRow)
			return false;
		
		else if(c<0)
			return false;
		
		else if(c>=numCol)
			return false;
		
		return true;
	}
	public void remove(int i)
	{
		myCS.remove(i);
	}
	public FloatCoordinate get(int i)
	{
		return myCS.get(i);
	}
	public int size()
	{
		return myCS.size();
	}
	public ArrayList<FloatCoordinate> getArray()
	{
		return myCS;
	}
}
