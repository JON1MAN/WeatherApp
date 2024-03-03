import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Application {

    //If I need to use JSON classes, first of all
    //I need to add JSON JAR to project structure
    //And in this code we are getting weather data
    //for location entered by user
    public static JSONObject getWeatherData(String locationName) {

        JSONArray locationData = getLocationData(locationName);
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
                //we need to store API results if success above

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
}
