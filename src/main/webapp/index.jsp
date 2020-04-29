<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>   
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>spritwatcher</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css"> 
    </head>
    
    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top"> 
            
            <div class="container-fluid">
                
              <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a href="index.jsp" class="navbar-brand"><span class="glyphicon glyphicon-tint" aria-hidden="true"></span>  spritwatcher</a>
              </div>  
              
              <!-- Collect the nav links, forms, and other content for toggling -->
              <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">          
                <ul class="nav navbar-nav navbar-right">
                  <li><a href="info.jsp">Info</a></li>
                  <li><a href="contact.jsp">Kontakt</a></li>
                </ul>
              </div><!-- /.navbar-collapse -->
              
            </div><!-- /.container-fluid -->
        
        </nav>
       
        <div class="container">
            <div class="row">
                <div class="col-xs-0"></div>
                <div id="suchradiusTextContainer" class="col-xs-5"><p id="suchradiusText">Suchradius</p></div>
                <div class="col-xs-2"></div>
                <div id="billigstTextContainer" class="col-xs-5"><p id="billigstText">Tankstellen</p></div>
                <div class="col-xs-0"></div>
            </div> 
            <div class="row">
                <div class="col-xs-0"></div>
                <div class="col-xs-5" id="radius">
                      
                      <select id="radiusChoice" class="form-control">
                            <option value="1">1 km</option>
                            <option value="2">2 km</option>
                            <option value="3">3 km</option>
                            <option value="4">4 km</option>
                            <option value="5" selected="selected">5 km (default)</option>
                            <option value="6">6 km</option>
                            <option value="7">7 km</option>
                            <option value="8">8 km</option>
                            <option value="9">9 km</option>
                            <option value="10">10 km</option>
                            <option value="15">15 km</option>
                            <option value="20">20 km</option>       
                      </select>
         
                </div>
                <div class="col-xs-2"></div>
                <div class="col-xs-5" id="billigst">
                      
                      <select id="billigstChoice" class="form-control">
                            <option value="alle" selected="selected">alle (default)</option>
                            <option value="e5">günstigste für E5</option>
                            <option value="e10">günstigste für E10</option>
                            <option value="diesel">günstigste für Diesel</option>
                          
                      </select>
         
                </div>          
                <div class="col-xs-0"></div>
            </div> 
            <div class="row">
                <div class="col-md-12 thumbnail" id="map-canvas" style="height:45pc"></div>
            </div> 
            <div class="row">
                <div id="snapshot_div" class="col-xs-12">
                    <button id="snapshot" type="button" class="btn btn-primary btn-lg hoverText"><span class="glyphicon glyphicon-camera" aria-hidden="true"></span> Snapshot</button>
                </div>
            </div> 
            <div class="row">
                <div class="col-xs-12" id="snapshotTextContainer"><p id="snapshotText">Aktualisiert und speichert die Preisdaten aller Tankstellen innerhalb des Suchradius.<p/></div>
            </div> 
            <div class="row">
                <div class="col-xs-12" id="gap"></div>
            </div> 
            <div id="chartContainer" class="row">
                <div class="col-xs-12 thumbnail" id="chart_div">
                </div>
                <div class="col-xs-12" id="button_div"></div>
            </div>
        </div>
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <script src="jsCode.js"></script> 
        <script src="https://maps.googleapis.com/maps/api/js?key=YourGoogleCloudAPIKey&callback=myMap"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    </body>
</html>
