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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.*;

import ucar.ma2.ArrayFloat;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;



public class PngWriter {

	private File outFile = null;
	private File fi = null;
	private BufferedImage bi = null;
	private File fo = null;
	private BufferedImage image =null;
	private	BufferedImage imIn = null;
	private int width, height;
	private int[][] rgb;
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
	 public static void main(String[] args){
		NetcdfFile dataFile = null;
		try
		{
			dataFile =  NetcdfFile.open("test.nc", null);
			// PngWriter png = new PngWriter("test.png");
			 //PngWriter png = new PngWriter("test.nc");
			 PngColor col = new PngColor();
			 Variable dataVar = dataFile.findVariable("tgrnd");
			 int[] origin = new int[3];
			 float q = findMaxVal((ArrayFloat.D2) dataVar.read(origin, dataVar.getShape()));
			
			
			
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
