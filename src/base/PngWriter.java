package base;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import javax.imageio.*;

import ucar.ma2.ArrayFloat;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;



public class PngWriter {
	static Comparator<Float> myComp = new Comparator<Float>()
			{
				public int compare(Float a, Float b)
				{
					return (a.compareTo(b));
				}
			};
			
			static Comparator<Double> myCompD = new Comparator<Double>()
					{
						public int compare(Double a, Double b)
						{
							return (int)(a.compareTo(b));
						}
					};
	private File outFile = null;
	private File fi = null;
	private BufferedImage bi = null;
	private File fo = null;
	private BufferedImage image =null;
	private	BufferedImage imIn = null;
	private int width, height;
	private static int[][] rgb;
	private static Font mFont = new Font("Tahoma", Font.PLAIN,13); 
	public PngWriter()
	{
		
	}
	public PngWriter(String inputImage)
	{
		fi =  new File(inputImage);
		try {
			System.out.println(fi.getAbsolutePath());
			Image inImage = javax.imageio.ImageIO.read(fi);
			width =  inImage.getWidth(null);
			height = inImage.getHeight(null);
			imIn = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			
			imIn.getGraphics().drawImage(inImage, 0, 0, width, height,null);
			rgb = new int[width][height];
			
			for(int i=0 ; i< width; i++)
			{
				for(int j=0; j< height; j++)
				{
					
					
					rgb[i][j]=imIn.getRGB(i, j);
					
					//System.out.println("rgb:"+rgb[i][j]);
				}
			}
			outFile = new File("sdg.png");
			this.createImage(rgb,outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PngWriter(String outputName, int w, int h)
	{
		width = w;
		height = h;
		outFile = new File(outputName+".png");
		this.createImage(rgb, outFile);
		
	}

	
	public void createImage(int[][]irgb, File fo)
	{
		//write rgb to any location
		bi = createBuffer(irgb,fo);
	}
	
	public void createImage(int[][]irgb, File fo, String[] legend, String para, String units)
	{
		//write rgb to any location
		bi = createBuffer(irgb,fo,legend,para,units);
	}
	
	public void createImage(int[][]irgb, File fo, String[] legend)
	{
		//write rgb to any location
		bi = createBuffer(irgb,fo,legend);
	}
	
	public void createImage(File fo)
	{
		int irgb[][] = new int[32][32];
		for(int i=0;i<irgb.length;i++)
		{
			for(int j=0; j<irgb[i].length;j++)
			{
				irgb[i][j]=0;
			}
		}
		bi = createBuffer(irgb,fo);
		
	}
	
	private BufferedImage createBuffer(int[][]irgb,File f)
	{
		//System.out.println(f.getAbsolutePath());
		
		BufferedImage buffer = new BufferedImage(irgb.length,irgb[0].length,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2D =  buffer.createGraphics();
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,irgb.length,irgb[0].length); 
		g2D.fill(rect);
	
		for(int i=0;i<irgb.length;i++){
            for(int j=0;j<irgb[i].length;j++){
            	if(irgb[i][j]!=-1)
            	{
                buffer.setRGB(i, j, irgb[i][j]);
                //System.out.println(irgb[i][j]);
            	}
            }
        }
        String  format = "PNG";   
        
        try {
			javax.imageio.ImageIO.write(buffer,format,f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return buffer;
	}
	
	private BufferedImage createBuffer(int[][]irgb,File f, String[] legend)
	{
		
BufferedImage buffer = new BufferedImage(irgb.length,irgb[0].length,BufferedImage.TYPE_INT_ARGB);
		
		/*Graphics2D g2D =  buffer.createGraphics();
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,irgb.length,irgb[0].length); 
		g2D.draw(rect);
		g2D.fill(rect);
		g2D.setColor(Color.black);
		g2D.setFont(mFont);
		g2D.drawString("**********", 0,0);

		*/
		
		//BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffer.getGraphics();
//        g.setColor(getRandColor(200,250));
       // g.fillRect(1, 1, width-1, height-1);
        g.setColor(Color.black);
       // g.drawRect(0, 0, width-1, height-1);
        g.setFont(mFont);
        for(int k = 0 ; k <legend.length; k++)
		{
        	g.drawString(legend[k], 10+75*k, irgb[0].length/2+15);
		}
        
       
        for(int i=0;i<irgb.length;i++){
            for(int j=0;j<irgb[i].length/2;j++){
            	if(irgb[i][j]!=-1)
            	{
                buffer.setRGB(i, j, irgb[i][j]);
               
            	}
            }
        }
        
        
        String  format = "PNG";   
        
        try {
			ImageIO.write(buffer,format,f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return buffer;
	}
	
	private BufferedImage createBuffer(int[][]irgb,File f, String[] legend, String para, String units)
	{
		
BufferedImage buffer = new BufferedImage(irgb.length,irgb[0].length,BufferedImage.TYPE_INT_ARGB);
		
		/*Graphics2D g2D =  buffer.createGraphics();
		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,irgb.length,irgb[0].length); 
		g2D.draw(rect);
		g2D.fill(rect);
		g2D.setColor(Color.black);
		g2D.setFont(mFont);
		g2D.drawString("**********", 0,0);

		*/
		
		//BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffer.getGraphics();
//        g.setColor(getRandColor(200,250));
       // g.fillRect(1, 1, width-1, height-1);
        g.setColor(Color.red);
       // g.drawRect(0, 0, width-1, height-1);
        g.setFont(mFont);
        //centralized the fonts
        FontMetrics FM=g.getFontMetrics(); ;
        int Ascent = FM.getAscent();
        int Descent = FM.getDescent();
        int Width = FM.stringWidth(para);
        int wCenter = ( irgb.length- Width) / 2;
       
       
        g.drawString(para+" ("+units+")", wCenter, 15);		//title
        for(int k = 0 ; k <legend.length; k++)
		{
        	g.drawString(legend[k], 10+75*k, 2*irgb[0].length/3+15);
		}
        
       
        for(int i=0;i<irgb.length;i++){
            for(int j=20;j<2*irgb[i].length/3;j++){
            	if(irgb[i][j]!=-1)
            	{
                buffer.setRGB(i, j, irgb[i][j]);
               
            	}
            }
        }
        
        
        String  format = "PNG";   
        
        try {
			ImageIO.write(buffer,format,f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return buffer;
	}
	
	public static float findMaxVal(ArrayFloat.D1 ar)
	{
		
		float max = ar.getFloat(0);
		
		for(float f : ar.getShape())
		{
			if(f > max)
				max = f;
			
		}
		return max;
		
	}
	public static float findMaxVal(ArrayFloat.D2 ar)
	{
		int[] shape = ar.getShape();
		float max = ar.get(0,0);
		for(int i = 0 ; i < shape[0];i++)
		{
			for(int j = 0 ; j < shape[1]; j++)
			{
				if( max < ar.get(i,j)  )
					max = ar.get(i,j);
			}
		}
		//System.out.println(max);
		return max;
		
	}
	public static float findMinVal(ArrayFloat.D2 ar)
	{
		int[] shape = ar.getShape();
		float min = ar.get(0,0);
		for(int i = 0 ; i < shape[0];i++)
		{
			for(int j = 0 ; j < shape[1]; j++)
			{
				if( min > ar.get(i,j)  )
					min = ar.get(i,j);
			}
		}
		return min;
		
	}
	public static float getDistance(int x1, int y1,int x2,int y2)
	{
		return (float)Math.sqrt( ((float)(x2-x1))*((float)(x2-x1)) + ((float)(y2-y1))*((float)(y2-y1))    );
	}
	public static float getDistance(float x1, float y1,float x2,float y2)
	{
		return (float)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	public static float getDistance(int x1,int y1, FloatCoordinate floatCoordinate)
	{
		return (float)Math.sqrt( (floatCoordinate.getX()-x1)*(floatCoordinate.getX()-x1) + (floatCoordinate.getY()-y1)*(floatCoordinate.getY()-y1)  );
	}
	
	public static double getDistance(int x1,int y1, DoubleCoordinate dc)
	{
		return Math.sqrt( (dc.getX()-(double)x1)*(dc.getX()-(double)x1) + (dc.getY()-(double)y1)*(dc.getY()-(double)y1)  );
	}
	
	/**
	 * generateIDWArray
	 * Generates a larger array with interpolated data based on the Inverse Distance Weighting method
	 * @param ar - 2D array of data
	 * @param scale - the multiple which the data will be increased by, as well as the image size
	 * @param expo - the specified exponent in the IDW formula
	 * @return a new array of interpolated data
	 */
	public static ArrayFloat.D2 generateIDWArray(ArrayFloat.D2 ar, int scale, int expo)
	{
		int[] baseShape = ar.getShape();
		ArrayFloat.D2 ret= new ArrayFloat.D2(baseShape[0]*scale,baseShape[1]*scale);	//new array rows and columns are a multiple of scale of the given one
		
		//the thought process behind this method is expanding and interpolating data around each old data cell and generating new data in between using IDW on neighboring cells old data
		//the structure of this loop system
		//outer loops: each old data cell
		//inner loops: each of the scale x scale new cells
		for(int i=0; i < baseShape[0];i++)			//base row
		{	
			for(int j=0; j < baseShape[1];j++)		//base col
			{
				//get all valid neighboring base points
				FloatCoordinateSystem myFCS = new FloatCoordinateSystem(baseShape[0],baseShape[1]);
				myFCS.addNeighbors(i,j,ar);	//adds all valid neighbors and holds the old data value, max 8 neighbors
				myFCS.scaleCenter(scale);		//expands the coordinate system to the scale of the new array and centers each old data point
 				
				float numer;
				float denom;
				float dist;
				float tempDenom;
				FloatCoordinate tempFC;
				//for each new cell, apply the IDW method to the neighbors to generate a new data point
				for(int a=0; a< scale; a++)
				{
					for(int b=0; b< scale; b++)
					{
						numer=0;
						denom=0;
						tempDenom=0;
						dist=0;
						//(sum of value/dist^n )/ (sum of 1/dist&n) 
						for(int fcs =0; fcs<myFCS.size();fcs++)
						{
							tempFC = myFCS.get(fcs);
							dist = getDistance(i*scale+a,j*scale+b,tempFC);		//distance between new cell and old cell
							tempDenom = (float) (1.0/Math.pow(dist, expo));
							
							numer+= tempFC.getValue() * tempDenom;
							denom+= tempDenom;
						}
						//once neighbors are done, account for the old cell the new cell is currently in
						dist= getDistance(i*scale+a,j*scale+b,i*scale+scale/2.0f,j*scale+scale/2.0f);
						if(dist==0)	// if the new cell corresponds to the old cell, use a dummy distance
							dist=1f;
						tempDenom = (float) (1.0/Math.pow(dist, expo));
						numer+= ar.get(i, j) * tempDenom;
						denom+= tempDenom;
						ret.set(i*scale+a, j*scale+b, numer/denom);	//add the new data point
					}
				}
				
			}
		}
		return ret;
		
	}
	
	/**
	 * generateIDWArrayRandom applies the same method as generateIDWArray except the expanding of the old data point has a random location within its own grid when interpolating the new data
	 * @param ar
	 * @param scale
	 * @param expo
	 * @return
	 */
	public static ArrayFloat.D2 generateIDWArrayRandom(ArrayFloat.D2 ar, int scale, int expo)
	{
		int[] baseShape = ar.getShape();
		ArrayFloat.D2 ret= new ArrayFloat.D2(baseShape[0]*scale,baseShape[1]*scale);	//new array rows and columns are a multiple of scale of the given one
		
		//the thought process behind this method is expanding and interpolating data around each old data cell and generating new data in between using IDW on neighboring cells old data
		//the structure of this loop system
		//outer loops: each old data cell
		//inner loops: each of the scale x scale new cells
		for(int i=0; i < baseShape[0];i++)			//base row
		{	
			for(int j=0; j < baseShape[1];j++)		//base col
			{
				//get all valid neighboring base points
				FloatCoordinateSystem myFCS = new FloatCoordinateSystem(baseShape[0],baseShape[1]);
				myFCS.addGrid(i,j,ar);	//adds all valid neighbors and holds the old data value, max 8 neighbors
				myFCS.scaleCenter(scale);		//expands the coordinate system to the scale of the new array and centers each old data point
 				
				float numer;
				float denom;
				float dist;
				float tempDenom;
				FloatCoordinate tempFC;
				//for each new cell, apply the IDW method to the neighbors to generate a new data point
				for(int a=0; a< scale; a++)
				{
					for(int b=0; b< scale; b++)
					{
						numer=0;
						denom=0;
						tempDenom=0;
						dist=0;
						//(sum of value/dist^n )/ (sum of 1/dist&n) 
						for(int fcs =0; fcs<myFCS.size();fcs++)
						{
							tempFC = myFCS.get(fcs);
							dist = getDistance(i*scale+a,j*scale+b,tempFC);		//distance between new cell and old cell
							if(dist==0)	// if the new cell corresponds to the old cell, use a dummy distance
								dist=1f;
							
							tempDenom = (float) (1.0/Math.pow(dist, expo));
							numer+= tempFC.getValue() * tempDenom;
							denom+= tempDenom;
						}
						//once neighbors are done, account for the old cell the new cell is currently in
						
						ret.set(i*scale+a, j*scale+b, numer/denom);	//add the new data point
					}
				}
				
			}
		}
		return ret;
	}

	/**
	 * generateIDWArrayMin
	 * This is a method of IDW interpolation to generate a larger array of interpolated data based on a given array. 
	 * The given data is scaled up with slight variation, then each new data point is calculated using the IDW formula on 
	 * given data points that are within a range of scale * thresholdFactor.
	 * @param ar				-original data
	 * @param scale				-new array dimensions of scale*given data dimension
	 * @param expo				-specified exponent in the IDW formula, not currently used in this implementation
	 * @param thresholdFactor	-range that the IDW interpolation use
	 * @return					-a new ArrayFloat.D2 Array with interpolated data
	 */
	public static ArrayFloat.D2 generateIDWArrayMin(ArrayFloat.D2 ar, int scale, int expo, float thresholdFactor)
	{
		int[] baseShape = ar.getShape();
		FloatCoordinateSystem myFCS = new FloatCoordinateSystem(baseShape[0], baseShape[1]);
		ArrayFloat.D2 ret = new ArrayFloat.D2(baseShape[0] * scale, baseShape[1] * scale);
		
		//load the data into an FloatCoordinateSystem ArrayList
		for(int i = 0; i < baseShape[0]; i ++)
			for(int j = 0; j < baseShape[1]; j++)
				myFCS.addCoor(i, j, ar.get(i, j));
			
		myFCS.scaleRandomCenter(scale);
		TreeMap<Float, FloatCoordinate> myTree;				//go through each new cell of the new array and generate an interpolated point
		int[] shape = ret.getShape();
		
		
		float numer;
		float denom;
		float dist=0;
		float tempDenom;
		Entry<Float, FloatCoordinate> myE;
		for(int i =0; i < shape[0]; i++)		//row
		{	
			for(int j = 0; j < shape[1]; j++)	//col
			{
				myTree = new TreeMap<Float, FloatCoordinate>(myComp);	//add each data point to a tree map, whose compare is based on the distance between the current cell and each old data point
				
				//float minDist=Float.MAX_VALUE;
				//FloatCoordinate minFC=new FloatCoordinate();
				for(FloatCoordinate fc : myFCS.getArray())
				{
					dist=getDistance(i,j,fc.getX(),fc.getY());
					if(dist < scale*thresholdFactor)
						myTree.put(dist, fc);
					
				/*	if(minDist > dist)
					{
						minDist=dist;
						minFC=fc;
					}*/
				}
				numer=0;
				denom=0;
				myE = myTree.pollFirstEntry();
				while(myE!=null)
				{
					dist = myE.getKey();
					if(dist<scale/thresholdFactor/2f)							//special case if the current cell corresponds to an old data cell's location
					{
						//dist*=scale/(thresholdFactor*thresholdFactor);
						
						dist=scale/thresholdFactor/2f;
					}
				
					tempDenom = (1.0f/dist);
					numer+=myE.getValue().getValue()*tempDenom;	//applying the IDW formula
					denom+=tempDenom;
					myE = myTree.pollFirstEntry();
					
				}
				
		/*		if(dist<1f)
				{
					//ret.set(i, j, myE.getValue().getValue());	
					ret.set(i,j,0f);
				}
				else*/
					ret.set(i, j, numer/denom);				//store the interpolated data point into the new array
				
			}
		}
		return ret;
	}
	
	public static ArrayFloat.D2 generateIDWArrayMinTrue(ArrayFloat.D2 ar, int scale, int expo, float thresholdFactor)
	{
		int[] baseShape = ar.getShape();
		FloatCoordinateSystem myFCS = new FloatCoordinateSystem(baseShape[0], baseShape[1]);
		ArrayFloat.D2 ret = new ArrayFloat.D2(baseShape[0] * scale, baseShape[1] * scale);
		
		//load the data into an FloatCoordinateSystem ArrayList
		for(int i = 0; i < baseShape[0]; i ++)
			for(int j = 0; j < baseShape[1]; j++)
				myFCS.addCoor(i, j, ar.get(i, j));
			
		myFCS.scaleRandomCenter(scale);
		TreeMap<Float, FloatCoordinate> myTree;				//go through each new cell of the new array and generate an interpolated point
		int[] shape = ret.getShape();
		
		
		float numer;
		float denom;
		float dist=0;
		float tempDenom;
		Entry<Float, FloatCoordinate> myE;
		for(int i =0; i < shape[0]; i++)		//row
		{	
			for(int j = 0; j < shape[1]; j++)	//col
			{
				myTree = new TreeMap<Float, FloatCoordinate>(myComp);	//add each data point to a tree map, whose compare is based on the distance between the current cell and each old data point
				
				//float minDist=Float.MAX_VALUE;
				//FloatCoordinate minFC=new FloatCoordinate();
				for(FloatCoordinate fc : myFCS.getArray())
				{
					dist=getDistance(i,j,fc.getX(),fc.getY());
					if(dist < scale*thresholdFactor)
						myTree.put(dist, fc);
					
				/*	if(minDist > dist)
					{
						minDist=dist;
						minFC=fc;
					}*/
				}
				numer=0;
				denom=0;
				myE = myTree.pollFirstEntry();
				while(myE!=null)
				{
					dist = myE.getKey();
					if(dist==0)							//special case if the current cell corresponds to an old data cell's location
					{
						//dist*=scale/(thresholdFactor*thresholdFactor);
						
						dist=0.01f;
					}
				
					tempDenom = (1.0f/dist);
					numer+=myE.getValue().getValue()*tempDenom;	//applying the IDW formula
					denom+=tempDenom;
					myE = myTree.pollFirstEntry();
					
				}
				
		/*		if(dist<1f)
				{
					//ret.set(i, j, myE.getValue().getValue());	
					ret.set(i,j,0f);
				}
				else*/
					ret.set(i, j, numer/denom);				//store the interpolated data point into the new array
				
			}
		}
		return ret;
	}
	
	/**
	 * this function is a slower method and obsolete
	 * @param ar
	 * @param scale
	 * @param expo
	 * @param numNearest
	 * @param percent
	 * @return
	 */
	public static ArrayFloat.D2 generateIDWArrayNeighbor(ArrayFloat.D2 ar, int scale, int expo,int numNearest, float percent)
	{
		int[] baseShape = ar.getShape();
		FloatCoordinateSystem myFCS = new FloatCoordinateSystem(baseShape[0], baseShape[1]);
		ArrayFloat.D2 ret = new ArrayFloat.D2(baseShape[0] * scale, baseShape[1] * scale);
		
		//load the data into an FloatCoordinateSystem ArrayList
		for(int i = 0; i < baseShape[0]; i ++)
		{
			for(int j = 0; j < baseShape[1]; j++)
			{
				myFCS.addCoor(i, j, ar.get(i, j));
			}
		}
		//System.out.println(baseShape[0]);
		int total = (int)(percent*((float)baseShape[0]*baseShape[1]));
		//System.out.println(myFCS.size()+" "+baseShape[0]*baseShape[1]);
		while(myFCS.size() > total)							//cut out data points randomly until a percent of the original amount is left
		{
			myFCS.remove((int)(Math.random() * myFCS.size()));
		}
		//System.out.println(myFCS.size()+" "+total);
		myFCS.scaleCenter(scale);
		int[] shape = ret.getShape();
		for(int i =0; i < shape[0]; i++)		//row
		{	
			for(int j = 0; j < shape[1]; j++)	//col
			{
				FloatCoordinateSystem newFCS = determineNeighbors(i,j,myFCS,numNearest);
				
				float numer=0;
				float denom=0;
				float tempDenom;
				float dist;
				FloatCoordinate myFC;
				for(int a = 0; a< newFCS.size(); a++)
				{	
					myFC=newFCS.get(a);
					dist = getDistance(i,j,myFC);
					if(dist==0)							//special case if the current cell corresponds to an old data cell's location
						dist=0.001f;	
						
					tempDenom = (float) (1.0f/Math.pow(dist, expo));
					numer+=myFC.getValue()*tempDenom;	//applying the IDW formula
					denom+=tempDenom;
				}
				//System.out.println(numer/denom);
				ret.set(i, j, numer/denom);				//store the interpolated data point into the new array
			}
		}
		return ret;
	}
	
	/**
	 * method used by obsolete method to determine if points coincide within a float coordinate system
	 * @param r
	 * @param c
	 * @param myFCS
	 * @param numNearest
	 * @return
	 */
	public static FloatCoordinateSystem determineNeighbors(float r, float c,FloatCoordinateSystem myFCS, int numNearest)
	{
		FloatCoordinateSystem ret = new FloatCoordinateSystem(myFCS, false);
		int dir=3; // 0 up, 1 left, 2 down, 3 right
		int stepSize=1;
		int stepCount=0;
		float row=r;
		float col=c;
		
		if(myFCS.isValidPoint(row, col) && myFCS.containsPointAndStore(row, col)){
			ret.addFloatCoordinate(myFCS.getLastFound());
		}
		
		while(ret.size() < numNearest)
		{
			//System.out.println(dir);
			stepCount++;
			if(dir==0)		//up
			{
				row--;
				if(myFCS.isValidPoint(row, col))
				{
					if(myFCS.containsPointAndStore(row, col))
						ret.addFloatCoordinate(myFCS.getLastFound());
				}
				else
				{
					row = row - (stepSize-stepCount);
					stepCount = stepSize;
				}		
				
				if(stepCount >= stepSize)
				{
					dir=1;
					stepCount=0;
					stepSize++;
				}
			}
			else if(dir==1)	//left
			{
				col--;
				if(myFCS.isValidPoint(row, col))
				{
					if(myFCS.containsPointAndStore(row, col))
						ret.addFloatCoordinate(myFCS.getLastFound());
				}
				else
				{
					col = col - (stepSize-stepCount);
					stepCount = stepSize;
				}
				if(stepCount >= stepSize)
				{
					dir=2;
					stepCount=0;
				}
			}
			else if(dir==2)	//down
			{
				row++;
				if(myFCS.isValidPoint(row, col))
				{
					if(myFCS.containsPointAndStore(row, col))
						ret.addFloatCoordinate(myFCS.getLastFound());
				}
				else
				{
					row = row + (stepSize-stepCount);
					stepCount = stepSize;
				}
				if(stepCount >= stepSize)
				{
					dir=3;
					stepCount=0;
					stepSize++;
				}
			}
			else			//right
			{
				col++;
				if(myFCS.isValidPoint(row, col))
				{
					if(myFCS.containsPointAndStore(row, col))
						ret.addFloatCoordinate(myFCS.getLastFound());
				}
				else
				{
					col = row + (stepSize-stepCount);
					stepCount = stepSize;
				}
				if(stepCount >= stepSize)
				{
					dir=0;
					stepCount=0;
				}
			}
		}
	//	System.out.println("FINISH");
		return ret;
	}
	
	
	 public static void main(String[] args){
		NetcdfFile dataFile = null;
		try
		{
			Scanner scanner = new Scanner( System.in );
			
			String filename = "test5.nc";
			boolean cont=true;
			boolean restart=false;
			System.out.println("Welcome to the NetCDF PNG Maker\n");
			while(cont)
			{
				restart=false;
			String var = "topog";
			String outfilename = var;
			
			dataFile =  NetcdfFile.open(filename, null);
			List<Variable> myL = dataFile.getVariables();
			System.out.print("Type variable name, \"la\" to list all varibles, or \"quit\": ");
			String input = scanner.nextLine();
			if(input.equals("quit"))
			{
				cont=false;
				restart=true;
			}
			else if(input.equals("la"))
			{
				System.out.println("\n"+filename+" contains "+dataFile.getVariables().size()+" variables.");
				System.out.println("==========================================================================");
				System.out.println("=== Variable =============== Description =================================");
				System.out.println("==========================================================================");
				for(Variable s: myL)
				{
					System.out.print(s.getFullName()+":");
						for(int a = 0; a < 20-s.getFullName().length();a++)
							System.out.print(" ");
					
					System.out.println("\t"+s.getDescription());
				}
				
				restart=true;
				cont=true;
				System.out.println("==========================================================================");
				System.out.println("=== Variable =============== Description =================================");
				System.out.println("==========================================================================\n");
			}
			
			if(restart==false)
			{
			
			boolean found = false;
			Variable sa = null;
			for( Variable s: myL)
			{
				if(input.equals(s.getFullName()))
				{
					var = input;
					outfilename= s.getDescription();
					found=true;
					sa=s;
					break;
				}
			}
			if(!found)
			{
				System.out.println("Could not find your variable: "+input);
			}
			
			else
			{
				System.out.println("You've requested "+sa.getDescription()+" ("+input+"). Please wait a moment...");
			
			
			Variable dataVar = dataFile.findVariable(var);
			
			int[] origin = new int[3];
			int[] shape = dataVar.getShape();
			
			ArrayFloat.D2 myF = new ArrayFloat.D2(shape[1], shape[2]);
			ArrayFloat.D3 dataArray = (ArrayFloat.D3) dataVar.read(origin, dataVar.getShape());
		

			//transfer the 3D array data into a 2D array data
			for(int i =0 ; i < shape[1]; i++) //latitude 
			{
				for(int j = 0 ; j < shape[2]; j++) //longitude
				{
					myF.set(i,j,dataArray.get(0,i,j));
				}
			}
			
			float max = findMaxVal(myF);
			float min = findMinVal(myF);
			
			//create a PngColor
			PngColor col = new PngColor(min,max,var,dataVar.getUnitsString());
			
			
			rgb = new int[shape[2]][shape[1]];
			for(int i = 0; i < shape[2];i++)
			{
				for(int j = 0; j < shape[1]; j++)
				{
					rgb[i][j]=col.getColorRGB(myF.get((shape[1]-1)-j,i));
				}
			}
			PngWriter png1 = new PngWriter(outfilename,(shape[1]),(shape[0]));
			
			col.myCreateLegend(outfilename+" LEGEND.png",var);	//create a legend
			
			
			//rgb for base image
			/*
			rgb = new int[shape[2]][shape[1]]; //assign the netCDF data into the rgb values
												//lat comes first but must be the Y values, so it is switched here
			for(int i = 0; i < shape[2];i++)
			{
				for(int j = 0; j < shape[1]; j++)
				{
					rgb[i][j]=col.getColorRGB(myF.get((shape[1]-1)-j,i));
				}
			}			*/
			
			int scale =4;
			int expo = 1;
			float threshold = 2f;
			StopWatch myWatch = new StopWatch();
			
			
				myWatch.start();
				ArrayFloat.D2 IDWArray = generateIDWArrayMin(myF, scale, expo, threshold);
				shape = IDWArray.getShape();
				//col= new PngColor(findMinVal(IDWArray),findMaxVal(IDWArray),var);
				
				//rgb for IDW generated image
				rgb = new int[shape[1]][shape[0]];
				for(int i = 0; i < shape[1];i++)
				{
					for(int j = 0; j < shape[0]; j++)
					{
						rgb[i][j]=col.getColorRGB(IDWArray.get((shape[0]-1)-j,i));
					}
				}
				
				PngWriter png2 = new PngWriter(outfilename+"x"+scale,(shape[1]),(shape[0]));
				//col.myCreateLegend(outfilename+" LEGENDx8.png");
				myWatch.stop();
				System.out.println("\ndone!\n");
				System.out.println("Images: "+outfilename+".png, "+outfilename+"x"+scale+".png, and "+outfilename+" LEGEND"+".png created!");
				System.out.println("=======time report=======");				System.out.println("Time: "+myWatch.getElapsedTime()/1000f+ "s");
				System.out.println("scale: "+scale+"\tthreshold: "+String.format("%1.2f", threshold));
				System.out.println("=======time report=======\n\n");				myWatch.reset();
			}
			}
			}
			
		} 
		catch (java.io.IOException e) 
		{
		        e.printStackTrace();
		        return;
		}  
		catch (IndexOutOfBoundsException e) 
		{
		        e.printStackTrace();
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally 
		{
		   if (dataFile != null)
		   try 
		   {
		     dataFile.close();
		   } 
		   catch (IOException ioe) 
		   {
		     ioe.printStackTrace();
		   }
		   
		 
		}
		
	 }
	
	
}
