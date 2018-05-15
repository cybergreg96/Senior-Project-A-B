/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

// MainMenu represents the main menu of tank tank.
// TODO decorate the main menu with tanks and also use idiomatic javafx layouting instead of hard coding.
class TankMainMenu {
    private static final String HELP_URL = "https://github.com/nhooyr/java-tanktank";
    private static final double WIDTH = 300;
    private static final double HEIGHT = 200;

    static void display(final Stage stage) {
        final Group root = new Group();
        final Scene scene = new Scene(root, WIDTH, HEIGHT);

        final Text title = new Text("Tank Tank");
        title.setFont(Font.font(30));
        final Bounds titleBounds = title.getLayoutBounds();
        title.setLayoutX(WIDTH / 2 - titleBounds.getWidth() / 2);
        title.setLayoutY(titleBounds.getHeight() + 10);

        final Button playButton = new Button("PLAY");
        playButton.setDefaultButton(true);
        playButton.setPrefWidth(60);
        playButton.setOnAction(event -> {
            final TankGame tankGame = new TankGame(stage);
            tankGame.start();
        });

        final Button helpButton = new Button("HELP");
        helpButton.setPrefWidth(60);
        helpButton.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(HELP_URL));
            } catch (final Exception e) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tank Tank");
                alert.setHeaderText("The help URL could not automatically be opened");
                alert.setContentText(String.format("Please manually visit %s for help.", HELP_URL));
                alert.show();
            }
        });

        final VBox vbox = new VBox(20);
        vbox.getChildren().addAll(
                playButton,
                helpButton
        );
        vbox.setLayoutX(WIDTH / 2 - playButton.getPrefWidth() / 2);
        vbox.setLayoutY(title.getLayoutY() + vbox.getSpacing() * 1.5);

        root.getChildren().addAll(title, vbox);
        stage.setScene(scene);
        // Must be called after the scene is set.
        stage.centerOnScreen();
    }
}
