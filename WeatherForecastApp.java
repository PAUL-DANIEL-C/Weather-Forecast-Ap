// WeatherForecastApp.java

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherForecastApp {

    // Replace this with your actual OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY_HERE";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== Weather Forecast App ====");
        System.out.print("Enter city name: ");
        String city = scanner.nextLine().trim();

        try {
            String response = getWeatherData(city);
            if (response != null) {
                parseAndDisplay(response);
            } else {
                System.out.println("Unable to fetch weather data.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String getWeatherData(String city) throws IOException {
        String endpoint = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + API_KEY + "&units=metric";

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();

        if (responseCode == 200) { // OK
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            return response.toString();
        } else {
            System.out.println("HTTP error code: " + responseCode);
            return null;
        }
    }

    public static void parseAndDisplay(String jsonResponse) {
        JSONObject obj = new JSONObject(jsonResponse);

        String city = obj.getString("name");
        JSONObject main = obj.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = obj.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("\nWeather in " + city + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Condition: " + description);
        System.out.println("Humidity: " + humidity + "%");
    }
}
