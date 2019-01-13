package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private GameEngine gameEngine;
    private int interval = 0;

    @FXML
    private Pane pane;

    final double change = 60.0;
    final double radius = 11.0;

    private int treeDepth = 0;
    private int treeWidth = 0;

    private double sizeY = 200;
    private double sizeX = 275;

    private double viewPointX = sizeX / 2;
    private double viewPointY = sizeY / 2;

    private int prevSize = 0;
    private TreeNode root;
    private TreeNode pnode;


    public void init(GameEngine engine)
    {
        gameEngine = engine;
    }

    @FXML
    private ImageView imageView;
    @FXML
    private WebView targetView;
    @FXML
    private WebView view;
    @FXML
    private TextArea startArticle;
    @FXML
    private TextArea currentArticle;
    @FXML
    private TextArea endArticle;
    @FXML
    private TextArea score;
    @FXML
    private TextArea time;
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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                interval++;
                time.setText("Time: " + interval);
            }

        }, 0,1000);
        targetView.setVisible(false);
        targetView.setDisable(false);
        endArticle.setOnMouseEntered(e -> {targetView.setDisable(false); targetView.setVisible(true); view.setVisible(false);});
        endArticle.setOnMouseExited(e -> {targetView.setDisable(false); targetView.setVisible(false); view.setVisible(true);});

    }

    @FXML
    public void newGameButtonClick()
    {
        pane.getChildren().clear();
        sizeX = 275;
        sizeY = 200;
        root = null;
        pnode = null;
        prevSize = 0;
        treeDepth = 0;
        treeWidth = 0;
        viewPointX = sizeX / 2;
        viewPointY = sizeY / 2;
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
        if (newValue!=null)
            startArticle.setText("Start article: " + System.lineSeparator() + newValue.title);
    }

    public void OnCurrentPageChanged(Page newValue)
    {
        if (newValue!=null)
            currentArticle.setText("Current article: " + System.lineSeparator() + newValue.title);
    }

    public void OnEndPageChanged(Page newValue)
    {
        if (newValue!=null)
            endArticle.setText("Target: " + System.lineSeparator() + newValue.title);
    }

    public void OnPathChanged(List<Page> newValue) {
        if (newValue.size() < 2)
            undoButton.setDisable(true);
        else
            undoButton.setDisable(false);

        String msg = newValue.get(newValue.size() - 1).title;

        if (prevSize == 0) {
            root = new TreeNode(20,20, 0, radius, msg);
            root.setActive(true);
            pnode = root;
        } else {
            if (prevSize < newValue.size()) {
                if (pnode.getUpperTreeNode() == null) {
                    TreeNode treeNode = new TreeNode(pnode.getCircleX(), pnode.getCircleY() + change, 0, radius, msg);
                    treeNode.setPrevTreeNode(pnode);
                    treeNode.depth = pnode.depth + 1;
                    if (treeDepth < treeNode.depth) {
                        sizeY += change;
                    }
                    pnode.setUpperTreeNode(treeNode);
                    pnode.setActive(false);
                    treeNode.setActive(true);
                    pnode = treeNode;
                    viewPointY = pnode.getCircleY();
                } else {
                    TreeNode lastRightTreeNode = pnode;
                    while (lastRightTreeNode.getRightTreeNode() != null) {
                        lastRightTreeNode = lastRightTreeNode.getRightTreeNode();
                    }

                    double blength = lastRightTreeNode.getRightLength();
                    TreeNode treeNode = new TreeNode(lastRightTreeNode.getCircleX() + blength + change, lastRightTreeNode.getCircleY(), blength, radius, msg);
                    treeNode.setPrevTreeNode(pnode);
                    treeNode.width = pnode.width + 1;
                    if (treeWidth < treeNode.width) {
                        sizeX += change;
                    }
                    lastRightTreeNode.setRightTreeNode(treeNode);
                    pnode.setActive(false);
                    treeNode.setActive(true);
                    pnode = treeNode;
                    viewPointX = pnode.getCircleX();
                }
            } else {
                pnode.setActive(false);
                pnode = pnode.getPrevTreeNode();
                pnode.setActive(true);
                viewPointX = pnode.getCircleX();
                viewPointY = pnode.getCircleY();
            }
        }

        prevSize = newValue.size();
        displayNodes();
    }

    public void OnScoreChanged(int newValue){

        score.setText("Score: " + newValue);
    }

    public void OnNewGameStart()
    {
        interval = 0;
        gameEngine.loadTarget(targetView.getEngine());
    }

    @FXML
    private ScrollPane scrollPane;
    @FXML
    public void displayNodes() {
        pane.getChildren().clear();

        pane.setPrefHeight(sizeY);
        pane.setPrefWidth(sizeX);

        scrollPane.layout();

        scrollPane.setVvalue(invLerp(scrollPane.getHeight() / 2.0, sizeY - scrollPane.getHeight() / 2.0, viewPointY));
        scrollPane.setHvalue(invLerp(scrollPane.getWidth() / 2.0, sizeX - scrollPane.getWidth() / 2.0, viewPointX));

        displayNode(root);
    }

    private void displayNode(TreeNode treeNode) {
        pane.getChildren().add(treeNode.getText());
        pane.getChildren().add(treeNode.getOuterCircle());
        pane.getChildren().add(treeNode.getInnerCircle());

        if (treeNode.getUpperTreeNode() != null) {
            displayNode(treeNode.getUpperTreeNode());
            pane.getChildren().add(new Line(treeNode.getCircleX(), treeNode.getCircleY() + radius, treeNode.getCircleX(), treeNode.getCircleY() + change - radius));
        }

        TreeNode rightTreeNode = treeNode.getRightTreeNode();
        if (rightTreeNode != null) {
            displayNode(rightTreeNode);
            pane.getChildren().add(new Line(treeNode.getCircleX() + radius, rightTreeNode.getCircleY(), rightTreeNode.getCircleX() - radius, rightTreeNode.getCircleY()));
        }
    }

    private double invLerp(double a, double b, double v)
    {
        return (v - a) / (b - a);
    }
}

