package com.toechsle.spritwatcher.controller;


import com.toechsle.spritwatcher.api.TankerkoenigApi;
import com.toechsle.spritwatcher.database_access.DatabaseAccess;
import com.toechsle.spritwatcher.exceptions.JsonException;
import com.toechsle.spritwatcher.models.GasStation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;


@WebServlet(name = "Controller",
            urlPatterns = {"/Controller"}    
           )
public class Controller extends HttpServlet {
      
    protected void processRequestArea(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonException  {
                   
        String lat = request.getParameter("latitude");
        String lon = request.getParameter("longitude");
        String rad = request.getParameter("radius");
        String tsChoice = request.getParameter("TSChoice");
        
        float latitude = Float.parseFloat(lat);
        float longitude = Float.parseFloat(lon);
        float radius = Float.parseFloat(rad);
        
        TankerkoenigApi api = new TankerkoenigApi();
        ArrayList<GasStation> gasStationList = new ArrayList<>();
        
        if(tsChoice.equals("alle")){
            gasStationList = api.areaSearch(latitude, longitude, radius, new String[] {"all"}); 
        }
        else{
            ArrayList<GasStation> gasStationList2 = new ArrayList<>(); 
            gasStationList2 = api.areaSearch(latitude, longitude, radius, new String[] {"all"}); 
            
            GasStation cheapestStation = gasStationList2.get(0);
            
            if(tsChoice.equals("e5")){
                for(GasStation station : gasStationList2){
                    if(station.getPriceE5() < cheapestStation.getPriceE5())
                        cheapestStation = station;
                }
            }
            else if(tsChoice.equals("e10")){
                for(GasStation station : gasStationList2){
                    if(station.getPriceE10() < cheapestStation.getPriceE10())
                        cheapestStation = station;
                }
            }
            else {
                for(GasStation station : gasStationList2){
                    if(station.getPriceDiesel() < cheapestStation.getPriceDiesel())
                        cheapestStation = station;
                }
            }
            
            gasStationList.add(cheapestStation);
        }
            
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder jsonArrayBuilder = factory.createArrayBuilder();
       
        for(GasStation station : gasStationList){
           String stationString = station.toJsonString();
           jsonArrayBuilder.add(stationString); 
        }    
        JsonArray jsonArray = jsonArrayBuilder.build();   
        
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonArray);
        }
    }
     
    protected void processRequestDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonException  {
                
        String id = request.getParameter("id");
       
        GasStation station = new GasStation();
        station.setId(id);
               
        TankerkoenigApi api = new TankerkoenigApi();
        api.detailRequest(station); 
      
        String oeffZeiten;
        oeffZeiten = station.getOeffZeiten();
        
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(oeffZeiten);
        }
    }
      
    protected void processRequestDB(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonException {
        
        //get data from Tankerkönig Web Service:     
        String lat = request.getParameter("latitude");
        String lon = request.getParameter("longitude");
        String rad = request.getParameter("radius");
        
        float latitude = Float.parseFloat(lat);
        float longitude = Float.parseFloat(lon);
        float radius = Float.parseFloat(rad);
        
        TankerkoenigApi api = new TankerkoenigApi();
        ArrayList<GasStation> gasStationList = new ArrayList<>();
        gasStationList = api.areaSearch(latitude, longitude, radius, new String[] {"all"}); 
        
        //Create JsonArray:      
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder jsonArrayBuilder = factory.createArrayBuilder();
       
        //Turn elements of type GasStation into elements of type String and insert each of the Strings into the JsonArray:
        for(GasStation station : gasStationList){
           String stationString = station.toJsonString();
           jsonArrayBuilder.add(stationString); 
        }    
        JsonArray jsonArray = jsonArrayBuilder.build();
       
        //Send data to client-side:
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {        
            out.println(jsonArray);  
        }
        
        //Save (part of the) data to database:
        DatabaseAccess databaseAccess = new DatabaseAccess();
        databaseAccess.writeToDB(gasStationList);
        databaseAccess.close();
        
    }
    
    protected void processRequestPrices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JsonException {
        
        String id = request.getParameter("id");
       
        //get price data from database:
        DatabaseAccess databaseAccess = new DatabaseAccess();
        Document doc = databaseAccess.readFromDB(id);
        databaseAccess.close();
        
        String info = doc.toJson();
        
        //Send data to client-side:
        response.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {        
            out.println(info);  
        }
            
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {    
        try {
            String type = request.getParameter("typ");
            if(type.equals("EinfacheAnfrage")){
                processRequestArea(request, response);
            }
            else if (type.equals("PreisInfoAnfrage")){
                processRequestPrices(request, response);
            }
            else{
                processRequestDetail(request, response);
            }
        } catch (JsonException exception) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {            
            processRequestDB(request, response);  
        } catch (JsonException exception) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, exception);
        }
    }


}

