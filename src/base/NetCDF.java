package base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.dt.grid.GridDataset;
import ucar.ma2.*;
import ucar.ma2.ArrayFloat.D1;
import ucar.ma2.ArrayFloat.D2;
import ucar.ma2.ArrayFloat.D3;

@SuppressWarnings("deprecation")
public class NetCDF
{
	static int latitude; //Latitude
    static int longitude; //Longitude 
    static int time = 12; //Time
    
    static ArrayFloat.D3 sumOfData;
    static ArrayFloat.D1 lonData;
    static ArrayFloat.D1 latData;
    static ArrayList<String> fileList;
    static int total;
    
    public static void readNetCDF(String fileName, String var)
    {
    	NetcdfFile dataFile = null;
		try
		{
			dataFile = NetcdfFile.open(fileName, null);
			System.out.println(dataFile.toString());
			// Retrieve the variable
			Variable dataVar = dataFile.findVariable(var);
			if (dataVar == null)
			{
				System.out.println("Cant find Variable data");
		        return;
		    }
		
			// Read dimension information on the variable
			int [] shape = dataVar.getShape();
			int[] origin = new int[3];
		
			ArrayFloat.D3 dataArray;
			dataArray = (ArrayFloat.D3) dataVar.read(origin, shape);
			
			//float[][][] dataArray = new float[shape[0]][shape[1]][shape[2]];
		
			
			for(int i = 0; i<shape[0];i++)	//time
			{
				for(int j =0; j < shape[1]; j++)
				{
					System.out.print("[");
					for(int k = 0; k < shape[2];k++)
					{
						System.out.print(""+dataArray.get(i,j,k)+", \t");
					}
					System.out.println("]");
				}
				System.out.println("");
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
    
	public static void parseDimensions(String fileName)
	{
		NetcdfFile dataFile = null;
		try
		{
			dataFile = NetcdfFile.open(fileName, null);
			// Retrieve the variable
			Variable lonVar = dataFile.findVariable("longitude");
			Variable latVar = dataFile.findVariable("latitude");
			
			if (lonVar == null)
			{
				System.out.println("Cant find longitude data");
		        return;
		    }
			if (latVar == null)
			{
				System.out.println("Cant find latitude data");
		        return;
		    }
		
			// Read dimension information on the variable
			int[] origin = new int[1];
		
			latData = (ArrayFloat.D1) latVar.read(origin, latVar.getShape());	
			lonData = (ArrayFloat.D1) lonVar.read(origin, lonVar.getShape());
			latitude=(int)latData.getSize();
			longitude = (int) lonData.getSize();
			
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
	
	
	/**
	 * old function
	 * @param fileName
	 * @param var
	 */
    public static void parseNetCDFVar(String fileName, String var)
	{
		NetcdfFile dataFile = null;
		try
		{
			dataFile = NetcdfFile.open(fileName, null);
			//System.out.println(dataFile.toString());
			// Retrieve the variable
			Variable dataVar = dataFile.findVariable(var);
			if (dataVar == null)
			{
				System.out.println("Cant find Variable data");
		        return;
		    }
		
			// Read dimension information on the variable
			int [] shape = dataVar.getShape();
			int[] origin = new int[3];
		
			ArrayFloat.D3 dataArray;
			dataArray = (ArrayFloat.D3) dataVar.read(origin, shape);
			
			
			//float[][][] dataArray = new float[shape[0]][shape[1]][shape[2]];
			
		
			total+=time;
			//System.out.println(shape[0]+" "+shape[1]+" "+shape[2]);
			for(int i = 0; i< shape[0];i++)	//time
			{
				for(int j =0; j < shape[1]; j++) //latitude
				{
					for(int k = 0; k < shape[2];k++) //longitude
					{
						sumOfData.set(0,j,k, sumOfData.get(0,j,k) + dataArray.get(i,j,k));
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
    /**
     * writeNetCDFAverage
     * a skeleton function that writes a new NetCDF file that only contains the average data of the specified variable over the time dimension
     * @param fileName
     * @param var
     */
	public static void writeNetCDFAverage(String fileName, String var)
	{
		// Create the file.
        NetcdfFileWriteable dataFile = null;
        NetcdfFile existingDataFile = null;
        
        try {
        	dataFile = NetcdfFileWriteable.createNew(fileName, false);

        	// Create netCDF dimensions
        	// as well as the coordinate variables
        	Dimension lon = dataFile.addDimension("longitude", longitude );
        	Dimension lat = dataFile.addDimension("latitude", latitude );
        	Dimension tim = dataFile.addDimension("time",1);
        	
        	
        	dataFile.addVariable("latitude",DataType.FLOAT,new Dimension[] {lat});
        	dataFile.addVariable("longitude",DataType.FLOAT,new Dimension[] {lon});
        	//coordinate attributes
        	dataFile.addVariableAttribute("longitude", "units", "degrees_east");
            dataFile.addVariableAttribute("latitude", "units", "degrees_north");
            
        	//Dimension lon = new Dimension("longitude", longitude );
        	//Dimension lat = new Dimension("latitude", latitude );
        	//Dimension tim = new Dimension("time",1);
	
        	// define variable dimensions
        	ArrayList<Dimension> dims =  new ArrayList<Dimension>();
        	dims.add(tim);
        	dims.add(lat);
        	dims.add(lon);
        	
        	existingDataFile = NetcdfFile.open(fileList.get(0), null);

            // add the variable into the new netcdf
        	dataFile.addVariable(var, DataType.FLOAT, dims);
        	
        	//adds the variable attributes that are present in the given netCDF
        	Variable dataVar = existingDataFile.findVariable(var);
        	for(Attribute s:dataVar.getAttributes())
        	{
        		if(s.getDataType() == DataType.STRING)
        			dataFile.addVariableAttribute(var, s.getShortName(), s.getStringValue());
        		
        		else
        			dataFile.addVariableAttribute(var, s.getShortName(),s.getNumericValue());
        	}
        	
            for(Dimension d : existingDataFile.getDimensions())
        	{
            	try
            	{
        			dataFile.addDimension(d.getShortName(), d.getLength() );	
            	}
            	catch(Exception e)
            	{
            		//System.out.println("duplicate dim: "+d.getShortName());
            	}
        	}
            
            // create the file
            dataFile.create();

            
            dataFile.write("latitude",latData);
            dataFile.write("longitude",lonData);
            
            // Write the pretend data to the file. Although netCDF supports
            // reading and writing subsets of data, in this case we write all
            // the data in one operation.
            
            //creating the data for latitude and longitude
            int[] origin = new int[3];
            dataFile.write(var,origin, sumOfData);
            
            System.out.println("NetCDF file created! \"test.nc\" contains the averages of variable: "+var+".");

            
            
            
            // The file is closed no matter what by putting inside a try/catch block.
            
        	}
        	
        	catch (IOException e) 
        	{
		       e.printStackTrace();
        	} 
        	catch (InvalidRangeException e) 
        	{
		       e.printStackTrace();
        	} 
        	finally 
        	{
        		if (null != dataFile)
        			try 
        			{
    					dataFile.close();
        			} 
        			catch (IOException ioe) 
        			{
        				ioe.printStackTrace();
        			}
        		if (null != existingDataFile)
        			try 
        			{
        				existingDataFile.close();
        			} 
        			catch (IOException ioe) 
        			{
        				ioe.printStackTrace();
        			}
        	}
	}
	/**
	 * calculateMean
	 * Calculates the mean value of a 3D float array
	 * @param ar	ArrayFloat.D3 array of values
	 * @return		the mean as a float
	 */
	public static float calculateMean(ArrayFloat.D3 ar)
	{
		float ret = 0f;
		int[] shape = ar.getShape();
		float total = shape[0] * shape[1] * shape[2];
		
		for(int i = 0; i < shape[0];i++)
		{
			for(int j = 0; j < shape[1] ; j++)
			{
				for(int k = 0; k < shape[2]; k++)
				{
					ret+=ar.get(i,j,k);
				}
			}
		}
		return ret/total;
	}
	
	public static float calculateMean(ArrayFloat.D2 ar)
	{
		float ret = 0f;
		int[] shape = ar.getShape();
		float total = shape[0] * shape[1];
		
		for(int i = 0; i < shape[0];i++)
		{
			for(int j = 0; j < shape[1] ; j++)
			{			
				ret+=ar.get(i,j);
			}
		}
		return ret/total;
	}
	
	public static float calculateAreaWeightedMean(ArrayFloat.D2 data, ArrayFloat.D1 areas)
	{
		float ret = 0f;
		float areaSum =0f;
		int[] dataShape = data.getShape();
		
		for(int i=0; i < dataShape[0]; i++)			//latitude
		{
			for(int j= 0; j < dataShape[1]; j++)	//longitude
			{
				ret+= data.get(i,j) * areas.get(i); //sum of DATAij * AREAi; data and corresponding area weight of latitude amount
				areaSum+= areas.get(i);
			}
		}
		
		return ret/areaSum;
		
		
	}
	public static float calculateStandardDeviation(ArrayFloat.D2 ar, float mean)
	{
		float ret= 0;
		int[] shape = ar.getShape();
		for(int i = 0; i < shape[0] ; i++)
		{
			for(int j = 0 ; j < shape[1] ; j++)
			{
				ret += (ar.get(i,j) - mean)*(ar.get(i,j) - mean);
			}
		}
		ret = ret/((float)shape[0]*shape[1]);
		ret = (float) Math.sqrt(ret);
		return ret;
	}
	
	public static ArrayFloat.D1 generateArrayD1(String var)
	{
		NetcdfFile dataFile = null;
		ArrayFloat.D1 ret = null;
		try
		{
			int[] origin = new int[1];
			dataFile = NetcdfFile.open(fileList.get(0));
			Variable dataVar = dataFile.findVariable(var);
			if (dataVar == null)
			{
				System.out.println("Cant find Variable data " + var);
				return ret;
		    }
			ret = (ArrayFloat.D1) dataVar.read(origin, dataVar.getShape());
		}
		catch(Exception e)
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
		return ret;
	}
	public static ArrayFloat.D2 generateArrayD2(String var)
	{
		NetcdfFile dataFile = null;
		ArrayFloat.D2 ret = null;
		try
		{
			int[] origin = new int[2];
			dataFile = NetcdfFile.open(fileList.get(0));
			Variable dataVar = dataFile.findVariable(var);
			//System.out.println(dataVar.read(origin,dataVar.getShape()).toString());
			if (dataVar == null)
			{
				System.out.println("Cant find Variable data " + var);
				return ret;
		    }
			ret = (ArrayFloat.D2) dataVar.read(origin, dataVar.getShape());
		}
		catch(Exception e)
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
		return ret;
	}
	/**
	 * This function searches each file in filelist.txt for the given variable name
	 * and averages the values over the time dimension, returning a new 3D array with 
	 * a time dimension of 1 and retains the old latitude and longitude dimensions.
	 * @param var 	input variable to be averaged
	 * @return ArrayFloat.D3 that averaged the values over the time dimension. Dimensions: 1 x latitude x longitude 
	 */
	public static ArrayFloat.D3 generateAverageArrayD3(String var)
	{
		ArrayFloat.D3 ret =new ArrayFloat.D3(1, latitude, longitude);
		float total=0;
		//calculate the average of the variable across the total time period
		NetcdfFile dataFile = null;
		try
		{
			int[] shape = new int[3];
			dataFile = NetcdfFile.open(fileList.get(0),null);
			Variable dataVar = dataFile.findVariable(var);
			if (dataVar == null)
			{
				System.out.println("Cant find Variable data " + var);
				return ret;
		    }
			shape = dataVar.getShape();
			ret = new ArrayFloat.D3(1, shape[1], shape[2]);
			
			for(String s : fileList)
			{
				dataFile = NetcdfFile.open(s, null);
				//System.out.println(s);
				// Retrieve the variable
				dataVar = dataFile.findVariable(var);
				if (dataVar == null)
				{
					System.out.println("Cant find Variable data " + var);
					return ret;
			    }
			
				// Read dimension information on the variable
				//ret = new ArrayFloat.D3(1,shape[1], shape[2]);
				int[] origin = new int[3];
				ArrayFloat.D3 dataArray;
				dataArray = (ArrayFloat.D3) dataVar.read(origin, shape);
				//float[][][] dataArray = new float[shape[0]][shape[1]][shape[2]];
				total+=time;
				//System.out.println(shape[0]+" "+shape[1]+" "+shape[2]);
				//System.out.println(total);
				for(int i = 0; i< shape[0];i++)	//time
				{
					for(int j =0; j < shape[1]; j++) //latitude
					{
						for(int k = 0; k < shape[2];k++) //longitude
						{
							ret.set(0,j,k, ret.get(0,j,k)+dataArray.get(i,j,k));
						}
					}
					//System.out.println(i+" the month"+ " "+total);
				}/*
				System.out.println(s);
				System.out.println(dataVar.getFullName());
				System.out.println(dataVar.getDimensionsString());
				System.out.println(dataVar.getDimensions().toString());
				System.out.println("*******");
				*/			
			}
			for(int i = 0; i < shape[1];i++)
			{
				for(int j = 0; j < shape[2]; j++)
				{
					ret.set(0,i,j, ret.get(0,i,j)/(total));
					//System.out.print(ret.get(0,i,j)+", \t");
				}
				//System.out.println(total);
			}
				
		} 
		catch (java.io.IOException e) 
		{
		        e.printStackTrace();
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
		return ret;
		
	}
	
	/**
	 * writeNetCDFAverageArray
	 * This function creates a new NetCDF file with fileName as the output file name, currently this requires that the input includes ".nc" at the end.
	 * This function averages all variables for all files present in filelist.txt over its time dimension as long as the variable has a time dimension.
	 * @param fileName	output filename, requires ".nc" to be present for a correct output file format
	 */
	public static void writeNetCDFAverageAllVariables(String fileName)
	{
        NetcdfFileWriteable dataFile = new NetcdfFileWriteable();	//Create the file
        NetcdfFile myDataFile = null;								//reference file
        
        try {
        	dataFile = NetcdfFileWriteable.createNew(fileName, false);
        	myDataFile = NetcdfFile.open(fileList.get(0), null);
        	
        	//Define DIMENSIONS
        	List<Dimension> myListD = myDataFile.getDimensions();	//Global Dimensions of the new file
        	int indexOfTime=0;										//determine the index of the time dimension
        	for(Dimension d: myListD)
        	{
        	 if(d.getFullName().equals("time"))
        		 break;
        	 indexOfTime++;
        	}
        	
        	myListD.get(indexOfTime).setLength(1);					//set the time dimension to 1
        	myListD.get(indexOfTime).setUnlimited(false);			//Must change time to be limited so that dimensions agree with calculations later
            for(Dimension d: myListD)								//add the dimensions to the new file
            {
            	dataFile.addDimension(d.getFullName(), d.getLength(), d.isShared(), d.isUnlimited(), d.isVariableLength());
            }
            
           
            
            List<Variable>myListV = myDataFile.getVariables();		//add each variable into the new file
  
            //Define VARIABLES
            for(Variable var : myListV)             
            {
            	
            	if(var.getShape().length < 3 )
            	{
            		if((var.findDimensionIndex("time"))!=-1)
            		{
            			var.getDimension(var.findDimensionIndex("time")).setLength(12);			//????? not sure how this works because it does not show up in the final netCDF file
            			//System.out.println(var.getFullName()+ " the time was set");			//however it matches the dimensions when copying array values
            		}
            	}
            	else
            		var.getDimension(var.findDimensionIndex("time")).setLength(1);
            	
            	dataFile.addVariable(var.getFullName(), var.getDataType(), var.getDimensions());
            	 
             	for(Attribute s : var.getAttributes())				//adds the variable attributes that are present in the given netCDF
             	{
             		if(s.getDataType() == DataType.STRING)			//String value template
             		{
             			dataFile.addVariableAttribute(var.getFullName(), s.getShortName(), s.getStringValue());
             			//System.out.println(var.getFullName() + "\t" +  s.getShortName()+ "\t" +s.getStringValue());
             		}  		
             		else											//Numeric value template
             		{
             			try	
             			{
             				dataFile.addVariableAttribute(var.getFullName(), s.getShortName(), s.getNumericValue());
             			}
             			catch(Exception e)							//if the data value is null, then place -9999 instead
             			{
             				dataFile.addVariableAttribute(var.getFullName(), s.getShortName(), -9999f);
             			}
             			//System.out.println(var.getFullName() + "\t" +  s.getShortName()+ "\t" +s.getNumericValue());
             		}
             	}  	
            }
          
            //Define GLOBAL ATTRIBUTES
            List<Attribute> AL = myDataFile.getGlobalAttributes();	//first, model all the global attributes off the first file in the filelist. Then, append the values for each attribute, separated by "; "
            List<Attribute> tempAL = null;							
            for(int i = 1 ; i < fileList.size() ; i++)				//skip over the first file since it is the base for the list
            {
            	myDataFile = NetcdfFile.open(fileList.get(i),null);	
            	tempAL = myDataFile.getGlobalAttributes();	
            	for(int j = 0 ; j < AL.size(); j++)					//iterate through each attribute for the AL and tempAL
            	{
            		Attribute a = AL.get(j);
            		Attribute b = tempAL.get(j);
            		if(a.getDataType()==DataType.STRING)    		//look to append attributes that hold strings		
            		{
            			if(a.getStringValue().equals(b.getStringValue()))	//if the values are the same for the attributes, nothing is appended
            			{
            				
            			}
            			else									//append the two attributes
            				AL.set(j, new Attribute(a.getFullName(), a.getStringValue() +"; "+ b.getStringValue())); 
            		}
            		else
            		{
            			if(a.getNumericValue().equals(b.getNumericValue()))	//similar procedure above, however the numeric values are preserved as strings if more than one different value exists
            			{
            				
            			}
            			else
            				AL.set(j, new Attribute(a.getFullName(), a.getNumericValue().toString() + "; "+b.getNumericValue().toString()));
            		}
            	}
            }
            for(Attribute a : AL)									//add the new Attribute List to the output file
        	{
        		dataFile.addGlobalAttribute(a);
        	}
        	
            // create the file; definition phase is over, writing phase begins 
            dataFile.create();
            
  
            
            int[] origin = new int[3];
            myListV = dataFile.getVariables();
            for(Variable var : myListV)             
            {  	
            	if(var.getDimensions().size()==1)
            	{
            		//System.out.println("size 1: "+var.getFullName()+" shape: "+var.getShape()[0]);
            		origin = new int[1];
            		dataFile.write(var.getFullName(), generateArrayD1(var.getFullName()));
            		
            	}
            	else if(var.getDimensions().size()==2)
            	{
            		//System.out.println("size 1: "+var.getFullName()+" shape: "+var.getShape()[0]+" "+var.getShape()[1]);
            		//dataFile.write(var.getFullName(),var.getShape(), generateAverageArrayD2(var.getFullName()));
            		origin = new int[2];
            		dataFile.write(var.getFullName(), generateArrayD2(var.getFullName()));
            	}
            	else if(var.getDimensions().size()==3)
            	{
            		origin = new int[3];
            		//System.out.println("size 3: "+var.getFullName());
            		dataFile.write(var.getFullName(), origin, generateAverageArrayD3(var.getFullName()));
            	}
            }
        }
    	catch (IOException | InvalidRangeException e) 
    	{
	       e.printStackTrace();
    	} 
    	finally 
    	{
    		if (null != dataFile)
    			try 
    			{
					dataFile.close();
    			} 
    			catch (IOException ioe) 
    			{
    				ioe.printStackTrace();
    			}
    		if (null != myDataFile)
    			try 
    			{
    				myDataFile.close();
    			} 
    			catch (IOException ioe) 
    			{
    				ioe.printStackTrace();
    			}
    	}
        System.out.println("Compiled and averaged "+fileList.size()+" files");
	}
	
	
	public static void printMeanByMonth(String fileName, String var, int month)
	{
		NetcdfFile dataFile = null;
		try
		{
			if(month <0 || month > 11)
			{
				System.out.println("Invalid Month ("+month+"). Could not print mean of ("+var+").");
				return;
			}
			dataFile = NetcdfFile.open(fileName);
			Variable dataVar = dataFile.findVariable(var);
			if(dataVar==null)
			{
				System.out.println("Variable not found. Could not print mean of ("+var+").");
				return;
			}
			
			int[] origin = new int[dataVar.getShape().length];
			ArrayFloat.D3 dataArray = (D3) dataVar.read(origin, dataVar.getShape());
			ArrayFloat.D2 varData = new ArrayFloat.D2(dataVar.getShape(1), dataVar.getShape(2));
			for(int i=0; i < dataVar.getShape(1); i++)
			{
				for(int j=0; j < dataVar.getShape(2); j++)
				{
					varData.set(i, j, dataArray.get(month, i, j));
				}
			}
			System.out.println(varData);
			String monthString;
			switch(month)
			{
			case 0: monthString= "January";
					break;	
			case 1: monthString= "February";
					break;	
			case 2: monthString= "March";
					break;
			case 3: monthString= "April";
					break;
			case 4: monthString= "May";
					break;
			case 5: monthString= "June";
					break;
			case 6: monthString= "July";
					break;
			case 7: monthString= "August";
					break;
			case 8: monthString= "September";
					break;
			case 9: monthString= "October";
					break;
			case 10: monthString= "November";
					break;
			case 11: monthString= "December";
					break;
			default: monthString= "Invalid Month";
					break;
			}
			try
			{		
				String units = dataVar.findAttribute("units").getStringValue();
				System.out.println("Mean of ("+var+") in "+monthString+": "+calculateMean(varData)+" "+units);
			}
			catch(Exception e)
			{
				System.out.println("Mean of ("+var+") in "+monthString+": "+calculateMean(varData));
			}
			
		}
		catch (IOException | InvalidRangeException e) 
    	{
	       e.printStackTrace();
    	} 
    	finally 
    	{
    		if (null != dataFile)
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
	public static void printStandardDeviationByMonth(String fileName, String var, int month)
	{
		NetcdfFile dataFile = null;
		try
		{
			if(month <0 || month > 11)
			{
				System.out.println("Invalid Month ("+month+"). Could not print standard deviation of ("+var+").");
				return;
			}
			dataFile = NetcdfFile.open(fileName);
			Variable dataVar = dataFile.findVariable(var);
			if(dataVar==null)
			{
				System.out.println("Variable not found. Could not print standard deviation of ("+var+").");
				return;
			}
			Variable areaVar = dataFile.findVariable("area");
			if(areaVar==null)
			{
				System.out.println("Could not find variable (area), the weighted mean could not be computed.");
				return;
			}
			int[] origin = new int[areaVar.getShape().length];
			ArrayFloat.D2 areaDataArray = (D2) areaVar.read(origin, areaVar.getShape());
			ArrayFloat.D1 areaData = new ArrayFloat.D1(areaVar.getShape(1));				//latitude
			for(int i=0; i < areaVar.getShape(1);i++)
			{
				areaData.set(i, areaDataArray.get(0,i));
			}
			
			origin = new int[dataVar.getShape().length];
			ArrayFloat.D3 dataArray = (D3) dataVar.read(origin, dataVar.getShape());
			ArrayFloat.D2 varData = new ArrayFloat.D2(dataVar.getShape(1), dataVar.getShape(2));
			for(int i=0; i < dataVar.getShape(1); i++)
			{
				for(int j=0; j < dataVar.getShape(2); j++)
				{
					varData.set(i, j, dataArray.get(month, i, j));
				}
			}
			String monthString;
			switch(month)
			{
			case 0: monthString= "January";
					break;	
			case 1: monthString= "February";
					break;	
			case 2: monthString= "March";
					break;
			case 3: monthString= "April";
					break;
			case 4: monthString= "May";
					break;
			case 5: monthString= "June";
					break;
			case 6: monthString= "July";
					break;
			case 7: monthString= "August";
					break;
			case 8: monthString= "September";
					break;
			case 9: monthString= "October";
					break;
			case 10: monthString= "November";
					break;
			case 11: monthString= "December";
					break;
			default: monthString= "Invalid Month";
					break;
			}
			float wMean = calculateAreaWeightedMean(varData, areaData);
			try
			{		
				String units = dataVar.findAttribute("units").getStringValue();
				System.out.println("Standard Deviation of ("+var+") in "+monthString+": "+calculateStandardDeviation(varData, wMean)+" "+units);
			}
			catch(Exception e)
			{
				System.out.println("Standard Deviation of ("+var+") in "+monthString+": "+calculateStandardDeviation(varData, wMean));
			}
			
		}
		catch (IOException | InvalidRangeException e) 
    	{
	       e.printStackTrace();
    	} 
    	finally 
    	{
    		if (null != dataFile)
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
	
	
	
	public static void printWeightedMeanByMonth(String fileName, String var, int month)
	{
		NetcdfFile dataFile = null;
		try
		{
			if(month <0 || month > 11)
			{
				System.out.println("Invalid Month ("+month+"). Could not print mean of ("+var+").");
				return;
			}
			dataFile = NetcdfFile.open(fileName);
			Variable dataVar = dataFile.findVariable(var);
			if(dataVar==null)
			{
				System.out.println("Variable not found. Could not print mean of ("+var+").");
				return;
			}
			Variable areaVar = dataFile.findVariable("area");
			if(areaVar==null)
			{
				System.out.println("Could not find variable (area), the weighted mean could not be computed.");
				return;
			}
			int[] origin = new int[areaVar.getShape().length];
			ArrayFloat.D2 areaDataArray = (D2) areaVar.read(origin, areaVar.getShape());
			ArrayFloat.D1 areaData = new ArrayFloat.D1(areaVar.getShape(1));				//latitude
			for(int i=0; i < areaVar.getShape(1);i++)
			{
				areaData.set(i, areaDataArray.get(0,i));
			}

			origin = new int[dataVar.getShape().length];
			ArrayFloat.D3 dataArray = (D3) dataVar.read(origin, dataVar.getShape());
			ArrayFloat.D2 varData = new ArrayFloat.D2(dataVar.getShape(1), dataVar.getShape(2));
			for(int i=0; i < dataVar.getShape(1); i++)
			{
				for(int j=0; j < dataVar.getShape(2); j++)
				{
					varData.set(i, j, dataArray.get(month, i, j));
				}
			}
			//System.out.println(varData);
			String monthString;
			switch(month)
			{
			case 0: monthString= "January";
					break;	
			case 1: monthString= "February";
					break;	
			case 2: monthString= "March";
					break;
			case 3: monthString= "April";
					break;
			case 4: monthString= "May";
					break;
			case 5: monthString= "June";
					break;
			case 6: monthString= "July";
					break;
			case 7: monthString= "August";
					break;
			case 8: monthString= "September";
					break;
			case 9: monthString= "October";
					break;
			case 10: monthString= "November";
					break;
			case 11: monthString= "December";
					break;
			default: monthString= "Invalid Month";
					break;
			}
			try
			{		
				String units = dataVar.findAttribute("units").getStringValue();
				System.out.println("Area Weighted Mean of ("+var+") in "+monthString+": "+calculateAreaWeightedMean(varData, areaData)+" "+units);
			}
			catch(Exception e)
			{
				System.out.println("Area Weighted Mean of ("+var+") in "+monthString+": "+calculateAreaWeightedMean(varData, areaData)+" (units not found)");
			}
			
		}
		catch (IOException | InvalidRangeException e) 
    	{
	       e.printStackTrace();
    	} 
    	finally 
    	{
    		if (null != dataFile)
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
	
	
	public static void main(String args[])
    {
		fileList = new ArrayList<String>();
		
		String var = null;
		try
		{
			Scanner s = new Scanner(new File(args[0]));
			while(s.hasNextLine())
			{
				fileList.add(s.nextLine());
			}
			s.close();
			
			if(fileList.size()==0)
				System.out.println("filelist.txt is empty");
			
		}
		catch(Exception e)
		{
			System.out.println("Could not read filelist.txt");
		}
		
		for(int i=0;i<12;i++)
		{
			printStandardDeviationByMonth(fileList.get(0),"tgrnd",i);
		}
		for(int i=0;i<12;i++)
		{
			printWeightedMeanByMonth(fileList.get(0),"tgrnd",i);
		}
	

		
		//parseDimensions(fileList.get(0));

		//StopWatch myWatch = new StopWatch();
		

		//myWatch.start();
		//writeNetCDFAverageAllVariables("test5.nc");
		//myWatch.stop();
	//	System.out.println("=======done!=======");
		
		/*
		try
		{
			NetcdfFile myn = NetcdfFile.open(fileList.get(0), null);
			System.out.println(myn.toString());
			
			Variable v = myn.findVariable("tgrnd");
		
		}
		catch(Exception e)
		{
			
		}*/
		
		//System.out.println(myn.toString());
		//System.out.println(myn.getGlobalAttributes());
		/*
		for(Attribute e : myn.getGlobalAttributes())
		{
			System.out.println(e.getFullName());
			System.out.println(e.getStringValue());
		}*/
		//writeNetCDFAverage("test2.nc",var);
		//addGeo2D(fileList.get(0));
		//readNetCDF("test.nc",var);
		
		
	
		
    }
}
