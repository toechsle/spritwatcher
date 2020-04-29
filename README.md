# spritwatcher
Finds the cheapest gas station for each type of fuel | Snapshot function for saving price data | Visualization of price trends  


In order to be able to run the application, you need to do the following:

1. In file src/main/java/com/toechsle/spritwatcher/config/Config.java replace the placeholder value "YourAPIKey" with an API Key for the tankerkoenig-API. You can request such an API Key here: https://creativecommons.tankerkoenig.de.
2. If you intend to use a local MongoDB connection:
   -> create a database named "tankerkoenig"
   -> inside database "tankerkoenig" create a collection named "pricesInfo"
   -> in file src/main/java/com/toechsle/spritwatcher/database_access/DatabaseAccess.java (and as indicated by the comments within the file) comment out line 39 and uncomment lines 26, 27, and 42
3. If you intend to use MongoDB Atlas (https://www.mongodb.com/cloud/atlas):
   -> create an Atlas account and deploy a cluster (see https://docs.atlas.mongodb.com/getting-started/)
   -> replace the placeholder value "YourConnectionString" in file src/main/java/com/toechsle/spritwatcher/config/Config.java with the connection string for the cluster you deployed in the previous step
   -> create a database named "tankerkoenig" on your MongoDB Atlas cluster
   -> inside database "tankerkoenig" create a collection named "pricesInfo"
4. In line 104 of file src/main/webapp/index.jsp replace the placeholder value "YourGoogleCloudAPIKey" with an API Key for the Maps JavaScript API (see https://developers.google.com/maps/documentation/javascript/get-api-key).
5. If you intend to run the application on a remote web server (e.g. using Amazon Web Services):
   -> in file src/main/webapp/jsCode.js (and as indicated by the comments within the file) uncomment line 25 and comment out line 28
   -> in line 25 of file src/main/webapp/jsCode.js replace the placeholder value "WebAddressOfApplication" with the web address of the application (e.g. "spritwatcher.us-east-1.elasticbeanstalk.com")
    

***********************************************************************************************************************************************
For a video presentation of the application see file "spritwatcher.MP4".