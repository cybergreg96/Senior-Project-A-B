package PacManLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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
    public MazePlayGround(int x, int y, Player player, MapOutline map) {
        try {
			this.gridImg = new Image(new FileInputStream("src/PacManImgs/Pac-Grid.png"));
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
        /*g.setFill(this.getColor());

        for (int i = 0; i < map.points.length; i++) {
            for (int j = 0; j < map.points[i].length; j++) {
                if (map.points[i][j] == 0) {
                    g.setFill(Color.rgb(0, 0, 35));
                    g.fillRoundRect(j * 20, i * 20, 20, 20, 0, 0);
                }

            }
        }*/
    }



}