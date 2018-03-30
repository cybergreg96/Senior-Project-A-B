/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class ChessTimer {
	public int whiteTimer = 900;
	public int blackTimer = 900;
	public int playerTurn = 0;
	public boolean timeIsOver = false;
	public boolean kingGone = false;
	private ChessBoard chessboard;
	
	public ChessTimer(ChessBoard _chessboard) {
		chessboard = _chessboard;
	}
	
	public Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			if (playerTurn == 1 && !timeIsOver && !kingGone)
			{
				whiteTimer -= 1;
				if(whiteTimer % 60 < 10)
					chessboard.getStatusBar().whitePlayerTimer.setText("White timer: " + TimeUnit.SECONDS.toMinutes(whiteTimer) + ":0" + (whiteTimer % 60));
				else
					chessboard.getStatusBar().whitePlayerTimer.setText("White timer: " + TimeUnit.SECONDS.toMinutes(whiteTimer) + ":" + (whiteTimer % 60));
			}
			else if (playerTurn == 2 && !timeIsOver && !kingGone)
			{
				blackTimer -= 1;
				if(blackTimer % 60 < 10)
					chessboard.getStatusBar().blackPlayerTimer.setText("Black timer: " + TimeUnit.SECONDS.toMinutes(blackTimer) + ":0" + (blackTimer % 60));
				else
					chessboard.getStatusBar().blackPlayerTimer.setText("Black timer: " + TimeUnit.SECONDS.toMinutes(blackTimer) + ":" + (blackTimer % 60));
			}
			if (!timeIsOver && (whiteTimer == 0 || blackTimer == 0))
			{
				chessboard.timerOver(playerTurn);
				timeIsOver = true;
			}
			if(!timeIsOver && (chessboard.kingTaken(1) || chessboard.kingTaken(2))) 
			{
				chessboard.kingTaken(playerTurn);
				kingGone = true;
			}
		}
	}));
}
