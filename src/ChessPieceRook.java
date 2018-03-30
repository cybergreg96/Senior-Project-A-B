/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessPieceRook extends ChessPiece {

	//	private int xPos;
	//	private int yPos;
	//	private int type;
	private Image image;
	//	private ImageView imageView = new ImageView(); 

	public ChessPieceRook(int type, int xPos, int yPos) {
		super(type, xPos, yPos);
		name = "Rook";
		// TODO Auto-generated constructor stub
		if(type==1){
			image = new Image("file:src/ChessPiece/White_Rook.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}else{
			image = new Image("file:src/ChessPiece/Black_Rook.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}
	}

	@Override
	public ImageView getImage() {
		return (imageView);
	}

	@Override
	public void SelectPiece(ChessBoard chessBoard) {
		chessBoard.colorSquare(this.xPos, this.yPos, true);
		
		// if the player is in check
		if(chessBoard.checkState)
		{
			// receives the correct king piece
			ChessPieceKing king = (ChessPieceKing) chessBoard.getKing(this.type);

			if(king.canMove(chessBoard))
			{
				// player cannot move this piece if the king can move
				return;
			}
		}

		// sets valid moves going down up board
		for (int y = this.yPos - 1; y >= this.yPos - 2 && y >= 0; y--)
		{
			if (chessBoard.getBoardPosition(this.xPos, y) == 0)
			{
				chessBoard.colorSquare(this.xPos, y, false);
			}
			else if (chessBoard.getBoardPosition(this.xPos, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(this.xPos, y, false);
				break;
			}
		}

		// sets valid moves going down the board
		for (int y = this.yPos + 1; y <= this.yPos + 2 && y < chessBoard.getBoardHeight(); y++)
		{
			if (chessBoard.getBoardPosition(this.xPos, y) == 0)
			{
				chessBoard.colorSquare(this.xPos, y, false);
			}
			else if (chessBoard.getBoardPosition(this.xPos, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(this.xPos, y, false);
				break;
			}
		}

		// sets valid moves going left
		for (int x = this.xPos - 1; x >= 0 && x >= this.xPos - 2; x--)
		{
			if (chessBoard.getBoardPosition(x, this.yPos) == 0)
			{
				chessBoard.colorSquare(x, this.yPos, false);
			}
			else if (chessBoard.getBoardPosition(x, this.yPos) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, this.yPos, false);
				break;
			}
		}

		// sets valid moves going right
		for (int x = this.xPos + 1; x < chessBoard.getBoardWidth() && x <= this.xPos + 2; x++)
		{
			if (chessBoard.getBoardPosition(x, this.yPos) == 0)
			{
				chessBoard.colorSquare(x, this.yPos, false);
			}
			else if (chessBoard.getBoardPosition(x, this.yPos) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, this.yPos, false);
				break;
			}
		}

	}	
}