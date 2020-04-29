<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>spritwatcher</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body class="infoPage">
        <nav class="navbar navbar-inverse navbar-fixed-top">
            
            <div class="container-fluid">

              <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp"><span class="glyphicon glyphicon-tint" aria-hidden="true"></span>  spritwatcher</a>
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
                <div class="col-lg-12">
                  <div class="thumbnail wow">
                        <img src="bluebg.jpg">
                        <div class="overlay">
                            <div class="infotext">
                                <p>spritwatcher macht es möglich, immer die günstigste Tankstelle zu finden - egal wo, egal wann.</p>
                                <ul>
                                    <li> mit einem Mausklick sämtliche Tankstellen in Ihrer Umgebung finden</li>
                                    <li> Filterfunktion, um die für den Kraftstoff Ihrer Wahl preisgünstigste Tankstelle zu finden</li>
                                    <li> für jede Tankstelle alle wichtigen Informationen (Adresse, Öffnungszeiten, aktuelle Preise, etc.) mit einem Klick erhalten</li>
                                    <li> mit Hilfe der Snapshot Funktion die aktuellen Daten einer Tankstelle abspeichern</li>
                                    <li> Visualisierung von Preistrends jeder beliebigen Tankstelle</li>
                                </ul>
                            </div>
                        </div>
                  </div>   
                </div>  
            </div>
               
        </div>
              
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <script src="jsCode.js"></script> 
    </body>
</html>