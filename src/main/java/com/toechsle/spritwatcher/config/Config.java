
package com.toechsle.spritwatcher.config;



public final class Config {
    
    /**
     * This String holds our API key (obtained here: https://creativecommons.tankerkoenig.de)
     */
    public static final String API_KEY = "YourAPIKey";
    
    /**
     * This is the address where our API is situated. The server accepts REQUESTS
     * at that URL. 
     */
    public static final String API_ENDPOINT = "https://creativecommons.tankerkoenig.de/json";
    
    
    public static final String DB_NAME = "tankerkoenig"; 
    
    public static final String DB_COLLECTION = "pricesInfo"; 
    
    public static final String MONGO_CLIENT_URI = "YourConnectionString"; 
    
    
}

