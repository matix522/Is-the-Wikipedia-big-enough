package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnlineRanking
{

    public static void addScore(String name, int score, String start_article, String end_article, String lang) throws IOException
    {
        sendGet("http://bigenough.5v.pl/insert.php?name=" +name
                +"&score="+score+"&start_article="+start_article+"&end_article="+end_article
        +"&language="+lang);
}

    public static String getRanking() throws IOException
    {
        return sendGet("http://bigenough.5v.pl/get.php");
    }

    private static String sendGet(String url) throws IOException
    {
        String res = "";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            res = response.toString();
        }
        return res;
    }
}
