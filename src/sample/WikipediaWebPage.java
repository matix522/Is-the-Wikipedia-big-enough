package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class WikipediaWebPage {
    private String wikipediaLink = "https://en.wikipedia.org";

    public WikipediaWebPage(String wikipediaLink) {
        this.wikipediaLink = wikipediaLink;
    }
    private Document getPage(String pageLink)throws IOException{
        Document doc = Jsoup.connect(wikipediaLink + pageLink).get();
        for (var element : doc.body().children()) {
            if (!element.id().equals("content")) {
                element.remove();
            } else {
                for (var contentElement : element.children()) {
                    if (!(contentElement.id().equals("firstHeading") || contentElement.id().equals("bodyContent"))) {
                        contentElement.remove();
                    }
                }
            }
        }
        doc.select("#catlinks").remove();
        Element styleNode = doc.createElement("style");
        doc.head().appendChild(styleNode);
        styleNode.append(".mw-body{margin:0}");
        return doc;
    }
    String loadPage(String pageLink) throws IOException {

        return getPage(pageLink).html()
                .replace("/w/", wikipediaLink+"/w/")
                .replace("//upload.", "https://upload.");
    }
}
