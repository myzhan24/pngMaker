package base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    
	public static void parseNetCDFDimensions(String fileName)
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
        	/*
        	for(Dimension d : existingDataFile.getDimensions())
        	{
        		if(d.getShortName().equals("time"))
        			dataFile.addDimension(d.getShortName(), 1 );	
        			
        		else
        			dataFile.addDimension(d.getShortName(), d.getLength() );		
        	}
            
*/
        	
        	System.out.println(existingDataFile.getCacheName());
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
        	
        	

        	// This is the data array we will write. It will just be filled
        	// with a progression of numbers for this example.
           // ArrayFloat.D3 dataOut = new ArrayFloat.D3(  tim.getLength(),lat.getLength(), lon.getLength());

            // Create some pretend data. If this wasn't an example program, we
            // would have some real data to write, for example, model output.
            /*int i,j;
           
            for (i=0; i<xDim.getLength(); i++) {
                 for (j=0; j<yDim.getLength(); j++) {
                     dataOut.set(i,j, i * NY + j);
                 }
            }*/
           // System.out.println(tim.getLength()+" "+lat.getLength()+" "+lon.getLength());
            /*for(int i=0; i < tim.getLength();i++)
            {
            	for(int j= 0 ; j < lat.getLength();j++)
            	{
            		for(int k = 0; k < lon.getLength();k++)
            		{
            			//System.out.println(i+" "+j+" "+k);
            			dataOut.set(i,j,k, sumOfData[j][k]);
            			//System.out.println(sumOfData[j][k]);
            		}
            	}
            }*/
            
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
	public static ArrayFloat.D1 generateAverageArrayD1(String var)
	{
		ArrayFloat.D1 ret = new ArrayFloat.D1(1);
		return ret;
	}
	public static ArrayFloat.D2 generateAverageArrayD2(String var)
	{
		ArrayFloat.D2 ret = new ArrayFloat.D2( latitude, longitude);
		return ret;
	}
	public static ArrayFloat.D3 generateAverageArrayD3(String var)
	{
		ArrayFloat.D3 ret = new ArrayFloat.D3(1, latitude, longitude);
		float total=0;
		//calculate the average of the variable across the total time period
		NetcdfFile dataFile = null;
		try
		{
			int[] shape = new int[3];
			for(String s : fileList)
			{
				dataFile = NetcdfFile.open(s, null);
				//System.out.println(dataFile.toString());
				// Retrieve the variable
				Variable dataVar = dataFile.findVariable(var);
				if (dataVar == null)
				{
					System.out.println("Cant find Variable data " + var);
					return ret;
			    }
			
				// Read dimension information on the variable
				shape = dataVar.getShape();
				ret = new ArrayFloat.D3(1,shape[1], shape[2]);
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
							ret.set(0,j,k, ret.get(0,j,k) + dataArray.get(i,j,k));
						}
					}
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
					ret.set(0,i,j, ret.get(0,i,j)/( total));
					//System.out.print(ret.get(0,i,j)+", \t");
				}
				//System.out.println();
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
	
	public static void writeNetCDFAverageAllVariables(String fileName)
	{
        NetcdfFileWriteable dataFile = new NetcdfFileWriteable();	//Create the file
        NetcdfFile myDataFile = null;								//reference file
        
        try {
        	dataFile = NetcdfFileWriteable.createNew(fileName, false);
        	myDataFile = NetcdfFile.open(fileList.get(0), null);

        	List<Dimension> myList = myDataFile.getDimensions();	//Global Dimensions of the new file
        	int indexOfTime=0;										//determine the index of the time dimension
        	for(Dimension d: myList)
        	{
        	 if(d.getFullName().equals("time"))
        		 break;
        	 indexOfTime++;
        	}
        	
        	myList.get(indexOfTime).setLength(1);					//set the time dimension to 1
        	myList.get(indexOfTime).setUnlimited(false);
            for(Dimension d: myList)								//add the dimensions to the new file
            {
            	dataFile.addDimension(d.getFullName(), d.getLength(), d.isShared(), d.isUnlimited(), d.isVariableLength());
            }
        
            List<Variable> myListV = myDataFile.getVariables();		//add each variable into the new file
            for(Variable var : myListV)             
            {
            	if(var.getShape().length < 3)
            	{
            		
            	}
            	else
            	{
            		var.getDimension(var.findDimensionIndex("time")).setLength(1);
            	}
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
            // create the file
            
            dataFile.create();
            int[] origin = new int[3];
            myListV = dataFile.getVariables();
            for(Variable var : myListV)             
            {
            	
            	if(var.getDimensions().size()==1)
            	{
            		System.out.println(var.getFullName());
            	}
            	else if(var.getDimensions().size()==2)
            	{
            		System.out.println(var.getFullName());
            		//dataFile.write(var.getFullName(),var.getShape(), generateAverageArrayD2(var.getFullName()));
            	}
            	else if(var.getDimensions().size()==3)
            	{
            		dataFile.write(var.getFullName(),origin, generateAverageArrayD3(var.getFullName()));
            	}

            }
            //dataFile.write("latitude",latData);
          //  dataFile.write("longitude",lonData);
            
            // Write the pretend data to the file. Although netCDF supports
            // reading and writing subsets of data, in this case we write all
            // the data in one operation.
            
            //creating the data for latitude and longitude
            
            //for()
           
            
            System.out.println("NetCDF file created! \"test.nc\" contains the averages of variable: "+"var"+".");
            
            
          
           /*  
        	
        	
        	//coordinate attributes
        	dataFile.addVariableAttribute("longitude", "units", "degrees_east");
            dataFile.addVariableAttribute("latitude", "units", "degrees_north");
            
           
           // myList.get(4).setVariableLength(false);
           
        	// define variable dimensions
        	ArrayList<Dimension> dims =  new ArrayList<Dimension>();
        	dims.add(tim);
        	dims.add(lat);
        	dims.add(lon);
        	*/
        	
        	/*
        	for(Dimension d : existingDataFile.getDimensions())
        	{
        		if(d.getShortName().equals("time"))
        			dataFile.addDimension(d.getShortName(), 1 );	
        			
        		else
        			dataFile.addDimension(d.getShortName(), d.getLength() );		
        	}
            
*/
        	/*
        	System.out.println(existingDataFile.getCacheName());
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
        	
        	

        	// This is the data array we will write. It will just be filled
        	// with a progression of numbers for this example.
           // ArrayFloat.D3 dataOut = new ArrayFloat.D3(  tim.getLength(),lat.getLength(), lon.getLength());

            // Create some pretend data. If this wasn't an example program, we
            // would have some real data to write, for example, model output.
            /*int i,j;
           
            for (i=0; i<xDim.getLength(); i++) {
                 for (j=0; j<yDim.getLength(); j++) {
                     dataOut.set(i,j, i * NY + j);
                 }
            }*/
           // System.out.println(tim.getLength()+" "+lat.getLength()+" "+lon.getLength());
            /*for(int i=0; i < tim.getLength();i++)
            {
            	for(int j= 0 ; j < lat.getLength();j++)
            	{
            		for(int k = 0; k < lon.getLength();k++)
            		{
            			//System.out.println(i+" "+j+" "+k);
            			dataOut.set(i,j,k, sumOfData[j][k]);
            			//System.out.println(sumOfData[j][k]);
            		}
            	}
            }*/
            /*
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
*/
            
            
            
            // The file is closed no matter what by putting inside a try/catch block.
            
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
	}
	
	public static void main(String args[])
    {
		fileList = new ArrayList<String>();
		
		String var = null;
		try
		{
			Scanner s = new Scanner(new File(args[0]));
			var=s.nextLine();
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
		
		for(String s : fileList)
		{
		//	parseNetCDFVar(s, var);
		}
		parseNetCDFDimensions(fileList.get(0));

		NetcdfFile myn = null;
		try {
			myn = NetcdfFile.open("test3.nc", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StopWatch myWatch = new StopWatch();
		
		System.out.println(myn.toString());
		/*myWatch.start();
		writeNetCDFAverageAllVariables("test3.nc");
		myWatch.stop();
		System.out.println("=======done!=======");
		System.out.println("compiled and averaged "+fileList.size()+" files, taking "+myWatch.getElapsedTime()/1000.0f+" seconds.");
		*/
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
