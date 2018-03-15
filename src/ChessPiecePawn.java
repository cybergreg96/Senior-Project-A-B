
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessPiecePawn extends ChessPiece{

//	private int xPos;
//	private int yPos;
//	private int type;
	private Image image;
//	private ImageView imageView = new ImageView(); 
	
	public ChessPiecePawn(int type, int xPos, int yPos) {
		super(type, xPos, yPos);
		name = "Pawn";
		// TODO Auto-generated constructor stub
		if(type==1){
			image = new Image("file:src/ChessPiece/White_Pawn.png");
			imageView.setImage(image);
			imageView.fitHeightProperty();
			imageView.fitWidthProperty();
	        imageView.setPreserveRatio(true);
	        imageView.setSmooth(true);
	        imageView.setCache(true);
		}else{
			image = new Image("file:src/ChessPiece/Black_Pawn.png");
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
		if (chessGameLogic.horizontalProtection(chessBoard, this.xPos, this.yPos, this.type))
			return;
		if (this.type == 1)
		{
			if (!chessGameLogic.slashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type) && !chessGameLogic.backslashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
			{
				if (this.yPos - 1 >= 0 && chessBoard.getBoardPosition(this.xPos, this.yPos - 1) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, this.yPos - 1, this.type))
							chessBoard.colorSquare(this.xPos, this.yPos - 1, false);
					}
					else
						chessBoard.colorSquare(this.xPos, this.yPos - 1, false);
				}
			}
			if (!chessGameLogic.verticalProtection(chessBoard, this.xPos, this.yPos, this.type))
			{
				if (!chessGameLogic.slashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
				{
					if (this.yPos - 1 >= 0 && this.xPos - 1 >= 0 && chessBoard.getBoardPosition(this.xPos - 1, this.yPos - 1) != this.type && chessBoard.getBoardPosition(this.xPos - 1, this.yPos - 1) != 0)
					{
						if (chessBoard.checkState)
						{
							if (chessGameLogic.isThisProtecting(chessBoard, this.xPos - 1, this.yPos - 1, this.type))
								chessBoard.colorSquare(this.xPos - 1, this.yPos - 1, false);
						}
						else
							chessBoard.colorSquare(this.xPos - 1, this.yPos - 1, false);
					}
				}
				if (!chessGameLogic.backslashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
				{
					if (this.yPos - 1 >= 0 && this.xPos + 1 < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.xPos + 1, this.yPos - 1) != this.type && chessBoard.getBoardPosition(this.xPos + 1, this.yPos - 1) != 0)
					{
						if (chessBoard.checkState)
						{
							if (chessGameLogic.isThisProtecting(chessBoard, this.xPos + 1, this.yPos - 1, this.type))
								chessBoard.colorSquare(this.xPos + 1, this.yPos - 1, false);
						}
						else
							chessBoard.colorSquare(this.xPos + 1, this.yPos - 1, false);
					}
				}
			}
		}
		else if (this.type == 2)
		{
			if (!chessGameLogic.slashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type) && !chessGameLogic.backslashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
			{
				if (this.yPos + 1 < chessBoard.getBoardHeight() && chessBoard.getBoardPosition(this.xPos, this.yPos + 1) == 0)
				{
					if (chessBoard.checkState)
					{
						if (chessGameLogic.isThisProtecting(chessBoard, this.xPos, this.yPos + 1, this.type))
							chessBoard.colorSquare(this.xPos, this.yPos + 1, false);
					}
					else
						chessBoard.colorSquare(this.xPos, this.yPos + 1, false);
				}
			}
			if (!chessGameLogic.verticalProtection(chessBoard, this.xPos, this.yPos, this.type))
			{
				if (!chessGameLogic.backslashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
				{
					if (this.yPos + 1 < chessBoard.getBoardHeight() && this.xPos - 1 >= 0 && chessBoard.getBoardPosition(this.xPos - 1, this.yPos + 1) != this.type && chessBoard.getBoardPosition(this.xPos - 1, this.yPos + 1) != 0)
					{
						if (chessBoard.checkState)
						{
							if (chessGameLogic.isThisProtecting(chessBoard, this.xPos - 1, this.yPos + 1, this.type))
								chessBoard.colorSquare(this.xPos - 1, this.yPos + 1, false);
						}
						else
							chessBoard.colorSquare(this.xPos - 1, this.yPos + 1, false);
					}
				}
				if (!chessGameLogic.slashDiagonalProtection(chessBoard, this.xPos, this.yPos, this.type))
				{
					if (this.yPos + 1 < chessBoard.getBoardHeight() && this.xPos + 1 < chessBoard.getBoardWidth() && chessBoard.getBoardPosition(this.xPos + 1, this.yPos + 1) != this.type && chessBoard.getBoardPosition(this.xPos + 1, this.yPos + 1) != 0)
					{
						if (chessBoard.checkState)
						{
							if (chessGameLogic.isThisProtecting(chessBoard, this.xPos + 1, this.yPos + 1, this.type))
								chessBoard.colorSquare(this.xPos + 1, this.yPos + 1, false);
						}
						else
							chessBoard.colorSquare(this.xPos + 1, this.yPos + 1, false);
					}
				}
			}
		}
	}
}
