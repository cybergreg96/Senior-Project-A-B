
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChessCustomControl extends Control {
	
	//similar to previous custom controlls but must handle more
	//complex mouse interactions and key interactions

	public static final int CANVAS_WIDTH = 850;
	public static final int CANVAS_HEIGHT = 650;
	
	public ChessCustomControl(){
		setSkin(new ChessCustomControlSkin(this));
		
		chessStatusBar = new ChessStatusBar();
		chessBoard = new ChessBoard(chessStatusBar);
		getChildren().addAll(chessStatusBar, chessBoard);
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				chessBoard.selectPiece(event.getX(), event.getY() - (statusBarSize / 2));
			}
			
		});
		
		Button goHome = new Button("Go home");
	    Scene home = new Scene(goHome);
	    home.snapshot(null);       
	    goHome.setTranslateX((CANVAS_WIDTH / 2) - goHome.getWidth() / 2);
	    goHome.setTranslateY(-50);  
	    chessBoard.getChildren().add(goHome);
	    goHome.setOnAction((ActionEvent e) -> {
	    	try {
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
	            Scene y = new Scene(x);
	            Stage w = (Stage)((Node)e.getSource()).getScene().getWindow();
	            w.setResizable(false);
	            w.setScene(y);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	        
	    });

		// Add a key listener that will reset the game
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE)
					chessBoard.resetGame();
			}
		});
		
		
		
		chessStatusBar.getResetButton().setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				chessBoard.resetGame();
			}
			
		});
		
	}
	
	public void resize(double width, double height){
		super.resize(width, height - statusBarSize);
		chessBoard.setTranslateY(statusBarSize / 2);
		chessBoard.resize(width, height - statusBarSize);
		chessStatusBar.resize(width, statusBarSize);
		chessStatusBar.setTranslateY(-(statusBarSize / 2));
	}
	
	private ChessBoard chessBoard;
	private ChessStatusBar chessStatusBar; 
	private int statusBarSize = 100;	
}
