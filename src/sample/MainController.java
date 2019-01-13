package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.Optional;


public class MainController {

    Scene scene;
    Parent secondRoot;
    GameEngine secondEngine;
    @FXML
    AnchorPane anchorPane;
    @FXML
    ImageView imageView;
    @FXML
    Button button;
    @FXML
    WebView webView;

    void initialize(String html, Scene scene, Parent sencondRoot, GameEngine secondEngine){
        webView.getEngine().loadContent(html);
        BoxBlur blur = new BoxBlur();
        blur.setWidth(5);
        blur.setHeight(5);
        blur.setIterations(2);
        webView.setEffect(blur);
        File file = new File("./images/logo_t.png");
        Image img = new Image(file.toURI().toString());
        imageView.setImage(img);        this.scene = scene;
        this.secondRoot = sencondRoot;
        this.secondEngine = secondEngine;
    }
    @FXML
    void newGame(){
        scene.setRoot(secondRoot);
        secondEngine.newGame();
    }

}
