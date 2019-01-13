package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


import static java.lang.Double.max;

public class TreeNode {
    final double radiusDelta = 1.0;
    double radius;

    private Circle outerCircle;
    private Circle innerCircle;
    private Text text;

    private double circleX;
    private double circleY;

    public int depth = 0;
    public int width = 0;

    public double leftDelta = 0;


    private TreeNode upperTreeNode = null;
    private TreeNode rightTreeNode = null;

    private TreeNode prevTreeNode = null;


    public TreeNode(double circleX, double circleY, double leftDelta, double radius, String message) {
        this.radius = radius;

        outerCircle = new Circle(radius);
        innerCircle = new Circle(radius - radiusDelta);

        this.circleX = circleX;
        this.circleY = circleY;

        outerCircle.setCenterX(circleX);
        outerCircle.setCenterY(circleY);

        innerCircle.setCenterX(circleX);
        innerCircle.setCenterY(circleY);

        text = new Text(message);

        text.setX(circleX + radius + 2);
        text.setY(circleY - 7);

        text.setVisible(false);

        innerCircle.setOnMouseEntered(e -> text.setVisible(true));
        innerCircle.setOnMouseExited(e -> text.setVisible(false));

        outerCircle.setFill(Color.BLACK);
        innerCircle.setFill(Color.WHITE);

        this.leftDelta = leftDelta;
    }

    public Circle getOuterCircle() {
        return outerCircle;
    }

    public double getCircleX() {
        return circleX;
    }

    public double getCircleY() {
        return circleY;
    }

    public Circle getInnerCircle() {
        return innerCircle;
    }

    public Text getText() {
        return text;
    }

    public void setRightTreeNode(TreeNode rightTreeNode) {
        this.rightTreeNode = rightTreeNode;
    }

    public TreeNode getRightTreeNode() {
        return rightTreeNode;
    }

    public void setUpperTreeNode(TreeNode upperTreeNode) {
        this.upperTreeNode = upperTreeNode;
    }

    public TreeNode getUpperTreeNode() {
        return upperTreeNode;
    }

    public void setPrevTreeNode(TreeNode treeNode) {
        this.prevTreeNode = treeNode;
    }

    public TreeNode getPrevTreeNode() {
        return prevTreeNode;
    }

    public void setActive(boolean activate) {
        if (activate) {
            outerCircle.setFill(Color.RED);
        } else {
            outerCircle.setFill(Color.BLACK);
        }
    }

    public double getRightLength() {
        if (rightTreeNode == null && upperTreeNode == null) {
            return 0;
        } else if (rightTreeNode != null && upperTreeNode == null) {
            return rightTreeNode.getCircleX() - circleX + rightTreeNode.getRightLength();
        } else if (rightTreeNode == null && upperTreeNode != null) {
            return upperTreeNode.getRightLength();
        } else {
            return max(rightTreeNode.getCircleX() - circleX + rightTreeNode.getRightLength(), upperTreeNode.getRightLength());
        }
    }
}
