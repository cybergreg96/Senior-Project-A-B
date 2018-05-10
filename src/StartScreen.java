

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class StartScreen extends Application {
	
	//boolean for the program to know when to stop loading the splash screen
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
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			       @Override
			       public void handle(WindowEvent e) {
			          Platform.exit();
			          System.exit(0);
			       }
			    });

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
