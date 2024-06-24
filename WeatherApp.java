package com.vince;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {

    public static JSONObject getWeatherData(String location) {
        try {
            // Get geocoding data
            String geoUrlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + location;
            URL geoUrl = new URL(geoUrlString);
            HttpURLConnection geoConnection = (HttpURLConnection) geoUrl.openConnection();
            geoConnection.setRequestMethod("GET");

            BufferedReader geoIn = new BufferedReader(new InputStreamReader(geoConnection.getInputStream()));
            String geoInputLine;
            StringBuilder geoContent = new StringBuilder();
            while ((geoInputLine = geoIn.readLine()) != null) {
                geoContent.append(geoInputLine);
            }
            geoIn.close();

            JSONParser parser = new JSONParser();
            JSONObject geoData = (JSONObject) parser.parse(geoContent.toString());
            JSONObject firstResult = (JSONObject) ((org.json.simple.JSONArray) geoData.get("results")).get(0);
            double latitude = (double) firstResult.get("latitude");
            double longitude = (double) firstResult.get("longitude");

            // Get weather data
            String weatherUrlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";
            URL weatherUrl = new URL(weatherUrlString);
            HttpURLConnection weatherConnection = (HttpURLConnection) weatherUrl.openConnection();
            weatherConnection.setRequestMethod("GET");

            BufferedReader weatherIn = new BufferedReader(new InputStreamReader(weatherConnection.getInputStream()));
            String weatherInputLine;
            StringBuilder weatherContent = new StringBuilder();
            while ((weatherInputLine = weatherIn.readLine()) != null) {
                weatherContent.append(weatherInputLine);
            }
            weatherIn.close();

            JSONObject weatherData = (JSONObject) parser.parse(weatherContent.toString());
            return (JSONObject) weatherData.get("current_weather");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


/*package com.vince;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*import java.util.List;
import java.util.Map;*/
/*import java.util.Scanner;

//import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 

public class WeatherApp {

    //private static JSONArray humidityData;

    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);
    
        //Extracts latitude and longitude data from their respective keys within JSON object locationData
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //API call with latitude and longitude from locationData
        String URLString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,wind_speed_10m&hourly=weather_code,visibility,uv_index&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset&timezone=auto&forecast_days=1&forecast_hours=1";

        try {
            
            HttpURLConnection connection = getAPIResponse(URLString);

            if (connection.getResponseCode() != 200){
                System.out.println("Error: Connection to API was not successful");
                return null;    
            }
            
            //stores result JSON data
            StringBuilder resultJSON = new StringBuilder();
            Scanner jsonScanner = new Scanner(connection.getInputStream());
            while(jsonScanner.hasNext()){
                //scans and stores data into string builder
                resultJSON.append(jsonScanner.nextLine());
            }

            jsonScanner.close();

            connection.disconnect();

            JSONParser jsonParser = new JSONParser();
            JSONObject resultJsonObject = (JSONObject) jsonParser.parse(String.valueOf(resultJSON));
            
            JSONObject current = (JSONObject) resultJsonObject.get("current");
            JSONObject hourly = (JSONObject) resultJsonObject.get("hourly");
            JSONObject daily = (JSONObject) resultJsonObject.get("daily");

            JSONArray time = (JSONArray) current.get("time");
            int index = findCurrentTimeIndex(time);

            JSONArray dayNightData = (JSONArray) current.get("is_day");
            String dayNight = convertWeatherCode((long)dayNightData.get(index));

            JSONArray temperatureData = (JSONArray) current.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray feelsLikeTemperatureData = (JSONArray) current.get("apparent_temperature");
            double feelsLikeTemperature = (double) feelsLikeTemperatureData.get(index);

            JSONArray windSpeedData = (JSONArray) current.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            JSONArray humidityData = (JSONArray) current.get("relative_humidity_2m");
            long humidity = (long) humidityData.get(index);

            JSONArray visibilityData = (JSONArray) hourly.get("visibility");
            long visibility = (long) visibilityData.get(index);

            JSONArray UVIndexData = (JSONArray) hourly.get("uv_index");
            long UVIndex = (long) UVIndexData.get(index);

            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long)weatherCode.get(index));

            JSONArray sunriseData = (JSONArray) daily.get("sunrise");
            String sunrise = convertWeatherCode((long)sunriseData.get(index));

            JSONArray sunsetData = (JSONArray) daily.get("sunrise");
            String sunset = convertWeatherCode((long)sunsetData.get(index));

            JSONObject weatherData = new JSONObject();
            weatherData.put("is_day", dayNight);
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windSpeed);
            weatherData.put("visibility", visibility);
            weatherData.put("uv_index", UVIndex);
            
            weatherData.put("feelslike", feelsLikeTemperature);            
            return weatherData;


        } catch(Exception e){

            e.printStackTrace();
        }

        return null;
    }

    private static String convertWeatherCode(long l) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertWeatherCode'");
    }


    public static JSONArray getLocationData(String locationName){
        //replaces whitespace in location name to follow API's request format
        locationName = locationName.replaceAll(" ", "+");
        
        //Geolocation API with locationName input  
        String locationURL = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        //"https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + locationName + "?unitGroup=metric&key=DJ3QF7PFURMPZN66XCFPKE8GF&contentType=json"; 
        
        try {
            HttpURLConnection connection = getAPIResponse(locationURL);
            
            if (connection.getResponseCode() != 200){
                System.out.println("Error: Connection to API was not successful");
                return null;    
            } 
            
            else {
                //stores API results
                StringBuilder resultJSON = new StringBuilder();
                Scanner jsonScanner = new Scanner(connection.getInputStream()); //reads JSON data from API call
                while (jsonScanner.hasNext()){
                    resultJSON.append(jsonScanner.nextLine()); //stores JSON data into resultJSON String
                }

                //close scanner and URL connection to save resources
                jsonScanner.close(); 
                connection.disconnect();
                //creates JSON parser into a JSON object to access JSON data from API ("_insert json property_")
                JSONParser jsonParser = new JSONParser();
                JSONObject resultJSONObject = (JSONObject) ((org.json.simple.parser.JSONParser) jsonParser).parse(String.valueOf(resultJSON));

                //gets list of API generated location data from location name input
                JSONArray locationData = (JSONArray) resultJSONObject.get("results");
                return locationData;

                /*Javascript: Array [] vs Object {} vs JSON "{}" */
            //}
        /* } catch(Exception e){
            e.printStackTrace();
        }
        return null; //couldn't find location
    }

    private static HttpURLConnection getAPIResponse(String locationURL){
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(locationURL); //attempts to create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); //sets request method to "get"
            connection.connect(); //connects to our API
            return connection;

        } catch (IOException e){
            e.printStackTrace();
        }
        return null; //incase connection isn't possible
    }

    private static int findCurrentTimeIndex(JSONArray timeList){
        //String currentTime = getCurrentTime();
        /*for(int i = 0; i < timeList.size(); i++);
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }*/
        /*return 0;
    }
    
    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        int check15minute = LocalDateTime.now().getMinute();
        String minute = "";

        /* -- 00 min -- check15minute -- 15 min -- check15minute -- 30 min -- check15minute -- 45 min -- 00
         * API only updates on a 15 minute basis
        */
    //Checks what time it is to round it back to the nearest number in the 15 minute intervals (round backwards not forward because we're getting weather data from the last 15 minute interval)
    /*if (check15minute == 00 || check15minute > 00 && check15minute < 15 ){ // 00 < mm < 15
        minute = "00";
    }

    else if (check15minute == 15 || check15minute > 15 && check15minute < 30){ // 15 < mm < 30
        minute = "15";
    }

    else if (check15minute == 30 || check15minute > 30 && check15minute < 45){ // 30 < mm < 45
        minute = "30";
    }

    else if (check15minute == 45 || check15minute > 45 && check15minute < 60){ // 45 < mm < 60 (00)
        minute = "45";
    }
        //date format: "2024-06-01T07:45"
        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        DateTimeFormatter apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':" + minute + "'");

        String formattedDateTime = currentDateTime.format(apiFormat);

        return formattedDateTime;
    }

    //Converts number weather code
    public static String convertWeatherCode(long weatherCode, long dayNight){
        String weatherCondition = "";
        //clear sky day/night
        if (weatherCode == 0){
            if (dayNight == 1){
                weatherCondition = "Sunny";
            }

            else if (dayNight == 0){
                weatherCondition = "Clear";
            }
        }
        
        //mainly sunny/clear
        else if (weatherCode == 1){
            if (dayNight == 1){
                weatherCondition = "Mostly Sunny"; //"Mainly Sunny" in API weathercode
            }

            else if (dayNight == 0){
                weatherCondition = "Mostly Clear"; //""Mainly Clear" in API weathercode
            }
        }

        //partly cloudy day/night
        else if (weatherCode == 2){
            if (dayNight == 1){
                weatherCondition = "Partly Cloudy";
            }

            else if (dayNight == 0){
                weatherCondition = "Partly Cloudy";
            }
        }

        else if (weatherCode == 3){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Cloudy";
        }

        else if (weatherCode == 45){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Foggy";
            }
        }

        else if (weatherCode == 48){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Rime Fog";
            }
        }

        else if (weatherCode == 51){
            if (dayNight == 1){
                weatherCondition = "Light Drizzle";
            }

            else if (dayNight == 0){
                weatherCondition = "Light Drizzle";
            }
        }

        else if (weatherCode == 53){
            if (dayNight == 1){
                weatherCondition = "Drizzle";
            }

            else if (dayNight == 0){
                weatherCondition = "Drizzle";
            }
        }

        else if (weatherCode == 55){
            if (dayNight == 1){
                weatherCondition = "Heavy Drizzle";
            }

            else if (dayNight == 0){
                weatherCondition = "Heavy Drizzle";
            }
        }

        else if (weatherCode == 56){
            if (dayNight == 1){
                weatherCondition = "Light Freezing Drizzle";
            }

            else if (dayNight == 0){
                weatherCondition = "Light Freezing Drizzle";
            }
        }

        else if (weatherCode == 57){
            if (dayNight == 1){
                weatherCondition = "Freezing Drizzle";
            }

            else if (dayNight == 0){
                weatherCondition = "Freezing Drizzle";
            }
        }

        else if (weatherCode == 61){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Light Rain";
            }
        }

        else if (weatherCode == 63){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Rain";
            }
        }

        else if (weatherCode == 65){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Heavy Rain";
            }
        }

        else if (weatherCode == 66){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Light Freezing Rain";
            }
        }   

        else if (weatherCode == 67){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Freezing Rain";
            }
        }   

        else if (weatherCode == 71){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Light Snow";
            }
        }   
            
        else if (weatherCode == 73 || weatherCode == 77){ //Snow and Snow Grains
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Snow";
            }
        }   

        else if (weatherCode == 75){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Heavy Snow";
            }
        }  

        else if (weatherCode == 80){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Light Showers";
            }
        }  

        else if (weatherCode == 81){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Showers";
            }
        }

        else if (weatherCode == 82){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Heavy Showers";
            }
        }

        else if (weatherCode == 85){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Light Snow Showers";
            }
        }

        else if (weatherCode == 86){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Snow Showers";
            }
        }

        else if (weatherCode == 95 && weatherCode == 96 && weatherCode == 99){
            if (dayNight == 1 || dayNight == 0){
                weatherCondition = "Thunderstorm";
            }
        }
    }
        return weatherCondition;
    }
} */
