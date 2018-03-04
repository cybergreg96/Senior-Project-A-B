

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;



public class StartScreen extends Application {
	
	//boolean for the program to know when to stop loading the splashscreen
	public static Boolean isSplashLoaded = false;
	
	@Override
	public void start(Stage primaryStage) throws IOException {

			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("StartScreen.fxml"));
			//sets background color
			root.setStyle("-fx-background-color: #a50000");
			primaryStage.setTitle("BIRDSTUFZ GamesMaster");
			primaryStage.setScene(new Scene(root, 850, 650));
			primaryStage.setResizable(false);
		    primaryStage.getIcons().add(new Image("resources/bird logo.PNG"));
			primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
