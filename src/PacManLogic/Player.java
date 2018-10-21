package PacManLogic;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Arash Abedin on 9-03-2017.
 */

public class Player implements GameObject{
  //  ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    private int X;
    private int Y;

    private MapOutline  map;
    private Image playerImg, imgLeft, imgRight;
    private int stepX;
    private int stepY;
    int lastMove = 0;
    public static int moveNumbers = 0;



    public Player(Point position, MapOutline map)
    {
    	try {
			imgLeft = new Image(new FileInputStream("src/PacManImgs/GoodBird.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			imgRight = new Image(new FileInputStream("src/PacManImgs/GoodBirdRight.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	playerImg = imgLeft;
        X = position.x;
        Y = position.y;
        this.map = map;
        setStepX(getX()/20);
        setStepY(getY()/20);
    }
    public void repeatLastMove(){
        switch(lastMove){
            case 0: break;
            case 1:
                if((map.points[getStepY()+1][getStepX()]==1)||(map.points[getStepY()+1][getStepX()]==3))
                {
                    moveDown();break;
                } else
                    {
                    break;
                }

            case 2:

                if((map.points[getStepY()][getStepX()-1]==1)||(map.points[getStepY()][getStepX()-1]==3))
                {
                playerImg = imgLeft;
                moveLeft();

                break;
            }else{
                    break;
                }

            case 3:    if((map.points[getStepY()][getStepX()+1]==1)||(map.points[getStepY()][getStepX()+1]==3)) {
            	playerImg = imgRight;
                moveRight(); 
                break;
            }else
            {
                break;
            }

            case 4:  if((map.points[getStepY()-1][getStepX()]==1)||(map.points[getStepY()-1][getStepX()]==3))
            {
                moveUp(); break;
            } else
                {
                    break;
                }
        }
    }

    public void moveDown(){
        if((map.points[getStepY()+1][getStepX()]==1)||(map.points[getStepY()+1][getStepX()]==3)){
            setY(getY() + 20);
            setStepY(getStepY()+1);
            lastMove =1;
            moveNumbers ++;
        }else{
            repeatLastMove();
        }
    }
    public void moveLeft(){
    	playerImg = imgLeft;
        if((map.points[getStepY()][getStepX()-1]==1)||(map.points[getStepY()][getStepX()-1]==3)){
            setX(getX() - 20);
            setStepX(getStepX()-1);
            lastMove = 2;
            moveNumbers ++;
            if ((this.getX()==0) && (this.getY()==300)){
                this.setX(520);
                setStepX(26);
            }
        }else{
          repeatLastMove();
        }
    }
    public void moveRight(){
    	playerImg = imgRight;
        if((map.points[getStepY()][getStepX()+1]==1)||(map.points[getStepY()][getStepX()+1]==3)) {

            setX(getX() + 20);
            setStepX(getStepX()+1);
            lastMove = 3;
            moveNumbers ++;
            if ((this.getX()==540) && (this.getY()==300)){
                this.setX(20);
                setStepX(1);
            }
        }else{
          repeatLastMove();
        }

    }
    public void moveUp(){
        if((map.points[getStepY()-1][getStepX()]==1)||(map.points[getStepY()-1][getStepX()]==3)){
            setY(getY() -20);
            setStepY(getStepY()-1);
            lastMove =4;
            moveNumbers ++;
        }else{
          repeatLastMove();
        }
    }


    @Override
    public void update(KeyCode keyCode) {

        switch (keyCode)
        {

            case S:

                moveDown();
                break;

            case A:

                moveLeft();
                break;

            case D:

                moveRight();
                break;
            case W:
                moveUp();

                break;
        }

    }


    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
        g.drawImage(playerImg, this.getX(), this.getY());
    }


    public int getX() {
        return X;
    }

    public void setX(int x) { X = x; }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
    public int getStepX() {
        return stepX;
    }

    public void setStepX(int stepX) {

        this.stepX = stepX;
    }

    public int getStepY() {
        return stepY;
    }

    public void setStepY(int stepY) {
        this.stepY = stepY;
    }
}
