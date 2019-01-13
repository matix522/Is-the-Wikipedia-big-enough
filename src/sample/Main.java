package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage stage) {
        try {

            FXMLLoader mainFxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Parent mainRoot = mainFxmlLoader.load();

            MainController mainController = mainFxmlLoader.getController();

            GameEngine mainGameEngine = new GameEngine((WebView) mainRoot.getChildrenUnmodifiable().get(0), "en");

            Scene scene = new Scene(mainRoot);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = fxmlLoader.load();
            Controller controller = fxmlLoader.getController();

            WebView webView = (WebView) root.getChildrenUnmodifiable().get(0);

            GameEngine engine = new GameEngine(webView, "pl");

            controller.init(engine);
            engine.addObserver(controller);

            mainController.initialize(mainGameEngine.loadNewWikiPage("/wiki/Main_Page").html, scene, root, engine);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
