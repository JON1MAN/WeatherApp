import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Application {

    //If I need to use JSON classes, first of all
    //I need to add JSON JAR to project structure
    //And in this code we are getting weather data
    //for location entered by user
    public static JSONObject getWeatherData(String locationName) {

        JSONArray locationData = getLocationData(locationName);

        //getting latitude and longitude data

        //the first Object in our JSON data we got
        //is the city that we looked for
        JSONObject location = (JSONObject) locationData.get(0); //we need first city, that's why we are getting(0)

        //getting coordinates, by using JSONObject's instance method .get()
        //we have our JSON data, and our latitude and longitude are
        //in fields "latitude" and "longitude"
        //also we always need to cast our data, when we are trying
        //to return value from JSONObject
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //now need to build API request URL with coordinates
        //that we got from JSON above

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude +"&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Europe%2FBerlin";

        try{
            //call API and getting response
            HttpURLConnection connection = fetchApiResponse(urlString);

            if(connection.getResponseCode() != 200){
                System.out.println("Error! Couldn't connect to API");
                return null;
            }else{
                StringBuilder resultJSON = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()){
                    resultJSON.append(scanner.next());
                }
                scanner.close();
                connection.disconnect();


                //parse trough data that we got to StringBuilder
                //because resultJSON is only a string, so we can't get
                //data like we did in JSON by using .get()
                //we need to parse it
                JSONParser parser = new JSONParser();
                JSONObject resultJSONObject= (JSONObject) parser.parse(String.valueOf(resultJSON));

                //hourly data
                JSONObject hourlyData = (JSONObject) resultJSONObject.get("hourly");
                //to get the current hour's data
                //need to get the index of our current hour
                JSONArray time = (JSONArray) hourlyData.get("time");

                int index = getIndexOfCurrentTime(time);

                JSONArray temperatureDataList = (JSONArray) hourlyData.get("temperature_2m");
                double temperature = (double) temperatureDataList.get(index);

                JSONArray weathercode = (JSONArray) hourlyData.get("weather_code");
                String weatherCondition = convertWeatherCode((long) weathercode.get(index));

                JSONArray humidityDataList = (JSONArray) hourlyData.get("relative_humidity_2m");
                long humidity = (long) humidityDataList.get(index);


                JSONArray windspeedData = (JSONArray) hourlyData.get("wind_speed_10m");
                double windspeed = (double) windspeedData.get(index);

                //next we build JSONObject to pass that values
                //we will need them to read in our GUI

                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temperature);            //here generating our JSONObject
                weatherData.put("weather_condition", weatherCondition); //we need to put "values" to their "keys"
                weatherData.put("humidity", humidity);                  //using method .put(), by entering field name and the value to that field
                weatherData.put("windspeed", windspeed);                //then we can take our data by using method .get()

                return weatherData;
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            //Calling API and getting a response
            HttpURLConnection connection = fetchApiResponse(urlString);


            //need to check response status
            //200 --- successful connection
            //400 --- Bad request
            //500 --- Internal Server Error
            if(connection.getResponseCode() != 200){
                System.out.println("Error! Couldn't connect to API");
                return null;
            }else{
                //we need to store API results if condition above successful

                StringBuilder resultJson = new StringBuilder();

                //getting data to Scanner
                Scanner scanner = new Scanner(connection.getInputStream());
                //storing data
                while(scanner.hasNext()){
                    resultJson.append(scanner.nextLine());
                }
                //we are closing scanner and disconnection http
                //connection to save resources
                scanner.close();
                connection.disconnect();


                //parsing JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));

                //get the list of location data API
                //generated from the location name

                JSONArray locationData = (JSONArray) resultJsonObject.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //setting request method to get
            connection.setRequestMethod("GET");

            //connect to our API
            connection.connect();
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static int getIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);

            if(time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }
    private static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        //formatting date to be the same format in JSON data that we got
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }
    private static String convertWeatherCode(long weathercode){

        String weatherCondition = "";
        if(weathercode == 0L){
            weatherCondition = "Clear";
        } else if (weathercode <= 3L && weathercode > 0L) {
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L)
                ||(weathercode >= 80L && weathercode <= 99L) ){
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
