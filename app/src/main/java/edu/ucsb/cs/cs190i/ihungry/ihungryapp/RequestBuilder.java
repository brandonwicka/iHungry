package edu.ucsb.cs.cs190i.ihungry.ihungryapp;

/**
 * Created by spencerprescott on 3/6/16.
 */
public class RequestBuilder {
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String filter = null;
    private double radius = 0.0;

    public RequestBuilder() {}

    /**
     * Builds a request to send to Yelp
     * Example usage:
     * Request r = new RequestBuilder().latitude(1.0).longitude(1.0).buildRequest();
     * @return Request
     */
    public Request buildRequest() {
        return new Request(latitude, longitude, radius, filter);
    }

    /**
     * Sets the Request's latitude
     * @param latitude
     * @return RequestBuilder
     */
    public RequestBuilder latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Sets the Request's longitude
     * @param longitude
     * @return RequestBuilder
     */
    public RequestBuilder longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Sets the Request's radius in meters
     * @param radius
     * @return RequestBuilder
     */
    public RequestBuilder radius(double radius) {
        this.radius = radius;
        return this;
    }

    /**
     * Sets the Request's filter term
     * @param filter
     * @return RequestBuilder
     */
    public RequestBuilder filter(String filter) {
        // Convert to Yelp accepted values
        String f = filter.toLowerCase();
        if (f.equals("indian")) {
            f = "indpak";
        }
        if (f.equals("american")) {
            f = "newamerican";
        }
        this.filter = f;
        return this;
    }
}
