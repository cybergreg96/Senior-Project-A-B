/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessPieceBishop extends ChessPiece{

	//	private int xPos;
	//	private int yPos;
	//	private int type;
	private Image image;
	//	private ImageView imageView = new ImageView();

	public ChessPieceBishop(int type, int xPos, int yPos) {
		super(type, xPos, yPos);
		name = "Bishop";
		//		this.type = type;
		//		this.xPos = xPos;
		//		this.yPos = yPos;
		// TODO Auto-generated constructor stub
		if(type==1){
			image = new Image("file:src/ChessPiece/White_Bishop.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			imageView.setCache(true);
		}
		else{
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
	public ImageView getImage() {
		return (imageView);
	}

	@Override
	public void SelectPiece(ChessBoard chessBoard) {
		int y = this.yPos + 1;
		chessBoard.colorSquare(this.xPos, this.yPos, true);

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
