/**
 * Created by mal97 on 3/20/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class MancalaUI extends Application {

    /*
        Description:    Runs the User Interface components of the Mancala JavaFX application.
        Purpose:        Display the Mancala Menu screen to the user. Creates the space to set the user interface.
     */
    Stage stageMenu;
    Parent rootMenu;


    public void start(Stage stage) throws Exception {
        //Mancala Menu
        stageMenu = new Stage();
        stageMenu.setTitle("Mancala Menu");
        rootMenu = FXMLLoader.load(getClass().getResource("mancala_menu.fxml"));
        rootMenu.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stageMenu.setScene(new Scene(rootMenu, 600, 400));
        stageMenu.setResizable(false);
        stageMenu.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
