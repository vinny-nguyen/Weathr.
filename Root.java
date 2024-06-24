package com.vince;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.json.simple.JSONObject;

public class Root extends JFrame {

    private JSONObject weatherData;

    public Root() {
        super("Weathr");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        addGUIComponents();
    }

    private void addGUIComponents() {
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("src\\main\\java\\com\\vince\\weathrlogo.png").getImage().getScaledInstance(90, 30, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(40, 90, 90, 30);
        logoLabel.setVisible(true);
        add(logoLabel);

        final JTextField searchTextField = new JTextField();
        searchTextField.setBounds(40, 30, 300, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        searchTextField.setVisible(true);
        add(searchTextField);

        JLabel locationText = new JLabel("Fredericton, New Brunswick");
        locationText.setBounds(0, 150, 450, 54);
        locationText.setFont(new Font("Dialog", Font.BOLD, 20));
        locationText.setHorizontalAlignment(SwingConstants.CENTER);
        locationText.setVisible(true);
        locationText.setBackground(Color.BLACK);
        add(locationText);

        final JLabel weatherConditionImage = new JLabel(loadImage("src\\main\\java\\com\\vince\\assets\\cloudy.png"));
        weatherConditionImage.setBounds(0, 150, 450, 217);
        weatherConditionImage.setVisible(true);
        add(weatherConditionImage);

        final JLabel temperatureText = new JLabel("°C");
        temperatureText.setBounds(0, 310, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureText.setVisible(true);
        add(temperatureText);

        final JLabel feelsLikeText = new JLabel("Feels like" + "°C");
        feelsLikeText.setBounds(0, 360, 450, 54);
        feelsLikeText.setFont(new Font("Dialog", Font.BOLD, 20));
        feelsLikeText.setHorizontalAlignment(SwingConstants.CENTER);
        feelsLikeText.setVisible(true);
        add(feelsLikeText);

        final JLabel weatherConditionDesc = new JLabel("Cloudy ahh");
        weatherConditionDesc.setBounds(0, 415, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        weatherConditionDesc.setVisible(true);
        add(weatherConditionDesc);

        final JLabel airQualityText = new JLabel("<html><center><b>Air quality<b><center><html>" + 10);
        airQualityText.setBounds(25, 480, 85, 55);
        airQualityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(airQualityText);

        final JLabel windText = new JLabel("<html><center><b>Wind<b><br><center><html>" + 10 + "km/h");
        windText.setBounds(110, 480, 85, 55);
        windText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windText);

        final JLabel humidityText = new JLabel("<html><center><b>Humidity<b><center><html> 100%");
        humidityText.setBounds(180, 480, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        final JLabel visibilityText = new JLabel("<html><center><b>Visibility<b><center><html>" + 10 + "km");
        visibilityText.setBounds(260, 480, 85, 55);
        visibilityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(visibilityText);

        final JLabel UVIndexText = new JLabel("<html><center><b>UV Index<b><center><html>" + 10);
        UVIndexText.setBounds(335, 480, 85, 55);
        UVIndexText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(UVIndexText);

        JPanel mainPanel = new JPanel();
        Color background = Color.decode("#535C91");
        mainPanel.setBounds(0, 0, 450, 650);
        mainPanel.setBackground(background);
        mainPanel.setVisible(true);
        add(mainPanel);

        ImageIcon searchIcon = new ImageIcon(new ImageIcon("src\\main\\java\\com\\vince\\assets\\searchButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton searchButton = new JButton(searchIcon);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String userInput = searchTextField.getText();
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);
                if (weatherData == null) {
                    JOptionPane.showMessageDialog(null, "Failed to retrieve weather data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String weatherCondition = (String) weatherData.get("weathercode"); // updated key
                int dayNight = (int) weatherData.get("is_day"); // updated key

                // Weather condition mapping
                switch (weatherCondition) {
                    case "0":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\clear.png"));
                        break;
                    case "1":
                    case "2":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\partlycloudy.png"));
                        break;
                    case "3":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\cloudy.png"));
                        break;
                    case "45":
                    case "48":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\fog.png"));
                        break;
                    case "51":
                    case "53":
                    case "55":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        break;
                    case "56":
                    case "57":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        break;
                    case "61":
                    case "63":
                    case "65":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\rain.png"));
                        break;
                    case "66":
                    case "67":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        break;
                    case "71":
                    case "73":
                    case "75":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\snow.png"));
                        break;
                    case "77":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;
                    case "80":
                    case "81":
                    case "82":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavyrain.png"));
                        break;
                    case "85":
                    case "86":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;
                    case "95":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\thunderstorm.png"));
                        break;
                    default:
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\cloudy.png"));
                        break;
                }

                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "°C");

                weatherConditionDesc.setText(getWeatherDescription(weatherCondition));

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><center><b>Humidity<b><center>" + humidity + "%<html>");

                double windSpeed = (double) weatherData.get("windspeed");
                windText.setText("<html><center><b>Wind<b><br><center>" + windSpeed + "km/h<html>");

                double visibility = (double) weatherData.get("visibility");
                visibilityText.setText("<html><center><b>Visibility<b><center>" + visibility + "km<html>");

                double UVIndex = (double) weatherData.get("uv_index");
                UVIndexText.setText("<html><center><b>UV Index<b><center>" + UVIndex + "<html>");
            }
        });
        searchButton.setBounds(360, 30, 47, 45);
        searchButton.setVisible(true);
        add(searchButton);
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Couldn't find resource.");
        return null;
    }

    private String getWeatherDescription(String weatherCode) {
        switch (weatherCode) {
            case "0":
                return "Clear Sky";
            case "1":
            case "2":
                return "Partly Cloudy";
            case "3":
                return "Cloudy";
            case "45":
            case "48":
                return "Foggy";
            case "51":
            case "53":
            case "55":
                return "Drizzle";
            case "56":
            case "57":
                return "Freezing Drizzle";
            case "61":
            case "63":
            case "65":
                return "Rain";
            case "66":
            case "67":
                return "Freezing Rain";
            case "71":
            case "73":
            case "75":
                return "Snow";
            case "77":
                return "Heavy Snow";
            case "80":
            case "81":
            case "82":
                return "Heavy Rain Showers";
            case "85":
            case "86":
                return "Heavy Snow Showers";
            case "95":
                return "Thunderstorms";
            default:
                return "Unknown";
        }
    }
}
/*package com.vince;

//import java.awt.Image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.awt.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.text.*;

//import org.json.JSONObject;

public class Root extends JFrame {  

    private org.json.simple.JSONObject weatherData;

    public Root(){

        //Create main window
        /*JFrame root = new JFrame();
        setTitle("Weathr"); //title
        setSize(450, 650); //size in pixels

        setDefaultCloseOperation(EXIT_ON_CLOSE); //configures GUI to end the program once it's been closed

        root.setLocationRelativeTo(null); //loads GUI at the centre of the screen
        root.setResizable(false); //prevents any resizing of GUI
        root.setVisible(true);*/
        //Create data interface*/
        /*super("Weathr");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        addGUIComponents();
    }

    /*public class VariablesActionListener {

        public final JTextField searchTextField = new JTextField();
    }*/
    /*private void addGUIComponents(){

        //Logo
        ImageIcon logoIcon = new ImageIcon(new ImageIcon("src\\main\\java\\com\\vince\\weathrlogo.png").getImage().getScaledInstance(90, 30, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(40, 90, 90, 30);
        logoLabel.setVisible(true);
        add(logoLabel);

        //City search text box
        final JTextField searchTextField = new JTextField();
        searchTextField.setBounds(40,30, 300, 45); //sets size and position of TextField
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        searchTextField.setVisible(true);
        add(searchTextField);

        //Location text from "resolvedAddress" on JSON
        JLabel locationText = new JLabel("Fredericton, New Brunswick");
        locationText.setBounds(0, 150, 450, 54);
        locationText.setFont(new Font("Dialog", Font.BOLD, 20));
        locationText.setHorizontalAlignment(SwingConstants.CENTER);
        locationText.setVisible(true);
        locationText.setBackground(Color.BLACK);
        add(locationText);

        //Weather condition image
        final JLabel weatherConditionImage = new JLabel(loadImage("src\\main\\java\\com\\vince\\assets\\cloudy.png"));
        weatherConditionImage.setBounds(0, 150, 450, 217);
        weatherConditionImage.setVisible(true);
        add(weatherConditionImage);

        //Temperature: "temperature_2m"
        final JLabel temperatureText = new JLabel("°C");
        temperatureText.setBounds(0, 310, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureText.setVisible(true);
        add(temperatureText);

        //Feels like: "apparent_temperature"
        final JLabel feelsLikeText = new JLabel("Feels like" + "°C");
        feelsLikeText.setBounds(0, 360, 450, 54);
        feelsLikeText.setFont(new Font("Dialog", Font.BOLD, 20));
        feelsLikeText.setHorizontalAlignment(SwingConstants.CENTER);
        feelsLikeText.setVisible(true);
        add(feelsLikeText);

        //Creates description for weather condition //weathercode
        final JLabel weatherConditionDesc = new JLabel("Cloudy ahh");
        weatherConditionDesc.setBounds(0, 415, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));   
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        weatherConditionDesc.setVisible(true);
        add(weatherConditionDesc);

        //Air quality from "quality" from "CAFC" or "CYFC" //Air quality from air quality index
        final JLabel airQualityText = new JLabel("<html><center><b>Air quality<b><center><html>" + 10);
        airQualityText.setBounds(25, 480, 85, 55);
        airQualityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(airQualityText);

        //Wind speed from "windspeed"
        final JLabel windText = new JLabel("<html><center><b>Wind<b><br><center><html>" + 10 + "km/h");
        windText.setBounds(110, 480, 85, 55);
        windText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windText);

        //Humidity from "humidity"
        final JLabel humidityText = new JLabel("<html><center><b>Humidity<b><center><html> 100%");
        humidityText.setBounds(180, 480, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);      
        
        final JLabel visibilityText = new JLabel("<html><center><b>Visibility<b><center><html>" + 10 + "km");
        visibilityText.setBounds(260, 480, 85, 55);
        visibilityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(visibilityText);

        //UV Index
        final JLabel UVIndexText = new JLabel("<html><center><b>UV Index<b><center><html>" + 10);
        UVIndexText.setBounds(335, 480, 85, 55);
        UVIndexText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(UVIndexText);

        //Creates main window as JPanel to customize
        JPanel mainPanel = new JPanel();
        Color background = Color.decode("#535C91");
        mainPanel.setBounds(0, 0, 450, 650);
        mainPanel.setBackground(background);
        mainPanel.setVisible(true);
        add(mainPanel);

        //Search button
        ImageIcon searchIcon = new ImageIcon(new ImageIcon("src\\main\\java\\com\\vince\\assets\\searchButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH ));
        JButton searchButton = new JButton(searchIcon);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //changes cursor to a hand cursor when hovering over the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String userInput = searchTextField.getText(); //gets location from user input
                
                if (userInput.replaceAll("\\s", "").length() <=0){ //removes whitespace 
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);

                String weatherCondition = (String) weatherData.get("weather_condition");
                int dayNight = (int) weatherData.get("is_day");

                switch(weatherCondition){
                    case "Sunny":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\clear.png"));
                        break;

                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\clearnight.png"));
                        break;

                    case "Mostly Sunny":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\clear.png"));
                        break;

                    case "Mostly Clear":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\clearnight.png"));
                        break;

                    case "Partly Cloudy":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\partlycloudy.png"));
                        }

                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\partlycloudynight.png"));
                        }
                        break;

                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\cloudy.png"));
                        break;

                    case "Foggy":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\fog.png"));
                        break;

                    case "Rime Fog":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\fog.png"));
                        break;

                    case "Light Drizzle":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        }
    
                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzlenight.png"));
                        }
                        break;

                    case "Drizzle":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        }
    
                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzlenight.png"));
                        }
                        break;
                    
                    case "Heavy Drizzle":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        }
    
                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzlenight.png"));
                        }
                        break;
                    
                    case "Light Freezing Drizzle":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        }
    
                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzlenight.png"));
                        }
                        break;

                    case "Freezing Drizzle":
                        if (dayNight == 1){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        }
    
                        else if (dayNight == 0){
                            weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        }
                        break;

                    case "Light Rain":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\rain.png"));
                        break;
                    
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\rain.png"));
                        break;

                    case "Heavy Rain":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavyrain.png"));
                        break;

                    case "Light Freezing Rain":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        break;

                    case "Freezing Rain":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\freezingrain.png"));
                        break;

                    case "Light Snow":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\snow.png"));
                        break;

                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;

                    case "Heavy Snow":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;

                    case "Light Showers":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\drizzle.png"));
                        break;

                    case "Showers":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\rain.png"));
                        break;

                    case "Heavy Showers":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavyrain.png"));
                        break;

                    case "Light Snow Showers":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;

                    case "Snow Showers":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\heavysnow.png"));
                        break;
                    
                    case "Thunderstorms":
                        weatherConditionImage.setIcon(loadImage("src\\\\main\\\\java\\\\com\\\\vince\\\\assets\\\\thunderstorm.png"));
                        break;
                }   

                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "°C");

                weatherConditionDesc.setText(weatherCondition);

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><center><b>Humidity<b><center>" + humidity + "%<html>");

                double windSpeed = (double) weatherData.get("wind_speed");
                windText.setText("<html><center><b>Wind<b><br><center>" + windSpeed + "km/h<html>");

                double visibility = (double) weatherData.get("visibility");
                visibilityText.setText("<html><center><b>Visibility<b><center>" + visibility + "km<html>");

                double UVIndex = (double) weatherData.get("uv_index");
                UVIndexText.setText("<html><center><b>UV Index<b><center>" + UVIndex + "<html>");

            }
        
        });
        searchButton.setBounds(360, 30, 47, 45);
        searchButton.setVisible(true);
        add(searchButton);

}       

    private ImageIcon loadImage(String resourcePath){
        try { 
            BufferedImage image = ImageIO.read(new File(resourcePath));

            return new ImageIcon(image);
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Couldn't find resource.");
        return null;
    }
}*/
