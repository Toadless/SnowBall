package uk.toadl3ss.lavalite.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Http
{
    private Http()
    {

    }

    public static String getReq(String URL)
    {
        try {
            URL url = new URL(URL);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            String inputLine;

            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }
}