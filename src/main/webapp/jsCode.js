//Global variables:

var map;
var marker;
var latitude;
var longitude;

var ID = "";

var gsMarkerArray = [];
var prevInfoWindow;

var chart;
var closeButton = null;

var openingTimesAlreadyShown = 0;
var kmZahl = 5;
var TSChoice = "alle";

var Circle;

var Location;

//for running the application on a remote server uncomment the following line:
//var url = "https://WebAddressOfApplication/Controller";

//for running the application on a remote server comment out the following line:
var url = "http://localhost:8080/spritwatcher/Controller";


//The myMap() function creates the map and also a listener, which - when the map is clicked on -
//sets a marker at the corresponding position (by calling the placeMarker() function).
//That marker represents the search center point.
function myMap() {
  //create map:
  var mapCanvas = document.getElementById("map-canvas");
  var myCenter=new google.maps.LatLng(52.504818,13.335082);
  var mapOptions = {center: myCenter, zoom: 7};
  map = new google.maps.Map(mapCanvas, mapOptions);
  
  $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
  
  //create listener, which sets the search center point when the map is clicked on:
  google.maps.event.addListener(map, 'click', function(event) {  
    //First delete any existing markers (for the search center point and petrol stations) as well as any open info window or a displayed chart:
    if (marker) marker.setMap(null);
    if(gsMarkerArray.length !== 0){
        gsMarkerArray.forEach(function(gsMarker){
            gsMarker.setMap(null);
        });
        gsMarkerArray = [];
    }
    if (chart) chart.clearChart();
    if(closeButton) {
        $(closeButton).remove();
        closeButton = null;
    }
    $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
    
    //set search center point:
    placeMarker(map, event.latLng);
    Location = event.latLng;
    //display search radius on the map:
    createCircle1();
  });
  
}

//The placeMarker() function not only sets the marker for the search center point,
//but also ensures that for each petrol station (or - depending on the
//parameters selected by the user - the cheapest petrol station) in the area
//a petrol station marker is set.
//This is done by calling the showGasStations() function.
function placeMarker(map, location) {
      
    var iconCenter = "center24.png";  
    
    latitude = location.lat();
    longitude = location.lng();
    
    //create marker:
    marker = new google.maps.Marker({
      position: location,
      map: map,
      icon: iconCenter,
      title: "Center"
    });
    
    //create info window for the marker:
    var infowindow = new google.maps.InfoWindow({
      content: '<div>Suchmittelpunkt</div>'
    });
    
    //create listener that opens an info window when the user clicks on the marker:
    marker.addListener('click', function() {
      infowindow.open(map, marker);
    });  
    
    //set a petrol station marker for each petrol station (or - depending on the parameters selected by the user - the cheapest petrol station) in the vicinity of the search center point:
    showGasStations();
}

//The showGasStations function sends an AJAX-GET request (with the parameters latitude, longitude, radius, TSChoice and typ)
//to the controller and receives from it the data of the corresponding petrol station(s) within the specified search radius. 
//By calling the placeMarkersGS() function, a gas station marker is set for the gas station(s) supplied by the controller.
function showGasStations(){ 
    ////////////////////////////AJAX-GET-Request - BEGIN////////////////////////////
    $.ajax({
        url: url,
        type: "get", 
        data: { 
          latitude: latitude, 
         longitude: longitude, 
            radius: kmZahl,
          TSChoice: TSChoice,
               typ: "EinfacheAnfrage"
        },
        success: function(response) {
              //alert(response);
              var jsArray = [];
              jsArray = JSON.parse(response);
              //sets a gas station marker for each gas station in the vicinity (or for the cheapest gas station in the area for the specified type of fuel):
              placeMarkersGS(map, jsArray); 
        },
        error: function() {
          alert("Ein Fehler beim Abfragen der Daten ist aufgetreten.");
        }
    });
    ////////////////////////////AJAX-GET-Request - END////////////////////////////
}

function placeMarkersGS(map, data) {
     
    data.forEach(function(jsonString){
        
        var object = JSON.parse(jsonString);
        var lati = object["lat"];
        var longi = object["lon"];   
        var iconGS = "gasstation24.png"; 
        var idValue = object["id"];
        
        //generate HTML string to be displayed in the info window of the petrol station:
        var contentString = '<div id="iw-container">' +     
                                '<h3 class="iw-title">' + object["brandName"] + '</h3>' +
                                '<table align="center">' + 
                                    '<tr>' +
                                      '<td align="left">Name</td>' + 
                                      '<td align="left">' + object["stationName"] + '</td>' + 
                                    '</tr>' +
                                    '<tr>' +
                                      '<td align="left" style=vertical-align:baseline;">Adresse</td>' +
                                      '<td align="left">' + object["street"] + ' ' + object["houseNumber"] + '<br>' + object["zip"] + ' ' + object["place"] + '</td>' +   
                                    '</tr>' +
                                    '<tr>' +
                                      '<td align="left">Preis für E5</td>' +
                                      '<td align="left">' + object["priceE5"] + '</td>' +
                                    '</tr>' +
                                    '<tr>' +
                                      '<td align="left">Preis für E10</td>' +
                                      '<td align="left">' + object["priceE10"] + '</td>' +   
                                    '</tr>' +
                                    '<tr>' +
                                      '<td align="left">Preis für Diesel</td>' +
                                      '<td align="left">' + object["priceDiesel"] + '</td>' +   
                                    '</tr>' +              
                                '</table>' +
                                '<p align="center"><button id="times" type="button" class="btn btn-info btn-xs" onclick="showOeffZeiten()">Öffnungszeiten anzeigen</button></p>' +
                                '<p id="oeffZ" align="center"></p>' +
                                '<p align="center"><button id="priceInfo" type="button" class="btn btn-info btn-xs" onclick="showPreishistorie()">Preis-Historie anzeigen</button></p>' +
                            '</div>';

        //erzeuge Marker:
        var LatLng = {lat: lati, lng: longi};
        var markerGS = new google.maps.Marker({
            position: LatLng,
            map: map,
            icon: iconGS,
            title: "Tankstelle"
        });
        
        //add gas station marker to array of gas station markers:
        gsMarkerArray.push(markerGS);

        //add info window for marker:
        var infowindow = new google.maps.InfoWindow({
            content: contentString,
                 id: idValue
        });
        
        infowindow.className = "iw";
        
        //create listener that opens the info window for the gas station when the user clicks on the marker for this gas station:
        markerGS.addListener('click', function() {
            openingTimesAlreadyShown = 0;
            
            //If an information window is already open, close it first:
            if(prevInfoWindow){ 
                prevInfoWindow.close(); 
            }
            
            //If a price history chart of another petrol station is open, 
            //first remove it (and the associated button):
            if (chart) {
                chart.clearChart();
            }
            if(closeButton){
                $(closeButton).remove();
                closeButton = null;
            }
            
            $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
            
            prevInfoWindow = infowindow;
            
            //open the new info window:
            infowindow.open(map, markerGS);
            ID = infowindow.id; 
        });
        

    });

    
}

//When the user clicks the snapshot button, the following function sends an AJAX-POST request (with the parameters latitude, longitude and radius) to the controller 
//and receives updated data for all petrol stations within the current search radius.
//By calling the placeMarkersGS() function, a gas station marker is set for the gas station(s) supplied by the controller. On the server side, the price data of all 
//petrol stations within the search area are saved in the database.
$("#snapshot").on("click", function(){
    ////////////////////////////AJAX-POST-Request - Begin//////////////////////////// 
    $.ajax({
          url: url,
          type: "post", 
          data: { 
            latitude: latitude, 
            longitude: longitude, 
            radius: kmZahl,
          },
          success: function(response) {
                //alert(response);
                var jsArray = [];
                jsArray = JSON.parse(response);
                //sets a petrol station marker for each of the petrol stations supplied:
                placeMarkersGS(map, jsArray); 
          },
          error: function() {
            alert("Ein Fehler bei der Verarbeitung des Snapshot-Befehls ist aufgetreten.");
          }
      });
    ////////////////////////////AJAX-POST-Request - End//////////////////////////// 
});

//When the user clicks on the button in the info window (of a petrol station) with the text "Öffnungszeiten anzeigen", the following function 
//sends an AJAX-GET request (with the parameters id and typ) to the controller and receives the opening times for the respective gas station. 
//The opening times are then displayed in the info window.
function showOeffZeiten(){ 
    ////////////////////////////AJAX-GET-Request (Öffnungszeitenanfrage) - Begin////////////////////////////
    $.ajax({
        url: url,
        type: "get", 
        data: { 
          id: ID,
         typ: "Öffnungszeitenanfrage"
        },
        success: function(response) {
              //alert(response);
              if (openingTimesAlreadyShown == 0){
                $("#oeffZ").append(response);
                openingTimesAlreadyShown = 1;
              }
        },
        error: function() {
          alert("Ein Fehler beim Abfragen der Daten ist aufgetreten.");
        }
    });
    ////////////////////////////AJAX-GET-Request (Öffnungszeitenanfrage) - End////////////////////////////
}

//When the user clicks on the button in the info window (of a petrol station) with the text "Preis-Historie anzeigen", the following function sends
//an AJAX-GET request (with the parameters id and typ) to the controller and receives the historical prices for the respective gas station. 
//The price history is then displayed graphically (by calling the showPriceInfo() function).
function showPreishistorie(){
    ////////////////////////////AJAX-GET-Request (Preishistorienanfrage) - Begin////////////////////////////
    $.ajax({
        url: url,
        type: "get", 
        data: { 
          id: ID,
         typ: "PreisInfoAnfrage"
        },
        success: function(response) { 
              var jsObj = JSON.parse(response);
              var jsArray = [];
              jsArray = jsObj["Preisdaten"];
              showPriceInfo(jsArray);
        },
        error: function() {
          alert("Für diese Tankstelle liegen (noch) keine Daten zu historischen Preisen vor. Mit einem Klick auf den Snapshot-Button können Sie für diese Tankstelle den ersten Dateneintrag in der Preishistorie veranlassen.");
        }
    });
    ////////////////////////////AJAX-GET-Request (Preishistorienanfrage) - End////////////////////////////
}

//makes preparations for the display of the graphic for the price history:
function showPriceInfo(jsArray){
    //remove any existing graphic and the associated button:
    if (chart) {
        chart.clearChart();
    }
    if(closeButton){
        $(closeButton).remove();
        closeButton = null;
    }
    $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
    //load the corechart package and call the function drawChart, which then creates the graphic:
    google.charts.load('45', {'packages' : ['corechart'], 'language' : 'de'});
    google.charts.setOnLoadCallback(drawChart(jsArray));  
}

//draw the graph for the given price data:
function drawChart(jsArray) {
    
    var arrayOfArrays = [];
    var firstArray = ['Datum', 'E5', 'E10', 'Diesel'];
    arrayOfArrays.push(firstArray);
    
    for (i = 0; i < jsArray.length; i++) { 
        
        var overallObject = jsArray[i];
        var infoArray = [];
        
        var date = overallObject["Datum"];
        infoArray.push(date);
        
        var priceObject = overallObject["Einzelpreise"];
        
        var priceE5String = priceObject["e5"]; 
        var priceE5 = Number(priceE5String);
        infoArray.push(priceE5);
        
        var priceE10String = priceObject["e10"];
        var priceE10 = Number(priceE10String);
        infoArray.push(priceE10);
        
        var priceDieselString = priceObject["diesel"];
        var priceDiesel = Number(priceDieselString);
        infoArray.push(priceDiesel);
        
        arrayOfArrays.push(infoArray);
             
    }
    
    var data = google.visualization.arrayToDataTable(arrayOfArrays);    
        
    var options = {
       title: 'Preis',
       curveType: 'function',
       legend: { position: 'bottom' }
    };

    chart = new google.visualization.LineChart(document.getElementById('chart_div'));
    if(!closeButton) {
        closeButton = document.createElement("BUTTON");
        var text = document.createTextNode("Chart wieder entfernen");
        closeButton.className = "btn btn-danger";
        closeButton.id = "closeBtn";

        closeButton.appendChild(text);                                      

        chart.draw(data, options);

        $("#button_div").append(closeButton); 
    }
        
}

//clicking the dynamically generated button with the text "Chart wieder entfernen" removes the chart and the button itself:
$("body").on("click", "#closeBtn", function(){
    chart.clearChart();
    $(closeButton).remove();
    closeButton = null;
    $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
});

//If the mouse moves over the snapshot button, an explanation is displayed:
$(".hoverText").hover(function(){      
    $("#snapshotText").css("visibility", "visible");
});

$(".hoverText").mouseout(function(){          
    $("#snapshotText").css("visibility", "hidden");
});

//User selection of the radius is saved in the global variable "kmZahl":
$("#radiusChoice").on("change", function(){  
    kmZahl = this.value;
    createCircle2();
});
 
//User selection of the petrol station(s) to be displayed is saved in the global variable "TSChoice":
$("#billigstChoice").on("change", function(){  
    TSChoice = this.value;
    createCircle2();
});

//Function creates a circle on the map around the center of the search (is called when - for a given search center point - either the search radius or the petrol station selection is changed):
function createCircle2(){
    
    if (Circle){
        Circle.setMap(null);
    }
    if (marker) marker.setMap(null);
    if(gsMarkerArray.length !== 0){
        gsMarkerArray.forEach(function(gsMarker){
            gsMarker.setMap(null);
        });
        gsMarkerArray = [];
    }
    if (chart) chart.clearChart();
    if(closeButton) {
        $(closeButton).remove();
        closeButton = null;
    }
    $("#chart_div").html('<p id="zufall2" style="position: absolute; color:white; text-align:center; margin-bottom:1em; font-size: 2em">spritwatcher  ...  nichts dem Zufall überlassen</p>');
    placeMarker(map, Location);
    Circle = new google.maps.Circle({
        strokeColor: '#0000FF',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#0000FF',
        fillOpacity: 0.35,
        map: map,
        center: new google.maps.LatLng(latitude, longitude),
        radius: kmZahl * 1000
    });
    
}

//Function creates a circle on the map around the search center point (is called when the search center point is set again):
function createCircle1(){
    
    if (Circle){
        Circle.setMap(null);
    }
    Circle = new google.maps.Circle({
        strokeColor: '#0000FF',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: '#0000FF',
        fillOpacity: 0.35,
        map: map,
        center: new google.maps.LatLng(latitude, longitude),
        radius: kmZahl * 1000
    });
    
}

//fade-in effect for text on "Info" page as well as "Kontakt" page 
$(".infoPage").ready(function() {
    $(".infotext").fadeIn(3000);
});


