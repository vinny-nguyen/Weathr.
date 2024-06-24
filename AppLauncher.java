package com.vince;

import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Root();
            }
        });
    }
}
/*package com.vince;

/*import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;*/

//import javax.swing.SwingUtilities;

//import org.apache.http.client.methods.HttpGet;

/*public class AppLauncher {
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable(){

            @Override

            public void run(){
                new Root(); //displays weather app GUI
                //System.out.println(WeatherApp.getLocationData("Fredericton"));
                System.out.println(WeatherApp.getCurrentTime());
                System.out.println(WeatherApp.getWeatherData("Fredericton"));
            }
        });

    }
}*/