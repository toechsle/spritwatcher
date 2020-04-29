package com.toechsle.spritwatcher.api;

import com.toechsle.spritwatcher.Spritwatcher;
import com.toechsle.spritwatcher.config.Config;
import com.toechsle.spritwatcher.exceptions.JsonException;
import com.toechsle.spritwatcher.models.GasStation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;


public class TankerkoenigApi {
    
    public enum API_METHOD {

        /**
         * This represents the method for searching gas stations within
         * a given area
         */
        AREA_SEARCH,
        
        /**
         * Detail Search
         */
        DETAILS,
        
         /**
         * Prices Search
         */
        PRICES
               
    }
    
    /**
     * This method returns a list of gas stations
     * around a given lat/lng point.
     * 
     * @param lat Latitude
     * @param lng Longtitude
     * @param radInKm radius we want to search in (given in kilometers)
     * @param types
     * @throws JsonException
     * @return ArrayList of GasStations
     */
    public ArrayList<GasStation> areaSearch(float lat, float lng, float radInKm, String[] types) throws JsonException, IOException {
            
        ArrayList<GasStation> stations = new ArrayList<>();  
        Map<String, ArrayList<String>> parameters = new HashMap<>();
        
        ArrayList<String> latParam = new ArrayList<>();
        latParam.add(String.valueOf(lat));
        
        ArrayList<String> lngParam = new ArrayList<>();
        lngParam.add(String.valueOf(lng));
            
        ArrayList<String> radParam = new ArrayList<>();
        radParam.add(String.valueOf(radInKm));
        
        ArrayList<String> typesParam = new ArrayList<>();
        typesParam.addAll(Arrays.asList(types));
            
        parameters.put("type", typesParam);
        parameters.put("lat", latParam);
        parameters.put("lng", lngParam);
        parameters.put("rad", radParam);
            
        JsonObject jsonObject = doJsonRequest(API_METHOD.AREA_SEARCH, parameters);
            
        JsonArray jsonStations = jsonObject.getJsonArray("stations");
        for(JsonValue jsonStation : jsonStations) {
            JsonObject jsonStationObject = (JsonObject) jsonStation;
            GasStation station = GasStation.buildFromAreaSearchResult(jsonStationObject);
            stations.add(station);
        }

        return stations;
    }
        
    /**
     * This method sends a detail request to the API
     * and extends the model with additional details
     * 
     * @param gasStation
     * @return Extended GasStation with more information
     * @throws JsonException
     * @return GasStation
     */
    public GasStation detailRequest(GasStation gasStation) throws JsonException, IOException {
        
        Map<String, ArrayList<String>> parameters = new HashMap<>();
        
        ArrayList<String> idParam = new ArrayList<>();
        idParam.add(String.valueOf(gasStation.getId()));
        
        ArrayList<String> apiKeyParam = new ArrayList<>();
        apiKeyParam.add(String.valueOf(Config.API_KEY));

        parameters.put("id", idParam);
        parameters.put("apikey", apiKeyParam);

        JsonObject jsonObject = doJsonRequest(API_METHOD.DETAILS, parameters);
        JsonObject jsonStationObject = jsonObject.getJsonObject("station");
        gasStation.extendDetails(jsonStationObject);

        return gasStation;
    }
    
    /**
     * This method builds the request string based on the METHOD we want to call
     * and adds the relevant parameters. Will also check if status is ok.
     * 
     * @param method
     * @param parameters [Parameter name => list of values]
     * @throws JsonException
     * @return JsonObject
     */
    private JsonObject doJsonRequest(API_METHOD method, Map<String, ArrayList<String>> parameters) throws JsonException, IOException {
        
        String methodEndpoint = "";

        switch(method) {
            case AREA_SEARCH:
                methodEndpoint = "list.php";
                break;
            case DETAILS:
                methodEndpoint = "detail.php";
                break;
            case PRICES:
                methodEndpoint = "prices.php";
                break;              
            default:
                throw new UnsupportedOperationException("The method: "
                        .concat(method.toString())
                        .concat(" is not implemented."));
        }
        
        String callParams = buildCallParameters(parameters);
        String endPoint = Config.API_ENDPOINT + "/" + methodEndpoint;

        // Print out some information
        java.util.logging.Logger.getLogger(Spritwatcher.class.getName())
            .log(Level.INFO, "Created request: ".concat(callParams));
    
        JsonObject jsonObject = new JsonRequest().doJsonRequest(endPoint, callParams);
  
        if(!jsonObject.getBoolean("ok")) {
            java.util.logging.Logger.getLogger(Spritwatcher.class.getName())
                .log(Level.INFO, "Sorry, your request returned error: ".concat(jsonObject.getString("message")));
            throw new JsonException();
        }
        
        return jsonObject;
    }
        
    /**
     * This method builds the call parameters string
     * 
     * @param parameters
     * @return url parameters string 
     */
    private String buildCallParameters(Map<String, ArrayList<String>> parameters) {
        
        String apiKey = Config.API_KEY;

        ArrayList<String> apiKeyParam = new ArrayList<String>();
        apiKeyParam.add(apiKey);
        parameters.put("apikey", apiKeyParam);

        String parametersString = "";
        
        for (Map.Entry<String, ArrayList<String>> parameter : parameters.entrySet()) {
            String paramName = parameter.getKey();
            ArrayList<String> values = parameter.getValue();
            
            for(String value : values) {
                parametersString += paramName + "=" + value + "&";
            }
        }
        
        return parametersString;
    }
    
}