package base;


import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.text.DecimalFormat;


import ucar.ma2.Array;

public class PngColor {

	private int[] rgb = new int[8];
	private float max; 
	private float min;
	private String para;
	//= Color.HSBtoRGB(0f, 0.938f, 90);
	//Color nRGB = new Color((rgb));
	private int[][] legendMap = new int[510][60];
	private String[] lengNum= new String[7];
	public PngColor()
	{
		
	}
	
	//input: a series of data
	//convert the attribute value into color
	public String[] getLegend()
	{
		return lengNum;
	}
	public PngColor(float inMin, float inMax)
	{
		this.max = inMax;
		this.min = inMin;
	}
	
	public PngColor(float inMin, float inMax, String para)
	{
		this.max = inMax;
		this.min = inMin;
		this.para = para;
	}
	
	public void trans(double left , double right, double up, double down)
	{
		double k1 = (44-24.9)/(42.68-29.53);
		double k2 = (-98.7-(-144.6))/(-78-(-125));
		double newUp = up-1.4;
		double newDown= newUp-(up-down)/k1;
		double newRight = ((right-left)/2+left)+k2*(right-left)/4;
		double newLeft =((right-left)/2+left)-k2*(right-left)/4;
	}
	
	
	//convert any data to color
	public int getColorRGB(float data)
	{
		//int color1=160;//(int) ((data-this.min)/(max-min)*8);
		//System.out.println(color1);
		//normalized:0-1
		float normal = (data-this.min)/(max-min);
		int rgb = Color.HSBtoRGB((1f-normal)/1.5f, 0.94f, 0.5f+normal*0.33f);
		
		return rgb;
	}
	
	/*public void createLegend(String location)
	{
		for(int i=0 ; i <legendMap.length; i++)
		{
			for(int j=0; j< legendMap[i].length; j++)
			{
				if(i<10||i>410)
					legendMap[i][j]=-1;
				else{
				float normal =(float) ((i-10)*1.0/(legendMap.length-20));
				legendMap[i][j]=  Color.HSBtoRGB((1f-normal)/1.5f, 0.94f, 0.5f+normal*0.33f);
				}
			}
		}
		
		File legend = new File(location);
		PngWriter pg = new PngWriter();
		String[] lengNum = new String[6];
		for(int k = 0 ; k <lengNum.length; k++)
		{
			float inLegend =  this.min+(this.max-this.min)/11f*k;
			DecimalFormat myformat=new DecimalFormat("0.000"); 
			lengNum[k] = (myformat.format(inLegend));
		}
		if(this.para!=null)
		pg.createImage(this.legendMap, legend,lengNum,para);
		else
			pg.createImage(this.legendMap, legend,lengNum);
	}*/
	public void myCreateLegend(String location)
	{
		for(int i=0 ; i <legendMap.length; i++)
		{
			for(int j=0; j< legendMap[i].length; j++)
			{
				if(i<10||i>502)
					legendMap[i][j]=-1;
				else{
				float normal =(float) ((i-10)*1.0/(legendMap.length-20));
				legendMap[i][j]=  Color.HSBtoRGB((1f-normal)/1.5f, 0.94f, 0.5f+normal*0.33f);
				}
			}
		}
		
		File legend = new File(location);
		PngWriter pg = new PngWriter();
		//String[] lengNum = new String[7];
		//System.out.println(""+this.max+" "+this.min+" "+(this.max-this.min));
		for(int k = 0 ; k <lengNum.length; k++)
		{
			float inLegend =  this.min+(this.max-this.min)/6f*k;
			DecimalFormat myformat=new DecimalFormat("0.000"); 
			lengNum[k] = (myformat.format(inLegend));
		}
		if(this.para!=null)
		pg.createImage(this.legendMap, legend,lengNum,para);
		else
			pg.createImage(this.legendMap, legend,lengNum);
	}
}
