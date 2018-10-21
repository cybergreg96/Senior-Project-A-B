package PacManLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
/**
 * Created by Arash Abedin on 9-03-2017.
 */
public class MazePlayGround implements GameObject {
    private Color color;
    private int x;
    private int y;
    private MapOutline map;
    public Biscuits[][] biscuits = new Biscuits[40][40];
    private Player player;
    private Image gridImg;
    private BackgroundImage bgImg;
    public MazePlayGround(int x, int y, Player player, MapOutline map) {
        //Hard-coded background image
    	try {
			this.gridImg = new Image(new FileInputStream("src/PacManImgs/Pac-Grid.png"), 560, 660, false, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.x = x;
        this.y = y;
        this.player = player;
        this.map = map;
    }



    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public Image getImg() {
    	return gridImg;
    }

    public void update(KeyCode keyCode) {

        // There is nothing special has to be updated in the playground yet





    }


    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
    	
    	g.drawImage(gridImg, x, y);

    }



}