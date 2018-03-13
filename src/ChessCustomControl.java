
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ChessCustomControl extends Control {
	
	//similar to previous custom controlls but must handle more
	//complex mouse interactions and key interactions
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
