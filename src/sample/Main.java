package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage stage) {
        try {
            Parent root = FXMLLoader.load(Controller.class.getResource("sample.fxml"));
            WebView webView = (WebView) root.getChildrenUnmodifiable().get(0);
            GameEngine engine = new GameEngine(webView,"pl");
            Controller.init(engine);
            engine.loadNewWikiPage("/wiki/Cat");
            stage.setScene(new Scene(root));
            stage.show();
            engine.newGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
