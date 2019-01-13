package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
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
import java.util.Optional;

public class GameEngine {

    private final WebEngine webEngine;
    private final WikipediaWebPage wikipediaWebPage;

    private int score = 0;
    private boolean hasBeenWon = false;
    private Page startPage = null;
    private Page current = null;
    private Page endPage = null;

    private List<Page> path = new ArrayList<>();

    private List<Controller> observers = new ArrayList<>();
    private String userName;

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

    private void newGameStarted() {
        for (var o : observers)
            o.OnNewGameStart();
    }

    private void win() {
        for (var o : observers)
            o.OnWin();
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
        if(!hasBeenWon)
        {
            score++;
            scoreChanged();
            if (current.url.equals(endPage.url))
            {
                hasBeenWon = true;
                win();
                showDialog();
            }
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
    public Page getRandomTargetPage() {
        try {
            Page page = wikipediaWebPage.getRandomTargetPage();
            return page;
        } catch (IOException e) {
            System.err.println("Page Error");
        }
        return null;
    }

    public void newGame() {
        path.clear();
        score = 0;
        hasBeenWon = false;
        startPage = getRandomPage();
        current = startPage;
        endPage = getRandomTargetPage(); //loadNewWikiPage("/wiki/Polska");
        path.add(startPage);


        newGameStarted();
        pathChanged();
        scoreChanged();
        startPageChanged();
        currentPageChanged();
        endPageChanged();

        webEngine.loadContent(startPage.html);
    }

    public void exit() {
        Platform.exit();
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
            if(!hasBeenWon)
            {
                score++;
                scoreChanged();
            }
            webEngine.loadContent(current.html);
        }
    }

    public void showDialog()
    {
        int oldScore = score;
        String oldStart = startPage.title;
        String oldEnd = endPage.title;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("You have won");
        alert.setHeaderText(null);
        alert.setContentText("You have won in " + score + " moves. Great job!" + System.lineSeparator());

        ButtonType new_game = new ButtonType("Start new game");
        ButtonType infinity = new ButtonType("Continue free exploration");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(new_game, infinity);

        Optional<ButtonType> result1 = alert.showAndWait();
        if(userName == null)
        {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Online Ranking");
            dialog.setContentText("Please enter your name to add your score to list:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> userName = name);
        }
        if(userName != null)
        {
            try
            {
                OnlineRanking.addScore(userName, oldScore, oldStart, oldEnd, "en");
            }
            catch (Exception e)
            {
                System.err.println("Error submitting score");
            }

        }
        if (result1.isPresent() && result1.get() == new_game){
            newGame();
        }

    }

    public void freeExploration()
    {
        hasBeenWon = true;
    }

    public void loadTarget(WebEngine engine) {
        engine.loadContent(endPage.html);
    }
}
