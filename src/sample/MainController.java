package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
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
    Button howToPlay;
    @FXML
    Button back;
    @FXML
    WebView webView;
    @FXML
    TextArea help;

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
    @FXML
    void getHelp(){
        howToPlay.setDisable(true);
        howToPlay.setVisible(false);
        button.setDisable(true);
        button.setVisible(false);
        back.setVisible(true);
        back.setDisable(false);
        help.setVisible(true);
        help.setDisable(false);
    }
    @FXML
    void backToMenu(){
        howToPlay.setDisable(false);
        howToPlay.setVisible(true);
        button.setDisable(false);
        button.setVisible(true);
        back.setVisible(false);
        back.setDisable(true);
        help.setVisible(false);
        help.setDisable(true);
    }

}
