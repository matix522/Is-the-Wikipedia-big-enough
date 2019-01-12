package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
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

    private final WebView webView;
    private final WebEngine webEngine;
    private final WikipediaWebPage wikipediaWebPage;

    public GameEngine(final Stage stage, String language) {
        webView = new WebView();
        webEngine = webView.getEngine();
        wikipediaWebPage = new WikipediaWebPage("https://" + language + ".wikipedia.org");

        setUpHyperlinkListener();

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {

                //stage.setTitle(webEngine.getLocation());

                stage.setTitle(webEngine.getTitle().replace(" - Wikipedia", ""));

            }
        });


        // Create the VBox

        VBox root = new VBox();

        // Add the WebView to the VBox

        root.getChildren().add(webView);


        // Set the Style-properties of the VBox

        root.setStyle("-fx-padding: 10;" +

                "-fx-border-style: solid inside;" +

                "-fx-border-width: 2;" +

                "-fx-border-insets: 5;" +

                "-fx-border-radius: 5;" +

                "-fx-border-color: blue;");


        // Create the Scene

        Scene scene = new Scene(root);

        // Add  the Scene to the Stage

        stage.setScene(scene);

        // Display the Stage

        stage.show();

    }

    public void setUpHyperlinkListener() {
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                EventListener listener = ev -> {
                    String href = ((Element) ev.getTarget()).getAttribute("href");
                    loadNewWikiPage(href);
                };

                Document doc = webEngine.getDocument();
                NodeList lista = doc.getElementsByTagName("a");
                for (int i = 0; i < lista.getLength(); i++)
                    ((EventTarget) lista.item(i)).addEventListener("click", listener, false);
            }
        });
    }

    public void loadNewWikiPage(String href) {
        if (href.startsWith("http")) {
            throw new RuntimeException("Link outside of Wikipedia");
        }
        try {
            webEngine.loadContent(wikipediaWebPage.loadPage(href));
        } catch (IOException e) {
            System.err.println("Page not Found: " + href);
        }
    }
}
