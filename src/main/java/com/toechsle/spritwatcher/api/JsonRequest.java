package com.toechsle.spritwatcher.api;

import com.toechsle.spritwatcher.exceptions.JsonException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


public class JsonRequest {
    
    public JsonObject doJsonRequest(String endpoint, String request) throws JsonException, IOException {
           InputStream plainData = this.doHttpGetRequest(endpoint, request);
           if(plainData == null) {
               Logger.getLogger(JsonRequest.class.getName())
                     .log(Level.SEVERE, "Something went wrong while sending your "
                                      + "request! Please check the log output.");
               throw new JsonException();
           }

           try {
               JsonReader reader = Json.createReader(plainData);
               JsonObject jsonObject = reader.readObject();
               return jsonObject;
           } catch(Exception exception) {
               Logger.getLogger(JsonRequest.class.getName())
                     .log(Level.SEVERE, "An error occurred while parsing the server response. "
                                      + "Error message is: " + exception.getMessage());
               throw new JsonException();
           }
    }

    private InputStream doHttpGetRequest(String endpoint, String request) throws MalformedURLException, IOException {
        URL url;
        try {
            url = new URL(endpoint+"?"+request);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            return connection.getInputStream();
        } catch (UnsupportedEncodingException exception) {
            Logger.getLogger(JsonRequest.class.getName()).log(Level.SEVERE, null, exception);
        } catch (MalformedURLException exception) {
            Logger.getLogger(JsonRequest.class.getName()).log(Level.SEVERE, null, exception);
        } catch (IOException exception) {
            Logger.getLogger(JsonRequest.class.getName()).log(Level.SEVERE, null, exception);
        }

        return null;
    }

}
