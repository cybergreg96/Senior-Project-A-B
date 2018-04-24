/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class ChessController implements Initializable {
	@FXML
	private Pane root;
	@FXML
	private StackPane sp_mainlayout;
	@FXML
	private ToolBar birdstufzBanner;

	private ChessCustomControl cc_custom;	//control which has a board and detects mouse and keyboard events

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		root.setStyle("-fx-background-color: #a50000");
		root.getStylesheets().add("styles.css");
		// initialize the chess layout, create a CustomControl and it to the layout
		cc_custom = new ChessCustomControl();
		sp_mainlayout.getChildren().add(cc_custom);
		Button goHome = new Button("Go home");
	    Scene home = new Scene(goHome);
	    home.snapshot(null);       
	    goHome.setTranslateX((850 / 2) - (goHome.getWidth() / 2)  - 50);
	    goHome.setTranslateY(4);  
	    root.getChildren().add(goHome);
	    goHome.setOnAction((ActionEvent e) -> {
	    	try {
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
	            Scene y = new Scene(x);
	            Stage w = (Stage)((Node)e.getSource()).getScene().getWindow();
	            w.setResizable(false);
	            w.setScene(y);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	        
	    });
	    
	    Button reset = new Button("New Game");
		Scene newchess = new Scene(reset);
		newchess.snapshot(null);
		reset.setTranslateX((850 / 2) - (goHome.getWidth() / 2)  + 50);
		reset.setTranslateY(4);
		root.getChildren().add(reset);
		reset.setOnAction((ActionEvent e) -> {
			Parent chess = null;
			try {
				chess = FXMLLoader.load(getClass().getResource("Chess.fxml"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	        Scene chessScene = new Scene(chess);
	        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
	        window.setResizable(false);
	        window.setScene(chessScene);

	        window.setResizable(false); 
	        window.show();
		});	
	}
			
}
