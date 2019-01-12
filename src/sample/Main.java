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
            Controller controller = new Controller();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
            fxmlLoader.setController(controller);
            Parent root = fxmlLoader.load();
            WebView webView = (WebView) root.getChildrenUnmodifiable().get(0);
            GameEngine engine = new GameEngine(webView,"pl");
            controller.init(engine);
            engine.addObserver(controller);
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
