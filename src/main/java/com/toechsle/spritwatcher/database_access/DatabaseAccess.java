package com.toechsle.spritwatcher.database_access;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import com.mongodb.client.model.Updates;
import com.toechsle.spritwatcher.config.Config;
import com.toechsle.spritwatcher.models.GasStation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.bson.Document;


public class DatabaseAccess {
    
    //for running the application locally uncomment the following two lines:
    //private final String DB_HOST = "localhost";
    //private final int DB_PORT = 27017;
    
    private final MongoClientURI uri;
    private final String DB_NAME = Config.DB_NAME;
    private final String DB_COLLECTION = Config.DB_COLLECTION;
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection; 
    
    public DatabaseAccess(){
        uri = new MongoClientURI(Config.MONGO_CLIENT_URI);
        //for running the application locally comment out the following line:
        mongoClient = new MongoClient(uri);
       
        //for running the application locally uncomment the following line:
        //mongoClient = new MongoClient(DB_HOST, DB_PORT);
        
        database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(DB_COLLECTION);
    }
    
    public void close() {
        mongoClient.close();
    }
    
    public Document readFromDB(String id){
        FindIterable<Document> iterableDocument = collection.find(eq("id", id)).projection(fields(include("Preisdaten"), excludeId()));       
        Document firstAndOnly = iterableDocument.first();
        return firstAndOnly;     
    }
    
    public void writeToDB(ArrayList<GasStation> gsList){
        
        for(GasStation gs : gsList) {
            String id = gs.getId();
            String date = getDate();
            double e5 = gs.getPriceE5();
            double e10 = gs.getPriceE10();
            double diesel = gs.getPriceDiesel();
             
            //check if gas station is already in DB:
            Document doc = collection.find(eq("id", id)).first();
            if(doc == null){
                //create new Document and insert data:
                Document document = createDocument(id, date, e5, e10, diesel); 
                //java.util.logging.Logger.getLogger(DatabaseAccess.databaseAccess.class.getName())
                //                        .log(Level.INFO, "Testoutput: " + document);
                collection.insertOne(document);
            }
            else{
                //augment existing Document with new data:
                Document subDoc = createSubDocument(date, e5, e10, diesel);
                collection.updateOne(new Document("id", id), Updates.push("Preisdaten", subDoc));
                
            }
        }
      
    }
    
    
    private Document createDocument(String id, String date, double e5, double e10, double diesel){
        Document document = new Document("id", id).append("Preisdaten", Arrays.asList(new Document().append("Datum", date)
                                                                                                    .append("Einzelpreise", new Document("e5", e5)
                                                                                                                                 .append("e10", e10)
                                                                                                                                 .append("diesel", diesel))));
        return document;     
    }
    
    private Document createSubDocument(String date, double e5, double e10, double diesel){
        Document subDoc = new Document().append("Datum", date)
                                        .append("Einzelpreise", new Document("e5", e5)
                                                                     .append("e10", e10)
                                                                     .append("diesel", diesel));    
        return subDoc;                                                                                                                                                                                                                                                                    
    }
       
    private String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateObject = new Date();
        return dateFormat.format(dateObject);
    }      
             
}

