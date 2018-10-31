package PacManLogic;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import PacManGUI.SceneInfo;
import PacManLogic.Ghosts.Ghosts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class PlayerIndicator implements GameObject {
	
	private Image indicatorImg;
	private Ghosts cG;
	private GameObject[] ghostObjs;
	private int x, y, ctrldGhost;
	public PlayerIndicator(ArrayList<GameObject> ghostObjs) {
		try {
			indicatorImg = new Image(new FileInputStream("src/PacManImgs/Indicator.png"));
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		this.ghostObjs = ghostObjs.toArray(new GameObject[ghostObjs.size()]);
		ctrldGhost = 0;
		determineIndicator();
		
	}
	public void update(KeyCode KeyPressed, int ghostControl) {
		ctrldGhost = ghostControl;
		determineIndicator();
	}
	
	public void draw(GraphicsContext g, SceneInfo sceneInfo) {
		g.drawImage(indicatorImg, x, y);
	}
	private void determineIndicator() {
		//System.out.println("Index to access: " + ctrldGhost);
		cG = (Ghosts)ghostObjs[ctrldGhost];
		this.x = cG.getX();
		this.y = cG.getY() - 20;
	}


}
