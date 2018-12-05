package BilliardGame;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;

public interface BObject {
    void draw(GraphicsContext graphicsContext, SceneInfo sceneInfo);
}
