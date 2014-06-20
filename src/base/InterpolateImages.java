package base;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;
import javax.imageio.*; 

public class InterpolateImages {
	
	public InterpolateImages(String filePath, int ratio)
	{
	
	       Image image;
	       
		try {
			image = ImageIO.read(new File(filePath));
			 int w = image.getWidth(null);
		       int h = image.getHeight(null);
		       BufferedImage bilinear = new BufferedImage(ratio*w, ratio*h,
		         BufferedImage.TYPE_INT_ARGB);
		       BufferedImage bicubic = new BufferedImage(ratio*w, ratio*h,
		         BufferedImage.TYPE_INT_ARGB);   
		   
		       Graphics2D bg = bilinear.createGraphics();
		       bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		         RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		       bg.scale(ratio, ratio);
		       bg.drawImage(image, 0, 0, null);
		       bg.dispose();   
		   
		       bg = bicubic.createGraphics();
		       bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		      bg.scale(ratio, ratio);
		      bg.drawImage(image, 0, 0, null);
		      bg.dispose();   
		      File f = new File("test.png");
	
		      
		      String  format = "PNG";   
		        
		        try {
					javax.imageio.ImageIO.write(bilinear,format,f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	      
	}
	
	  public static void main(String args[]){
		 // InterpolateImages outimage = new InterpolateImages("F:\\workspace\\ClimateViz\\20110830\\ij_map1\\Surface Air Temperature, mean.png",50);
		  
	  }

}
