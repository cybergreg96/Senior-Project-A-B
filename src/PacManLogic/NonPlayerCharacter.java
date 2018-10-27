package PacManLogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//import javafx.scene.paint.Color;

public class NonPlayerCharacter implements GameObject {
	public enum Powerup {
		DOUBLE_POINTS;
	    /**
	     * Pick a random value of the Powerup enum.
	     * @return a random Powerup.
	     */
	    public static Powerup getRandomPower() {
	        Random random = new Random();
	        return values()[random.nextInt(values().length)];
	    }
	};
	private Image npcImg, eatenImg;
    private int x;
    private int y;
    private MapOutline map;
    private Player player;
    private boolean obtained;
    private int timer = 0;
    private Powerup power = null;
    
    // Colors
    private int red;
    private int green;
    private int blue;
    
    public NonPlayerCharacter(Player player, MapOutline map){
    	try {
			npcImg = new Image(new FileInputStream("src/PacManImgs/NPC.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			eatenImg = new Image(new FileInputStream("src/PacManImgs/EatenImage.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.setX(x);
        this.setY(y);
        this.player = player;
        this.map = map;
        this.obtained = true;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    
    public int getTimer() {
    	return timer;
    }
    
    public void setTimer(int time) {
    	timer = time;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public Powerup getPower() {
    	return this.power;
    }
    
    public int selectRandom(int bound) {
    	Random rng = new Random();
    	return rng.nextInt(bound);
    }
	
	@Override
    public void update(KeyCode keyCode) {
	}
	
    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
    	// Check to see if timer still going.
    	// if timer still going, decrement
    	if(obtained && timer > 0) {
    		setTimer(timer - 1);
    	}
    	
    	// Variables to create random point on map for NPC
        int x = selectRandom(map.points[0].length);
        int y = selectRandom(map.points.length);
        
        // Check if game needs to recreate NPC
        if((map.points[y][x] == 1) && obtained && timer == 0) {
        	this.power = null;
        	setX(x);
        	setY(y);
            obtained = false;
            red = 255;
            green = 0;
            blue = 255;
        }
        
        // Detect if player steps over NPC
        if((player.getStepX() == getX()) && (player.getStepY() == getY())) {
        	setX(0);
        	setY(0);
        	red = 0;
        	green = 0;
        	blue = 0;
        	g.drawImage(eatenImg, (getX() * 20), (getY() * 20));
        	this.power = Powerup.getRandomPower();
        	obtained = true;
        	setTimer(25);
        } else {
            // Draw NPC
            g.drawImage(npcImg, (getX() * 20), (getY() * 20));
        }
    }
}