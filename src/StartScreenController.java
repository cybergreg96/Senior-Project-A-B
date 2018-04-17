


import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class StartScreenController implements Initializable {
	@FXML
    private Pane rootPane;
	@FXML
	private Button MazeButton;
	@FXML
	private Button ChessButton;
	@FXML
	private Button TankButton;
	@FXML
	private Button ConcentrationButton;
	@FXML
	private Button MancalaButton;
	@FXML
	private Text WelcomeText;
	@FXML
	private ToolBar birdstufzBanner;
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//set banner background color
		birdstufzBanner.setStyle("-fx-background-color: #FFFFFF");
		
        //load the splashscreen if not loaded yet
		if(!StartScreen.isSplashLoaded) {
			loadSplashScreen();
		}
		

    }  
	// Event Listener on Button[#MazeButton].onAction
	@FXML
	public void MazeButtonClick(ActionEvent event) throws IOException {
		Parent maze = FXMLLoader.load(getClass().getResource("Maze.fxml"));
        Scene mazeScene = new Scene(maze);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(mazeScene);
        window.setResizable(false); 
        window.show();
	}
	
	// Event Listener on Button[#TankButton].onAction
		@FXML
		public void TankButtonClick(ActionEvent event) throws IOException {
			
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        TankGameController tankC = new TankGameController();
	        tankC.setStage(window);
	        final TankGame tankGame = new TankGame(window);
            tankGame.start();
	        
		}
	
	// Event Listener on Button[#ChessButton].onAction
		@FXML
		public void ChessButtonClick(ActionEvent event) throws IOException {
			
			Parent chess = FXMLLoader.load(getClass().getResource("Chess.fxml"));
	        Scene chessScene = new Scene(chess);
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        window.setResizable(false);
	        window.setScene(chessScene);

	        window.setResizable(false); 
	        window.show();
		}
	// Event Listener on Button[#ConcentrationButton].onAction
	@FXML
	public void ConcentrationButtonClick(ActionEvent event) throws IOException {
		Parent concentration = FXMLLoader.load(getClass().getResource("Concentration.fxml"));
        Scene concentrationScene = new Scene(concentration);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(concentrationScene);

        window.setResizable(false); 
        window.show();
				
		
	}
	// Event Listener on Button[#MancalaButton].onAction
	@FXML
	public void MancalaButtonClick(ActionEvent event) throws IOException {

		//Mancala Menu
		Parent mancala = FXMLLoader.load(getClass().getResource("mancala_scene.fxml"));
		mancala.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        Scene mancalaScene = new Scene(mancala);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mancalaScene);
        window.setResizable(false);
        window.show();
	}
	
	//this methods loads a splash screen from within the same window as start screen
	private void loadSplashScreen() {
        try {
        	StartScreen.isSplashLoaded = true;
            Pane pane = FXMLLoader.load(getClass().getResource(("SplashScreen.fxml")));
            rootPane.getChildren().setAll(pane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            fadeOut.setOnFinished((e) -> {
                try {
                    Pane parentContent = FXMLLoader.load(getClass().getResource(("StartScreen.fxml")));
                    rootPane.getChildren().setAll(parentContent);

                } catch (IOException ex) {
                    Logger.getLogger(StartScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(StartScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
