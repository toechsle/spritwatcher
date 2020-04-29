package com.toechsle.spritwatcher;

import com.toechsle.spritwatcher.api.TankerkoenigApi;
import com.toechsle.spritwatcher.exceptions.JsonException;
import com.toechsle.spritwatcher.models.GasStation;
import java.io.IOException;
import java.util.ArrayList;


public class Spritwatcher {
    
    /**
     * Main entry point.
     * 
     * @param args the command line arguments
     * @throws Exceptions.JsonException
     */
    public static void main(String[] args) throws JsonException, IOException {

        TankerkoenigApi api = new TankerkoenigApi();
        
        // Start an areaSearch to get gas stations nearby the given location
        ArrayList<GasStation> gs = api.areaSearch(52.521f,
                                                  13.438f, 
                                                  10.0f,
                                                  new String[] {"all"});
        
        // Print the number of gas stations found
        System.out.println(gs.size() + " gas stations found");
        
        // Send a detail request for the first two gas stations
        api.detailRequest(gs.get(0));
        api.detailRequest(gs.get(1));

        // ... and print out their respective JSON Strings
        System.out.println("First station: ");
        System.out.println(gs.get(0).toJsonString());
        System.out.println("-----------------------");
        System.out.println("Second station: ");
        System.out.println(gs.get(1).toJsonString());   
        
    }
    
}
