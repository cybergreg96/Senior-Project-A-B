package PacManLogic;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
/**
 * Created by Arash Abedin on 9-03-2017.
 */
public interface GameObject {

    void update(KeyCode keypressed);

    void draw(GraphicsContext graphicsContext, SceneInfo sceneInfo);
}
