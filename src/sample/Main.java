package sample;

import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;


import static javafx.concurrent.Worker.State;

public class Main extends Application {

    @Override
    public void start(final Stage stage) {
        try {
            Parent root = FXMLLoader.load(Controller.class.getResource("sample.fxml"));
            WebView webView = (WebView) root.getChildrenUnmodifiable().get(0);
            GameEngine engine = new GameEngine(webView,"en");
            engine.loadNewWikiPage("/wiki/Cat");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
