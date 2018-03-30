/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessPieceQueen extends ChessPiece {

	private Image image;
	//	private ImageView imageView = new ImageView(); 

	public ChessPieceQueen(int type, int xPos, int yPos) {
		super(type, xPos, yPos);
		name = "Queen";
		// TODO Auto-generated constructor stub
		if(type==1){
			image = new Image("file:src/ChessPiece/White_Queen.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}else{
			image = new Image("file:src/ChessPiece/Black_Queen.png");
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

		int currentX = this.xPos;
		int currentY = this.yPos;
		
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
		
		for (int y = this.yPos - 2; y <= this.yPos + 2; y++)
		{
			for (int x = this.xPos - 2; x <= this.xPos + 2; x++)
			{
				// alternate every other row to add knight move set to queen
				if((currentY - y) % 2 == 0)
				{
					x += 1;
				}

				// checks for valid space before highlighting
				if(y >= 0 && y < chessBoard.getBoardHeight() && x >= 0 && x < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(x, y) != this.type)
				{
					chessBoard.colorSquare(x, y, false);
				}

				// end x loop after coloring the second valid space (helps implement knight movement)
				if((currentY - y) % 2 == 0 && x == this.xPos + 1)
				{
					x += 2;
				}
			}
		}	
	}
}
