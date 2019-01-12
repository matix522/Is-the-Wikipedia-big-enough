package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.Scanner;

public class WikipediaWebPage {
    private String wikipediaLink;
    private String wikiRandomLink;

    public WikipediaWebPage(String language) {
        try{
            wikipediaLink = "https://" + language + ".wikipedia.org";
            Scanner reader = new Scanner(new File("links.txt"));
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                if(line.split(" ")[0].equals(language))
                {
                    wikiRandomLink = line.split(" ")[1];
                    break;
                }
            }
            if(wikiRandomLink == null)
            {
                wikipediaLink = "https://en.wikipedia.org";
                wikiRandomLink = "/wiki/Special:Random";
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    public String loadPage(String pageLink) throws IOException {

        return getPage(pageLink).html()
                .replace("/w/", wikipediaLink+"/w/")
                .replace("//upload.", "https://upload.");
    }

    public String getRandomPage() throws IOException {
        return loadPage(wikiRandomLink);
    }

}
