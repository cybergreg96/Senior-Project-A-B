package ChessGame;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
 * citation: https://github.com/GuiBon/ChessGame
 * 
 * This class allows the user to play a mini-chess game with slightly modified rules based on our professor's specification
 * (No Knight Chess)
 */
public class ChessBoard extends Pane 
{

	public ChessBoard()
	{
	}
	
	/**
	 * This class initializes the chess game
	 * @param newStatusBar	The status bar used for the chess game
	 */
	public ChessBoard(ChessStatusBar newStatusBar) 
	{
		// initalize the board: background, data structures, inital layout of
		// pieces
		chessStatusBar = newStatusBar;
		chessStatusBar.whitePlayerAlert.setText("White Player's turn");
		chessStatusBar.blackPlayerAlert.setText("");
		chessStatusBar.whitePlayerTimer.setText("White timer: 15:00");
		chessStatusBar.blackPlayerTimer.setText("Black timer: 15:00");

		background = new Rectangle();
		background.setFill(Color.WHITE);

		getChildren().add(background);

		// initialize board array to the correct size
		board = new int[boardWidth][boardHeight];

		// initialize pieces array to the correct size
		chessPieces = new ChessPiece[boardWidth][boardHeight];

		// initialize windows array to the correct size
		chessWindows = new ChessWindow[boardWidth][boardHeight];

		// for loop to populate all arrays to default values and add the windows
		// to the board
		for (int i = 0; i < 6; i++) 
		{
			if(i%2 == 0 || i ==0){
				isBlack = false;
			}
			else 
				isBlack = true;
			for (int j = 0; j < 6; j++) 
			{
				board[i][j] = EMPTY;
				if(isBlack){
					chessWindows[i][j] = new ChessWindow(0); //Black 
					isBlack=false;
				}else{
					chessWindows[i][j] = new ChessWindow(1); //White
					isBlack = true; 
				}
				getChildren().add(chessWindows[i][j]);
				chessPieces[i][j] = null;
			}
		}

		// set the current player to white
		current_player = PlayerWhite;
		initPiece();
		chessTimer = new ChessTimer(this);
		chessTimer.timeline.setCycleCount(Timeline.INDEFINITE);
		chessTimer.timeline.play();
		chessTimer.playerTurn = current_player;
	}

	public void initPiece()
	{
		// Initialize the pieces and put it on the board
		// BLACK Pieces
		rook_2_1 = new ChessPieceRook(2, 0, 0); 

		bishop_2_1 = new ChessPieceBishop(2, 1, 0);
		queen_2 = new ChessPieceQueen(2, 2, 0); 
		king_2 = new ChessPieceKing(2, 3, 0);
		bishop_2_2 = new ChessPieceBishop(2, 4, 0);

		rook_2_2 = new ChessPieceRook(2, 5, 0);

		pawn_2_1 = new ChessPiecePawn(2, 0, 1);
		pawn_2_2 = new ChessPiecePawn(2, 1, 1);
		pawn_2_3 = new ChessPiecePawn(2, 2, 1);
		pawn_2_4 = new ChessPiecePawn(2, 3, 1);
		pawn_2_5 = new ChessPiecePawn(2, 4, 1);
		pawn_2_6 = new ChessPiecePawn(2, 5, 1);


		//WHITE Pieces
		rook_1_1 = new ChessPieceRook(1, 0, 5);
		bishop_1_1 = new ChessPieceBishop(1, 1, 5);
		queen_1 = new ChessPieceQueen(1, 2, 5);
		king_1 = new ChessPieceKing(1, 3, 5);
		bishop_1_2 = new ChessPieceBishop(1, 4, 5);

		rook_1_2 = new ChessPieceRook(1, 5, 5);
		pawn_1_1 = new ChessPiecePawn(1, 0, 4);
		pawn_1_2 = new ChessPiecePawn(1, 1, 4);
		pawn_1_3 = new ChessPiecePawn(1, 2, 4);
		pawn_1_4 = new ChessPiecePawn(1, 3, 4);
		pawn_1_5 = new ChessPiecePawn(1, 4, 4);
		pawn_1_6 = new ChessPiecePawn(1, 5, 4);


		chessPieces[0][0] = rook_2_1;

		chessPieces[1][0] = bishop_2_1;
		chessPieces[2][0] = queen_2;
		chessPieces[3][0] = king_2;
		chessPieces[4][0] = bishop_2_2;

		chessPieces[5][0] = rook_2_2;

		chessPieces[0][1] = pawn_2_1;
		chessPieces[1][1] = pawn_2_2;
		chessPieces[2][1] = pawn_2_3;
		chessPieces[3][1] = pawn_2_4;
		chessPieces[4][1] = pawn_2_5;
		chessPieces[5][1] = pawn_2_6;

		
		for (int y = 2; y < 6; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				chessPieces[x][y] = null;
			}
		}

		//set starting positions for all chess pieces
		chessPieces[0][4] = pawn_1_1;
		chessPieces[1][4] = pawn_1_2;
		chessPieces[2][4] = pawn_1_3;
		chessPieces[3][4] = pawn_1_4;
		chessPieces[4][4] = pawn_1_5;
		chessPieces[5][4] = pawn_1_6;

		chessPieces[0][5] = rook_1_1;

		chessPieces[1][5] = bishop_1_1;
		chessPieces[2][5] = queen_1;
		chessPieces[3][5] = king_1;
		chessPieces[4][5] = bishop_1_2;

		chessPieces[5][5] = rook_1_2;

		//populates array that keeps track of which team each piece is on
		for (int y = 0; y < boardHeight; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				if (y == 0 || y == 1)
					board[x][y] = 2;
				else if (y == 4 || y == 5)
					board[x][y] = 1;
				else
					board[x][y] = 0;
			}
		}
		
		//adds each piece's assigned image to the board
		for(int i = 0; i < 6; i++)
		{
			getChildren().addAll(chessPieces[i][0].getImage(), chessPieces[i][1].getImage(), chessPieces[i][4].getImage(), chessPieces[i][5].getImage());
		}
	}

	// resize method
	@Override
	public void resize(double width, double height) 
	{
		// call the superclass resize method
		super.resize(width, height);

		// resize the rectangle to take the full size of the widget
		background.setWidth(width);
		background.setHeight(height);

		// calculate the width and height of a cell in which a windows and a
		// piece will sit
		cell_width = width / 6.0;
		cell_height = height / 6.0;

		// nested for loop to reset the sizes and positions of all pieces that
		// were already placed
		// and update the position of the windows in the board
		for (int i = 0; i < 6; i++) 
		{
			for (int j = 0; j < 6; j++) 
			{
				if (board[i][j] != 0) 
				{
					chessPieces[i][j].relocate(i * cell_width, j * cell_height);
					chessPieces[i][j].resize(cell_width, cell_height);
				}
				chessWindows[i][j].relocate(i * cell_width, j * cell_height);
				chessWindows[i][j].resize(cell_width, cell_height);
			}
		}
	}

	// reset game method
	public void resetGame() 
	{
		chessTimer.playerTurn = 0;
		for (int i = 0; i < 6; i++) 
		{
			for (int j = 0; j < 6; j++) 
			{
				board[i][j] = 0;
				if (chessPieces[i][j] != null)
					getChildren().remove(chessPieces[i][j].getImage());
				getChildren().remove(chessPieces[i][j]);
				chessPieces[i][j] = null;
			}
		}
		
		current_player = PlayerWhite;
		initPiece();
		unhighlightWindow();
		chessStatusBar.whitePlayerAlert.setText("White Player's turn");
		chessStatusBar.blackPlayerAlert.setText("");
		chessStatusBar.whitePlayerTimer.setText("White timer: 15:00");
		chessStatusBar.blackPlayerTimer.setText("Black timer: 15:00");
		chessStatusBar.winner.setText("");
		selectedPiece = null;
		playerOneRook = 2;
		playerOneBishopLightSquare = 1;
		playerOneBishopDarkSquare = 1;

		playerOneQueen = 1;
		playerOnePawn = 6;
		playerTwoRook = 2;
		playerTwoBishopLightSquare = 1;
		playerTwoBishopDarkSquare = 1;

		playerTwoQueen = 1;
		playerTwoPawn = 6;
		chessTimer.timeIsOver = false;
		chessTimer.whiteTimer = 900;
		chessTimer.blackTimer = 900;
		chessTimer.playerTurn = current_player;
		chessTimer.timeline.play();
	}

	//select a piece given the coordinate on the board
	public void selectPiece(final double x, final double y)
	{
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);

		if (!chessTimer.timeIsOver && !kingTaken())
		{
			if (chessWindows[indexX][indexY].isHighlighted())
			{
				movePiece(x, y);
				unhighlightWindow();
				selectedPiece = null;
			}
			else
			{
				if(board[indexX][indexY] == current_player){
					unhighlightWindow();

					this.isCheck(current_player);

					chessPieces[indexX][indexY].SelectPiece(this);
					selectedPiece = chessPieces[indexX][indexY];
				}
			}
		}
	}

	// move piece method
	public void movePiece(final double x, final double y)
	{
		int indexX = (int) (x/ cell_width);
		int indexY = (int) (y/ cell_height);

		// add a condition to know if the player can put his piece there
		selectedPiece.MovePiece(this, indexX, indexY);
		if (current_player == PlayerWhite) 
		{
			isCheck(2);
			if (checkState) 
			{
				chessStatusBar.loser.setText("Black King in check");
			} else 
			{
				chessStatusBar.loser.setText("");
			}
		} else if(current_player == PlayerBlack)
		{
			isCheck(1);
			if (checkState) 
			{
				chessStatusBar.loser.setText("White King in check");
			} else 
			{
				chessStatusBar.loser.setText("");
			}
		}
		if (current_player == PlayerWhite) 
		{
			isCheck(1);
			if (checkState) 
			{
				chessStatusBar.loser.setText("White King in check");
			} 
		} else if(current_player == PlayerBlack)
		{
			isCheck(2);
			if (checkState) 
			{
				chessStatusBar.loser.setText("Black King in check");
			} 
		}
		
		// switches the player
		if (current_player == PlayerWhite)
		{
			current_player = PlayerBlack;
			chessStatusBar.whitePlayerAlert.setText("");
			chessStatusBar.blackPlayerAlert.setText("Black Player's turn");
		}
		else
		{
			current_player = PlayerWhite;
			chessStatusBar.blackPlayerAlert.setText("");
			chessStatusBar.whitePlayerAlert.setText("White Player's turn");
		}
		chessTimer.playerTurn = current_player;
	}

	//allows the user to promote a pawn that has reached the opponents end of the board
	public void createPromotePiece(ChessPiece chessPiece)
	{
		ChessPiece promotedPiece;

		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Promote a piece");
		alert.setHeaderText("You can promote your pawn into another piece");
		alert.setContentText("Choose one of the following piece");

		ButtonType buttonRook = new ButtonType("Rook");
		ButtonType buttonBishop = new ButtonType("Bishop");
		ButtonType buttonQueen = new ButtonType("Queen");

		alert.getButtonTypes().setAll(buttonRook, buttonBishop, buttonQueen);

		Optional<ButtonType> result = alert.showAndWait();
		
		//replaces the pawn with whatever piece is chosen by the user
		if (result.get() == buttonRook)
		{
			promotedPiece = new ChessPieceRook(chessPiece.type, chessPiece.xPos, chessPiece.yPos);
			getChildren().remove(chessPiece.getImage());
			getChildren().add(promotedPiece.getImage());
			chessPieces[chessPiece.xPos][chessPiece.yPos] = promotedPiece;
			if (chessPiece.type == 1)
				playerOneRook++;
			else
				playerTwoRook++;
		}
		else if (result.get() == buttonBishop) 
		{
			promotedPiece = new ChessPieceBishop(chessPiece.type, chessPiece.xPos, chessPiece.yPos);
			getChildren().remove(chessPiece.getImage());
			getChildren().add(promotedPiece.getImage());
			chessPieces[chessPiece.xPos][chessPiece.yPos] = promotedPiece;
			if (chessPiece.type == 1)
			{
				if ((chessPiece.xPos + chessPiece.yPos) % 2 != 0)
					playerOneBishopDarkSquare++;
				else
					playerOneBishopLightSquare++;
			}
			else
			{
				if ((chessPiece.xPos + chessPiece.yPos) % 2 == 0)
					playerTwoBishopLightSquare++;
				else
					playerTwoBishopDarkSquare++;
			}
		}
		else if (result.get() == buttonQueen) 
		{
			promotedPiece = new ChessPieceQueen(chessPiece.type, chessPiece.xPos, chessPiece.yPos);
			getChildren().remove(chessPiece.getImage());
			getChildren().add(promotedPiece.getImage());
			chessPieces[chessPiece.xPos][chessPiece.yPos] = promotedPiece;
			if (chessPiece.type == 1)
				playerOneQueen++;
			else
				playerTwoQueen++;
		}
	}

	//highlights the square when a user selects a piece or when a square is a valid move for the chosen piece
	public void colorSquare(int x, int y, boolean selectedPiece) 
	{
		if (selectedPiece)
			chessWindows[x][y].highlightWindow(Color.ORANGE);
		else
			chessWindows[x][y].highlightWindow(Color.GREEN);			
	}

	// unhighlights all windows
	public void unhighlightWindow()
	{
		for (int y = 0; y < boardHeight; y++)
		{
			for (int x = 0; x < boardWidth; x++)
			{
				if (chessWindows[x][y].getRectangle().getStroke() != null)
					chessWindows[x][y].unhighlight();
			}
		}
	}

	//ends the game when a player runs out of time
	public void timerOver(int playerOutOfTime)
	{
		chessTimer.timeline.stop();
		if (playerOutOfTime == 1)
		{
			chessStatusBar.whitePlayerAlert.setText("White player run out of time");
			chessStatusBar.winner.setText("Black player won !");
		}
		else if (playerOutOfTime == 2)
		{
			chessStatusBar.blackPlayerAlert.setText("Black player run out of time");
			chessStatusBar.winner.setText("White player won !");
		}
	}
	
	//ends the game when a king is taken
	public boolean kingTaken() 
	{
		if(current_player == PlayerWhite) 
		{
			if(kingPosition(1) != null) 
			{
				return false;
			}
			chessTimer.timeline.stop();
			noKing(1);
			return true;
		}
		else
		{
			if(kingPosition(2) != null) 
			{
				return false;
			}
			chessTimer.timeline.stop();
			noKing(2);
			return true;
		}
	}
	
	//alerts user that the king has been taken
	public void noKing(int losingPlayer)
	{
		if(losingPlayer == 1)
		{
			chessStatusBar.whitePlayerAlert.setText("White player had their King taken!");
			chessStatusBar.winner.setText("Black player won!");
		}
		else if(losingPlayer == 2) {
			chessStatusBar.blackPlayerAlert.setText("Black Player had their King Taken!");
			chessStatusBar.winner.setText("White player won!");
		}
	}

	// returns the king of a given type
	public ChessPiece getKing(int type)
	{
		if (type == 1)
			return (king_1);
		return (king_2);
	}

	//searches for and returns the kings position on the board
	public int[] kingPosition(int player) 
	{
		int[] kpos = new int[2];
		if(player == 1)
		{
			for(int x=0;x<6;x++) 
			{
				for(int y=0;y<6;y++) 
				{
					if(chessPieces[x][y] == king_1) 
					{
						kpos[0] = x;
						kpos[1] = y;
						return kpos;
					}
				}
			}
			return null;
		}
		else 
		{
			for(int x=0;x<6;x++) 
			{
				for(int y=0;y<6;y++) 
				{
					if(chessPieces[x][y] == king_2) 
					{
						kpos[0] = x;
						kpos[1] = y;
						return kpos;
					}
				}
			}
			return null;
		}
	}

	public int getBoardHeight()
	{
		return (this.boardHeight);
	}

	public int getBoardWidth()
	{
		return (this.boardWidth);
	}

	public int getBoardPosition(int x, int y)
	{
		return (this.board[x][y]);
	}
	
	//sets a type to a specific location on the board
	public void setBoard(int x, int y, int type)
	{
		this.board[x][y] = type;
	}

	public ChessPiece getPiece(int x, int y)
	{
		return (chessPieces[x][y]);
	}

	public void setPiece(int x, int y, ChessPiece chessPiece)
	{
		this.chessPieces[x][y] = chessPiece;
	}

	public ChessStatusBar getStatusBar()
	{
		return (chessStatusBar);
	}

	//checks if the king of a given type is in check
	public boolean isCheck(int type)
	{
		// reset check state
		checkState = false;

		// get correct king piece
		ChessPieceKing king = (ChessPieceKing) this.getKing(type);

		// original position of the king
		int kingX = king.xPos;
		int kingY = king.yPos;


		// tests if there are valid check pieces within a one space radius of the king
		for(int x = king.xPos - 1; x <= king.xPos + 1; x++)
		{
			// ensures that x is a valid value
			if(x < this.boardWidth && x >= 0)
			{
				for(int y = king.yPos - 1; y <= king.yPos + 1; y++)
				{
					// ensures y is a valid value
					if(y < this.boardHeight && y >= 0)
					{
						ChessPiece piece = this.getPiece(x, y);

						// checks if there is a piece at the given coordinate
						if(piece != null)
						{
							// checks if the piece at a given location is an enemy piece
							if(piece.type != type)
							{
								// checks pieces vertically and horizontally
								if(x - kingX == 0 || y - kingY == 0)
								{
									if(piece.name.equals("Rook"))
									{
										checkState = true;
										return true;
									}
								}
								//checks diagonal pieces
								else
								{
									//checks for bishops
									if(piece.name.equals("Bishop"))
									{
										checkState = true;
										return true;
									}
									
									if(((y < kingY && type == 1) || (y > kingY && type == 2)) && piece.name.equals("Pawn"))
									{
										checkState = true;
										return true;
									}
								}

								//check for king and queen
								if(piece.name.equals("King") || piece.name.equals("Queen"))
								{
									checkState = true;
									return true;
								}
							}
						}
					}
				}
			}
		}

		// tests if there are valid check pieces within a two space radius of the king
		for(int x = king.xPos - 2; x <= king.xPos + 2; x++)
		{
			// ensures that x is a valid value and is two spaces left or right of the king
			if(x < this.boardWidth && x >= 0)
			{
				for(int y = king.yPos - 2; y <= king.yPos + 2; y++)
				{
					// ensures y is a valid value
					if(y < this.boardHeight && y >= 0)
					{
						ChessPiece piece = this.getPiece(x, y);

						if(piece != null)
						{
							// checks that the piece at the coordinate is an enemy piece
							if(piece.type != type)
							{
								// if y is two spaces above or below the king
								if(Math.abs(kingY - y) == 2)
								{
									// checks if the piece is an unimpeded attacking bishop
									if(piece.name.equals("Bishop") && Math.abs(kingX - x) == 2 && this.getBoardPosition(kingX - ((kingX - x) / 2), kingY - ((kingY - y) / 2)) == 0)
									{
										// bishop is unimpeded
										checkState = true;
										return true;
									}
									// checks if piece is an unimpeded attacking rook
									else if(piece.name.equals("Rook") && kingX == x && this.getBoardPosition(kingX, kingY - ((kingY - y) / 2)) == 0)
									{
										// rook is unimpeded
										checkState = true;
										return true;
									}
									// checks if piece is an attacking queen
									else if(piece.name.equals("Queen") && Math.abs(kingX - x) == 1)
									{
										// queen can attack king
										checkState = true;
										return true;
									}
								}
								// if y is the same as the king
								else if(kingY == y)
								{
									// checks if piece is an unimpeded rook
									if(piece.name.equals("Rook") && this.getBoardPosition(kingX - ((kingX - x) / 2), kingY) == 0)
									{
										//rook is unimpeded
										checkState = true;
										return true;
									}
								}
								// if y is one space above or below the king
								else
								{
									// checks if piece is an attacking queen
									if(piece.name.equals("Queen") && Math.abs(kingX - x) == 2)
									{
										// queen can attack king
										checkState = true;
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	// private fields
	private int boardWidth = 6;
	private int boardHeight = 6;
	private int[][] board;

	private ChessPiece[][] chessPieces;
	private ChessWindow[][] chessWindows;

	// pieceName_color_number
	private ChessPieceRook rook_2_1; 

	private ChessPieceBishop bishop_2_1;
	private ChessPieceQueen queen_2; 
	private ChessPieceKing king_2; 
	private ChessPieceBishop bishop_2_2;

	private ChessPieceRook rook_2_2;
	private ChessPiecePawn pawn_2_1;
	private ChessPiecePawn pawn_2_2;
	private ChessPiecePawn pawn_2_3;
	private ChessPiecePawn pawn_2_4;
	private ChessPiecePawn pawn_2_5;
	private ChessPiecePawn pawn_2_6;


	private ChessPieceRook rook_1_1; 

	private ChessPieceBishop bishop_1_1;
	private ChessPieceQueen queen_1; 
	private ChessPieceKing king_1;
	private ChessPieceBishop bishop_1_2;

	private ChessPieceRook rook_1_2;
	private ChessPiecePawn pawn_1_1;
	private ChessPiecePawn pawn_1_2;
	private ChessPiecePawn pawn_1_3;
	private ChessPiecePawn pawn_1_4;
	private ChessPiecePawn pawn_1_5;
	private ChessPiecePawn pawn_1_6;


	private ChessPiece selectedPiece = null;

	private ChessStatusBar chessStatusBar = null;

	public int	playerOneRook = 2;
	public int	playerOneBishopLightSquare = 1;
	public int	playerOneBishopDarkSquare = 1;

	public int	playerOneQueen = 1;
	public int	playerOnePawn = 6;
	public int	playerTwoRook = 2;
	public int	playerTwoBishopLightSquare = 1;
	public int	playerTwoBishopDarkSquare = 1;

	public int	playerTwoQueen = 1;
	public int	playerTwoPawn = 6;
	private Alert alert;

	private Rectangle background;
	private double cell_width;
	private double cell_height;
	private int current_player;
	private boolean isBlack = false; 
	public boolean checkState = false;

	private final int EMPTY = 0;
	private final int PlayerWhite = 1;
	private final int PlayerBlack = 2;

	private ChessTimer chessTimer;
}