package PacManGUI;

import javafx.scene.canvas.Canvas;

import java.awt.*;
import java.util.Random;

/**
 * Created by Arash Abedin on 9-03-2017.
 */
public class SceneInfo {
    private double fieldHeight;
    private double fieldWidth;
    private int width =  20;
    private int height = 20;
    private Random random = new Random();

    public SceneInfo(Canvas canvas)
    {
        fieldHeight = canvas.getHeight() / height;
        fieldWidth =canvas.getWidth() / width;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(double fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(double fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getRandomPoint() {
        return new Point(random.nextInt(width), random.nextInt(height));
    }
}


