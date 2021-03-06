package ChessGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 * 
 * This class initializes a bishop and defines the bishops type and moveset
 */
public class ChessPieceBishop extends ChessPiece
{

	//image associated with the chess piece
	private Image image;

	//assigns image and position to the bishop
	public ChessPieceBishop(int type, int xPos, int yPos) 
	{
		super(type, xPos, yPos);
		name = "Bishop";

		if(type==1)
		{
			//white piece
			image = new Image("file:src/ChessPiece/White_Bishop.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}
		else
		{
			//black piece
			image = new Image("file:src/ChessPiece/Black_Bishop.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}
	}

	@Override
	public ImageView getImage() 
	{
		return (imageView);
	}

	@Override
	public void SelectPiece(ChessBoard chessBoard)
	{
		int y = this.yPos + 1;
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

		// sets valid moves going diagonally down and right
		for(int x = this.xPos + 1; x < chessBoard.getBoardWidth() && y < chessBoard.getBoardHeight() && x <= this.xPos + 2; x++, y++)
		{
			if (chessBoard.getBoardPosition(x, y) == 0)
			{
				chessBoard.colorSquare(x, y, false);
			}
			else if (chessBoard.getBoardPosition(x, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, y, false);
				break;
			}
		}
		y = this.yPos - 1;

		// sets valid moves going diagonally up and left
		for(int x = this.xPos - 1; x >= 0 && y >= 0 && x >= this.xPos - 2; x--, y--)
		{
			if (chessBoard.getBoardPosition(x, y) == 0)
			{
				chessBoard.colorSquare(x, y, false);
			}
			else if (chessBoard.getBoardPosition(x, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, y, false);
				break;
			}
		}

		y = this.yPos + 1;

		// sets valid moves going diagonally down and left
		for (int x = this.xPos - 1; x >= 0 && y < chessBoard.getBoardHeight() && x >= this.xPos - 2; x--, y++)
		{
			if (chessBoard.getBoardPosition(x, y) == 0)
			{
				chessBoard.colorSquare(x, y, false);
			}
			else if (chessBoard.getBoardPosition(x, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, y, false);
				break;
			}
		}
		y = this.yPos - 1;

		// sets valid moves going diagonally up and right
		for (int x = this.xPos + 1; x < chessBoard.getBoardWidth() && y >= 0 && x <= this.xPos + 2; x++, y--)
		{
			if (chessBoard.getBoardPosition(x, y) == 0)
			{
				chessBoard.colorSquare(x, y, false);
			}
			else if (chessBoard.getBoardPosition(x, y) == this.type)
				break;
			else
			{
				chessBoard.colorSquare(x, y, false);
				break;
			}
		}
	}
}
