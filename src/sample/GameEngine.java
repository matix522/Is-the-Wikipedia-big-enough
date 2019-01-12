package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.io.IOException;

public class GameEngine {

    private final WebEngine webEngine;
    private final WikipediaWebPage wikipediaWebPage;

    private int score = 0;
    private Page startPage = null; /// <URL,HTML>
    private Page current = null; /// <URL,HTML>
    private Page endPage = null; /// <URL,HTML>

    public GameEngine(WebView view, String language) {
        webEngine = view.getEngine();
        wikipediaWebPage = new WikipediaWebPage(language);

        setUpHyperlinkListener();

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {

          /*  if (newState == Worker.State.SUCCEEDED) {

                //stage.setTitle(webEngine.getLocation());

                stage.setTitle(webEngine.getTitle().replace(" - Wikipedia", ""));

            }*/
        });
    }

    public void setUpHyperlinkListener() {
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                EventListener listener = ev -> {
                    String href = ((Element) ev.getTarget()).getAttribute("href");
                    handleHyperlinkEvent(href);
                };

                Document doc = webEngine.getDocument();
                NodeList lista = doc.getElementsByTagName("a");
                for (int i = 0; i < lista.getLength(); i++)
                    ((EventTarget) lista.item(i)).addEventListener("click", listener, false);
            }
        });
    }

    public void handleHyperlinkEvent(String href) {
        current = loadNewWikiPage(href);
        score++;
        if (current.url.equals(endPage.url)) {
            System.out.println("you have won with score: " + score);
        }
    }

    public Page getRandomPage() {
        try {
            Page page = wikipediaWebPage.getRandomPage();
            return page;
        } catch (IOException e) {
            System.err.println("Page Error");
        }
        return null;
    }

    public void newGame() {
        score = 0;
        startPage = getRandomPage();
        current = startPage;
        endPage = loadNewWikiPage("/wiki/Adolf_Hitler");

        webEngine.loadContent(startPage.html);
        System.out.println(endPage.title);
    }

    public void exit() {
        System.exit(0);
    }

    public int getScore() {
        return score;
    }

    public Page loadNewWikiPage(String href) {
        if (href.startsWith("http")) {
            throw new RuntimeException("Link outside of Wikipedia");
        }
        try {
            var page = wikipediaWebPage.loadPage(href);
            webEngine.loadContent(page.html);
            return page;
        } catch (IOException e) {
            System.err.println("Page not Found: " + href);
        }
        return null;
    }
}
