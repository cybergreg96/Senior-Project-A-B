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

/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 * 
 * This class formats the chess game window and handles mouse clicks to select a piece
 * Note: This class is similar to previous custom controls but must handle more complex mouse interactions and key interactions
 */
public class ChessCustomControl extends Control 
{
	public static final int CANVAS_WIDTH = 850;
	public static final int CANVAS_HEIGHT = 650;
	
	// constructor of the controller object and initializes the main components of the chess game
	public ChessCustomControl()
	{
		setSkin(new ChessCustomControlSkin(this));
		
		chessStatusBar = new ChessStatusBar();
		chessBoard = new ChessBoard(chessStatusBar);
		getChildren().addAll(chessStatusBar, chessBoard);
		
		//creates event handler for when the mouse clicks on a chess piece
		setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				chessBoard.selectPiece(event.getX(), event.getY() - (statusBarSize / 2));
			}
		});		
	}
	
	//resizes the window
	public void resize(double width, double height)
	{
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
