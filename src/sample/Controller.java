package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;

public class Controller {

    private GameEngine gameEngine;

    public void init(GameEngine engine)
    {
        gameEngine = engine;
    }

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea startArticle;
    @FXML
    private TextArea currentArticle;
    @FXML
    private TextArea endArticle;
    @FXML
    private TextArea score;

    @FXML
    private Button newGameButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize(){
        File file = new File("./images/logo.png");
        Image img = new Image(file.toURI().toString());
        imageView.setImage(img);
    }

    @FXML
    public void newGameButtonClick()
    {
        gameEngine.newGame();
    }

    @FXML
    public void undoButtonClick()
    {
        gameEngine.undoMove();
    }

    @FXML
    public void exitButtonClick()
    {
        gameEngine.exit();
    }


    public void OnStartPageChanged(Page newValue)
    {
        startArticle.setText("Start article: " + System.lineSeparator() + newValue.title);
    }

    public void OnCurrentPageChanged(Page newValue)
    {
        currentArticle.setText("Current article: " + System.lineSeparator() + newValue.title);
    }

    public void OnEndPageChanged(Page newValue)
    {
        endArticle.setText("Target: " + System.lineSeparator() + newValue.title);
    }

    public void OnPathChanged(List<Page> newValue)
    {
        if(newValue.size() < 2)
            undoButton.setDisable(true);
        else
            undoButton.setDisable(false);

    }

    public void OnScoreChanged(int newValue)
    {
        score.setText("Score: " + newValue);
    }
}

