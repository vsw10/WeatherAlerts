# WeatherAlerts
 Weather Alert App allows the user to mark certain location and displays the  weather forecast of that particular location

Version 0.0.1

Implemented the following things:
1. Navigation View to display the actions
2. Maps Integration API keys
3. Mark and Selecting the particular location.
4. UI FLow


Things to do:
Display the Weather forecast of Selected city
Add the Search Functionality
Add settings
Storing locatons in db.
Deletion of bookmarked locations
Display the Help Screen Page
Add Logs mechanism

Version 0.0.2(Released on 21-08-2021)

Implemented the following things:
1. Display the Weather forecast of Selected city
2. Add settings
3. Storing locatons in db.
4. Display FAQ/Help Screen using Web View
5. Display the Help Screen Page
6. Added Logs mechanism

Known Issues: 
1.In some scenarios, for the bookmarked cities OpenWeather APIs are not providing the weather updates .
2. While selecting / marking the location on the map, it shows same names for 2 3 places.
 

Things to do:
Add the Search Functionality
Deletion of bookmarked locations
Code Refactoring 

Following components has been used 
1. MVVM
2. RxJava
3. Realm 
4. ViewBinding
5. Glide
6. Maps APIs
7. Retrofit API calls
8. RecyclerView
9. Material Design
10. Clean code Architecture




In oder to see the Logs,
Please run the below command
"adb logcat -s wa"


O/P:

Snippet:

08-21 04:54:58.948 28112 28112 I wa      :   CLicked Item Position Location = proxy[{id:9},{mCountryName:India},{mPostalCode:482001},{mCountryCode:IN},{mLocality:Jabalpur},{mLatitude:23.1850587},{mLongitude:79.9905927},{isDelete:0}]
08-21 04:55:00.444 28112 28112 D wa      : OpenWeatherMapHelper getCurrentWeatherByCityName onResponse CurrentWeather(coord=Coord(lon=79.9501, lat=23.167), weather=[Weather(id=502, main=Rain, description=heavy intensity rain, icon=10n)], base=stations, main=Main(temp=296.3, feelsLike=297.15, tempMin=296.3, tempMax=296.3, pressure=1005.0, humidity=95.0, seaLevel=1005.0, grndLevel=958.0, tempKf=null), visibility=10000, wind=Wind(speed=3.89, deg=180.0, gust=7.99), clouds=Clouds(all=80.0), rain=Rain(oneHour=4.86, threeHour=null), snow=null, dt=1629501898, sys=Sys(type=0.0, id=null, message=null, country=IN, sunrise=1629505125, sunset=1629551302, pod=null), timezone=19800, id=1269633, name=Jabalpur, cod=200)
08-21 04:55:00.445 28112 28112 D wa      : MainActivity responseCityObservable SUCCESS
08-21 04:55:00.445 28112 28112 D wa      : MainActivity Observable Success  Main Values Main(temp=296.3, feelsLike=297.15, tempMin=296.3, tempMax=296.3, pressure=1005.0, humidity=95.0, seaLevel=1005.0, grndLevel=958.0, tempKf=null)
08-21 04:55:00.445 28112 28112 D wa      : MainActivity Observable Success  CurrentWeather(coord=Coord(lon=79.9501, lat=23.167), weather=[Weather(id=502, main=Rain, description=heavy intensity rain, icon=10n)], base=stations, main=Main(temp=296.3, feelsLike=297.15, tempMin=296.3, tempMax=296.3, pressure=1005.0, humidity=95.0, seaLevel=1005.0, grndLevel=958.0, tempKf=null), visibility=10000, wind=Wind(speed=3.89, deg=180.0, gust=7.99), clouds=Clouds(all=80.0), rain=Rain(oneHour=4.86, threeHour=null), snow=null, dt=1629501898, sys=Sys(type=0.0, id=null, message=null, country=IN, sunrise=1629505125, sunset=1629551302, pod=null), timezone=19800, id=1269633, name=Jabalpur, cod=200)
08-21 04:55:00.445 28112 28112 D wa      : MainActivity  performCalculations 296.3 296.3
08-21 04:55:00.445 28112 28112 D wa      : MainActivity showAddLocationFragment

Issues faced and resolved:

Issues Faced:
Resolving Map API key in gradle
Resolving gradle plugins
Realm Plugin Issues
Binary inflate exception
Exception in adding Observables in Retrofit
Bad request with the API key shared in the assignment












