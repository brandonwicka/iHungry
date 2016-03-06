package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

/**
 * Created by spencerprescott on 3/6/16.
 */
public class Request {
    public double latitude;
    public double longitude;
    public String filter;
    public double radius;

    public Request(double latitude,
                   double longitude,
                   double radius,
                   String filter){
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.filter = filter;
    }
}
