/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessPieceKing extends ChessPiece{


	//	private int xPos;
	//	private int yPos;
	//	private int type;
	private Image image;
	//	private ImageView imageView = new ImageView();

	public ChessPieceKing(int type, int xPos, int yPos) {
		super(type, xPos, yPos);
		name = "King";
		// TODO Auto-generated constructor stub

		if(type==1){
			image = new Image("file:src/ChessPiece/White_King.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}
		else{
			image = new Image("file:src/ChessPiece/Black_King.png");
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
		int x = this.xPos;
		int y = this.yPos;
		chessBoard.colorSquare(this.xPos, this.yPos, true);
		for (y = this.yPos - 1; y <= this.yPos + 1; y++)
		{
			for (x = this.xPos - 1; x <= this.xPos + 1; x++)
			{
				if(y >= 0 && y < chessBoard.getBoardHeight() && x >= 0 && x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(x, y) != this.type)
				{
					chessBoard.colorSquare(x, y, false);
				}
			}
		}	
	}

	//checks if the king has a valid move
	public boolean canMove(ChessBoard chessBoard)
	{
		for (int y = this.yPos - 1; y <= this.yPos + 1; y++)
		{
			for (int x = this.xPos - 1; x <= this.xPos + 1; x++)
			{
				// checks y values for validity
				if(y >= 0 && y != this.yPos && y < chessBoard.getBoardHeight() && chessBoard.getBoardPosition(x, y) != this.type)
				{
					//checks x values for validity
					if(x >= 0 && x != this.xPos && x < chessBoard.getBoardWidth())
					{
						// there is at least one valid move
						return true;
					}
				}
			}
		}

		// there are no valid moves
		return false;
	}
}
