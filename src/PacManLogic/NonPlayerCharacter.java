package PacManLogic;

import java.util.Random;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

//import javafx.scene.paint.Color;

public class NonPlayerCharacter implements GameObject {
    private int x;
    private int y;
    private MapOutline map;
    private Player player;
    private boolean obtained;
    private int timer = 0;
    
    // Colors
    private int red;
    private int green;
    private int blue;
    
    public NonPlayerCharacter(Player player, MapOutline map){

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

    public int selectRandom(int bound) {
    	Random rng = new Random();
    	return rng.nextInt(bound);
    }
	
	@Override
    public void update(KeyCode keyCode) {
	}
	
    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
    	if(obtained && timer > 0) {
    		setTimer(timer - 1);
    	}
    	
        int x = selectRandom(map.points[0].length);
        int y = selectRandom(map.points.length);
        
        // Check if needs to recreate NPC
        if((map.points[y][x] == 1) && obtained && timer == 0) {
        	setX(x);
        	setY(y);
            obtained = false;
            red = 255;
            green = 0;
            blue = 255;
        }
        
        // Detect if player steps over NPC
        if((player.getStepX() == getX()) && (player.getStepY() == getY())) {
        	System.out.println("got it");
        	setX(0);
        	setY(0);
        	red = 0;
        	green = 0;
        	blue = 0;
        	g.fillRoundRect((getX() * 20) + 5, (getY() * 20) + 5, 10, 10, 0, 0);
        	obtained = true;
        	setTimer(25);
        } else {
            // Draw NPC
        	g.setFill(Color.rgb(red,green,blue));
            g.fillRoundRect((getX() * 20) + 5, (getY() * 20) + 5, 10, 10, 0, 0);
        }
    }
}