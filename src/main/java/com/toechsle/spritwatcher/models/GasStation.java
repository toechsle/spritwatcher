package com.toechsle.spritwatcher.models;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;


public class GasStation {
    
    private String id;
    private String street;
    private String place;
    private int zip;
    private String houseNumber;
    private String stationName;
    private String brandName;
    private double lat;
    private double lon;
    private double priceE10;
    private double priceDiesel;
    private double priceE5;
    private List<String> openingTimes = new ArrayList<>();
    private boolean isWholeDayOpened;

    
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getPriceE10() {
        return priceE10;
    }
    public void setPriceE10(double priceE10) {
        this.priceE10 = priceE10;
    }

    public double getPriceDiesel() {
        return priceDiesel;
    }
    public void setPriceDiesel(double priceDiesel) {
        this.priceDiesel = priceDiesel;
    }

    public double getPriceE5() {
        return priceE5;
    }
    public void setPriceE5(double priceE5) {
        this.priceE5 = priceE5;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        if (street == null){ 
            return "Straßenname unbekannt";
        }
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public boolean isWholeDayOpened() {
        return isWholeDayOpened;
    }
    public void setIsWholeDayOpened(boolean isWholeDayOpened) {
        this.isWholeDayOpened = isWholeDayOpened;
    }
    
    public String getOeffZeiten() {
        if (isWholeDayOpened) {
            return "ganztägig geöffnet"; 
        }
        else {
           String oeffZeiten = createOpeningTimesString();
           return oeffZeiten;
        }
    }

    public List<String> getOpeningTimes() {
        return openingTimes;
    }

    
    /**
     * This factory method produces a GasStation object
     * based on a station JSON object
     * */
    public static GasStation buildFromAreaSearchResult(JsonObject obj) {
        
        GasStation station = new GasStation();
       
        station.setBrandName(obj.getString("brand"));
        station.setStationName(obj.getString("name"));
        station.setLat(obj.getJsonNumber("lat").doubleValue());
        station.setLon(obj.getJsonNumber("lng").doubleValue());
       
        // If we have the "price" keyword, we searched for a specific price.
        // Since (here) we don't know which one, we set every fuel price
        // to the given value
        if (obj.containsKey("price") && !obj.isNull("price")) {
            station.setPriceDiesel(obj.getJsonNumber("price").doubleValue());
            station.setPriceE10(obj.getJsonNumber("price").doubleValue());
            station.setPriceE5(obj.getJsonNumber("price").doubleValue());
        }
        
        if(obj.containsKey("diesel") && !obj.isNull("diesel")) 
            station.setPriceDiesel(obj.getJsonNumber("diesel").doubleValue());
        
        if(obj.containsKey("e10") && !obj.isNull("e10")) 
            station.setPriceE10(obj.getJsonNumber("e10").doubleValue());
        
        if(obj.containsKey("e5") && !obj.isNull("e5")) 
            station.setPriceE5(obj.getJsonNumber("e5").doubleValue());
        
        station.setZip(obj.getInt("postCode"));
        station.setStreet(obj.getString("street"));
        station.setHouseNumber(obj.getString("houseNumber"));
        station.setPlace(obj.getString("place"));
        station.setId(obj.getString("id"));
        
        return station;
    }

    /**
     * This method adds information to the model based on data from an api request
     * 
     * @param details represents a station
     */
    public GasStation extendDetails(JsonObject details) {
        
        //retrieve from the JSON object the value of "wholeDay" and set the attribute "isWholeDayOpened" accordingly:
        boolean isWholeDayOpened = details.getBoolean("wholeDay");
        this.setIsWholeDayOpened(isWholeDayOpened);

        //retrieve from the JSON object the openingTimes array and add the value of each entry to the list "openingTimes":
        JsonArray openingTimesArray = details.getJsonArray("openingTimes");
        for (JsonValue item : openingTimesArray) {
           JsonObject jsonObject = (JsonObject) item;
           
           String when = jsonObject.getString("text");
           String start = jsonObject.getString("start");
           String end = jsonObject.getString("end");
           
           this.addOpeningTimes(when, start, end);
        }
        
        //return the gas station (which now includes more information):
        return this;
    }

    /**
     * This method generates a JSON String based on this class
     * @return JSON representation
     */
    public String toJsonString() {
        
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder objBuilder = factory.createObjectBuilder();
        
        objBuilder.add("id", getId());
        objBuilder.add("street", getStreet());
        objBuilder.add("place", getPlace());
        objBuilder.add("zip", getZip());
        objBuilder.add("houseNumber", getHouseNumber());
        objBuilder.add("stationName", getStationName());
        objBuilder.add("brandName", getBrandName());
        objBuilder.add("lat", getLat());
        objBuilder.add("lon", getLon());
        objBuilder.add("priceE10", getPriceE10()); 
        objBuilder.add("priceDiesel", getPriceDiesel()); 
        objBuilder.add("priceE5", getPriceE5());
        
        JsonObject object = objBuilder.build();
        String strObj = object.toString();
        
        return strObj;
    }


    private String createOpeningTimesString() {
        String times = "";
        for(String time : openingTimes){
            if (time == openingTimes.get(openingTimes.size()-1)) {
                times += time;
            }
            else {
                times += time + ", ";
            }
        }
        return times;
    }

    /**
     * This method adds an opening time to the list openingTimes
     *
     * @param when key of the opening times (e.g. "Mo-Do")
     * @param start start time
     * @param end  end time
     */
    private void addOpeningTimes(String when, String start, String end) {
        this.openingTimes.add(when + ": "+ start + " bis " + end);
    }
    
}

    


