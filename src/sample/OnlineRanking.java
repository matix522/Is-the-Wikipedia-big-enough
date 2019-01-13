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
        name = escapeHTML(name);
        start_article = escapeHTML(start_article);
        end_article = escapeHTML(end_article);
        lang = escapeHTML(lang);
        sendGet("http://bigenough.5v.pl/insert.php?name=" +name
                +"&score="+score+"&start_article="+start_article+"&end_article="+end_article
        +"&language="+lang);
    }

    public static String getRanking() throws IOException
    {
        return sendGet("http://bigenough.5v.pl/get.php");
    }

    public static String getRanking(String start_article, String end_article, String lang) throws IOException
    {
        return sendGet("http://bigenough.5v.pl/get.php?start_article="+start_article
            + "&end_article=" + end_article + "&language="+ lang);
    }

    public static String getRankingFiltered() throws IOException
    {
        return sendGet("http://bigenough.5v.pl/table.php");
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

    private static final String escapeHTML(String s){
        StringBuffer sb = new StringBuffer();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '&': sb.append("&amp;"); break;
                case '"': sb.append("&quot;"); break;
                case 'à': sb.append("&agrave;");break;
                case 'À': sb.append("&Agrave;");break;
                case 'â': sb.append("&acirc;");break;
                case 'Â': sb.append("&Acirc;");break;
                case 'ä': sb.append("&auml;");break;
                case 'Ä': sb.append("&Auml;");break;
                case 'å': sb.append("&aring;");break;
                case 'Å': sb.append("&Aring;");break;
                case 'æ': sb.append("&aelig;");break;
                case 'Æ': sb.append("&AElig;");break;
                case 'ç': sb.append("&ccedil;");break;
                case 'Ç': sb.append("&Ccedil;");break;
                case 'é': sb.append("&eacute;");break;
                case 'É': sb.append("&Eacute;");break;
                case 'è': sb.append("&egrave;");break;
                case 'È': sb.append("&Egrave;");break;
                case 'ê': sb.append("&ecirc;");break;
                case 'Ê': sb.append("&Ecirc;");break;
                case 'ë': sb.append("&euml;");break;
                case 'Ë': sb.append("&Euml;");break;
                case 'ï': sb.append("&iuml;");break;
                case 'Ï': sb.append("&Iuml;");break;
                case 'ô': sb.append("&ocirc;");break;
                case 'Ô': sb.append("&Ocirc;");break;
                case 'ö': sb.append("&ouml;");break;
                case 'Ö': sb.append("&Ouml;");break;
                case 'ø': sb.append("&oslash;");break;
                case 'Ø': sb.append("&Oslash;");break;
                case 'ß': sb.append("&szlig;");break;
                case 'ù': sb.append("&ugrave;");break;
                case 'Ù': sb.append("&Ugrave;");break;
                case 'û': sb.append("&ucirc;");break;
                case 'Û': sb.append("&Ucirc;");break;
                case 'ü': sb.append("&uuml;");break;
                case 'Ü': sb.append("&Uuml;");break;
                case '®': sb.append("&reg;");break;
                case '©': sb.append("&copy;");break;
                case '€': sb.append("&euro;"); break;
                case ' ': sb.append("&nbsp;");break;

                default:  sb.append(c); break;
            }
        }
        return sb.toString();
    }
}
