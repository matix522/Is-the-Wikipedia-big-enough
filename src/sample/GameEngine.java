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
import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private final WebEngine webEngine;
    private final WikipediaWebPage wikipediaWebPage;

    private int score = 0;
    private Page startPage = null; /// <URL,HTML>
    private Page current = null; /// <URL,HTML>
    private Page endPage = null; /// <URL,HTML>

    private List<Page> path = new ArrayList<>();

    private List<Controller> observers = new ArrayList<>();

    public void addObserver(Controller controller) {
        observers.add(controller);
    }

    private void startPageChanged() {
        for (var o : observers)
            o.OnStartPageChanged(startPage);
    }

    private void currentPageChanged() {
        for (var o : observers)
            o.OnCurrentPageChanged(current);
    }

    private void endPageChanged() {
        for (var o : observers)
            o.OnEndPageChanged(endPage);
    }

    private void pathChanged() {
        for (var o : observers)
            o.OnPathChanged(path);
    }

    private void scoreChanged() {
        for (var o : observers)
            o.OnScoreChanged(score);
    }

    public GameEngine(WebView view, String language) {
        webEngine = view.getEngine();
        wikipediaWebPage = new WikipediaWebPage(language);

        setUpHyperlinkListener();

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {

        });
    }

    public void setUpHyperlinkListener() {
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                EventListener listener = ev -> {
                    String href = ((Element) ev.getTarget()).getAttribute("href");
                    if (href != null) {
                        handleHyperlinkEvent(href);
                    }
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
        if (current == null) return;
        path.add(current);
        currentPageChanged();
        pathChanged();
        score++;
        scoreChanged();
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
        path.clear();
        score = 0;
        startPage = getRandomPage();
        current = startPage;
        endPage = loadNewWikiPage("/wiki/Zebra");
        path.add(startPage);

        pathChanged();
        scoreChanged();
        startPageChanged();
        currentPageChanged();
        endPageChanged();

        webEngine.loadContent(startPage.html);
    }

    public void exit() {
        System.exit(0);
    }


    public Page loadNewWikiPage(String href) {
        if (href.startsWith("http")) {
            try {
                System.out.println(wikipediaWebPage.loadPage(href).html);

            } catch (IOException e) {
            }
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

    public void undoMove() {
        if (path.size() > 1) {
            current = path.get(path.size() - 2);
            path.remove(current);
            currentPageChanged();
            pathChanged();
            score++;
            scoreChanged();
            webEngine.loadContent(current.html);
        }
    }
}
