/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

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
				chessBoard.selectPiece(event.getX(), event.getY() - (statusBarSize / 2));
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
