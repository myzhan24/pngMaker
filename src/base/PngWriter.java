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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;

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
	private static Font mFont = new Font("Tahoma", Font.PLAIN,14); 
	public PngWriter()
	{
		
	}
	public PngWriter(String inputImage)
	{
		System.out.println("yesyaas");
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
			System.out.println("yesys");
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
	
	public void createImage(int[][]irgb, File fo, String[] legend, String para)
	{
		//write rgb to any location
		bi = createBuffer(irgb,fo,legend,para);
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
	
	private BufferedImage createBuffer(int[][]irgb,File f, String[] legend, String para)
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
       
       
        g.drawString(para, wCenter, 15);
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
			
			System.out.println(f);
		}
		System.out.println(max);
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
		return (float)Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)    );
	}
	public static ArrayFloat.D2 generateIDWArray(ArrayFloat.D2 ar, int scale, int numNearest, int factor)
	{
		int[] baseShape = ar.getShape();
		ArrayFloat.D2 ret= new ArrayFloat.D2(baseShape[0]*scale,baseShape[1]*scale);
		TreeMap<Float,IDWPoint> myTree;
		
		for(int i = 0 ; i < baseShape[0]; i++) //row
		{
			for(int j = 0; j < baseShape[1]; j++) //column
			{
				//determine nearest y(4) base points, this includes the current i and j
				
				
				 myTree = new TreeMap<Float, IDWPoint>(myComp);
				 
				 //puts all the distances between the base points and the current base point into a tree
				 for(int t = 0 ; t < baseShape[0]; t++) //row
				 {
					 for(int y = 0; y < baseShape[1]; y++) //column
					 {
						// System.out.println(""+getDistance(i,j,t,y));
						 myTree.put(getDistance(i,j,t,y), new IDWPoint(t,y,getDistance(i,j,t,y))   );
					 }
				 }
				 //the tree is traversed for the first numNearest elements
				 NavigableSet<Float> myNS = myTree.navigableKeySet();
				/* Iterator<Float> myI = myNS.iterator();
				 while(myI.hasNext())
					 System.out.println(myI.next());
				 
				*/
				 //System.out.println(myTree.pollFirstEntry().getValue().getDistance());
				 
				 //System.out.println("********");
				// System.out.println(""+myTree.size());
				ArrayList<IDWPoint> neighbors= new ArrayList<IDWPoint>();
				for(int asdf =0 ; asdf< numNearest;asdf++)
				{
					neighbors.add(myTree.pollFirstEntry().getValue());
				}
				myTree.pollFirstEntry().getValue();
				for(int a = 0; a < scale; a++)	//inner row
				{
					for(int b = 0; b < scale; b++) //inner column
					{
						/*if(a==0 && b==0)
						{
							ret.set(i*scale, j*scale, ar.get(i, j));
						}
						else*/
						{
							float numer=0;
							float denom=0;
							float dist=0;
							//TreeMap<Float,IDWPoint> myTempTree = (TreeMap<Float, IDWPoint>) myTree.clone();
							
							IDWPoint p;
							for(int nu =0 ; nu < numNearest; nu++)
							{
								p=neighbors.get(nu);
							//	System.out.print(p.getDistance()+", \t");
								dist=getDistance(p.getX()*scale+scale/2,p.getY()*scale+scale/2,i*scale+a,j*scale+b);
								numer+= ar.get(p.getX(),p.getY())/(Math.pow(dist, factor));
								denom+= 1.0/(Math.pow(dist, factor));
							}
							ret.set(i*scale+a, j*scale+b, numer/denom);
							//System.out.println("");
							
						}
					}
				}
			}
		}
		return ret;
		
	}
	
	 public static void main(String[] args){
		NetcdfFile dataFile = null;
		try
		{
			dataFile =  NetcdfFile.open("test.nc", null);
			// PngWriter png = new PngWriter("test.png");
			 //PngWriter png = new PngWriter("test.nc");
		
			 Variable dataVar = dataFile.findVariable("tgrnd");
			 int[] origin = new int[3];
			 int[] shape =  dataVar.getShape();
			 //System.out.println(shape[0]+" "+shape[1]+" "+shape[2]+" ");
			 ArrayFloat.D2 myF = new ArrayFloat.D2(shape[1], shape[2]);
			
			 ArrayFloat.D3 lel =((ArrayFloat.D3) dataVar.read(origin, dataVar.getShape()));
			 
			 for(int i = 0; i < shape[1];i++)
			 {
				 for(int j = 0; j < shape[2]; j++)
				 {
					 myF.set(i, j, lel.get(0, i, j));
					 //System.out.println(lel.get(0,i,j));
				 }
			 }
			 
			 float max = findMaxVal(myF);
			 float min = findMinVal(myF);
			 System.out.println(min+" "+max+" "+shape[1]+" "+shape[2]);
			 PngColor col = new PngColor(min,max,"tgrnd");
			 col.myCreateLegend("legendaryg.png");
			 
			/*rgb = new int[shape[1]][shape[2]];
			 for(int i = 0; i < shape[1];i++)
			 {
				 for(int j = 0; j < shape[2]; j++)
				 {
					rgb[i][j]=col.getColorRGB(myF.get(i,j));
					 //System.out.println(lel.get(0,i,j));
				 }
			 }*/
			 
			 rgb = new int[shape[2]][shape[1]];
			 for(int i = 0; i < shape[2];i++)
			 {
				 for(int j = 0; j < shape[1]; j++)
				 {
					rgb[i][j]=col.getColorRGB(myF.get((shape[1]-1)-j,i));
					 //System.out.println(lel.get(0,i,j));
				 }
			 }
			/* int wi = 720;
			 int he = 460;
			 rgb = new int[wi][he];
			 float range = max-min;
			 for(int i = 0; i < wi; i++)
			 {
				 for(int j = 0; j < he;j++)
				 {
					 rgb[i][j]=col.getColorRGB((float) ((Math.random()*range) +min));
				 }
			 }*/
			PngWriter png = new PngWriter("testerino",(shape[1]*3),(shape[2]*3));
			
			
			for(int a=0;a<40;a=a+5)
			{
			ArrayFloat.D2 testerino = generateIDWArray(myF,30,4,a);
			shape=testerino.getShape();
		/*	 max = findMaxVal(myF);
			 min = findMinVal(myF);
			// System.out.println(min+" "+max+" "+shape[1]+" "+shape[2]);
			  col = new PngColor(min,max,"tgrnd");
			 col.myCreateLegend("legendarygBIG.png");*/
			 
		
			 
			 rgb = new int[shape[1]][shape[0]];
			 for(int i = 0; i < shape[1];i++)
			 {
				 for(int j = 0; j < shape[0]; j++)
				 {
					rgb[i][j]=col.getColorRGB(testerino.get((shape[0]-1)-j,i));
					 //System.out.println(lel.get(0,i,j));
				 }
			 }
			PngWriter png2 = new PngWriter("testerinoBIGBIG4pow"+a,(shape[0]*3),(shape[1]*3));
		}
			/*		ArrayFloat.D2 tet = new ArrayFloat.D2(10, 10);
			for(int i=0; i < 10; i ++)
			{
				for(int j =0; j< 10; j ++)
				{
					tet.set(i, j, i+j);
				}
			}
			ArrayFloat.D2 tes = generateIDWArray(tet,2,4);*/
			
			/*
			int[] tetshape = tet.getShape();
			for(int i=0;i<tetshape[0];i++)
			{
				System.out.print("[");
				for(int j=0;j<tetshape[1];j++)
				{
				//	System.out.print(tet.get(i,j)+", \t");
				}
				System.out.println("]");
			}*/
			/*int[] tesshape = tes.getShape();
			for(int i=0;i<tesshape[0];i++)
			{
				System.out.print("[");
				for(int j=0;j<tesshape[1];j++)
				{
					System.out.print(String.format("%2.5f",tes.get(i,j))+", \t");
				}
				System.out.println("]");
			}*/
			
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
