# TestLibertyRider
Application for liberty rider

Simple application with 6 classes and 2 interfaces.

The MainActivity do all the requests at the start. But you can replay the request with a click on the driver picture.

Classes arborescence :
  -|
   |- MainActivity
   |- Database
   |- Callback
      |- HttpRaceHandler
      |- HttpPilotHandler
   |- Parser
      |- ParserXML
      |- ParserRace
      
 Link use for requests :
    - http://ergast.com/api/f1/current/last
    - http://ergast.com/api/f1/current/last/drivers
