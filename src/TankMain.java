
import javafx.application.Application;
import javafx.stage.Stage;


// TODO bad UX to always center the stage when first showing a scene. maybe keep separate coordinates for the game/menu so when recreating, they are in the right place. or just separate stages.
public class TankMain extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setResizable(false);
        stage.setTitle("Tank Tank");

        TankMainMenu.display(stage);
        stage.show();
    }
}