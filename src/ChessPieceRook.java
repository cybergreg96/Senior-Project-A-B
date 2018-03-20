
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
		if (chessBoard.checkState && !this.isASavior)
			return;
		if (chessGameLogic.slashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type) || chessGameLogic.backslashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
			return;
		if (!chessGameLogic.horizontalProtection(chessBoard, this.xPos, this.yPos, this.type))
		{
			// sets valid moves going down up board
			for (int y = this.yPos - 1; y >= this.yPos - 2 && y >= 0; y--)
			{
				if (chessBoard.getBoardPosition(this.xPos, y) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, y, this.type))
							chessBoard.colorSquare(this.xPos, y, false);
					}
					else
						chessBoard.colorSquare(this.xPos, y, false);
				}
				else if (chessBoard.getBoardPosition(this.xPos, y) == this.type)
					break;
				else
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, y, this.type))
							chessBoard.colorSquare(this.xPos, y, false);
					}
					else
						chessBoard.colorSquare(this.xPos, y, false);
					break;
				}
			}
			
			// sets valid moves going down the board
			for (int y = this.yPos + 1; y <= this.yPos + 2 && y < chessBoard.getBoardHeight(); y++)
			{
				if (chessBoard.getBoardPosition(this.xPos, y) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, y, this.type))
							chessBoard.colorSquare(this.xPos, y, false);
					}
					else
						chessBoard.colorSquare(this.xPos, y, false);
				}
				else if (chessBoard.getBoardPosition(this.xPos, y) == this.type)
					break;
				else
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, y, this.type))
							chessBoard.colorSquare(this.xPos, y, false);
					}
					else
						chessBoard.colorSquare(this.xPos, y, false);
					break;
				}
			}
		}
		if (!chessGameLogic.verticalProtection(chessBoard, this.xPos, this.yPos, this.type))
		{
			// sets valid moves going left
			for (int x = this.xPos - 1; x >= 0 && x >= this.xPos - 2; x--)
			{
				if (chessBoard.getBoardPosition(x, this.yPos) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, x, this.yPos, this.type))
							chessBoard.colorSquare(x, this.yPos, false);
					}
					else
						chessBoard.colorSquare(x, this.yPos, false);
				}
				else if (chessBoard.getBoardPosition(x, this.yPos) == this.type)
					break;
				else
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, x, this.yPos, this.type))
							chessBoard.colorSquare(x, this.yPos, false);
					}
					else
						chessBoard.colorSquare(x, this.yPos, false);
					break;
				}
			}
			
			// sets valid moves going right
			for (int x = this.xPos + 1; x < chessBoard.getBoardWidth() && x <= this.xPos + 2; x++)
			{
				if (chessBoard.getBoardPosition(x, this.yPos) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, x, this.yPos, this.type))
							chessBoard.colorSquare(x, this.yPos, false);
					}
					else
						chessBoard.colorSquare(x, this.yPos, false);
				}
				else if (chessBoard.getBoardPosition(x, this.yPos) == this.type)
					break;
				else
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, x, this.yPos, this.type))
							chessBoard.colorSquare(x, this.yPos, false);
					}
					else
						chessBoard.colorSquare(x, this.yPos, false);
					break;
				}
			}
		}
	}	
}