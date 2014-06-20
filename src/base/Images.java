package base;
import java.util.ArrayList;
import java.util.List;
/*
 * Daily outputs
	Models
		Parameters
			Spatial Resolution	
				Temporal Resolution
					Spatial coverage(Merge)
						Sorted by date

 */

public class Images 
{
	  public ArrayList<image> imageArray= new ArrayList<image>();
      public double north=0.0;
      public double south=0.0;
      public double east=0.0;
      public double west=0.0;
      
  public Images(double north,double south,double east,double west)
  {
	  this.north=north;
	  this.south=south;
	  this.east=east;
	  this.west=west;
  }
public Images()
{}

  
  public void AddImage(String url, String beginDate, String endDate)
  {
	  this.imageArray.add(new image(url,beginDate,endDate));
  }

  public class image
  {
	 public String url;
	 public String beginDate;
	 public String endDate;
	public  image(String url, String beginDate, String endDate)
	  {
		  this.url=url;
		  this.beginDate=beginDate;
		  this.endDate=endDate; 
	  }
  }
  
}
