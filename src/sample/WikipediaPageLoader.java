package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class WikipediaPageLoader {
    private static String wikipediaLink = "https://en.m.wikipedia.org";
    private static String marginRemoval = ".mw-body{margin:0}";

    WikipediaPageLoader() {

    }

    String loadPage(String pageLink) throws IOException {
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
        styleNode.append(marginRemoval);
        System.out.println(doc.html());
        return doc.html()
                .replace("/w/", wikipediaLink+"/w/")
                .replace("//upload.", "https://upload.");
    }
}
